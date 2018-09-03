package hu.oe.nik.szfmv.environment.models;

public class Pedestrian extends Movable {

    private final int height = 338;
    private int moveStatus = 0;
    private int moveDirection = 5;

    /**
     * @param x             pedestrian x coordinate
     * @param y             pedestrian y coordinate
     * @param imageFileName pedestrian image file
     */
    public Pedestrian(int x, int y, String imageFileName) {
        super(x, y, imageFileName);
        generateDimens();
        generateShape();
    }

    /**
     * Creates an object with default parameter values.
     */
    public Pedestrian() {
        super(0, 0, null);
        generateDimens();
        generateShape();
    }

    /**
     * Method of pedestrian move
     */
    public void moveOnCrosswalk() {
        this.move(this.getX(), this.getY() - moveDirection, (float) this.getRotation());
        final int movingUnit = 5;
        final int manSize = 102;

        moveStatus += movingUnit;
        if (moveStatus == height + manSize) {
            moveStatus = 0;
            moveDirection = -moveDirection;
        }
    }
}
