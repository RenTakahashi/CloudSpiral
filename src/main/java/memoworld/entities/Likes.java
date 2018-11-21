package memoworld.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Likes {
	
	@XmlElement(name="likes")
	private List<Like> likes = new ArrayList<>();
	
	public Likes() {
	}

	public Likes(List<Like> list) {
		this.likes = list;
	}
	
}
