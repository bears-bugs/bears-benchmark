package hu.oe.nik.szfmv.environment;

import hu.oe.nik.szfmv.environment.models.Road;
import org.junit.Assert;
import org.junit.Test;

public class WorldTest {

    @Test
    public void WorldListNull() {
        World testWorld = new World(0, 0);
        Assert.assertNotNull(testWorld.getWorldObjects());
    }

    @Test
    public void WorldHeightNull() {
        World testWorld = new World(0, 0);
        Assert.assertEquals(testWorld.getHeight(), 0);
    }

    @Test
    public void WorldWidthNull() {
        World testWorld = new World(0, 0);
        Assert.assertEquals(testWorld.getWidth(), 0);
    }

    @Test
    public void WorldSetHeight() {
        World testWorld = new World(0, 0);
        testWorld.setHeight(1);
        Assert.assertEquals(testWorld.getHeight(), 1);
    }

    @Test
    public void WorldListSetWidth() {
        World testWorld = new World(0, 0);
        testWorld.setWidth(1);
        Assert.assertEquals(testWorld.getWidth(), 1);
    }

    @Test
    public void WorldListHasXml() {
        World testWorld = new World(0, 0);
        testWorld.build("");
        Assert.assertTrue(!testWorld.getWorldObjects().isEmpty());
    }

    @Test
    public void WorldListNotNull() {
        World testWorld = new World(0, 0);
        testWorld.addObjectToWorld(new Road());
        Assert.assertTrue(!testWorld.getWorldObjects().isEmpty());
    }

    @Test
    public void WorldListProperAdd() {
        World testWorld = new World(0, 0);
        testWorld.addObjectToWorld(new Road());
        Assert.assertTrue(Road.class.isInstance(testWorld.getWorldObjects().get(0)));
    }
}