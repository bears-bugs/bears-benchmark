package hu.oe.nik.szfmv.automatedcar.sensors;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.environment.World;
import hu.oe.nik.szfmv.environment.models.RoadSign;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UltrasonicSensorTest {

    private static final double THRESHOLD = 0.0001d;
    private static final World w = new World(800, 600);
    private static final AutomatedCar car = new AutomatedCar(0, 0, "");

    /**
     * Initializes the car and the world needed for the tests.
     */
    @Before
    public void initialize() {
        car.setRotation(Math.toRadians(270));
        car.setX(100);
        car.setY(100);
        w.getWorldObjects().clear();

        RoadSign closer = new RoadSign(120, 60, "roadsign_speed_40.png");
        closer.setWidth(30);
        closer.setHeight(40);
        closer.generateShape();
        w.addObjectToWorld(closer);

        RoadSign other = new RoadSign(150, 100, "roadsign_speed_50.png");
        other.generateShape();
        w.addObjectToWorld(other);
    }

    /**
     * Tests the nearest object method of the ultrasonic sensor.
     */
    @Test
    public void testNearestObject() {
        UltrasonicSensor sensor = new UltrasonicSensor(0, 0, 0, car, w);
        assertEquals(sensor.getNearestObject().getImageFileName(), "roadsign_speed_40.png");
    }

    /**
     * Tests the nearest object distance method of the ultrasonic sensor.
     */
    @Test
    public void testNearestObjectDistance() {
        UltrasonicSensor sensor = new UltrasonicSensor(0, 0, 0, car, w);
        int expected = 2000;
        assertEquals(sensor.getNearestObjectDistance(), Math.sqrt(expected), THRESHOLD);
    }

    /**
     * Tests the nearest object dimensions method of the ultrasonic sensor.
     */
    @Test
    public void testNearestObjectDimensions() {
        UltrasonicSensor sensor = new UltrasonicSensor(0, 0, 0, car, w);
        int[] dimensions = sensor.getNearestObjectDimensions();

        int expectedX = 30;
        int expectedY = 40;
        assertNotNull(dimensions);
        assertEquals(dimensions[0], expectedX);
        assertEquals(dimensions[1], expectedY);
    }

    /**
     * Tests if the correct number of sensors are created
     */
    @Test
    public void testSensorCreation() {
        UltrasonicSensor.createUltrasonicSensors(car, w);
        assertEquals(8, car.getUltrasonicSensors().size());
    }
}