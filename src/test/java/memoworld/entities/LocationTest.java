package memoworld.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LocationTest {

    @Test
    public void Test() {
        double latitude = 35.402949;
        double longitude = 135.171579;
        Location location1 = new Location();
        Location location2 = new Location(latitude, longitude);

        location1.setLatitude(latitude);
        location1.setLongitude(longitude);
        assertEquals(latitude, location1.getLatitude(), 0.000001);
        assertEquals(longitude, location1.getLongitude(), 0.000001);
        assertEquals(location1.getLatitude(), location2.getLatitude(), 0.000001);
        assertEquals(location1.getLongitude(), location2.getLongitude(), 0.000001);
    }
}
