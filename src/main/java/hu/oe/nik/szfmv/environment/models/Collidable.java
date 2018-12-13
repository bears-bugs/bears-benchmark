package hu.oe.nik.szfmv.environment.models;

import hu.oe.nik.szfmv.environment.WorldObject;

public abstract class Collidable extends WorldObject {

    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public Collidable(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public Collidable() {
        super(0, 0, null);
    }
}
