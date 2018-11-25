package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
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
	public ArrayList<Photo> photos = new ArrayList<>();
    @XmlElement(name = "likes")
	public Likes likes = new ArrayList<>();
    @XmlElement(name = "photos_id")
    private ArrayList<Integer> photos_id = new ArrayList<>();
    
   
    
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
    
    
    public ArrayList<Integer> getPhotos_id() {
    	return photos_id;
    }
    
    public void setPhotos_id(ArrayList<Integer> photos_id) {
    	this.photos_id = photos_id; 
    }
    
}
