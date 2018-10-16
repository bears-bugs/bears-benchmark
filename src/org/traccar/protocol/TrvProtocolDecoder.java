/*
 * Copyright 2015 - 2017 Anton Tananaev (anton@traccar.org)
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

import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class TrvProtocolDecoder extends BaseProtocolDecoder {

    public TrvProtocolDecoder(TrvProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .expression("[A-Z]{2,3}")
            .number("APdd")
            .number("(dd)(dd)(dd)")              // date (yymmdd)
            .expression("([AV])")                // validity
            .number("(dd)(dd.d+)")               // latitude
            .expression("([NS])")
            .number("(ddd)(dd.d+)")              // longitude
            .expression("([EW])")
            .number("(ddd.d)")                   // speed
            .number("(dd)(dd)(dd)")              // time (hhmmss)
            .number("([d.]{6})")                 // course
            .number("(ddd)")                     // gsm
            .number("(ddd)")                     // satellites
            .number("(ddd)")                     // battery
            .number("(d)")                       // acc
            .number("dd")                        // arm status
            .number("dd,")                       // working mode
            .number("(d+),")                     // mcc
            .number("(d+),")                     // mnc
            .number("(d+),")                     // lac
            .number("(d+)")                      // cell
            .any()
            .compile();

    private static final Pattern PATTERN_HEATRBEAT = new PatternBuilder()
            .expression("[A-Z]{2,3}")
            .text("CP01,")
            .number("(ddd)")                     // gsm
            .number("(ddd)")                     // gps
            .number("(ddd)")                     // battery
            .number("(d)")                       // acc
            .number("(dd)")                      // arm status
            .number("(dd)")                      // working mode
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;

        String id = sentence.startsWith("TRV") ? sentence.substring(0, 3) : sentence.substring(0, 2);
        String type = sentence.substring(id.length(), id.length() + 4);

        if (channel != null) {
            if (type.equals("AP00") && id.equals("IW")) {
                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                channel.write(id + (char) (type.charAt(0) + 1) + type.substring(1) + "," + time + ",0#");
            } else {
                channel.write(id + (char) (type.charAt(0) + 1) + type.substring(1) + "#");
            }
        }

        if (type.equals("AP00")) {
            getDeviceSession(channel, remoteAddress, sentence.substring(id.length() + type.length()));
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        if (type.equals("CP01")) {

            Parser parser = new Parser(PATTERN_HEATRBEAT, sentence);
            if (!parser.matches()) {
                return null;
            }

            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            getLastLocation(position, null);

            position.set(Position.KEY_RSSI, parser.nextInt(0));
            position.set(Position.KEY_SATELLITES, parser.nextInt(0));
            position.set(Position.KEY_BATTERY, parser.nextInt(0));
            position.set(Position.KEY_IGNITION, parser.nextInt(0) != 0);

            position.set("arm", parser.nextInt(0));
            position.set("mode", parser.nextInt(0));

            return position;

        } else if (type.equals("AP01") || type.equals("AP10")) {

            Parser parser = new Parser(PATTERN, sentence);
            if (!parser.matches()) {
                return null;
            }

            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            DateBuilder dateBuilder = new DateBuilder()
                    .setDate(parser.nextInt(0), parser.nextInt(0), parser.nextInt(0));

            position.setValid(parser.next().equals("A"));
            position.setLatitude(parser.nextCoordinate());
            position.setLongitude(parser.nextCoordinate());
            position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble(0)));

            dateBuilder.setTime(parser.nextInt(0), parser.nextInt(0), parser.nextInt(0));
            position.setTime(dateBuilder.getDate());

            position.setCourse(parser.nextDouble(0));

            int rssi = parser.nextInt(0);
            position.set(Position.KEY_SATELLITES, parser.nextInt(0));
            position.set(Position.KEY_BATTERY, parser.nextInt(0));

            int acc = parser.nextInt(0);
            if (acc != 0) {
                position.set(Position.KEY_IGNITION, acc == 1);
            }

            position.setNetwork(new Network(CellTower.from(
                    parser.nextInt(0), parser.nextInt(0), parser.nextInt(0), parser.nextInt(0), rssi)));

            return position;
        }

        return null;
    }

}
