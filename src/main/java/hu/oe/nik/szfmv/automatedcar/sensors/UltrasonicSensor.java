package hu.oe.nik.szfmv.automatedcar.sensors;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.common.Utils;
import hu.oe.nik.szfmv.detector.classes.Detector;
import hu.oe.nik.szfmv.detector.classes.Triangle;
import hu.oe.nik.szfmv.environment.World;
import hu.oe.nik.szfmv.environment.models.Collidable;

import java.util.List;

import java.awt.*;

public class UltrasonicSensor {

    private static final int F_ROT = 0;
    private static final int R_ROT = 90;
    private static final int B_ROT = 180;
    private static final int L_ROT = 270;
    private static final int FBL_X = -25;
    private static final int FBR_X = 25;
    private static final int FRONT_Y = -115;
    private static final int BACK_Y = 115;
    private static final int RIGHT_X = 45;
    private static final int LEFT_X = -45;
    private static final int RLF_Y = -70;
    private static final int RLB_Y = 70;

    private int relativeX;
    private int relativeY;
    private double relativeRotation;

    private double sensorRange = Utils.convertMeterToPixel(3);
    private double angleOfView = 100;

    private AutomatedCar car;
    private World world;

    /**
     * Constructor for the ultrasonic sensor
     * @param relativeX the sensor's X coordinate relative to the car
     * @param relativeY the sensor's Y coordinate relative to the car
     * @param relativeRotation the sensor's rotation relative to the car (in degrees)
     * @param car the car the sensor belongs to
     * @param world the world the car (and the sensor) is in
     */
    public UltrasonicSensor(int relativeX, int relativeY, double relativeRotation, AutomatedCar car, World world) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.relativeRotation = relativeRotation;
        this.car = car;
        this.world = world;
    }

    /**
     * Creates the ultrasonic sensors and adds them to the car's list of ultrasonic sensors.
     * @param car the car the sensors belong to
     * @param world the world the sensors are in
     */
    public static void createUltrasonicSensors(AutomatedCar car, World world) {
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(FBL_X, FRONT_Y, F_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(FBR_X, FRONT_Y, F_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(FBL_X, BACK_Y, B_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(FBR_X, BACK_Y, B_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(RIGHT_X, RLF_Y, R_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(RIGHT_X, RLB_Y, R_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(LEFT_X, RLF_Y, L_ROT, car, world));
        car.getUltrasonicSensors().add(
                new UltrasonicSensor(LEFT_X, RLB_Y, L_ROT, car, world));
    }

    /**
     * Gets the collidable object nearest to the sensor.
     * @return the collidable object that is closest to the sensor, null if no such object exists.
     */
    public Collidable getNearestObject() {
        Point[] triangle = getTriangleForSensor();
        Detector detector = Detector.getDetector();
        List<Collidable> collidables = detector.getCollidableObjects(triangle[0], triangle[1], triangle[2]);
        double minDistance = Double.MAX_VALUE;
        Collidable nearestObject = null;
        for (Collidable collidable : collidables) {
            double distance = Utils.getDistanceBetweenTwoPoints(collidable.getX(), collidable.getY(),
                    getX(), getY());
            if (distance < minDistance) {
                minDistance = distance;
                nearestObject = collidable;
            }
        }

        return nearestObject;
    }

    /**
     * Gets the distance of the closest collidable object to the sensor.
     * @return the distance between the sensor and the closest collidable object, null if no such object exists.
     */
    public Double getNearestObjectDistance() {
        Collidable nearestObject = getNearestObject();
        if (nearestObject != null) {
            return Utils.getDistanceBetweenTwoPoints(getX(), getY(), nearestObject.getX(), nearestObject.getY());
        }

        return null;
    }

    /**
     * Gets the dimensions of the nearest collidable object.
     * @return an int[] array where the first element is the width and the second element is the height of the object,
     * null if no such object exists.
     */
    public int[] getNearestObjectDimensions() {
        Collidable nearestObject = getNearestObject();
        if (nearestObject != null) {
            int[] dimensions = { nearestObject.getWidth(), nearestObject.getHeight() };
            return dimensions;
        }

        return null;
    }

    /**
     * Updates the sensor according to car movement
     */
    public void updateSensor() {
        double carDegree = Math.toDegrees(car.getRotation());
        double angleDifference = relativeRotation - carDegree;
        relativeRotation = carDegree + angleDifference;

        relativeX = (int)(Math.cos(angleDifference) * (relativeX - car.getX()) - Math.sin(angleDifference)
                * (relativeY - car.getY()) + car.getX());
        relativeY = (int)(Math.sin(angleDifference) * (relativeX - car.getX()) - Math.cos(angleDifference)
                * (relativeY - car.getY()) + car.getY());
    }

    /**
     * Gets the triangle for our sensor.
     * @return a Point[] array containing the 3 points of the triangle created for our sensor
     */
    private Point[] getTriangleForSensor() {
        Point sensorPosition = new Point(getX(), getY());
        double sensorRotation = getRotation();
        return Triangle.trianglePoints(sensorPosition, sensorRange, angleOfView, sensorRotation);
    }

    /**
     * Gets the sensor's X coordinate.
     * @return the X coordinate of the sensor
     */
    private int getX() {
        return car.getX() + relativeX;
    }

    /**
     * Gets the sensor's Y coordinate.
     * @return the Y coordinate of the sensor
     */
    private int getY() {
        return car.getY() + relativeY;
    }

    /**
     * Gets the sensor's rotation.
     * @return the rotation of the sensor
     */
    private double getRotation() {
        return Math.toDegrees(car.getRotation()) + relativeRotation;
    }
}
