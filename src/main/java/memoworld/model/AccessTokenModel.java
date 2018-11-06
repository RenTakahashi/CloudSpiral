package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import org.bson.Document;

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
        AccessToken token = new AccessToken(account);

        Document document = toDocument(token);
        document.append("id", newId());

        tokens.insertOne(document);
        return token;
    }

    public Document toDocument(AccessToken token) {
        Document document = new Document();
        document.put("access_token", token.getAccessToken());
        document.put("refresh_token", token.getRefreshToken());
        document.put("expires_at", token.getExpiresAt());
        document.put("token_type", token.getTokenType());
        return document;
    }

    /**
     * アクセストークンが有効かを返す。
     * アクセストークンがDBに登録されていて、かつ有効期限内であれば有効
     *
     * @param accessToken アクセストークン
     * @return アクセストークンが有効ならtrue
     */
    public boolean validate(String accessToken) {
        removeExpiredTokens();

        return tokens.count(Filters.eq("access_token", accessToken)) > 0;
    }

    /**
     * 期限切れのトークンを削除する
     */
    private void removeExpiredTokens() {
        tokens.deleteMany(Filters.lte("expires_at", new Date()));
    }
}
