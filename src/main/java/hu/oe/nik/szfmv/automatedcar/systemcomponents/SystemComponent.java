package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.exception.MissingPacketException;

/**
 * This class represents common features for system components By extending this
 * class, the component is registered towards the virtual function bus
 * automatically on instantiation.
 */
public abstract class SystemComponent {
    protected final VirtualFunctionBus virtualFunctionBus;

    /**
     *
     * @param virtualFunctionBus VirtualFunctuonBus parameter
     */
    protected SystemComponent(VirtualFunctionBus virtualFunctionBus) {
        this.virtualFunctionBus = virtualFunctionBus;
        virtualFunctionBus.registerComponent(this);
    }

    /**
     *
     * @throws MissingPacketException when VirtualFunctionBus packet not initiated
     */
    public abstract void loop() throws MissingPacketException;
}
