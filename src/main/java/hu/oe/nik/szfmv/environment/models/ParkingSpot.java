package hu.oe.nik.szfmv.environment.models;

public class ParkingSpot extends Crossable {
    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public ParkingSpot(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public ParkingSpot() {
        super(0, 0, null);
    }
}
