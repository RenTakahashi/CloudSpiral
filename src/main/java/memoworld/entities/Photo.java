package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Photo {
    @XmlElement(name = "id")
    private int id;
    @XmlElement(name = "date")
    private Date date;
    @XmlElement(name = "description")
    private String description;
    @XmlElement(name = "location")
    private Location location;
    @XmlElement(name = "author")
    private String author;
    @XmlElement(name = "raw_uri")
    private String raw_uri;
    @XmlElement(name = "raw")
    private String raw_image;

    public Photo() {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRawURI() {
        return raw_uri;
    }

    public void setRawURI(String raw_uri) {
        this.raw_uri = raw_uri;
    }

    public String getRawImage() {
        return raw_image;
    }

    public void setRawImage(String raw_image) {
        this.raw_image = raw_image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}