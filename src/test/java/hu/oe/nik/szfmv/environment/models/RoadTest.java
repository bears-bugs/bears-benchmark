package hu.oe.nik.szfmv.environment.models;

import hu.oe.nik.szfmv.environment.WorldObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoadTest {

    @Test
    public void IsCrossable() {
        Road road = new Road();
        assertTrue(Crossable.class.isInstance(road) );
    }
    @Test
    public void IsWorldObject() {
        Road road = new Road();
        assertTrue(WorldObject.class.isInstance(road) );
    }
}