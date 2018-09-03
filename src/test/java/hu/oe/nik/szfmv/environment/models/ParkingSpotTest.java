package hu.oe.nik.szfmv.environment.models;

import hu.oe.nik.szfmv.environment.WorldObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParkingSpotTest {

    @Test
    public void IsCrossable() {
        ParkingSpot parkingSpot = new ParkingSpot();
        assertTrue(Crossable.class.isInstance(parkingSpot) );
    }
    @Test
    public void IsWorldObject() {
        ParkingSpot parkingSpot = new ParkingSpot();
        assertTrue(WorldObject.class.isInstance(parkingSpot) );
    }
}