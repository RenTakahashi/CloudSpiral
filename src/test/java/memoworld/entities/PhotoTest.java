package memoworld.entities;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class PhotoTest {

    @Test
    public void Test() {
        Photo p = new Photo();
        int id = 1;
        Date date = new Date();
        String desc = "foo";
        String location = "bar";
        String raw_uri = "baz";
        String raw_image = "/vtIGhuilKGYU=";

        p.setId(id);
        p.setDate(date);
        p.setDescription(desc);
        p.setLocation(location);
        p.setRawURI(raw_uri);
        p.setRawImage(raw_image);
        assertEquals(id, p.getId());
        assertEquals(date, p.getDate());
        assertEquals(desc, p.getDescription());
        assertEquals(location, p.getLocation());
        assertEquals(raw_uri, p.getRawURI());
        assertEquals(raw_image, p.getRawImage());
    }
}