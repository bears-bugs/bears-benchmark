package hu.oe.nik.szfmv.common;

public final class Utils {

    private static final int METER_PIXEL_RATIO = 50;

    /**
     * Converts the length defined in pixels to meters according the rule of 350 pixel = 7 meters.
     *
     * @param pixel is the length in pixels
     * @return the length in meters
     */
    public static double convertPixelToMeter(int pixel) {
        return pixel / METER_PIXEL_RATIO;
    }

    /**
     * Converts the length defined in meters to pixels according the rule of 350 pixel = 7 meters.
     *
     * @param meters is the length in meters
     * @return the length in pixels
     */
    public static double convertMeterToPixel(int meters) {
        return meters * METER_PIXEL_RATIO;
    }


    // Copied from https://github.com/SzFMV2017-Tavasz/AutomatedCar
    // /src/main/java/hu/oe/nik/szfmv17t/environment/utils/XmlParser.java#L257

    /**
     * Converts a 2x2 transformation matrix, read from the virtual world xml to radians
     *
     * @param m11 first element of the 2x2 transformation matrix providing the angle of rotation
     * @param m12 second element of the 2x2 transformation matrix providing the angle of rotation
     * @param m21 third element of the 2x2 transformation matrix providing the angle of rotation
     * @param m22 fourth element of the 2x2 transformation matrix providing the angle of rotation
     * @return the rotation in radian
     */
    public static double convertMatrixToRadians(double m11, double m12, double m21, double m22) {
        double radian = Math.atan2(m21, m11);
        return radian > 0 ? radian : 2 * Math.PI + radian;
    }

    /**
     * Converts a 2x2 transformation matrix, read from the virtual world xml to radians
     *
     * @param matrix the 2x2 transformation matrix providing the angle of rotation
     * @return the rotation in radian
     */
    public static double convertMatrixToRadians(double[][] matrix) {
        return convertMatrixToRadians(matrix[0][0], matrix[0][1], matrix[1][0], matrix[1][1]);
    }

    /**
     * Converts radian to degree. It basically just a wrapper for Math.toDegrees()
     *
     * @param rad is the angle in radian
     * @return the angle in degree
     */
    public static double radianToDegree(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Gets the distance between the coordinates of the two points.
     * @param x1 the x coordinate of the first point
     * @param y1 the y coordinate of the first point
     * @param x2 the x coordinate of the second point
     * @param y2 the y coordinate of the second point
     * @return the distance between the first and second points
     */
    public static double getDistanceBetweenTwoPoints(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
