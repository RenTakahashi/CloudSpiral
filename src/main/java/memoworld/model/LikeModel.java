package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import memoworld.entities.Likes;
import memoworld.model.LikeModel;
import memoworld.entities.Like;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeModel implements AutoCloseable {
    private MongoCollection<Document> ids;
    private MongoCollection<Document> likes;
    private MongoClient client;

    public LikeModel() {
        this.client =
                new MongoClient("localhost", 27017);
        likes = MongoClientPool.getInstance()
                .collection("likes");
        ids = MongoClientPool.getInstance()
                .collection("ids");
    }

    public void close() {
        this.client.close();
    }

    public Like findById(int id) {

        Document document = likes
                .find(Filters.eq("id", id))
                .first();

        return toLike(document);
    }
	
    public Likes getLikes() {
		List<Like> list = new ArrayList<>();
		this.likes.find().map(LikeModel::toLike).into(list);
		return new Likes(list);
	}

    public boolean deleteLikes(int id) {
		DeleteResult result = this.likes.deleteOne(Filters.eq("id", id));		
		return result.getDeletedCount() > 0;
	}
	
    private static Like toLike(Document document) {

        if (document == null)
            return null;
        Like like = new Like();
        like.setId(document.getInteger("id", 0));
        like.setAuthor(document.getString("author"));
        like.setDate(document.getDate("date"));
        return like;
    }

    private Document toDocument(Like like) {
        if (like == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", like.getId());
        map.put("author", like.getAuthor());
        map.put("date", like.getDate());
        return new Document(map);
    }

    public int newId() {
        if (ids.count() == 0L)
            return 0;
        return ids.find()
                .sort(Sorts.descending("id"))
                .first()
                .getInteger("id", 0);
    }

    public Like register(Like like) {
        like.setId(newId() + 1);
        likes.insertOne(toDocument(like));
        Document idDoc =
                new Document("id", like.getId());
        ids.insertOne(idDoc);
        return like;
    }
}
