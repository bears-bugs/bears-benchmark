package hu.oe.nik.szfmv.automatedcar.bus.packets.sample;

import hu.oe.nik.szfmv.automatedcar.input.enums.GearEnum;

public interface ReadOnlySamplePacket {
    /**
     * Gets gaspedal position for testing
     *
     * @return Gaspedal position
     */
    int getGaspedalPosition();

    /**
     * Gets brakepedal position for testing
     *
     * @return Brakepedal position
     */
    int getBrakepedalPosition();

    /**
     * Gets gearbox state for testing
     *
     * @return Gearbox state
     */
    GearEnum getGearState();
}
