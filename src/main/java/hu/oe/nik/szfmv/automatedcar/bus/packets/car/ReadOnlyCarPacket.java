package hu.oe.nik.szfmv.automatedcar.bus.packets.car;

public interface ReadOnlyCarPacket {
    /**
     * @return AutomatedCar x position
     */
    int getX();

    /**
     * @return AutomatedCar y position
     */
    int getY();

    /**
     * @return AutomatedCar rotation in radians
     */
    double getRotation();
}
