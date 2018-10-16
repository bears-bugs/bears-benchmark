package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class WialonProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        WialonProtocolDecoder decoder = new WialonProtocolDecoder(new WialonProtocol());

        verifyNothing(decoder, text(
                "#L#123456789012345;test"));
        
        verifyNothing(decoder, text(
                "#L#2002;NA"));
        
        verifyNothing(decoder, text(
                "#P#"));

        verifyPosition(decoder, text(
                "#D#151216;135910;5321.1466;N;04441.7929;E;87;156;265.000000;12;1.000000;241;NA;NA;NA;odo:2:0.000000,total_fuel:1:430087,can_fls:1:201,can_taho:1:11623,can_mileage:1:140367515"));

        verifyPosition(decoder, text(
                "#D#151216;140203;5312.59514;N;04830.37834;E;53;273;NA;10;NA;NA;NA;NA;NA;EvId:1:1,Board:2:12.81,Accum:2:4.28"));

        verifyPosition(decoder, text(
                "#SD#270413;205601;5544.6025;N;03739.6834;E;1;2;3;4"),
                position("2013-04-27 20:56:01.000", true, 55.74338, 37.66139));

        verifyPosition(decoder, text(
                "#SD#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5"));

        verifyPosition(decoder, text(
                "#D#270413;205601;5544.6025;N;03739.6834;E;1;2;3;4;0.0;0;0;14.77,0.02,3.6;NA;count1:1:564,fuel:2:45.8,hw:3:V4.5"));
        
        verifyPosition(decoder, text(
                "#D#190114;051312;4459.6956;N;04105.9930;E;35;306;204.000000;12;NA;452986639;NA;106.000000;NA;sats_gps:1:9,sats_glonass:1:3,balance:2:12123.000000,stay_balance:1:0"));
        
        verifyPosition(decoder, text(
                "#D#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5;1.74;NA;NA;NA;NA;NA"));

        verifyPosition(decoder, text(
                "#D#021214;065947;2237.7552;N;11404.8851;E;0.000;;170.9;5;1.74;NA;NA;;NA;NA"));

        verifyPositions(decoder, text(
                "#B#080914;073235;5027.50625;N;03026.19321;E;0.700;0.000;NA;4;NA;NA;NA;;NA;Батарея:3:100 %|080914;073420;5027.50845;N;03026.18854;E;1.996;292.540;NA;4;NA;NA;NA;;NA;Батарея:3:100 %"));
        
        verifyPositions(decoder, text(
                "#B#110914;102132;5027.50728;N;03026.20369;E;1.979;288.170;NA;NA;NA;NA;NA;;NA;Батарея:3:100 %"));

        verifyPositions(decoder, text(
                "#B#110315;045857;5364.0167;N;06127.8262;E;0;155;965;7;2.40;4;0;;NA;Uacc:2:3.4,Iacc:2:0.000,Uext:2:13.2,Tcpu:2:14.4,Balance:2:167.65,GPS:3:Off"));

        verifyPositions(decoder, text(
                "#B#110315;045857;5364.0167;N;06127.8262;E;0;155;965;7;2.40;4;0;14.77,0.02,3.6;AB45DF01145;"));

    }

}
