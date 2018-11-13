package memoworld.model;


import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.util.JSON;

import memoworld.entities.Likes;
import memoworld.model.LikeModel;
import memoworld.entities.Like;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeModel implements AutoCloseable {
    private MongoCollection<Document> lids;
    private MongoCollection<Document> likes;

    public LikeModel() {
        likes = MongoClientPool.getInstance()
                .collection("likes");
        lids = MongoClientPool.getInstance()
                .collection("lids");
    }

    public void close() {
        
    }

    public Like findById(int traveloguesid) {
    	//Document searchDcument = new Document();
    	//searchDcument.append("traveloguesid",traveloguesid );
    	//String before = JSON.serialize(likes.find(searchDocument));
    	
        Document document = likes
                .find(Filters.eq("traveloguesid", traveloguesid))
                .first();
       
    	
        return toLike(document);
    }
	
    public Likes getLikes() {
		List<Like> list = new ArrayList<>();
		this.likes.find().map(LikeModel::toLike).into(list);
		return new Likes(list);
	}

    public boolean deleteLikes(int lid) {
		DeleteResult result = this.likes.deleteOne(Filters.eq("lid", lid));		
		return result.getDeletedCount() > 0;
	}
	
    private static Like toLike(Document document) {

        if (document == null)
            return null;
        Like like = new Like();
        like.setLid(document.getInteger("lid", 0));
        like.setTraveloguesid(document.getInteger("traveloguesid"));
        like.setDate(document.getDate("date"));
        like.setAuthor(document.getString("author"));
        return like;
    }

    private Document toDocument(Like like) {
        if (like == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("lid", like.getLid());
        map.put("traveloguesid", like.getTraveloguesid());
        map.put("author", like.getAuthor());
        map.put("date", like.getDate());
        return new Document(map);
    }

    public int newId() {
        if (lids.count() == 0L)
            return 0;
        return lids.find()
                .sort(Sorts.descending("lid"))
                .first()
                .getInteger("lid", 0);
    }

    public Like register(Like like) {
        like.setLid(newId() + 1);
        likes.insertOne(toDocument(like));
        Document idDoc =
                new Document("lid", like.getLid());
        lids.insertOne(idDoc);
        return like;
    }
}
