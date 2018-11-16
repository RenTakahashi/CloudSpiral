package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Travelogue {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "title")
    private String title;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "photos")
    private String photos;
    @XmlElement(name = "likes")
    private String likes;

    public Travelogue() {
        date = new Date();
    }

    public Travelogue(String title) {
    	this.title=  title;
        date = new Date();
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
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getPhotos() {
    	return photos;
    }
    
    public void setPhotos(String photos) {
    	this.photos = photos;
    }
    
    public String getLikes() {
    	return likes;
    }
    
    public void setLikes(String likes) {
    	this.likes = likes;
    }
}