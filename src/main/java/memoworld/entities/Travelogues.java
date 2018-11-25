package memoworld.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class Travelogues {
	
	@XmlElement(name="travelogues")
	public List<Travelogue> travelogues = new ArrayList<>();
	
	public Travelogues() {
	}
	
	
	public Travelogues(List<Travelogue> list) {
		this.travelogues = list;
	}
	
}
