package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Like {
    @XmlElement(name = "lid")
    private int lid;
    //いいねをしたtraveloguesのid
    @XmlElement(name = "tid")
    private String tid;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "author")
    private String author;
    

    public Like() {
        date = new Date();
    }

    public Like(String tid) {
        date = new Date();
        this.tid = tid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLid() {
        return lid;
    }

    public void setLid(int lid) {
        this.lid = lid;
    }
    
    public String getTid() {
    	return tid;
    }
    
    public void setTid(String tid) {
    	this.tid = tid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}