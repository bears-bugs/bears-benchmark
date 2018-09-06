package hu.oe.nik.szfmv.automatedcar.steering;

import hu.oe.nik.szfmv.automatedcar.AutomatedCar;
import hu.oe.nik.szfmv.automatedcar.SteeringMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;

public class WheelPositionTest extends AutomatedCar {
    private double THRESHOLD;
    private Point2D carPosition;
    private double wheelBase;
    private double eastRotation = Math.toRadians(0);
    private double northRotation = Math.toRadians(-90);
    private double northEastRotation = Math.toRadians(-45);

    public WheelPositionTest() {
        super(0, 0, null);
    }

    @Before
    public void setUp() {
        THRESHOLD = 0.0001d;
        carPosition = new Point2D.Double(0, 0);
        wheelBase = 100;
    }

    @Test
    public void FacingEastFrontWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D frontWheelPos = SteeringMethods.getFrontWheel(eastRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] frontWheelArray = {frontWheelPos.getX(), frontWheelPos.getY()};
            double[] expected = {wheelBase / 2, 0d};
            Assert.assertArrayEquals(expected, frontWheelArray, THRESHOLD);
        }
    }

    @Test
    public void FacingEastBackWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D backWheelPos = SteeringMethods.getBackWheel(eastRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] backWheelArray = {backWheelPos.getX(), backWheelPos.getY()};
            double[] expected = {-wheelBase / 2, 0d};
            Assert.assertArrayEquals(expected, backWheelArray, THRESHOLD);
        }
    }

    @Test
    public void FacingNorthFrontWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D frontWheelPos = SteeringMethods.getFrontWheel(northRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] frontWheelArray = {frontWheelPos.getX(), frontWheelPos.getY()};
            double[] expected = {0, -wheelBase / 2};
            Assert.assertArrayEquals(expected, frontWheelArray, THRESHOLD);
        }
    }

    @Test
    public void FacingNorthBackWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D backWheelPos = SteeringMethods.getBackWheel(northRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] backWheelArray = {backWheelPos.getX(), backWheelPos.getY()};
            double[] expected = {0, wheelBase / 2};
            Assert.assertArrayEquals(expected, backWheelArray, THRESHOLD);
        }
    }

    @Test
    public void FacingNorthEastFrontWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D frontWheelPos = SteeringMethods.getFrontWheel(northEastRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] frontWheelArray = {frontWheelPos.getX(), frontWheelPos.getY()};
            double[] expected = {35.3553, -35.3553}; //calc'd using http://www.cleavebooks.co.uk/scol/calrtri.htm
            Assert.assertArrayEquals(expected, frontWheelArray, THRESHOLD);
        }

    }

    @Test
    public void FacingNorthEastBackWheelTest() {
        for (int i = -10; i <= 10; i++) {
            double multipleOf360 = Math.toRadians(360) * i;

            Point2D backWheelPos = SteeringMethods.getBackWheel(northEastRotation + multipleOf360,
                    wheelBase / 2, carPosition);
            double[] backWheelArray = {backWheelPos.getX(), backWheelPos.getY()};
            double[] expected = {-35.3553, +35.3553}; //calc'd using http://www.cleavebooks.co.uk/scol/calrtri.htm
            Assert.assertArrayEquals(expected, backWheelArray, THRESHOLD);
        }
    }

}
