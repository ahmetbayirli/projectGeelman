package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by ahmet.bayirli on 19.8.2015.
 */
@Entity
//@Table(name = "content")
public class Content extends Model
{
    @ManyToOne
    public WebPage webPage;

    public Integer contentOrder;

    @Column(length = 1000)
    public String content;

    public Content(WebPage webPage, int contentOrder, String content) {

        this.webPage = webPage;
        this.contentOrder = contentOrder;
        this.content = content;
    }
}
