package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.sample.SamplePacket;
import hu.oe.nik.szfmv.automatedcar.input.InputHandler;

public class Driver extends SystemComponent {

//    private final InputPacket inputPacket;

    private InputHandler inputHandler;

    public Driver(VirtualFunctionBus virtualFunctionBus) {
        super(virtualFunctionBus);
//        inputPacket = new InputPacket();
//        virtualFunctionBus.inputPacket = inputPacket;

        virtualFunctionBus.samplePacket = new SamplePacket();

        inputHandler = InputHandler.getInstance();
    }

    @Override
    public void loop() {


    }
}