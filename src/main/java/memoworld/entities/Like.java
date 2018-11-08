package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Like {
    @XmlElement(name = "id")
    private int id;
    //@XmlElement(name = "location")
    //private String location;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "author")
    private String author;
    

    public Like() {
        date = new Date();
    }

    public Like(String author) {
        date = new Date();
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        //setLocation();
    }
    
    //public String getLocation() {
    	//return location;
    //}
    
    //public void setLocation() {
    	//this.location = "/likes/" + Integer.toString(id);
    //}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}