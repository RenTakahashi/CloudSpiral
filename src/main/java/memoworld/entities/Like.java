package memoworld.entities;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Like {
    @XmlElement(name = "date")
    private Date date;

    public Like() {
        date = new Date();
    }

    public Like(Date date) {
    	this.date = date;
    }

    public Date getDate() {
        return date;
    }

}