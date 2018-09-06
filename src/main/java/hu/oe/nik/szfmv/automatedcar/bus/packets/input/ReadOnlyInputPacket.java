package hu.oe.nik.szfmv.automatedcar.bus.packets.input;

import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;

public interface ReadOnlyInputPacket {
    /**
     * Gets the gas pedal's current position
     *
     * @return the gas pedal's current position
     */
    int getGasPedalPosition();

    /**
     * Gets the break pedal's current position
     *
     * @return the break pedal's current position
     */
    int getBreakPedalPosition();

    /**
     * Gets the steering wheel's current position (between -100 and 100).
     *
     * @return the steering wheel's current position
     */
    double getSteeringWheelPosition();

    /**
     * Gets the ACC target speed (between 30 and 160).
     *
     * @return the ACC's target speed
     */
    int getACCTargetSpeed();

    /**
     * Gets the ACC target distance.
     *
     * @return the ACC's target distance
     */
    double getACCTargetDistance();

    /**
     * Gets whether lane keeping is on or off.
     *
     * @return the lane keeping switch's status
     */
    boolean getLaneKeepingStatus();

    /**
     * Gets whether parking pilot is on or off.
     *
     * @return the parking pilot switch's status
     */
    boolean getParkingPilotStatus();

    /**
     * Gets the gear's current state.
     *
     * @return the gear's current state
     */
    GearEnum getGearState();

    /**
     * Gets whether the left turn signal is on or off.
     *
     * @return the left turn signal's status
     */
    boolean getLeftTurnSignalStatus();

    /**
     * Gets whether the right turn signal is on or off.
     *
     * @return the right turn signal's status
     */
    boolean getRightTurnSignalStatus();

    /**
     * Gets whether the radar test is on or off.
     *
     * @return the  radar test's status
     */
    boolean getRadarVizualizerStatus();

    /**
     * Gets whether the camera test is signal is on or off.
     *
     * @return the  camera test's status
     */
    boolean getCameraVizualizerStatus();

    /**
     * Gets whether the ultrasonic test is on or off.
     *
     * @return the  ultrasonic test's status
     */
    boolean getUltrasonicVizualizerStatus();

    /**
     * Gets whether the shape border vizualizer is on or off.
     *
     * @return the shape border vizualizer's status
     */
    boolean getShapeBorderVizualizerState();
}