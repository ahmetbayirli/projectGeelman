package controllers;

import helpers.CacheHelper;
import models.Content;
import models.User;
import models.WebPage;
import play.mvc.Before;
import play.mvc.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by ahmet.bayirli on 20.8.2015.
 */
public class AdminController extends Controller {
    @Before(unless = {"login","superadminlogin"})
    public static void securityCheck()
    {
        if(CacheHelper.getCurrentUser()==null)
        {
            login(null);
        }
    }

    public static void index() {
//        List<WebPage> webPageList = createDummyPage();
        List<WebPage> webPageList = WebPage.getParentList();
        render(webPageList);
    }

    private static List<WebPage> createDummyPage() {
        List<WebPage> webPageList = WebPage.findAll();
        int pageNumber = webPageList.size() + 1;
        String title = pageNumber + ".samplePage";
        WebPage myWebPage = new WebPage(pageNumber, title);
        Content content = new Content(myWebPage, 1, "Sample Content Lorem Ipsum Dolor Sit Amet pageNo: " + pageNumber);
        myWebPage.save();
        content.save();
        return webPageList;
    }

    public static void openPage(Long pageId, String succes, String error) {
        WebPage webPage = null;
        List<WebPage> parentList = WebPage.findAll();
        if(pageId!=null) webPage = WebPage.findById(pageId);
        if (succes != null) flash.success(succes);
        if (error != null) flash.error(error);
        render(webPage,parentList);
    }

    public static void deletePage(Long pageId) {
        WebPage webPage = null;
        if(pageId!=null) webPage = WebPage.findById(pageId);
        webPage.deleteWithChilds();
        CacheHelper.setParentPageListList(null);
        redirect("/admincontroller/index");
    }

    public static void savePage(WebPage webPage, String pageContent) {
        String error = null;
        String success = null;
        try {
            webPage.deleteOldContent();
            webPage.setContent(pageContent);
            webPage.save();
        } catch (Exception e) {
            e.printStackTrace();
            error = "Page save failed!";
        }
        if (error == null) success = "Page saved successfully.";
        CacheHelper.setParentPageListList(null);
//        cleanTempFiles();
        openPage(webPage.getId(), success, error);
    }

    private static void cleanTempFiles() {
        try {
            File tmpDir = new File("./tmp");
            for (String tmpFileURL : tmpDir.list()) {
               File tmpFile = new File("./tmp/"+tmpFileURL);
                if(!tmpFile.isDirectory()&&tmpFileURL.indexOf(".")==-1)
                {
                    new PrintWriter(tmpFile).close();
                    tmpFile.delete();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("tmp file could not be erased.");
        }
    }

    public static void login(User user) {

        boolean loginOk = true;

        if(CacheHelper.getUserTryCount()>3)
        {
            flash.error("Because of security concerns, all user access has been blocked. Talk to administration.");
            loginOk = false;
            render(loginOk);
        }

        if(user.isEmpty()) render(user,loginOk);

        User kul = User.findByUserNameAndPassword_NonSuper(user);


        if(kul==null)
        {
            flash.error("Invalid user name or password");
            CacheHelper.setTryCount();
            render(user,loginOk);
        }

        CacheHelper.setTryCount(0);
        CacheHelper.setCurrentUser(kul);
        index();
    }
    public static void superadminlogin(User user)
    {
        CacheHelper.checkSuperAdminExists();

        if(user.isEmpty()) render(user);

        User superAdmin = User.findSuperAdmin(user);

        if(superAdmin==null)
        {
            flash.error("Invalid user name or password");
            CacheHelper.setTryCount();
            render(superAdmin);
        }

        CacheHelper.setCurrentUser(superAdmin);
        CacheHelper.setTryCount(0);
        superadmin();
    }

    public static void superadmin()
    {
        List<User> userList = User.getNonSuperList();
        render(userList);
    }
    public static void openUser(Long kullaniciId)
    {
        User user = null;
        if(kullaniciId!=null) user = User.findById(kullaniciId);
        render(user);
    }
    public static void saveUser(User user) {

        try {
            user.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        superadmin();
    }
}
