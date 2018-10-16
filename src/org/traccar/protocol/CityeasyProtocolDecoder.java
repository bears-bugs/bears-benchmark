/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.helper.Checksum;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class CityeasyProtocolDecoder extends BaseProtocolDecoder {

    public CityeasyProtocolDecoder(CityeasyProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .groupBegin()
            .number("(dddd)(dd)(dd)")            // date
            .number("(dd)(dd)(dd),")             // time
            .number("([AV]),")                   // validity
            .number("(d+),")                     // satellites
            .number("([NS]),(d+.d+),")           // latitude
            .number("([EW]),(d+.d+),")           // longitude
            .number("(d+.d),")                   // speed
            .number("(d+.d),")                   // hdop
            .number("(d+.d)")                    // altitude
            .groupEnd("?").text(";")
            .number("(d+),")                     // mcc
            .number("(d+),")                     // mnc
            .number("(d+),")                     // lac
            .number("(d+)")                      // cell
            .any()
            .compile();

    public static final int MSG_ADDRESS_REQUEST = 0x0001;
    public static final int MSG_STATUS = 0x0002;
    public static final int MSG_LOCATION_REPORT = 0x0003;
    public static final int MSG_LOCATION_REQUEST = 0x0004;
    public static final int MSG_LOCATION_INTERVAL = 0x0005;
    public static final int MSG_PHONE_NUMBER = 0x0006;
    public static final int MSG_MONITORING = 0x0007;
    public static final int MSG_TIMEZONE = 0x0008;

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        buf.skipBytes(2); // header
        buf.readUnsignedShort(); // length

        String imei = ChannelBuffers.hexDump(buf.readBytes(7));
        DeviceSession deviceSession = getDeviceSession(
                channel, remoteAddress, imei, imei + Checksum.luhn(Long.parseLong(imei)));
        if (deviceSession == null) {
            return null;
        }

        int type = buf.readUnsignedShort();

        if (type == MSG_LOCATION_REPORT || type == MSG_LOCATION_REQUEST) {

            String sentence = buf.toString(buf.readerIndex(), buf.readableBytes() - 8, StandardCharsets.US_ASCII);
            Parser parser = new Parser(PATTERN, sentence);
            if (!parser.matches()) {
                return null;
            }

            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            if (parser.hasNext(15)) {

                DateBuilder dateBuilder = new DateBuilder()
                        .setDate(parser.nextInt(), parser.nextInt(), parser.nextInt())
                        .setTime(parser.nextInt(), parser.nextInt(), parser.nextInt());
                position.setTime(dateBuilder.getDate());

                position.setValid(parser.next().equals("A"));
                position.set(Position.KEY_SATELLITES, parser.next());

                position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG));
                position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG));

                position.setSpeed(parser.nextDouble());
                position.set(Position.KEY_HDOP, parser.nextDouble());
                position.setAltitude(parser.nextDouble());

            } else {

                getLastLocation(position, null);

            }

            position.setNetwork(new Network(CellTower.from(
                    parser.nextInt(), parser.nextInt(), parser.nextInt(), parser.nextInt())));

            return position;
        }

        return null;
    }

}
