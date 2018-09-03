package hu.oe.nik.szfmv.environment;

import hu.oe.nik.szfmv.detector.classes.Detector;
import hu.oe.nik.szfmv.environment.interfaces.IWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class World implements IWorld {
    private static final Logger LOGGER = LogManager.getLogger();

    private int width = 0;
    private int height = 0;
    private List<WorldObject> worldObjects = new ArrayList<>();
    Detector d;

    /**
     * Creates the virtual world with the given dimension.
     * To populate the world with objects from xml use the build(String xmlLocation) function
     *
     * @param width  the width of the virtual world
     * @param height the height of the virtual world
     */
    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.build("src/main/resources/test.xml");
        //create detector
        d = Detector.getDetector();
        //set detector's list
        d.setWorldObjects(getWorldObjects());
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public List<WorldObject> getWorldObjects() {
        return worldObjects;
    }

     /**
     * Add an object to the virtual world.
     *
     * @param o {@link WorldObject} to be added to the virtual world
     */
    public void addObjectToWorld(WorldObject o) {
        worldObjects.add(o);
    }

    @Override
    public void build(String xmlLocation) {
        try {
            worldObjects = XmlToModelConverter.build(xmlLocation);
        } catch (Exception ex) {
            LOGGER.info("Error in World build - " + ex.getMessage());
        }
    }


}
