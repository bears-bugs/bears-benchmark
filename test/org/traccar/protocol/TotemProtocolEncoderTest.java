package org.traccar.protocol;

import org.junit.Assert;
import org.junit.Test;
import org.traccar.ProtocolTest;
import org.traccar.model.Command;

public class TotemProtocolEncoderTest extends ProtocolTest {

    @Test
    public void testEncode() throws Exception {

        TotemProtocolEncoder encoder = new TotemProtocolEncoder();
        
        Command command = new Command();
        command.setDeviceId(2);
        command.setType(Command.TYPE_ENGINE_STOP);
        command.set(Command.KEY_DEVICE_PASSWORD, "000000");
        
        Assert.assertEquals("*000000,025,C,1#", encoder.encodeCommand(command));

    }

}
