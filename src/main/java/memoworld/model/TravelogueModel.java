package memoworld.model;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.DeleteResult;

import memoworld.entities.Travelogues;
import memoworld.model.TravelogueModel;
import memoworld.entities.Travelogue;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TravelogueModel implements AutoCloseable {
    private MongoCollection<Document> ids;
    public MongoCollection<Document> travelogues;

    public TravelogueModel() {
        travelogues = MongoClientPool.getInstance()
                .collection("travelogues");
        ids = MongoClientPool.getInstance()
                .collection("ids");
    }

    public void close() {
    }
    
    public Travelogue findById(int id) {
        Document document = travelogues
                .find(Filters.eq("id", id))
                .first();
        return toTravelogue(document);
    }
    
    //traveloguesの要素数を返す
	public long Collcount() {
		long count = travelogues.count();
		return count;
	}
    
    
    public Travelogues getTravelogues() {
		List<Travelogue> list = new ArrayList<>();
		this.travelogues.find().map(TravelogueModel::toTravelogue).into(list);
	
		return new Travelogues(list);
	}
    
   
    
    public boolean deleteTravelogues(int id) {
		DeleteResult result = this.travelogues.deleteOne(Filters.eq("id", id));		
		return result.getDeletedCount() > 0;
	}
	
    private static Travelogue toTravelogue(Document document) {

        if (document == null)
            return null;
        Travelogue travel = new Travelogue();
        travel.setId(document.getInteger("id", 0));
        travel.setTitle(document.getString("title"));
        travel.setDate(document.getDate("date"));
        travel.setAuthor(document.getString("author"));
        travel.setPhotos_id((ArrayList<Integer>) document.get("photos_id"));
        return travel;
    }

    
    private Document toDocument(Travelogue travelogue) {
        if (travelogue == null)
            return null;
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", travelogue.getId());
        map.put("title", travelogue.getTitle());
        map.put("date", travelogue.getDate());
        map.put("author", travelogue.getAuthor());
        map.put("photos_id",travelogue.getPhotos_id());
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

    public Travelogue register(Travelogue travelogue) {
        travelogue.setId(newId() + 1);
        travelogues.insertOne(toDocument(travelogue));
        Document idDoc =
                new Document("id", travelogue.getId());
        ids.insertOne(idDoc);
        return travelogue;
    }
}
