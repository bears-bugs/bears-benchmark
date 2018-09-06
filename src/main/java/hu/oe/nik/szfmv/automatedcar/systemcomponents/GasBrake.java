package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.input.InputPacket;
import hu.oe.nik.szfmv.automatedcar.input.InputHandler;

public class GasBrake extends SystemComponent {

    private static final int MAXGASPEDALVALUE = 100;

    private static final int MINGASPEDALVALUE = 0;

    private static final int GASSTEPVALUE = 4;

    private static final int BRAKESTEPVALUE = 4;

    private int gaspedalvalue;

    private int brakepedalvalue;

    private final InputPacket inputPacket;

    private InputHandler inputHandler;

    /**
     * Class constructor
     *
     * @param virtual is the given functionbus
     */
    public GasBrake(VirtualFunctionBus virtual) {
        super(virtual);
        gaspedalvalue = 0;
        brakepedalvalue = 0;
        inputPacket = InputPacket.getInstance();
        inputHandler = InputHandler.getInstance();
    }

    /**
     * Set the gaspedal value
     *
     * @param value is the gaspedal value
     */
    private void setGaspedalvalue(int value) {
        if (gaspedalvalue + value <= MAXGASPEDALVALUE && gaspedalvalue + value >= MINGASPEDALVALUE) {
            gaspedalvalue += value;
        }
    }

    /**
     * Set the brakepedal value
     *
     * @param value is the brakepedal value
     */
    private void setBrakepedalvalue(int value) {
        if (brakepedalvalue + value <= MAXGASPEDALVALUE && brakepedalvalue + value >= MINGASPEDALVALUE) {
            brakepedalvalue += value;
        }
    }

    @Override
    public void loop() {
        if (inputHandler.isGasPressed() && inputHandler.isBrakePressed()) {
            return;
        }

        if (!inputHandler.isGasPressed() && !inputHandler.isBrakePressed()) {
            setGaspedalvalue(-GASSTEPVALUE);
            setBrakepedalvalue(-BRAKESTEPVALUE);
        }

        if (inputHandler.isGasPressed()) {
            setGaspedalvalue(GASSTEPVALUE);
            setBrakepedalvalue(-BRAKESTEPVALUE);
        }

        if (inputHandler.isBrakePressed()) {
            setBrakepedalvalue(BRAKESTEPVALUE);
            setGaspedalvalue(-GASSTEPVALUE);
        }

        inputPacket.setGaspeadalposition(gaspedalvalue);
        inputPacket.setBrakepedalvalue(brakepedalvalue);
    }
}
