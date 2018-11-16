package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import memoworld.entities.Account;
import memoworld.entities.Accounts;
import memoworld.entities.PasswordUtil;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountModel implements AutoCloseable {
	private MongoCollection<Document> db_ids;
	private MongoCollection<Document> accounts;
	
	public AccountModel() {
		accounts = MongoClientPool.getInstance().collection("accounts");		
		db_ids = MongoClientPool.getInstance().collection("db_ids");
	}
	
	public void close() {
	}
	//指定したIDのアカウント情報を取得するためのメソッド
	public Account findByDb_Id (int id) {		
		try {
		Document document = accounts.find(Filters.eq("db_id",id)).first();
		return toAccount(document);
		}catch(NullPointerException e) {
			return null;
		}
	}
	
	//指定したIDのアカウント情報を取得するためのメソッド
	public Account findByUser_Id (String id) {
		try {
			Document document = accounts.find(Filters.eq("user_id",id)).first();
			return toAccount(document);
		}catch(NullPointerException e) {
			return null;
		}
	}

	/**
	 * ユーザIDが登録されていて、パスワードが合っているときに Account を返す
	 * @param userId ユーザID
	 * @param rawPassword 平文パスワード
	 * @return Account
	 */
	public Account findByUserIdAndPassword(String userId, String rawPassword) {
		Account account = findByUser_Id(userId);
		if (account == null) {
			// IDが登録されていない
			return null;
		}

		String safetyPassword = PasswordUtil.getSafetyPassword(rawPassword, userId);
		if (!account.getPassword().equals(safetyPassword)) {
			// パスワードが合っていない
			return null;
		}

		return account;
	}

	//アカウントの一覧を取得するためのメソッド
	public Accounts getAccounts() {
		List<Account> list = new ArrayList<>();
		this.accounts.find().map(AccountModel::toAccount).into(list);
		return new Accounts(list);
	}
	
	//IDを指定して削除できるようにするためのメソッド
	public boolean deleteAccounts(int id) {
		DeleteResult result = this.accounts.deleteOne(Filters.eq("db_id", id));
		return result.getDeletedCount() > 0;
	}
	
	private static Account toAccount(Document document) {		
		if(document == null) {
			return null;
		}
		Account act = new Account();
		act.setDb_id(document.getInteger("db_id", 0));
		act.setUser_id(document.getString("user_id"));
		act.setName(document.getString("name"));
		act.setPassword(document.getString("password"));
		return act;
	}
	
	private Document toDocument (Account account) {
		if(account == null) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("db_id", account.getDb_id());
		map.put("user_id", account.getUser_id());
		map.put("name", account.getName());
		map.put("password", account.getPassword());
		return new Document(map);	
	}
	
	public int newId() {
		if(db_ids.count() == 0L) {
			return 0;
		}
		return db_ids.find().sort(Sorts.descending("db_id")).first().getInteger("db_id", 0);
	}
	
	//toDocumentで生成したアカウント情報をDBに保存するメソッド
	public Account register(Account account) {
		account.setDb_id(newId() + 1);
		accounts.insertOne(toDocument(account));
		Document idDoc = new Document("db_id", account.getDb_id());
		db_ids.insertOne(idDoc);
		return account;
	}
}