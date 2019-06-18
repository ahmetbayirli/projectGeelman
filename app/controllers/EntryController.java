package controllers;

import helpers.CacheHelper;
import helpers.FileHelper;
import models.*;
import play.db.jpa.Blob;
import play.mvc.Before;
import play.mvc.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by ahmet.bayirli on 20.8.2015.
 */
public class EntryController extends Controller {
    @Before
    public static void securityCheck()
    {
        if(CacheHelper.getCurrentUser()==null)
        {
            AdminController.login(null);
        }
    }

    public static void openEntryKeyword(Long entryKeywordId, String succes, String error) {
        EntryKeyword entryKeyword = null;
        if(entryKeywordId!=null) entryKeyword = EntryKeyword.findById(entryKeywordId);
        if (succes != null) flash.success(succes);
        if (error != null) flash.error(error);
        render(entryKeyword);
    }

    public static void saveEntryKeyword(EntryKeyword entryKeyword) {
        String error = null;
        String success = null;
        try {
            entryKeyword.save();
        } catch (Exception e) {
            e.printStackTrace();
            error = "EntryKeyword save failed!";
        }
        if (error == null) success = "EntryKeyword saved successfully.";
        openEntryKeyword(entryKeyword.getId(), success, error);
    }

    public static void openEntry(Long entryKeywordId, Long entryId, String succes, String error) {
        EntryKeyword entryKeyword = null;
        Entry entry = null;
        List<Entry> neighbourEntryList = null;
        if(entryKeywordId!=null)
        {
            entryKeyword = EntryKeyword.findById(entryKeywordId);
            neighbourEntryList = Entry.findByEntryKeywordId(entryKeywordId);
        }

        if(entryId!=null) entry = Entry.findById(entryId);
        if (succes != null) flash.success(succes);
        if (error != null) flash.error(error);
        render(entryKeyword,entry,neighbourEntryList);
    }

    public static void saveEntry(Entry entry, Blob imageFile) {
        String error = null;
        String success = null;
        try {
            if(imageFile!=null)
            {
                File file = imageFile.getFile();
                boolean isFileImage = FileHelper.isImageFile(file);
                if (isFileImage)
                {
                    Image image = new Image(imageFile);
                    image.save();
                    entry.image = image;
                } else
                {
                    flash.error("This is not an image file!");
                }
            }
            entry.save();
        } catch (Exception e) {
            e.printStackTrace();
            error = "Entry save failed!";
        }
        if (error == null) success = "Entry saved successfully.";
        openEntry(entry.entryKeyword.getId(), entry.getId(), success, error);
    }

}
