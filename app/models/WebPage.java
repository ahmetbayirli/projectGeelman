package models;

import helpers.CacheHelper;
import net.sf.ehcache.config.ConfigurationHelper;
import play.Play;
import play.db.jpa.Model;

import javax.persistence.*;
import javax.security.auth.login.Configuration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ahmet.bayirli on 19.8.2015.
 */
@Entity
//@Table(name = "webPage")
public class WebPage extends Model
{
    public String title;
    public Integer pageNumber;

    @OneToMany(mappedBy = "webPage", targetEntity = Content.class, cascade = CascadeType.ALL)
    public List<Content> contentList;

    @OneToOne
    public WebPage parent;

    public WebPage(int pageNumber, String title)
    {

        this.pageNumber = pageNumber;
        this.title = title;
    }

    @OneToMany(mappedBy = "parent", targetEntity = WebPage.class, cascade = CascadeType.ALL)
    public List<WebPage> childList;

    public List<WebPage> fetchChildList()
    {
        return WebPage.find("parent = ?", this).fetch();
    }


    @Override
    public String toString()
    {
        return "Page No: " + pageNumber + ", Page Title: " + title;
    }

    public String getRawContent()
    {
        String pageContent = "";

        Collections.sort(contentList, new Comparator<Content>()
        {
            @Override
            public int compare(Content o1, Content o2)
            {
                return Integer.compare(o1.contentOrder, o2.contentOrder);
            }
        });

        for (Content content : contentList)
        {
            pageContent += content.content;
        }
        return pageContent;
    }

    public String getContent()
    {
        String pageContent = "";
        pageContent = getRawContent();
        pageContent =  replaceEntryLists(pageContent);
        return pageContent;
    }


    private static String replaceEntryLists(String content)
    {
        String entrylistTagBegin = Play.configuration.getProperty("entrylist.begin");
        String entrylistTagEnd = Play.configuration.getProperty("entrylist.end");
        final Pattern pattern = Pattern.compile(entrylistTagBegin +"(.+?)" + entrylistTagEnd, Pattern.DOTALL);
//        final Matcher matcher = pattern.matcher("<tag>String I want to extract</tag>");
        while(content.contains(entrylistTagBegin)&&content.contains(entrylistTagEnd))
        {
            List<String> entryKeywords = getEntryKeywords(pattern.matcher(content));
            for (String keyword : entryKeywords) {
                EntryKeyword entryKeyword = EntryKeyword.findByKeyword(keyword);
                content = content.replace(entrylistTagBegin+keyword+entrylistTagEnd, entryKeyword.getHTML() );
            }
        }
        return content;
    }

    private static List<String> getEntryKeywords(final Matcher matcher) {
        final List<String> entryKeywords = new ArrayList<String>();
        while (matcher.find()) {
            entryKeywords.add(matcher.group(1));
        }
        return entryKeywords;
    }

    public void deleteOldContent()
    {
        if (this.getId() != null) Content.delete("webPage = ?", this);
    }

    public void setContent(String pageContent)
    {
        List<Content> contentList = new ArrayList<>();

        int contentSizeLimit = Integer.parseInt(Play.configuration.getProperty("contentSizeLimit"));

        int startIndex = 0;
        for (int i = 0; i <= (int) (pageContent.length() / contentSizeLimit); i++)
        {
            int endIndex = (i + 1) * contentSizeLimit < pageContent.length() ? (i + 1) * contentSizeLimit : pageContent.length();
            contentList.add(new Content(this, i, pageContent.substring(startIndex, endIndex)));
            startIndex = endIndex;
        }
        this.contentList = contentList;
    }

    public static List<WebPage> getParentList()
    {
        List<WebPage> webPageList = CacheHelper.getParentPageListList();

        if (webPageList == null)
        {
            webPageList = WebPage.find("parent is null and pageNumber > 0").fetch();
            for (WebPage webPage : webPageList)
            {
                fetchChilds(webPage);
            }

            sortPages(webPageList);
            CacheHelper.setParentPageListList(webPageList);
        }
        return webPageList;
    }

    private static void fetchChilds(WebPage webPage)
    {
        String fetchedContent = webPage.getContent();
        List<WebPage> childList = webPage.childList;
        if (childList.size() > 0)
        {
            sortPages(childList);
            for (WebPage page : childList)
            {
                fetchChilds(page);
            }
        }
    }

    private static void sortPages(List<WebPage> webPageList)
    {
        Collections.sort(webPageList, new Comparator<WebPage>()
        {
            @Override
            public int compare(WebPage o1, WebPage o2)
            {
                return Integer.compare(o1.pageNumber, o2.pageNumber);
            }
        });
    }


    public void deleteWithChilds()
    {
        List<WebPage> childList = this.fetchChildList();
        if (childList.size() > 0)
        {
            for (WebPage webPage : childList)
            {
                webPage.deleteWithChilds();
            }
        }
        this.delete();
    }

    public static WebPage findByName(String pageName)
    {
        return WebPage.find("title = ? ", pageName).first();

    }

    public static WebPage getById(Long pageId)
    {
        List<WebPage> parentList = getParentList();
        return findOutOfList(pageId, parentList);
    }

    private static WebPage findOutOfList(Long pageId, List<WebPage> parentList)
    {
        for (WebPage webPage : parentList)
        {
            if (webPage.id.equals(pageId))
            {
                return webPage;
            }
            List<WebPage> childList = webPage.childList;
            if (childList != null && childList.size() > 0)
            {
                WebPage child = findOutOfList(pageId, childList);
                if(child != null) return child;
            }
        }
        return null;
    }
}
