package memoworld.rest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import memoworld.entities.AccessToken;
import memoworld.entities.Account;
import memoworld.entities.Authentication;
import memoworld.entities.ErrorMessage;
import memoworld.model.AccessTokenModel;
import memoworld.model.AccountModel;
import memoworld.model.MongoClientPool;
import org.bson.Document;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class AuthenticationRestTest extends JerseyTest {
    private static final Account TEST_ACCOUNT = new Account("Password", "Test User", "testuser");
    private static final Authentication TEST_REQUEST_BODY = new Authentication();

    static {
        TEST_REQUEST_BODY.setUserId(TEST_ACCOUNT.getUser_id());
        TEST_REQUEST_BODY.setPassword(TEST_ACCOUNT.getPassword());
    }

    private int accessTokenCount;

    @Test
    public void getAccessTokenTest() {
        Response response = post(TEST_REQUEST_BODY);
        assertThat(response.getStatus(), is(200));
        AccessToken token = response.readEntity(AccessToken.class);
        assertNotNull(token);
        assertThat(token.getAccessToken(), not(isEmptyOrNullString()));
        assertNotNull(token.getExpiresAt());
        assertEquals(token.getTokenType(), "Bearer");
    }

    @Test
    public void getTwiceTest() {
        Response firstResponse = post(TEST_REQUEST_BODY);
        assertThat(firstResponse.getStatus(), is(200));
        AccessToken firstResposedToken = firstResponse.readEntity(AccessToken.class);

        Response secondResponse = post(TEST_REQUEST_BODY);
        assertThat(secondResponse.getStatus(), is(200));
        AccessToken secondResposedToken = secondResponse.readEntity(AccessToken.class);

        assertNotEquals(firstResposedToken.getAccessToken(), secondResposedToken.getAccessToken());
        assertThat(secondResposedToken.getExpiresAt(), greaterThan(firstResposedToken.getExpiresAt()));
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // テスト用ユーザーの登録
        try (AccountModel model = new AccountModel()) {
            model.register(TEST_ACCOUNT);
        }

        try (AccessTokenModel model = new AccessTokenModel()) {
            accessTokenCount = model.newId();
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        MongoCollection<Document> tokens = MongoClientPool.getInstance().collection("access_tokens");
        tokens.deleteMany(Filters.gte("id", accessTokenCount));
    }

    @Test
    public void unexistingUserTest() {
        Authentication authentication = new Authentication();
        authentication.setUserId("unexistinguser");
        authentication.setPassword(TEST_ACCOUNT.getPassword());

        Response response = post(authentication);
        assertEquals(response.getStatus(), 401);
        ErrorMessage message = response.readEntity(ErrorMessage.class);
        assertThat(message.getMessage(),
                   allOf(containsString("user_id"), containsString("wrong")));
    }

    private Response post(Authentication body) {
        return target("/authentication")
                .request()
                .post(Entity.entity(body, MediaType.APPLICATION_JSON));
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(AuthenticationRest.class);
    }
}
