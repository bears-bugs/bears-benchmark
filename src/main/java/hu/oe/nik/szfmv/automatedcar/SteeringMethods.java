package hu.oe.nik.szfmv.automatedcar;

import java.awt.geom.Point2D;

public class SteeringMethods {

    /**
     * Returns the position of the car based on its two wheels by calculating the middle point between two points
     *
     * @param frontWheel Position of the front wheel
     * @param backWheel  Position of the back wheel
     * @return Position of the car based on its wheels
     */
    public static double getCarHeading(Point2D frontWheel, Point2D backWheel) {
        return Math.atan2(frontWheel.getY() - backWheel.getY(), frontWheel.getX() - backWheel.getX());
    }

    /**
     * Get [-100,100] percent and it give back a value which between -60 and 60 degree.
     *
     * @param wheelPosition in percent form.
     * @return steeringAngle between -60 and 60 degree.
     * @throws Exception wrong parameter Exception
     */
    public static double getSteerAngle(double wheelPosition) throws Exception {

        final double maxLeft = 100d;
        final double maxRight = -100d;

        if (wheelPosition > maxLeft || wheelPosition < maxRight) {
            throw new Exception();
        }

        // From -60 to 60 degree
        double steerAngle;
        final double MULTIPLIER = -0.6;


        steerAngle = wheelPosition * MULTIPLIER;
        steerAngle = Math.toRadians(steerAngle);
        return steerAngle;
    }

    /**
     * Get front wheel position before moving
     *
     * @param carHeading    Car rotation
     * @param halfWheelBase Distance between wheels
     * @param carPosition   Car position, x and y point
     * @return front wheel position
     **/
    public static Point2D getFrontWheel(double carHeading, double halfWheelBase, Point2D carPosition) {
        return new Point2D.Double((Math.cos(carHeading) * halfWheelBase) + carPosition.getX(),
                (Math.sin(carHeading) * halfWheelBase) + carPosition.getY());
    }

    /**
     * Get back wheel position before moving
     *
     * @param carHeading    Car rotation
     * @param halfWheelBase Distance between wheels
     * @param carPosition   Car position, x and y point
     * @return back wheel position
     **/
    public static Point2D getBackWheel(double carHeading, double halfWheelBase, Point2D carPosition) {
        return new Point2D.Double(carPosition.getX() - (Math.cos(carHeading) * halfWheelBase),
                carPosition.getY() - (Math.sin(carHeading) * halfWheelBase));
    }

    /**
     * Get back wheel displacement after moving
     *
     * @param carHeading Car rotation
     * @param speed      Car actual speed
     * @param fps        Display fps
     * @return Back wheel displacement after moving
     **/
    public static Point2D getBackWheelDisplacement(double carHeading, double speed, double fps) {
        return new Point2D.Double(Math.cos(carHeading) * speed * (1 / fps),
                (Math.sin(carHeading) * speed * (1 / fps)));
    }

    /**
     * Get front wheel displacement after moving
     *
     * @param carHeading   Car rotation
     * @param speed        Car actual speed
     * @param angularSpeed Car steering angle
     * @param fps          Display fps
     * @return Front wheel displacement after moving
     **/
    public static Point2D getFrontWheelDisplacement(double carHeading, double angularSpeed, double speed, double fps) {
        return new Point2D.Double(Math.cos(carHeading + angularSpeed) * speed * (1 / fps),
                (Math.sin(carHeading + angularSpeed) * speed * (1 / fps)));
    }

    /**
     * Get new front wheel position
     *
     * @param frontWheel             Old front wheel position
     * @param frontWheelDisplacement Front wheel displacement
     * @return New front position
     */
    public static Point2D getNewFrontWheelPosition(Point2D frontWheel, Point2D frontWheelDisplacement) {
        return new Point2D.Double(frontWheel.getX() + frontWheelDisplacement.getX(),
                frontWheel.getY() + frontWheelDisplacement.getY());
    }

    /**
     * Get new back wheel position
     *
     * @param backWheel             Old back wheel position
     * @param backWheelDisplacement Back wheel displacement
     * @return New back position
     */
    public static Point2D getNewBackWheelPosition(Point2D backWheel, Point2D backWheelDisplacement) {
        return new Point2D.Double(backWheel.getX() + backWheelDisplacement.getX(),
                backWheel.getY() + backWheelDisplacement.getY());
    }

    /**
     * Get back new car position
     *
     * @param frontWheel Front wheel position
     * @param backWheel  Back wheel position
     * @return Car position
     **/
    public static Point2D getCarPosition(Point2D frontWheel, Point2D backWheel) {
        return new Point2D.Double((frontWheel.getX() + backWheel.getX()) / 2,
                (frontWheel.getY() + backWheel.getY()) / 2);
    }

}
