package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class VtfmsProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        VtfmsProtocolDecoder decoder = new VtfmsProtocolDecoder(new VtfmsProtocol());

        verifyPosition(decoder, text(
                "(865733028143493,00I76,00,000,,,,,A,133755,210617,10.57354,077.24912,SW,000,00598,00000,K,0017368,1,12.7,,,0.000,,,0,0,0,0,1,1,0,,)074"));

        verifyPosition(decoder, text(
                "(863071010087648,0HK44,00,000,14,2,9,,A,114946,180313,11.0244,076.9768,282,000,00000,00000,K,0000128,1,12.8,,200,2.501,,4.001,0,0,0,0,0,0,0,,)105"));

    }

}
