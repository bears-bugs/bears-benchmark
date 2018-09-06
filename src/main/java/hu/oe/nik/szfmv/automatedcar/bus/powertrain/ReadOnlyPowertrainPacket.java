package hu.oe.nik.szfmv.automatedcar.bus.powertrain;

public interface ReadOnlyPowertrainPacket {
    /**
     * Gets the engine revolution (in revolution per minute)
     *
     * @return int (rpm)
     */
    int getRpm();

    /**
     * Gets the car actual speed (if the sign > 0 then move forward, if the sign < 0 then moving backwards)
     *
     * @return dobule
     */
    double getSpeed();
}
