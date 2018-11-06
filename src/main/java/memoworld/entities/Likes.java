package memoworld.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Likes {
    @XmlElement(name = "likes")
    private List<Like> likes;

    public Likes() {
        likes = new ArrayList<Like>();
    }
    
    public Likes(List<Like> list) {
    	likes = list;
    }
    
    public List<Like> getLikes() {
        return likes;
    }

}