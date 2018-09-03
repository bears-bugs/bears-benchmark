package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;

/**
 * Steering system is responsible for the turning of the car.
 */
public class SteeringSystem extends SystemComponent {
    private double angularSpeed = 0;

    /**
     * Creates a steering system that connects the Virtual Function Bus
     *
     * @param virtualFunctionBus {@link VirtualFunctionBus} used to connect {@link SystemComponent}s
     */
    public SteeringSystem(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
    }

    @Override
    public void loop() {
    }

    public double getAngularSpeed() {
        return this.angularSpeed;
    }
}
