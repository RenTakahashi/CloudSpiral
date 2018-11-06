package memoworld.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Authentication {
	@XmlElement(name = "user_id")
	private String userId;
	@XmlElement(name = "password")
	private String password;
	
	public Authentication() {
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswordHash() {
		return password;
	}

	public void setPasswordHash(String passwordHash) {
		this.password = passwordHash;
	}
	
	
}
