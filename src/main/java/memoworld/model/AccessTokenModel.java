package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

public class AccessTokenModel implements AutoCloseable {
    private MongoCollection<Document> tokens = MongoClientPool.getInstance().collection("access_tokens");

    public AccessTokenModel() {
    }

    @Override
    public void close() {
    }

    /**
     * 次に追加されるアクセストークンのidを返す
     *
     * @return 次に追加されるアクセストークンのid (idの最大値+1)
     */
    public int newId() {
        if (tokens.count() == 0L) {
            return 1;
        }

        return tokens.find()
                     .sort(Sorts.descending("id"))
                     .first()
                     .getInteger("id", 0) + 1;
    }

    /**
     * アクセストークンを発行し、DBに登録する
     *
     * @param account トークンを発行するアカウント
     * @return 発行されたアクセストークンオブジェクト
     */
    public AccessToken register(Account account) {
        tokens.deleteMany(Filters.eq("user_id", account.getUser_id()));

        AccessToken token = new AccessToken();

        Document document = toDocument(token);
        document.append("id", newId());
        document.append("user_id", account.getUser_id());
        tokens.insertOne(document);

        return token;
    }

    /**
     * アクセストークンが有効かを返す。
     * アクセストークンがDBに登録されていて、かつ有効期限内であれば有効
     *
     * @param accessToken アクセストークン
     * @return アクセストークンが有効ならtrue
     */
    public boolean validate(String accessToken) {
        return countValidToken(Filters.eq("access_token", accessToken)) > 0;
    }

    /**
     * 有効期限切れのトークンを削除してから tokens.count(filter) する
     *
     * @param filter tokens.count() の引数と同じもの
     * @return filter の条件を満たす有効期限内のトークンの個数
     */
    private long countValidToken(Bson filter) {
        removeExpiredTokens();
        return tokens.count(filter);
    }

    public Account getAccount(String accessToken){
        removeExpiredTokens();
        Document document = tokens
                .find(Filters.eq("access_token", accessToken))
                .first();
        AccountModel model = new AccountModel();
        return model.findByUser_Id(document.getString("user_id"));
    }

    /**
     * 期限切れのトークンを削除する
     */
    private void removeExpiredTokens() {
        tokens.deleteMany(Filters.lte("expires_at", new Date()));
    }

    private AccessToken toAccessToken(Document existingToken) {
        AccessToken token = new AccessToken();
        token.setAccessToken(existingToken.getString("access_token"));
        token.setExpiresAt(existingToken.getDate("expires_at"));
        token.setTokenType(existingToken.getString("token_type"));
        return token;
    }

    private Document toDocument(AccessToken token) {
        Document document = new Document();
        document.put("access_token", token.getAccessToken());
        document.put("expires_at", token.getExpiresAt());
        document.put("token_type", token.getTokenType());
        return document;
    }
}
