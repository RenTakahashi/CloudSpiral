package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.management.*"})
@PrepareForTest({AccessTokenModel.class})
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
        try (AccessTokenModel model = new AccessTokenModel()) {
            AccessToken accessToken = model.register(TEST_ACCOUNT);
            assertTrue(model.validate(accessToken.getAccessToken()));

            // 時刻を1日後に設定
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.add(Calendar.HOUR, 24);
            Date mockDate = calendar.getTime();
            PowerMockito.mock(Date.class);
            PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(mockDate);

            // 1日後にはトークンが無効になっているはず
            assertFalse(model.validate(accessToken.getAccessToken()));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void notRegisteredTokenTest() {
        try (AccessTokenModel model = new AccessTokenModel()) {
            AccessToken accessToken = new AccessToken(TEST_ACCOUNT);
            assertFalse(model.validate(accessToken.getAccessToken()));
        }
    }

    @Test
    public void registerTokenTest() {
        try (AccessTokenModel model = new AccessTokenModel()) {
            AccessToken accessToken = model.register(TEST_ACCOUNT);
            assertEquals(itemCount + 1, model.newId());

            assertEquals(AccessToken.TOKEN_TYPE, accessToken.getTokenType());
            assertFalse(accessToken.getAccessToken().isEmpty());
            assertFalse(accessToken.isExpired());

            // 再登録
            AccessToken anotherToken = model.register(TEST_ACCOUNT);
            assertEquals(itemCount + 1, model.newId()); // 古いのは削除するので、newIdは変わらない

            // 前のトークンとは変わっているはず
            assertNotEquals(accessToken.getAccessToken(), anotherToken.getAccessToken());
            assertEquals(AccessToken.TOKEN_TYPE, anotherToken.getTokenType());
            assertFalse(anotherToken.isExpired());
            assertTrue(anotherToken.getExpiresAt().after(accessToken.getExpiresAt()));

            // 古いトークンは使えない
            assertFalse(model.validate(accessToken.getAccessToken()));
        }
    }

    @Before
    public void setUp() {
        try (AccessTokenModel model = new AccessTokenModel()) {
            itemCount = model.newId();
        }
    }

    @After
    public void tearDown() {
        MongoCollection<Document> tokens = MongoClientPool.getInstance().collection("access_tokens");
        tokens.deleteMany(Filters.gte("id", itemCount));
    }

    @Test
    public void validateTokenTest() {
        try (AccessTokenModel model = new AccessTokenModel()) {
            AccessToken accessToken = model.register(TEST_ACCOUNT);
            assertTrue(model.validate(accessToken.getAccessToken()));
        }
    }

}
