package memoworld.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class PhotoTest {

    @Test
    public void Test() {
        Photo p = new Photo();
        int id = 1;
        Date date = new Date();
        String desc = "foo";
        Location location = new Location(35.402949, 135.171579);
        String raw_uri = "baz";
        String raw_image = "/vtIGhuilKGYU=";
        String author = "bar";

        p.setId(id);
        p.setDate(date);
        p.setDescription(desc);
        p.setLocation(location);
        p.setRaw_uri(raw_uri);
        p.setRaw(raw_image);
        p.setAuthor(author);
        assertEquals(id, p.getId());
        assertEquals(date, p.getDate());
        assertEquals(desc, p.getDescription());
        assertEquals(location, p.getLocation());
        assertEquals(raw_uri, p.getRaw_uri());
        assertEquals(raw_image, p.getRaw());
        assertEquals(author, p.getAuthor());
    }
}
