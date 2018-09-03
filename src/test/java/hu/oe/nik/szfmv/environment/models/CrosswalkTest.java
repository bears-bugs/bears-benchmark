package hu.oe.nik.szfmv.environment.models;

import hu.oe.nik.szfmv.environment.WorldObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class CrosswalkTest {
    @Test
    public void IsCrossable() {
        Crosswalk crosswalk = new Crosswalk();
        assertTrue(Crossable.class.isInstance(crosswalk) );
    }
    @Test
    public void IsWorldObject() {
        Crosswalk crosswalk = new Crosswalk();
        assertTrue(WorldObject.class.isInstance(crosswalk) );
    }

}