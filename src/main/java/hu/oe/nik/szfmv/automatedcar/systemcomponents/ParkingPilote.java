package hu.oe.nik.szfmv.automatedcar.systemcomponents;

import hu.oe.nik.szfmv.automatedcar.bus.VirtualFunctionBus;
import hu.oe.nik.szfmv.automatedcar.bus.packets.input.InputPacket;
import hu.oe.nik.szfmv.automatedcar.input.InputHandler;

public class ParkingPilote extends SystemComponent {

    private InputHandler inputhandler;

    private final InputPacket packet;

    private boolean on;

    /**
     * Pilote constructor
     *
     * @param bus is the given functionbus
     */
    public ParkingPilote(VirtualFunctionBus bus) {
        super(bus);
        on = false;
        this.packet = InputPacket.getInstance();
        bus.inputPacket = packet;
        inputhandler = InputHandler.getInstance();
    }

    @Override
    public void loop() {
        if (inputhandler.isParkinPilotePressed()) {
            if (!on) {
                on = true;
            } else {
                on = false;
            }
        }

        packet.setParkingPiloteStatus(on);
    }
}
