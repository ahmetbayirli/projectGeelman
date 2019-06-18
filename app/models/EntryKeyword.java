package models;

import helpers.CacheHelper;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by ahmet.bayirli on 11.6.2019.
 */
@Entity
public class EntryKeyword  extends Model
{
    public String keyword;
    public EntryStyleEnum style;

    @OneToMany(mappedBy = "entryKeyword")
    public List<Entry> entryList;

    @Override
    public String toString()
    {
        return "Keyword: " + keyword + ", Style: " + style;
    }

    public static EntryKeyword findByKeyword(String keyword) {
        return EntryKeyword.find("keyword=?", keyword).first();
    }

    public String getHTML() {
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
        else return "";
    }

    private String getAccordionHTML() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<div class=\"accordion\" id=\"accordionEntryKeyword"+id+"\">");
        for (Entry entry : entryList) {
            stringBuilder.append(entry.getHTML(style));
        }
        stringBuilder.append("</div>");
        return stringBuilder.toString().replaceFirst("aria-expanded=\"false\"","aria-expanded=\"true\"").replaceFirst("class=\"collapse\"","class=\"collapse show\"");
    }
    private String getParagraphHTML() {
        StringBuilder stringBuilder = new StringBuilder("");
        for (Entry entry : entryList) {
            stringBuilder.append(entry.getHTML(style));
        }
        return stringBuilder.toString();
    }
     private String getTabbedHTML() {
        StringBuilder stringBuilder = new StringBuilder("");
         stringBuilder.append("<div class=\"card border-dark mb-3 w-100\">");
             stringBuilder.append("<div class=\"card-body text-dark\">");
                 stringBuilder.append("<nav>");
                 stringBuilder.append("<div class=\"nav nav-tabs\" id=\"nav-tab\" role=\"tablist\">");
                 boolean isSelected = true;
                 for (Entry entry : entryList)
                 {
                     stringBuilder.append(entry.getNavTab(isSelected));
                     isSelected = false;
                 }
                 stringBuilder.append("</div>");
                 stringBuilder.append("</nav>");
                 stringBuilder.append("<div class=\"tab-content\" id=\"nav-tabContent\">");
                 for (Entry entry : entryList)
                 {
                     stringBuilder.append(entry.getHTML(style));
                 }
                stringBuilder.append("</div>");
             stringBuilder.append("</div>");
         stringBuilder.append("</div>");
        return stringBuilder.toString().replaceFirst("tab-pane fade","tab-pane fade active show ");
    }
}
