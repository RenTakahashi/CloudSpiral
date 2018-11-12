package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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
	private MongoCollection<Document> accounts;
	
	public AccountModel() {
		accounts = MongoClientPool.getInstance().collection("accounts");		
	}
	
	public void close() {
	}
	//指定したIDのアカウント情報を取得するためのメソッド
	public Account findById (String id) {		
		Document document = accounts.find(Filters.eq("user_id",id)).first();
		return toAccount(document);
	}		
	//アカウントの一覧を取得するためのメソッド
	public Accounts getAccounts() {
		List<Account> list = new ArrayList<>();
		this.accounts.find().map(AccountModel::toAccount).into(list);
		return new Accounts(list);
	}
	//IDを指定して削除できるようにするためのメソッド
	public boolean deleteAccounts(String id) {
		DeleteResult result = this.accounts.deleteOne(Filters.eq("user_id", id));
		return result.getDeletedCount() > 0;
	}
	
	private static Account toAccount(Document document) {		
		if(document == null) {
			return null;
		}
		Account act = new Account();
		act.setUser_id(document.getString("user_id"));
		act.setName(document.getString("name"));
		act.setPassword(document.getString("password"));
		return act;
	}
	
	private Document toDocument (Account account) {
		if(account == null) return null;
				Map<String, Object> map = new HashMap<String, Object>();
		map.put("user_id", account.getUser_id());
		map.put("name", account.getName());
		map.put("pass", account.getPassword());
		return new Document(map);
		
	}
	//toDocumentで生成したアカウント情報をDBに保存するメソッド
	public Account register(Account account) {
		accounts.insertOne(toDocument(account));
		return account;
	}
}