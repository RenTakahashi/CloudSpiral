package memoworld.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import memoworld.entities.Location;
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

    public Photo findById(int id) {

        Document document = photos
                .find(Filters.eq("id", id))
                .first();

        return toPhoto(document);
    }

    private static Photo toPhoto(Document document) {

        if (document == null)
            return null;
        Photo photo = new Photo();
        photo.setId(document.getInteger("id", 0));
        photo.setDate(document.getDate("date"));
        photo.setDescription(document.getString("description"));
        photo.setLocation(new Location(document.getDouble("latitude"), document.getDouble("longitude")));
        photo.setAuthor(document.getString("author"));
        photo.setRawURI(document.getString("raw_uri"));
        photo.setRawImage(document.getString("raw_image"));
        return photo;
    }

    private Document toDocument(Photo photo) {
        if (photo == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", photo.getId());
        map.put("latitude", photo.getLocation().getLatitude());
        map.put("longitude", photo.getLocation().getLongitude());
        map.put("description", photo.getDescription());
        map.put("author", photo.getAuthor());
        map.put("date", photo.getDate());
        map.put("raw_uri", photo.getRawURI());
        map.put("raw_image", photo.getRawImage());
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
        photo.setRawURI("/photos/" + Integer.toString(photo.getId()) + "/raw");
        photos.insertOne(toDocument(photo));
        return photo;
    }
}
