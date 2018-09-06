package hu.oe.nik.szfmv.detector.classes;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static java.lang.Math.round;

import java.awt.Point;

/**
 * Create a Isosceles triangle
 */
public class Triangle {

    /**
     * @param sensorPosition - Start point of the sensor (Point)
     * @param sensorRange -view range of the sensor (Double)
     * @param angleOfView - Angle of view in degrees (Double)
     * @param sensorRotation - in degrees (Double)
     * @return a Point array with 3 elemtns. First element: start point, second element: A point, third element: B
     */
    public static Point[] trianglePoints(Point sensorPosition, Double sensorRange,
                                         Double angleOfView, Double sensorRotation) {
        final Double utilAngle = 90.0;
        final int numberOfPoints = 3;
        Point[] triangle = new Point[numberOfPoints];

        Double triangleEdge = sensorRange / cos(toRadians(angleOfView / 2));

        Point pointA = getPolarPoint(triangleEdge, utilAngle + angleOfView / 2 + sensorRotation);
        Point pointB = getPolarPoint(triangleEdge, utilAngle - angleOfView / 2 + sensorRotation);

        pointA = movePoint(pointA, sensorPosition);
        pointB = movePoint(pointB, sensorPosition);

        triangle[0] = sensorPosition;
        triangle[1] = pointA;
        triangle[2] = pointB;
        return triangle;
    }

    /**
     * @param distance - view range of the sensor
     * @param angle - angle of view in degrees
     * @return A point from polar coordinate system
     */
    private static Point getPolarPoint(Double distance, Double angle) {
        Double x = (distance * cos(toRadians(angle)));
        Double y = (distance * sin(toRadians(angle)));
        return new Point((int)round(x.doubleValue()), (int)round(y.doubleValue()));
    }

    /**
     * @param originalPoint - point in polar coordinate system
     * @param transformationPoint - new origo
     * @return the original point with moved coordinates
     */
    private static Point movePoint(Point originalPoint, Point transformationPoint) {
        return new Point(originalPoint.x + transformationPoint.x, originalPoint.y + transformationPoint.y);
    }
}
