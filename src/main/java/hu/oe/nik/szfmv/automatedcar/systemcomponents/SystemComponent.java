package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;

/**
 * This class represents common features for system components By extending this
 * class, the component is registered towards the virtual function bus
 * automatically on instantiation.
 */
public abstract class SystemComponent {
    final protected VirtualFunctionBus virtualFunctionBus;

    protected SystemComponent(VirtualFunctionBus virtualFunctionBus) {
        this.virtualFunctionBus = virtualFunctionBus;
        virtualFunctionBus.registerComponent(this);
    }

    public abstract void loop();
}
