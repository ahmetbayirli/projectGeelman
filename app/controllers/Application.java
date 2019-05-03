package controllers;

import models.WebPage;
import org.apache.commons.lang.StringUtils;
import play.mvc.Controller;

import java.sql.Timestamp;
import java.util.List;

public class Application extends Controller {

    public static void index(String pageName)
    {
        List<WebPage> webPageList = WebPage.getParentList();

        WebPage webPage = null;

        if (webPageList.size()>0)
        {
            webPage = webPageList.get(0);

            if(!StringUtils.isEmpty(pageName))
            {
                for (WebPage menuItem : webPageList)
                {
                    if(menuItem.title.equals(pageName))
                    {
                        webPage = menuItem;
                    }
                }

            }
        }
        System.err.println(new Timestamp(System.currentTimeMillis()) + "  ip: "+ request.remoteAddress+" has opened index.");
        render(webPageList, webPage);
    }
    public static void pageLoad(Long pageId)
    {
        WebPage webPage = WebPage.getById(pageId);
        String content = webPage.getContent();
        System.err.println(new Timestamp(System.currentTimeMillis()) + "  ip: "+ request.remoteAddress+" has opened "+ webPage.title);
        renderHtml(content);
    }

}