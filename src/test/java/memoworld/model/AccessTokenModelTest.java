package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AccessTokenModelTest {

    private static final Account TEST_ACCOUNT = new Account();

    static {
        TEST_ACCOUNT.setUserId("test");
        TEST_ACCOUNT.setPassword("9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05");
        TEST_ACCOUNT.setUserName("テスト");
    }

    private int itemCount;

    @Test
    public void expiredTokenTest() {
        // TODO: 有効期限切れトークンのテスト
    }

    @Test
    public void notRegisteredTokenTest() {
        // TODO: 登録されていない（不正な）トークンのテスト
    }

    @Test
    public void registerTokenTest() {
        AccessTokenModel model = new AccessTokenModel();

        AccessToken accessToken = model.register(TEST_ACCOUNT);
        assertEquals(itemCount + 1, model.newId());

        assertEquals(AccessToken.TOKEN_TYPE, accessToken.getTokenType());
        assertFalse(accessToken.getAccessToken().isEmpty());
        assertFalse(accessToken.getRefreshToken().isEmpty());
        assertNotEquals(accessToken.getAccessToken(), accessToken.getRefreshToken());
        assertFalse(accessToken.isExpired());

        AccessToken anotherToken = model.register(TEST_ACCOUNT);
        assertEquals(itemCount + 2, model.newId());

        assertNotEquals(accessToken.getAccessToken(), anotherToken.getAccessToken());
        assertNotEquals(accessToken.getRefreshToken(), anotherToken.getRefreshToken());
        assertEquals(AccessToken.TOKEN_TYPE, anotherToken.getTokenType());
        assertFalse(anotherToken.isExpired());
        assertTrue(anotherToken.getExpiresAt().after(accessToken.getExpiresAt()));
    }

    @Before
    public void setUp() {
        AccessTokenModel model = new AccessTokenModel();
        itemCount = model.newId();
    }

    @After
    public void tearDown() {
        MongoCollection<Document> tokens = MongoClientPool.getInstance().collection("access_tokens");
        tokens.deleteMany(Filters.gte("id", itemCount));
    }

    @Test
    public void validateTokenTest() {
        // TODO: アクセストークンがDBに登録されていて、有効期限内であることを確認するテスト
    }

}
