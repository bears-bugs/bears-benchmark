package hu.oe.nik.szfmv.detector;

import hu.oe.nik.szfmv.detector.classes.Triangle;

import java.awt.Point;

import org.junit.Assert;
import org.junit.Test;


public class TriangleTest {

    @Test
    public void notNull() {
        final Double sensorRange = 3.0;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 0.0;
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertNotNull(actualOutput);
    }

    @Test
    public void rotationOf0Degrees() {
        final Double sensorRange = 3.0;
        final int expectedAX = -3;
        final int expectedAY = 3;
        final int expectedBX = 3;
        final int expectedBY = 3;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 0.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void rotationOf90Degrees() {
        final Double sensorRange = 3.0;
        final int expectedAX = -3;
        final int expectedAY = -3;
        final int expectedBX = -3;
        final int expectedBY = 3;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 90.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void rotationOf180Degrees() {
        final Double sensorRange = 3.0;
        final int expectedAX = 3;
        final int expectedAY = -3;
        final int expectedBX = -3;
        final int expectedBY = -3;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 180.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void rotationOf270Degrees() {
        final Double sensorRange = 3.0;
        final int expectedAX = 3;
        final int expectedAY = 3;
        final int expectedBX = 3;
        final int expectedBY = -3;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 270.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void rotationOf360Degrees() {
        final Double sensorRange = 3.0;
        final int expectedAX = -3;
        final int expectedAY = 3;
        final int expectedBX = 3;
        final int expectedBY = 3;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 360.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void zeroSensorRange() {
        final Double sensorRange = 0.0;
        final int expectedAX = 0;
        final int expectedAY = 0;
        final int expectedBX = 0;
        final int expectedBY = 0;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 0.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void notOriginSensorPosition() {
        final Double sensorRange = 3.0;
        final int originX = 3;
        final int originY = 3;
        final int expectedAX = 0;
        final int expectedAY = 6;
        final int expectedBX = 6;
        final int expectedBY = 6;
        final Double angleOfView = 90.0;
        final Double sensorRotation = 0.0;

        Point[] expectedOutput = new Point[]{new Point(originX, originY), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(3, 3), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void viewAngle120Degrees() {
        final Double sensorRange = 4.0;
        final int expectedAX = -7;
        final int expectedAY = 4;
        final int expectedBX = 7;
        final int expectedBY = 4;
        final Double angleOfView = 120.0;
        final Double sensorRotation = 0.0;

        Point[] expectedOutput = new Point[]{new Point(0, 0), new Point(expectedAX, expectedAY), new Point(expectedBX, expectedBY)};
        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Assert.assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void zeroAngleOfView() {
        final Double sensorRange = 3.0;
        final Double angleOfView = 0.0;
        final Double sensorRotation = 0.0;

        Point[] actualOutput = Triangle.trianglePoints(new Point(0, 0), sensorRange, angleOfView, sensorRotation);
        Point pointA = actualOutput[1];
        Point pointB = actualOutput[2];
        Assert.assertEquals(pointA, pointB);
    }
}
