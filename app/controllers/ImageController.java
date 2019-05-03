package controllers;

import helpers.FileHelper;
import models.Image;
import play.db.jpa.Blob;
import play.mvc.Controller;

import java.io.File;

/**
 * User: pair
 * Date: 8/20/14
 * Time: 8:19 PM
 */
public class ImageController extends Controller
{
    public static void browse(int CKEditorFuncNum, Long imageId)
    {
        render(CKEditorFuncNum, imageId);
    }

    public static void saveImageAction(int CKEditorFuncNum, Blob imageFile)
    {
        File file = imageFile.getFile();
        boolean isFileImage = FileHelper.isImageFile(file);
        Long imageId = null;
        if (isFileImage)
        {
            Image image = new Image(imageFile);
            image.save();
            imageId = image.id;
        } else
        {
            flash.error("This is not an image file!");
        }
        browse(CKEditorFuncNum, imageId);
    }

    public static void renderImage(Long imageId)
    {
        Image image = null;
        try
        {
            image = Image.findByIdWithException(imageId);
        } catch (Exception e)
        {
            e.printStackTrace();
            flash.error(e.getMessage());
            renderText(e.getMessage());
        }
        renderBinary(image.getImage().getFile());
    }

    public static void renderImageByBlobId(String blobId)
    {
        Image image = Image.find("blobId = ?", blobId).first();
        if (image == null)
        {
            renderText("Image not found!");
        } else
        {
            renderBinary(image.getImage().getFile());
        }
    }

    public static void favicon()
    {
        renderBinary(new File("public/images/faviconPlay.png"));
    }
}
