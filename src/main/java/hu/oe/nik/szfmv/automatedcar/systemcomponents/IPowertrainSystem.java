package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.exception.MissingPacketException;

public interface IPowertrainSystem {

    /**
     * Get the singals from the VirtualFunctionBus
     *
     * @throws MissingPacketException when VirtualFunctionBus packet not initiated
     */
    void getVirtualFunctionBusSignals() throws MissingPacketException;
}
