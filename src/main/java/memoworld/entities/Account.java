package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement
public class Account {
	@XmlElement(name = "aid")
	private int aid;
	@XmlElement(name = "uid")
	private String uid;
	@XmlElement(name = "location")
	private String location;	
	@XmlElement(name = "name")
	private String name;
	@XmlElement(name = "pass")
	private String pass;
	public Account() {
	}
	public Account(String pass) {
		this.pass = pass;
	}
	public Account(String pass, String name, String uid) {
		this.pass = pass;
		this.name = name;
		this.uid = uid;
	}
	public Account(int aid, String pass, String name, String uid) {
		this.aid = aid;
		this.pass = pass;
		this.name = name;
		this.uid = uid;
	}
	//アカウントidの取得とセット
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	//ユニークidの取得とセット
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	//Locationの取得とセット
	public String getLocation() {
		return location;
	}
	public void setLocation() {
		this.location = "/accounts/" + aid;
	}
	//名前の取得とセット
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	//パスワードの取得とセット
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}

}



