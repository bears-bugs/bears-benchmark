package org.traccar.geofence;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;

public class GeofencePolygonTest {

    @Test
    public void testPolygonWkt() throws ParseException {
        String test = "POLYGON ((55.75474 37.61823, 55.75513 37.61888, 55.7535 37.6222, 55.75315 37.62165))";
        GeofenceGeometry geofenceGeometry = new GeofencePolygon();
        geofenceGeometry.fromWkt(test);
        Assert.assertEquals(geofenceGeometry.toWkt(), test);
    }

    @Test
    public void testContainsPolygon() throws ParseException {
        String test = "POLYGON ((55.75474 37.61823, 55.75513 37.61888, 55.7535 37.6222, 55.75315 37.62165))";
        GeofenceGeometry geofenceGeometry = new GeofencePolygon();
        geofenceGeometry.fromWkt(test);
        Assert.assertTrue(geofenceGeometry.containsPoint(55.75476, 37.61915));
        Assert.assertTrue(!geofenceGeometry.containsPoint(55.75545, 37.61921));

    }
    
    @Test
    public void testContainsPolygon180() throws ParseException {
        String test = "POLYGON ((66.9494 179.838, 66.9508 -179.8496, 66.8406 -180.0014))";
        GeofenceGeometry geofenceGeometry = new GeofencePolygon();
        geofenceGeometry.fromWkt(test);
        Assert.assertTrue(geofenceGeometry.containsPoint(66.9015, -180.0096));
        Assert.assertTrue(geofenceGeometry.containsPoint(66.9015, 179.991));
        Assert.assertTrue(!geofenceGeometry.containsPoint(66.8368, -179.8792));

    }
    
    @Test
    public void testContainsPolygon0() throws ParseException {
        String test = "POLYGON ((51.1966 -0.6207, 51.1897 0.4147, 50.9377 0.5136, 50.8675 -0.6082))";
        GeofenceGeometry geofenceGeometry = new GeofencePolygon();
        geofenceGeometry.fromWkt(test);
        Assert.assertTrue(geofenceGeometry.containsPoint(51.0466, -0.0165));
        Assert.assertTrue(geofenceGeometry.containsPoint(51.0466, 0.018));
        Assert.assertTrue(!geofenceGeometry.containsPoint(50.9477, 0.5836));

    }

}
