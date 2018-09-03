package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.input.InputPacket;
import hu.oe.nik.szfmv.automatedcar.input.InputHandler;

public class ACC extends SystemComponent {
    private static final int ACCSPEEDMINVALUE = 30;
    private static final int ACCSPEEDMAXVALUE = 160;
    private static final int ACCSPEEDSTEPVALUE = 5;
    private static final double ACC_DISTANCE_STEP = 0.2;
    private static final double ACC_DISTANCE = 0.8;
    private static final double MAX_ACC_DISTANCE = 1.6;

    private double accDistanceValue;
    private int accSpeedValue;
    private final InputPacket inputPacket;

    private InputHandler inputHandler;

    /**
     * ACC constructor
     *
     * @param bus is the given functionbus
     */
    public ACC(VirtualFunctionBus bus) {
        super(bus);

        accDistanceValue = 0.8;
        accSpeedValue = 30;

        inputPacket = InputPacket.getInstance();
        inputHandler = InputHandler.getInstance();
    }

    @Override
    public void loop() {
        if (inputHandler.isAccDistancePressed()) {
            rotateDistanceValue();
        }
        if (inputHandler.isAccSpeedIncrementPressedPressed() && !inputHandler.isAccSpeedDecrementPressedPressed()) {
            setAccSpeedValue(+ACCSPEEDSTEPVALUE);
        }
        if (!inputHandler.isAccSpeedIncrementPressedPressed() && inputHandler.isAccSpeedDecrementPressedPressed()) {
            setAccSpeedValue(-ACCSPEEDSTEPVALUE);
        }

        inputPacket.setAccDistanceValue(accDistanceValue);
        inputPacket.setAccSpeedValue(accSpeedValue);

    }

    /**
     * Set the distance value
     */
    private void rotateDistanceValue() {
        accDistanceValue += ACC_DISTANCE_STEP;
        if (accDistanceValue == MAX_ACC_DISTANCE) {
            accDistanceValue = ACC_DISTANCE;
        }
    }

    /**
     * Set the acc speed
     *
     * @param diff is the given diff
     */
    private void setAccSpeedValue(int diff) {
        accSpeedValue += diff;
        if (accSpeedValue < ACCSPEEDMINVALUE) {
            accSpeedValue = ACCSPEEDMINVALUE;
        } else if (accSpeedValue > ACCSPEEDMAXVALUE) {
            accSpeedValue = ACCSPEEDMAXVALUE;
        }
    }
}
