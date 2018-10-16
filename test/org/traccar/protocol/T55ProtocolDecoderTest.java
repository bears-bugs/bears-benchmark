package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class T55ProtocolDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        T55ProtocolDecoder decoder = new T55ProtocolDecoder(new T55Protocol());

        verifyNothing(decoder, text(
                "086415031C20"));

        verifyNothing(decoder, text(
                "358244017671308"));

        verifyNotNull(decoder, text(
                "$GPRMC,161223.000,A,2517.0545,S,05739.1788,W,0.0,0.0,011196,,,A*61"));

        verifyPosition(decoder, text(
                "4711/022789000688081/$GPRMC,133343,A,5308.56325,N,1029.12850,E,0.000000,0.000000,290316,,*2A"));

        verifyPosition(decoder, text(
                "$GPRMC,073446.000,A,1255.5125,N,07738.2948,E,0.00,0.53,080316,D*71,11,865733027593268,1,090,086"));

        verifyNothing(decoder, text(
                "$GPFID,ID123456ABC"));

        verifyNothing(decoder, text(
                "$PGID,359853000144328*0F"));

        verifyNothing(decoder, text(
                "$PCPTI,CradlePoint Test,184453,184453.0,6F*57"));
        
        verifyNothing(decoder, text(
                "IMEI 351467108700000"));
        
        verifyPosition(decoder, text(
                "$GPRMC,012006,A,4828.10,N,1353.52,E,0.00,0.00,180915,020.3,E*42"));

        verifyPosition(decoder, text(
                "$GPRMC,094907.000,A,6000.5332,N,03020.5192,E,1.17,60.26,091111,,*33"));

        verifyPosition(decoder, text(
                "$GPRMC,115528.000,A,6000.5432,N,03020.4948,E,,,091111,,*06"));
        
        verifyPosition(decoder, text(
                "$GPRMC,064411.000,A,3717.240078,N,00603.046984,W,0.000,1,010313,,,A*6C"));
        
        verifyPosition(decoder, text(
                "$GPGGA,000000.0,4337.200755,N,11611.955704,W,1,05,3.5,825.5,M,-11.0,M,,*6F"));
        
        verifyPosition(decoder, text(
                "$GPGGA,000000,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47"));
        
        verifyPosition(decoder, text(
                "$GPRMA,V,0000.00,S,00000.00,E,,,00.0,000.,11.,E*7"));
        
        verifyPosition(decoder, text(
                "$TRCCR,20140101001122.333,V,60.0,-100.0,1.1,2.2,3.3,4.4,*00"));
        
        verifyPosition(decoder, text(
                "$TRCCR,20140111000000.000,A,60.000000,60.000000,0.00,0.00,0.00,50,*3a"));
        
        verifyPosition(decoder, text(
                "$GPRMC,125735.000,A,6010.34349,N,02445.72838,E,1.0,101.7,050509,6.9,W,A*1F"));

        verifyPosition(decoder, text(
                "$GPGGA,000000.000,6010.34349,N,02445.72838,E,1,05,1.7,0.9,M,35.1,M,,*59"));
        
        verifyPosition(decoder, text(
                "123456789$GPGGA,000000.000,4610.1676,N,00606.4586,E,0,00,4.3,0.0,M,50.7,M,,0000*59"));
        
        verifyPosition(decoder, text(
                "123456789$GPRMC,155708.252,V,4610.1676,N,00606.4586,E,000.0,000.0,060214,,,N*76"));
        
        verifyPosition(decoder, text(
                "990000561287964,$GPRMC,213516.0,A,4337.216791,N,11611.995877,W,0.0,335.4,181214,,,A * 72"));

        verifyPosition(decoder, text(
                "355096030432529$GPGGA,000000.00,3136.599,S,5213.981,W,1,7,2.13,250.00,M,-16.384,M,3550960304325290.0,1"));

        verifyPosition(decoder, text(
                "355096030432529$GPGGA,000000.00,3136.628,S,5213.990,W,1,7,2.13,250.00,M,-16.384,M,0.0,1"));

    }

    @Test
    public void testMaxonDecode() throws Exception {

        // Maxon devices can send NMEA before identification

        T55ProtocolDecoder decoder = new T55ProtocolDecoder(new T55Protocol());

        verifyNothing(decoder, text(
                "$GPRMC,012006,A,4828.10,N,1353.52,E,0.00,0.00,180915,020.3,E*42"));

        verifyPosition(decoder, text(
                "$GPFID,ID123456ABC"));

    }

}
