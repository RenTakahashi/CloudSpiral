package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MongoClientPoolTest {

    @Before
    public void setUp() {
        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase db = client.getDatabase("memoworld");
        db.createCollection("test");
    }

    @After
    public void tearDown() {
        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase db = client.getDatabase("memoworld");
        db.getCollection("test").drop();
    }


    @Test
    public void getInstance() {
        MongoClientPool a = MongoClientPool.getInstance();
        MongoClientPool b = MongoClientPool.getInstance();
        assertEquals(a, b);
    }

    @Test
    public void collection() {
        MongoClientPool client = MongoClientPool.getInstance();
        assertEquals("memoworld.test", client.collection("test").getNamespace().toString());
    }

    @Test
    public void getDatabase() {
        MongoClientPool client = MongoClientPool.getInstance();
        assertEquals("memoworld", client.getDatabase().getName());
    }
}