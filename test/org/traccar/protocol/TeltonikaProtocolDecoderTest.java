package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class TeltonikaProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        TeltonikaProtocolDecoder decoder = new TeltonikaProtocolDecoder(new TeltonikaProtocol());

        verifyNothing(decoder, binary(
                "000F313233343536373839303132333435"));

        verifyPositions(decoder, false, binary(
                "00000000000002cb08080000015a71ccbec00002fc9bfc1e53a1e00016004cf80005001914150216056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d20003480100c6000ac500ce02c80000654ec700004ee8000000015a725aaac80002fc933c1e539d4000150049f80000001914150116056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f4c6000ac500ce02c80000654ec700004ee8000000015a75a42c900002fc97d01e539640001d0008020000001914150016056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f8c6000ac500ce02c80006ba5ac700004ee8000000015a75a440180002fc931c1e539b60001d00b9020001001914150016056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800fac6000ac500ce02c80006ba5ac700004ee8000000015a75a453a00002fc93601e539cc0001d015d0c0000001914150016056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f9c6000ac500ce02c80006ba5ac700004ee8000000015a75a467280002fc93801e539cc0001d013c0c0000001914150016056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f9c6000ac500ce02c80006ba5ac700004ee8000000015a75a47ab00002fc92cc1e539c80001d00b00c0000001914150016056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f8c60004c5000a02c800003085c70006ba5a000000015a75a48e380002fc92ec1e539c40001d00410c0000001914150116056500ee00ef00f0009d029e029f02a002a102a202a302a402a502a602020003000164d200034800f8c6000ac500ce02c80000c83dc700004ee800080000e0b2"));

        verifyPositions(decoder, false, binary(
                "0000000000000000080100000113fc208dff00209cca800f14f650006f00d60400040004030101150316030001460000015d000100000000")); // invalid length and checksum

        verifyPositions(decoder, false, binary(
                "000000000000009f080400000159738f76b8012e13b796110ab27600d700000b00004e01000000014e000000000000000000000159738f6ee8012e13b796110ab27600d700000a00004e01000000014e01000b00791c179300000159738f6b00012e13b796110ab27600d700000a00004e01000000014e000000000000000000000159738f5f48012e13b796110ab27600d700000b00004e01000000014e01000b00791c17930400009671"));

        verifyPositions(decoder, false, binary(
                "00000000000000710c0106000000694154244d5347534e443d342c225354474234302c50522c3335363630313036303236353035302c313630343232313531372c313630343232313531382c432c2b3032332e332c302c2b3032332e312c302c4445414354492c302c4445414354492c302c312c30220d0a010000d8db"));

        verifyPositions(decoder, false, binary(
                "0000000000000055070450aa14320201f00150aa17f3031f42332a4c4193d68c008d00020901f00150aa1b6a031f423383f54193624f009d00000a01f00150aa1c230fc01a0000552b040164f400dd00f0010143100c0105000000050400006846"));

        verifyPositions(decoder, binary(
                "000000000000003508010000014f8e016420002141bbaf0f4e96a7fffa0000120000000602010047030242669c92000002c7000000009100000000000100002df3"));
        
        verifyPositions(decoder, binary(
                "00000000000000A7080400000113fc208dff000f14f650209cca80006f00d60400040004030101150316030001460000015d0000000113fc17610b000f14ffe0209cc580006e00c00500010004030101150316010001460000015e0000000113fc284945000f150f00209cd200009501080400000004030101150016030001460000015d0000000113fc267c5b000f150a50209cccc0009300680400000004030101150016030001460000015b00040000"));
        
        verifyPositions(decoder, binary(
                "000000000000014708060000013e5a60a4cb003fa7b780fc424518004200000a000000090501010200b300b400f000034268a746011818000001c700000000000000013e5dc8ba28003fa7c080fc4246040001000005000000090501010200b300b400f001034268b44600ef18000001c700000000000000013e5dc90455003fa7b640fc424388003a0000070000f0090501010200b300b400f000034268dc4600f718000001c70000001d000000013e5dc9d368003fa7b800fc4244300049000004000000090501010200b300b400f001034267de46010718000001c700000000000000013e5dca311d003fa7b680fc4243cc00420000070000f0090501010200b300b400f0000342685346010b18000001c700000000000000013e5dcfafe9003fa7b600fc4242f0003d000008000000090501010200b300b400f0000342685246011918000001c700000000000600000275"));

        verifyPositions(decoder, binary(
                "000000000000002c08010000013eff8d6f9800173295002111f400008100ae0b0000000401010003090016432980422f7200000100007a5d"));
        
        verifyPositions(decoder, binary(
                "00000000000000c7070441bf9db00fff425adbd741ca6e1e009e1205070001030b160000601a02015e02000314006615000a160067010500000ce441bf9d920fff425adbb141ca6fc900a2b218070001030b160000601a02015e02000314006615000a160067010500000cc641bf9d740fff425adbee41ca739200b6c91e070001030b1f0000601a02015f02000314006615000a160066010500000ca841bf9cfc0fff425adba041ca70c100b93813070001030b1f0000601a02015f02000314002315000a160025010500000c3004000000"));

        verifyPositions(decoder, binary(
                "000000000000003107024c61410b013f4231c2c141d0beb9003d000005006483ff4c6140eb013f4231c2c141d0beb9003d000005006483ff02000041df"));

        verifyPositions(decoder, binary(
                "000000000000002b080100000140d4e3ec6e000cc661d01674a5e0fffc00000900000004020100f0000242322318000000000100007a04"));

        verifyPositions(decoder, false, binary(
                "000000000000002d0c01060000002523464d323d3236323033323736313732313339362c32363230332c30372e30322e30350d0a0100009a2e"));

        verifyPositions(decoder, binary(
                "00000000000000a608010000013f14a1d1ce000f0eb790209a778000ab010c0500000000000000000100003390"));
        
    }

}