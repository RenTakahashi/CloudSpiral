package memoworld.rest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import memoworld.model.AccessTokenModel;
import memoworld.model.MongoClientPool;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotEquals;

public class AuthenticationRestTest {
    private static final String TEST_REQUEST_BODY = "{\"user_id\":\"test\",\"password\":\"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05\"}";

    private int accessTokenCount;

    @Test
    @Ignore
    public void getAccessTokenTest() {
        post(TEST_REQUEST_BODY)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .body("access_token", not(isEmptyOrNullString()))
                .body("expires_at", notNullValue())
                .body("token_type", is("Bearer"));
    }

    @Test
    @Ignore
    public void getTwiceTest() {
        String firstResponse = post(TEST_REQUEST_BODY).asString();
        String secondResponse = post(TEST_REQUEST_BODY).asString();

        assertNotEquals(firstResponse, secondResponse);
    }

    @Before
    public void setUp() {
        RestAssured.basePath = "/memoworld/api";

        try (AccessTokenModel model = new AccessTokenModel()) {
            accessTokenCount = model.newId();
        }
    }

    @After
    public void tearDown() {
        MongoCollection<Document> tokens = MongoClientPool.getInstance().collection("access_tokens");
        tokens.deleteMany(Filters.gte("id", accessTokenCount));
    }

    private Response post(String body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/authentication");
    }
}
