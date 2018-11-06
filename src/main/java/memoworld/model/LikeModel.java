package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;
import memoworld.entities.Like;
import memoworld.entities.Likes;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LikeModel implements AutoCloseable {
    private static final String LIKE_KEY = "date";
    private MongoClient client;
    private MongoCollection<Document> collection;

    public LikeModel() {
        this.client =
                new MongoClient("localhost", 27017);
        collection = MongoClientPool.getInstance()
                .collection("likes");
    }

    public void close() {
        this.client.close();
    }

    public Likes list() {
        FindIterable<Document> iterable =
                this.collection.find()
                        .sort(Sorts.descending(LIKE_KEY));

        List<Like> list = new ArrayList<>();
        for (Document doc : iterable) {
            list.add(toLike(doc));
        }
        return new Likes(list);
    }

    private Like toLike(Document doc) {
        return new Like(doc.getDate(LIKE_KEY));
    }

    public Like register(Like like) {
        this.collection.insertOne(
                toDocument(like));
        return like;
    }

    private Document toDocument(Like like) {
        Document document = new Document();
        document.append(LIKE_KEY, like.getDate());
        return document;
    }
}