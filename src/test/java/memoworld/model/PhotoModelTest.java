package memoworld.model;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import memoworld.entities.Location;
import memoworld.entities.Photo;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PhotoModelTest {
    private int itemCount;

    @Before
    public void setUp() throws Exception {
        PhotoModel model = new PhotoModel();
        itemCount = model.newId();
    }

    @After
    public void tearDown() throws Exception {
        MongoCollection<Document> photos = MongoClientPool.getInstance().collection("photos");
        photos.deleteOne(Filters.eq("id", itemCount + 1));
    }

    @Test
    public void Test() {
        PhotoModel model = new PhotoModel();
        assertEquals(itemCount, model.newId());
        Photo photo = new Photo();
        photo.setDate(new Date());
        photo.setLocation(new Location());
        Photo p = model.register(photo);
        assertEquals(itemCount + 1, model.newId());
        Photo p2 = model.findById(p.getId());
        assertEquals(p.getId(), p2.getId());
        assertEquals(p.getDate(), p2.getDate());
        assertEquals(p.getDescription(), p2.getDescription());
        assertEquals(p.getLocation().getLatitude(), p2.getLocation().getLatitude(), 0.000001);
        assertEquals(p.getLocation().getLongitude(), p2.getLocation().getLongitude(), 0.000001);
        assertEquals(p.getRawURI(), p2.getRawURI());
        assertEquals(p.getRawImage(), p2.getRawImage());
        assertEquals(p.getAuthor(), p2.getAuthor());
    }
}