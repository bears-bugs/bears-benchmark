package hu.oe.nik.szfmv.automatedcar.steering;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.SteeringMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

public class WheelDisplacementTest extends AutomatedCar {
    private double FPS;
    private double THRESHOLD;

    public WheelDisplacementTest() {
        super(0, 0, null);
    }

    @Before
    public void setUp() {
        FPS = 24;
        THRESHOLD = 0.0001d;
    }

    @Test
    public void RestingInPlaceTest() {
        double carHeading = 0;
        double wheelHeading = 0;
        double speed = 0;

        Point2D displacement = SteeringMethods.getFrontWheelDisplacement(carHeading, wheelHeading, speed, FPS);
        double[] displacementArray = {displacement.getX(), displacement.getY()};
        double[] expectedArray = {0, 0};

        Assert.assertArrayEquals(expectedArray, displacementArray, THRESHOLD);
    }

    @Test
    public void GoingForwardTest() {
        double carHeading = 0;
        double wheelHeading = 0;

        for (int n = -100; n <= 100; n++) {

            double[] position = {0, 0};

            //run for 1 second <-> 24 frames
            for (int i = 0; i < FPS; i++) {
                Point2D displacement = SteeringMethods.getFrontWheelDisplacement(carHeading, wheelHeading, n, FPS);
                double[] displacementArray = {displacement.getX(), displacement.getY()};
                position[0] += displacementArray[0];
                position[1] += displacementArray[1];
            }

            double[] expectedArray = {n, 0};
            Assert.assertArrayEquals(expectedArray, position, THRESHOLD);
        }
    }

    @Test
    public void GoingBackwardTest() {
        double carHeading = 0;
        double wheelHeading = 0;

        for (int n = -100; n <= 100; n++) {

            double[] position = {0, 0};

            for (int i = 0; i < FPS; i++) {
                Point2D displacement = SteeringMethods.getFrontWheelDisplacement(carHeading, wheelHeading, -n, FPS);
                double[] displacementArray = {displacement.getX(), displacement.getY()};
                position[0] -= displacementArray[0];
                position[1] -= displacementArray[1];
            }

            double[] expectedArray = {n, 0};
            Assert.assertArrayEquals(expectedArray, position, THRESHOLD);
        }
    }
}
