package models;

import org.apache.commons.lang.StringUtils;
import play.Play;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ahmet.bayirli on 11.6.2019.
 */
@Entity
public class Entry  extends Model
{
    @ManyToOne
    public EntryKeyword entryKeyword;
    public String title;
    public Integer orderNo;

    @Column(length = 2000)
    public String content;

    @OneToOne
    public Image image;

    public static List<Entry> findByEntryKeywordId(Long entryKeywordId) {
        return Entry.find ("entryKeyword.id = ? order by orderNo", entryKeywordId).fetch();
    }

    @Override
    public String toString() {
        if(StringUtils.isEmpty(title))
        {
            return "Title_"+ id;
        }
        else
        {
            return title;
        }
    }

    public String getHTML(EntryStyleEnum style) {
        if(EntryStyleEnum.Accordion.equals(style))
        {
            return getAccordionHTML();
        }
        else if(EntryStyleEnum.Paragraph.equals(style))
        {
            return getParagraphHTML();
        }
        else if(EntryStyleEnum.Tabbed.equals(style))
        {
            return getTabbedHTML();
        }
        return "";
    }

    public String getAccordionHTML() {

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<div class=\"card\">");
            stringBuilder.append("<div class=\"card-header\" id=\"headingEntry"+id+"\">");
                stringBuilder.append("<h5 class=\"mb-0\">");
                stringBuilder.append("<button aria-controls=\"collapseEntry"+id+"\" aria-expanded=\"false\" class=\"btn btn-link\"  data-target=\"#collapseEntry"+id+"\" data-toggle=\"collapse\" type=\"button\">");
                    stringBuilder.append(StringUtils.isEmpty(title)?"-":title);
                stringBuilder.append("</button>");
                stringBuilder.append("</h5>");
            stringBuilder.append("</div>");
            stringBuilder.append("<div aria-labelledby=\"headingEntry"+id+"\" class=\"collapse\" data-parent=\"#accordionEntryKeyword"+entryKeyword.id+"\" id=\"collapseEntry"+id+"\">");
                stringBuilder.append("<div class=\"card-body\">");
                    stringBuilder.append("<div class=\"row\">");
                        if(image!=null)
                        {
                            stringBuilder.append("<div class=\"col-md-3\">");
                            stringBuilder.append(getImageHTML());
                            stringBuilder.append("</div>");
                        }
                        stringBuilder.append(image!=null?"<div class=\"col-md-9\">":"<div class=\"col-md-12\">");
                            stringBuilder.append(getContentHTML(content));
                        stringBuilder.append("</div>");
                    stringBuilder.append("</div>");
                stringBuilder.append("</div>");
            stringBuilder.append("</div>");
        stringBuilder.append("</div>");

        return stringBuilder.toString();
    }
    public String getParagraphHTML() {

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<section>");
        stringBuilder.append("<div class=\"container py-3\">");
        stringBuilder.append("<div class=\"card condJustify\" style=\"text-align:justify;\">");
            stringBuilder.append("<div class=\"row\">");
                if(image!=null)
                {
                    stringBuilder.append("<div class=\"col-md-3\">");
                    stringBuilder.append(getImageHTML());
                    stringBuilder.append("</div>");
                }
                stringBuilder.append(image!=null?"<div class=\"col-md-9\">":"<div class=\"col-md-12\">");
                    stringBuilder.append("<div class=\"card-block px-3\">");
                        stringBuilder.append("<h4 class=\"card-title\">");
                        stringBuilder.append(StringUtils.isEmpty(title)?"":title);
                        stringBuilder.append("</h4>");
                        stringBuilder.append(getContentHTML(content));
                    stringBuilder.append("</div>");
                stringBuilder.append("</div>");
        stringBuilder.append("</div>");
        stringBuilder.append("</div>");
        stringBuilder.append("</section>");

        return stringBuilder.toString();
    }
    public String getTabbedHTML() {

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<div class=\"tab-pane fade\" id=\"nav-entry-"+id+"\" role=\"tabpanel\" aria-labelledby=\"nav-tab-"+id+"\">");
        stringBuilder.append("<div class=\"card condJustify\" style=\"text-align:justify;\">");
            stringBuilder.append("<div class=\"row\">");
                if(image!=null)
                {
                    stringBuilder.append("<div class=\"col-md-3\">");
                    stringBuilder.append(getImageHTML());
                    stringBuilder.append("</div>");
                }
                stringBuilder.append(image!=null?"<div class=\"col-md-9\">":"<div class=\"col-md-12\">");
                    stringBuilder.append("<div class=\"card-block px-3\">");
                        stringBuilder.append(getContentHTML(content));
                    stringBuilder.append("</div>");
                stringBuilder.append("</div>");
            stringBuilder.append("</div>");
        stringBuilder.append("</div>");
        stringBuilder.append("</div>");

        return stringBuilder.toString();
    }

    private String getImageHTML() {
        return "<img src=\"/imagecontroller/renderimage?imageId="+image.id+"\" class=\"w-100\" style=\"margin-top: 3px;\" />";
    }

    private String getContentHTML(String content) {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<p>");
        stringBuilder.append(content);
        stringBuilder.append("</p>");
        return putInnerParagraphs(stringBuilder.toString());
    }

    private String putInnerParagraphs(String contenHTML) {
        contenHTML = contenHTML.replaceAll("\n", "</p><p>");
        contenHTML = clearItemListsFromParagraphs(contenHTML);
        return contenHTML;
    }

    //OL and UL can't be in P but browser will handle it
    //here only Ps inside of UL and OL are cleared.
    private String clearItemListsFromParagraphs(String contenHTML) {
        final Pattern pattern1 = Pattern.compile("<ol>(.+?)</ol>", Pattern.DOTALL);
        final Pattern pattern2 = Pattern.compile("<ul>(.+?)</ul>", Pattern.DOTALL);


        Matcher matcher1 = pattern1.matcher(contenHTML);
        while (matcher1.find()) {
            String someOL = matcher1.group(1);
            contenHTML = contenHTML.replace(someOL,someOL.replace("<p>","").replace("</p>",""));
        }
        Matcher matcher2 = pattern2.matcher(contenHTML);
        while (matcher2.find()) {
            String someUL = matcher2.group(1);
            contenHTML = contenHTML.replace(someUL,someUL.replace("<p>","").replace("</p>",""));
        }
        return contenHTML;
    }

    public String getNavTab(boolean isSelected) {
        return "<a class=\"card-title nav-item nav-link "+(isSelected?"active":"")+"\" " +
                " id=\"nav-tab-"+id+"\" " +
                " data-toggle=\"tab\" " +
                " href=\"#nav-entry-"+id+"\" role=\"tab\" " +
                " aria-controls=\"nav-entry-"+id+"\"" +
                " "+(isSelected?"aria-selected=\"true\"":"aria-selected=\"false\"")+
                " >"+title+"</a>";
    }
}
