package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import memoworld.entities.Photo;
import org.bson.Document;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoModel implements AutoCloseable {
    private MongoCollection<Document> photos;

    public PhotoModel() {
        photos = MongoClientPool.getInstance().collection("photos");
    }

    public void close() {
    }

    private Document toDocument(Photo photo) {
        if (photo == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", photo.getId());
        map.put("location", photo.getLocation());
        map.put("description", photo.getDescription());
        map.put("date", photo.getDate());
        map.put("raw_uri", photo.getRawURI());
        return new Document(map);
    }

    public int newId() {
        if (photos.count() == 0L)
            return 0;
        return photos.find()
                .sort(Sorts.descending("id"))
                .first()
                .getInteger("id", 0);
    }

    public Photo register(Photo photo) {
        photo.setId(newId() + 1);
        photos.insertOne(toDocument(photo));
        return photo;
    }
}
