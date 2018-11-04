package memoworld.rest;

import com.mongodb.util.JSON;
import memoworld.entities.Location;
import memoworld.entities.Photo;
import memoworld.model.PhotoModel;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import java.util.Date;

import static org.junit.Assert.*;

// TODO: いい感じに正しい画像データを投げるテストもする
public class PhotoRestTest {

    @Test
    public void badImage() {
        Client client = ClientBuilder.newClient();
        Photo p = new Photo();
        p.setDate(new Date());
        p.setLocation(new Location());
        p.setDescription("foo");
        p.setAuthor("bar");
        p.setRawImage("/invalid=");
        Response result = client.target("http://localhost:8080")
                .path("/memoworld/api/photos")
                .request()
                .post(Entity.json(p));
        assertEquals(400, result.getStatus());
    }

    @Test
    public void getPhoto() {
        PhotoModel model = new PhotoModel();
        int id = model.newId();
        if (id != 0) {
            Client client = ClientBuilder.newClient();
            Response result = client.target("http://localhost:8080")
                    .path("/memoworld/api/photos/" + Integer.toString(id))
                    .request()
                    .get();
            assertEquals(200, result.getStatus());
        }
    }
}