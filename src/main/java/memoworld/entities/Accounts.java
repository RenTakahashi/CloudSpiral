package memoworld.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement
public class Accounts {
	@XmlElement(name = "accounts")
	private List<Account> accounts = new ArrayList<>();
	public Accounts() {
	}
	public Accounts(List<Account> list) {
		this.accounts = list;
	}
}