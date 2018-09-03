package hu.oe.nik.szfmv.environment.models;

//This class will be the parent of the "car" classes.
public abstract class Movable extends Collidable {
    /**
     * Creates an object of the virtual world on the given coordinates with the given image.
     *
     * @param x             the initial x coordinate of the object
     * @param y             the initial y coordinate of the object
     * @param imageFileName the filename of the image representing the object in the virtual world
     */
    public Movable(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
    }

    /**
     * Creates an object with default parameter values.
     */
    public Movable() {
        super(0, 0, null);
    }

    /**
     * Use this method if you want to move a movable object.
     *
     * @param newX        X component of the new location
     * @param newX        Y component of the new location
     * @param newRotation The value of the new Rotation(radian)
     */
    public void move(int newX, int newY, float newRotation) {
        this.location.x = newX;
        this.location.y = newY;
        this.rotation = newRotation;
        generateShape();
    }
}
