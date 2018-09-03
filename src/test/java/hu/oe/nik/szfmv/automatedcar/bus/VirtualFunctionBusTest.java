package hu.oe.nik.szfmv.automatedcar.bus;

import hu.oe.nik.szfmv.automatedcar.bus.exception.MissingPacketException;
import hu.oe.nik.szfmv.automatedcar.bus.packets.sample.SamplePacket;
import hu.oe.nik.szfmv.automatedcar.systemcomponents.SystemComponent;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class VirtualFunctionBusTest {

    private VirtualFunctionBus virtualFunctionBus;
    private SenderComponentMock senderComponent;
    private ReceiverComponentMock receiverComponent;
    private boolean senderLoopCalled = false;
    private boolean receiverLoopCalled = false;

    @Before
    public void registerComponent() {
        virtualFunctionBus = new VirtualFunctionBus();

        senderComponent = new SenderComponentMock(virtualFunctionBus);
        receiverComponent = new ReceiverComponentMock(virtualFunctionBus);

        virtualFunctionBus.registerComponent(senderComponent);
        virtualFunctionBus.registerComponent(receiverComponent);

        senderLoopCalled = false;
        receiverLoopCalled = false;
    }

    @Test
    public void sendLoopfunctions() throws MissingPacketException {
        virtualFunctionBus.loop();
        assertThat(senderLoopCalled, is(true));
        assertThat(receiverLoopCalled, is(true));
    }

    @Test
    public void testSignalWritingReading() throws MissingPacketException {
        virtualFunctionBus.loop();
        assertThat(receiverComponent.gaspedalPosition, is(42));
    }

    class SenderComponentMock extends SystemComponent {
        SamplePacket samplePacket = new SamplePacket();

        protected SenderComponentMock(VirtualFunctionBus virtualFunctionBus) {
            super(virtualFunctionBus);
            virtualFunctionBus.samplePacket = samplePacket;
        }

        @Override
        public void loop() {
            senderLoopCalled = true;
            samplePacket.setGaspedalPosition(42);
        }
    }

    class ReceiverComponentMock extends SystemComponent {
        int gaspedalPosition = 0;

        protected ReceiverComponentMock(VirtualFunctionBus virtualFunctionBus) {
            super(virtualFunctionBus);
        }

        @Override
        public void loop() {
            receiverLoopCalled = true;
            gaspedalPosition = virtualFunctionBus.samplePacket.getGaspedalPosition();
        }
    }
}
