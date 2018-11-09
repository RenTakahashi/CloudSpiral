package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;




@XmlRootElement
public class Account {
	@XmlElement(name = "uid")
	private int uid;
	@XmlElement(name = "aid")
	private String aid;
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
	public Account(String pass, String name, String aid) {
		this.pass = pass;
		this.name = name;
		this.aid = aid;
	}
	public Account(int uid, String pass, String name, String aid) {
		this.uid = uid;
		this.pass = pass;
		this.name = name;
		this.aid = aid;
	}
	//アカウントidの取得とセット
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	//ユニークidの取得とセット
	public String getAid() {
		return aid;
	}
	public void setAid(String aid) {
		this.aid = aid;
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



