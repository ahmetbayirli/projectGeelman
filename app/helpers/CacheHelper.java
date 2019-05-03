package helpers;

import models.User;
import models.WebPage;
import play.cache.Cache;
import play.mvc.Scope;

import java.util.List;

/**
 * Created by ahmet.bayirli on 30.8.2015.
 */
public class CacheHelper
{

    private static final String parentPageListKey = "parentPageListKey";
    private static final String currentUser = "currentUser";
    private static final String userTries = "userTries";
    private static final String superAdmin = "superAdmin";

    public static List<WebPage> getParentPageListList()
    {
        return (List<WebPage>) getFromCache(parentPageListKey);
    }

    public static void setParentPageListList(List<WebPage> parentPageList)
    {
        putToCache(parentPageListKey, parentPageList);
    }

    public static void putToCache(String key, Object object)
    {
        Cache.set(key, object);
    }

    public static Object getFromCache(String key)
    {
        return Cache.get(key);
    }

    public static User getCurrentUser()
    {
        return (User) getFromSession(currentUser);
    }

    public static void setCurrentUser(User user)
    {
        putToSession(currentUser, user);
    }

    public static Integer getUserTryCount()
    {
        Integer userTries = (Integer) getFromCache(CacheHelper.userTries);
         if(userTries==null) return 0;
        return userTries;
    }

    public static void setTryCount()
    {
        Integer tryCount = getUserTryCount();
        tryCount++;
        putToCache(userTries, tryCount);
    }

    public static void setTryCount(Integer kulTrial)
    {
        putToCache(userTries, kulTrial);
    }

    public static void putToSession(String key, Object object)
    {
        Cache.set(Scope.Session.current().getId() + key, object);
    }

    public static Object getFromSession(String key)
    {
        return Cache.get(Scope.Session.current().getId() + key);
    }

    public static boolean checkSuperAdminExists()
    {
        User superAdmin = (User) getFromCache(CacheHelper.superAdmin);
        if (superAdmin!=null&&!superAdmin.isEmpty()) return true;

        try
        {
            superAdmin = User.find("role = 0").first();
            if(superAdmin ==null)
            {
                superAdmin = new User("superadmin","supersecret",0);
                superAdmin.save();
                putToCache(CacheHelper.superAdmin, superAdmin);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
