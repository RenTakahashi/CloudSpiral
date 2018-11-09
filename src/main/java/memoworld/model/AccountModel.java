package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import memoworld.entities.Account;
import memoworld.model.AccountModel;
import memoworld.entities.Accounts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AccountModel implements AutoCloseable {
	private MongoCollection<Document> uids;
	private MongoCollection<Document> accounts;
	private MongoClient client;
	
	public AccountModel() {
		this.client = new MongoClient("localhost", 27017);
		accounts = MongoClientPool.getInstance().collection("accounts");
		uids = MongoClientPool.getInstance().collection("uid");
		
	}
	
	public void close() {
		this.client.close();
	}
		
	public Account findById (int id) {
		
		Document document = accounts
				.find(Filters.eq("uid",id)).first();
		return toAccount(document);
	}
	
	public Accounts getAccounts() {
		List<Account> list = new ArrayList<>();
		this.accounts.find().map(AccountModel::toAccount).into(list);
		return new Accounts(list);
	}
	
	public boolean deleteAccounts(int id) {
		DeleteResult result = this.accounts.deleteOne(Filters.eq("uid", id));
		return result.getDeletedCount() > 0;
	}
	
	private static Account toAccount(Document document) {
		
		if(document == null) {
			return null;
		}
		Account act = new Account();
		act.setUid(document.getInteger("uid"));
		act.setAid(document.getString("aid"));
		act.setName(document.getString("name"));
		act.setPass(document.getString("pass"));
		return act;
	}
	
	
	private Document toDocument (Account account) {
		if(account == null) return null;
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("aid", account.getAid());
		map.put("uid", account.getUid());
		map.put("location", account.getLocation());
		map.put("name", account.getName());
		map.put("pass", account.getPass());
		return new Document(map);
		
	}
	
	public int newId() {
		if(uids.count() == 0L )
			return 0;
		return uids.find().sort(Sorts.descending("uid")).first().getInteger("uid",0);
		
	}
	public Account register(Account account) {
		account.setUid(newId() + 1);
		accounts.insertOne(toDocument(account));
		Document idDoc = new Document("uid", account.getUid());
		uids.insertOne(idDoc);
		return account;
	}
}