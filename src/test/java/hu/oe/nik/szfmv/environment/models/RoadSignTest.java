package hu.oe.nik.szfmv.environment.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoadSignTest {

    @Test
    public void IsStationary() {
        RoadSign roadSign = new RoadSign();
        assertTrue(Stationary.class.isInstance(roadSign) );
    }
    @Test
    public void IsCollidable() {
        RoadSign roadSign = new RoadSign();
        assertTrue(Collidable.class.isInstance(roadSign) );
    }
    @Test
    public void IsWorldObject() {
        RoadSign roadSign = new RoadSign();
        assertTrue(Collidable.class.isInstance(roadSign) );
    }

}