package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class TrakMateProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        TrakMateProtocolDecoder decoder = new TrakMateProtocolDecoder(new TrakMateProtocol());

        verifyPosition(decoder, text(
                "^TMPER|354678456723764|1|12.59675|77.56789|123456|030414|2.3|34.0|1|0|0|0.015|3.9|12.0|23.4|23.4|1|1|0|#"));

        verifyPosition(decoder, text(
                "^TMALT|354678456723764|3|2|1|12.59675|77.56789|123456|030414|1.2|34.0|#"));

        verifyPosition(decoder, text(
                "^TMSRT|354678456723764|12.59675|77.56789|123456|030414|1.03|1.01|#"));

    }

}
