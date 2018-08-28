package hu.oe.nik.szfmv.automatedcar.bus.packets.sample;

public class SamplePacket implements ReadOnlySamplePacket {
    private int gaspedalPosition = 0;

    public SamplePacket() {
    }

    public int getGaspedalPosition() {
        return this.gaspedalPosition;
    }

    public void setGaspedalPosition(int gaspedalPosition) {
        this.gaspedalPosition = gaspedalPosition;
    }

    // TODO implement all of the HMI signals
}
