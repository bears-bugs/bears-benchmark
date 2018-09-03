package hu.oe.nik.szfmv.environment.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class TreeTest {

    @Test
    public void IsStationary() {
        Tree tree = new Tree();
        assertTrue(Stationary.class.isInstance(tree) );
    }
    @Test
    public void IsCollidable() {
        Tree tree = new Tree();
        assertTrue(Collidable.class.isInstance(tree) );
    }
    @Test
    public void IsWorldObject() {
        Tree tree = new Tree();
        assertTrue(Collidable.class.isInstance(tree) );
    }
}