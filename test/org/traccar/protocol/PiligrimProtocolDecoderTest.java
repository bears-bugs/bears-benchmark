package org.traccar.protocol;

import org.jboss.netty.handler.codec.http.HttpMethod;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class PiligrimProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        PiligrimProtocolDecoder decoder = new PiligrimProtocolDecoder(new PiligrimProtocol());
        
        verifyPositions(decoder, request(HttpMethod.POST,
                "/bingps?imei=868204005544720&csq=18&vout=00&vin=4050&dataid=00000000",
                binary("fff2200d4110061a32354f3422310062000a0005173b0000a101000300005e00fff2200d4110100932354f2b22310042000b000e173b00009f01000700006000")));

    }

}
