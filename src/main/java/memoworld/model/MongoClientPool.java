package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Optional;

public class MongoClientPool {
    private static final MongoClientPool
            INSTANCE = new MongoClientPool();
    private MongoClient client;
    private MongoDatabase database;

    private MongoClientPool() {
        this.client =
                new MongoClient("localhost", 27017);
        this.database =
                this.client.getDatabase("lama");
        closeOnExit();
    }

    public static MongoClientPool getInstance() {
        return INSTANCE;
    }

    public MongoCollection<Document>
    collection(String name) {
        return database.getCollection(name);
    }

    private void closeOnExit() {
        Optional<MongoClient> mc = Optional.of(client);
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> mc.ifPresent(client -> client.close())));
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
