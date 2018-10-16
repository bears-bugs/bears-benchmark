package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class GotopProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        GotopProtocolDecoder decoder = new GotopProtocolDecoder(new GotopProtocol());

        verifyNothing(decoder, text(
                ""));
        
        verifyNothing(decoder, text(
                "353327020412763,CMD-X"));

        verifyPosition(decoder, text(
                "013226009991924,CMD-T,A,DATE:130802,TIME:153721,LAT:25.9757433S,LOT:028.1087816E,Speed:000.0,X-X-X-X-81-26,000,65501-00A0-4B8E"));

        verifyPosition(decoder, text(
                "353327020115804,CMD-T,A,DATE:090329,TIME:223252,LAT:22.7634066N,LOT:114.3964783E,Speed:000.0,84-20,000"),
                position("2009-03-29 22:32:52.000", true, 22.76341, 114.39648));

        verifyPosition(decoder, text(
                "353327020115804,CMD-T,A,DATE:090329,TIME:223252,LAT:22.7634066N,LOT:114.3964783E,Speed:000.0,1-1-0-84-20,000"));
        
        verifyPosition(decoder, text(
                "353327020412763,CMD-F,V,DATE:140125,TIME:183636,LAT:51.6384466N,LOT:000.2863866E,Speed:000.0,61-19,"));

        verifyPosition(decoder, text(
                "013949008891817,CMD-F,A,DATE:150225,TIME:175441,LAT:50.000000N,LOT:008.000000E,Speed:085.9,0-0-0-0-52-31,000,26201-1073-1DF5"));

    }

}
