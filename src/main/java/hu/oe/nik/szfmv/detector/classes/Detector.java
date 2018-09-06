package hu.oe.nik.szfmv.detector.classes;

import hu.oe.nik.szfmv.detector.interfaces.ICamera;
import hu.oe.nik.szfmv.detector.interfaces.IRadarUltrasonic;
import hu.oe.nik.szfmv.environment.WorldObject;
import hu.oe.nik.szfmv.environment.models.Collidable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Responsible for Sensor Functionality
 */
public final class Detector implements ICamera, IRadarUltrasonic {

    private static Detector detector;
    private List<WorldObject> worldObjects;

    private Detector(/*List<WorldObject> worldObjects*/) {
        //this.worldObjects = worldObjects;
    }

    /**
     * Creates a triangle from the three points.
     *
     * @param a first point of the triangle
     * @param b second point of the triangle
     * @param c third point of the triangle
     * @return Polygon created from the three points.
     */
    private Polygon createTriangle(Point a, Point b, Point c) {
        Polygon p = new Polygon();
        p.addPoint(a.x, a.y);
        p.addPoint(b.x, b.y);
        p.addPoint(c.x, c.y);

        return p;
    }

    /**
     * Creates a list of WorldObjects which are in the triangle
     *
     * @param a first point of the triangle
     * @param b second point of the triangle
     * @param c third point of the triangle
     * @return List<WorldObject> which contains the object which are in the triangle
     */
    private List<WorldObject> getTheObjectsWhichAreInTheTriangle(Point a, Point b, Point c) {
        Shape sensorVision = createTriangle(a, b, c);
        List<WorldObject> noticeableObjects = new ArrayList<WorldObject>();
        for (int x = 0; x < worldObjects.size(); x++) {
            WorldObject actualObject = worldObjects.get(x);
            if (actualObject.getShape().intersects(sensorVision.getBounds())) {
                noticeableObjects.add(actualObject);
            }
        }
        return noticeableObjects;
    }

    /**
     * Creates a Detector and sets the Detector variable
     * @return Detector responsible for sensor functionality
     */
    public static Detector getDetector() {
        if (detector == null) {

            detector = new Detector();
            return detector;

        } else {
            return detector;
        }
    }

    public void setWorldObjects(List<WorldObject> wo) {
        worldObjects = wo;
    }

    @Override
    public List<WorldObject> getWorldObjects(Point a, Point b, Point c) {
        return getTheObjectsWhichAreInTheTriangle(a, b, c);
    }

    @Override
    public List<Collidable> getCollidableObjects(Point a, Point b, Point c) {
        List<WorldObject> allObjectsInTheTriangle =
                getTheObjectsWhichAreInTheTriangle(a, b, c);
        List<Collidable> noticeableObjects = new ArrayList<Collidable>();
        for (int i = 0; i < allObjectsInTheTriangle.size(); i++) {
            if (allObjectsInTheTriangle.get(i) instanceof Collidable) {
                noticeableObjects.add((Collidable) allObjectsInTheTriangle.get(i));
            }
        }
        return noticeableObjects;
    }
}
