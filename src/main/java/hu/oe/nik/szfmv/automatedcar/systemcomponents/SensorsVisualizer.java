package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.input.InputPacket;
import hu.oe.nik.szfmv.automatedcar.input.InputHandler;



public class SensorsVisualizer extends SystemComponent {

    private InputHandler inputHandler;
    private InputPacket inputPacket;
    private boolean radarVizualizerPressed;
    private boolean cameraVizualizerPressed;
    private boolean ultrasonicVizualizerPressed;
    private  boolean  shapeBorderVizualizerPressed;

    /**
     * @param virtualFunctionBus VirtualFunctuonBus parameter
     */
    public SensorsVisualizer(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);

        inputPacket = InputPacket.getInstance();
        virtualFunctionBus.inputPacket = inputPacket;
        inputHandler = InputHandler.getInstance();
        radarVizualizerPressed = false;
        cameraVizualizerPressed = false;
        ultrasonicVizualizerPressed = false;
        shapeBorderVizualizerPressed = false;
    }

    @Override
    public void loop() {
        if (inputHandler.isRadarTestPressed()) {
            radarVizualizerPressed = !radarVizualizerPressed;
            inputPacket.setRadarVizualizerState(radarVizualizerPressed);
        }
        if (inputHandler.isCameraTestPressed()) {
            cameraVizualizerPressed = !cameraVizualizerPressed;
            inputPacket.setCameraVizualizerState(cameraVizualizerPressed);
        }
        if (inputHandler.isUltrasonicTestPressed()) {
            ultrasonicVizualizerPressed = !ultrasonicVizualizerPressed;
            inputPacket.setUltrasonicVizualizerState(ultrasonicVizualizerPressed);
        }
        if (inputHandler.isShapeBorderTestPressed()) {
            shapeBorderVizualizerPressed = !shapeBorderVizualizerPressed;
            inputPacket.setShapeBorderVizualizerState(shapeBorderVizualizerPressed);
        }

    }
}

