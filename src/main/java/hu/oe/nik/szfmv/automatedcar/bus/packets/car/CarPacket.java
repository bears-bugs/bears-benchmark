package hu.oe.nik.szfmv.automatedcar.bus.packets.car;

public class CarPacket implements ReadOnlyCarPacket {
    private int x;
    private int y;
    private double rotation;

    /**
     * Car packet constructor
     */
    public CarPacket() {
        x = 0;
        y = 0;
        rotation = 0;
    }

    /***
     * Constructor for the car packet
     *
     * @param x Carposition x coordinate
     * @param y Carposition y coordinate
     * @param rotation Carposition rotation
     */
    public CarPacket(int x, int y, double rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
