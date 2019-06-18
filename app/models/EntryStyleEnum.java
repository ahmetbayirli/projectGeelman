package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.List;

/**
 * Created by ahmet.bayirli on 11.6.2019.
 */
public enum  EntryStyleEnum
{
    Paragraph(0),
    Accordion(1),
    Tabbed(2);

    public int value;

    EntryStyleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static EntryStyleEnum getEnum(int value) {
        for(EntryStyleEnum v : values())
            if(v.getValue() == value) return v;
        throw new IllegalArgumentException();
    }
}
