package org.traccar.protocol;

import java.nio.ByteOrder;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class MxtProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        MxtProtocolDecoder decoder = new MxtProtocolDecoder(new MxtProtocol());

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631144c7e0008643ad2f456fb2d49747cfe4cbe0ffd002008800000001021000fd43d3f1403000000ff300000f42760001031102445a81fda04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631361e7a00082471418b052a2c46b587ffc01ae3fd000008800000000000003345422203000000f000f00000000000ea1e04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a63118787d00086440628d226e2bc26a97feac8a3afd10210010308000000000000018003d2b10240000005e2f0000f427f21031feff0000593804"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631bd777d0008646e319e17292ce86798fed4cd3afd102110211030800000102403001f15003e2b102400000034300000f4271021007b175535a7be04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631e3f97e00087cf40a98151c2cc46898fee0ce3afd1021001030c0000006102116072e003829bb00000036102100001024000000062b0000f42730004b06a6384b4304"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a63118787d00086468457a466a2bc26a97feac8a3afd10212010308000000000001fe1053d291024000000922f0000f4271021007b17553599bb04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a63118787d0008648645ec486a2bc26a97feac8a3afd1021001030c0000000001419eb05372b1024000000982a0000f4271021007b17000010308c04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631e3f97e00087cfa0af3151c2c126798febace3afd1021801030c0000006102122082f003e29bb00000037102100001024000000ab2f0000f42730004b060000488c04"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a631e3f97e00087cfe0a4b161c2c126798febace3afd1021801030800000071021240731003e2abb00000038102100001024000000c12f0000f42730004b06a638633104"));

        verifyPosition(decoder, binary(ByteOrder.LITTLE_ENDIAN,
                "01a63118787d0008648645ec486a2bc26a97feac8a3afd1021001030c0000000001419eb05372b1024000000982a0000f4271021007b17000010308c04"));

    }

}
