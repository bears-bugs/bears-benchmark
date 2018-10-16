/*
 * Copyright 2012 - 2016 Anton Tananaev (anton@traccar.org)
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
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class V680ProtocolDecoder extends BaseProtocolDecoder {

    public V680ProtocolDecoder(V680Protocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .groupBegin()
            .number("#(d+)#")                    // imei
            .expression("([^#]*)#")              // user
            .groupEnd("?")
            .number("(d+)#")                     // fix
            .expression("([^#]+)#")              // password
            .expression("([^#]+)#")              // event
            .number("(d+)#")                     // packet number
            .expression("([^#]+)?#?")            // gsm base station
            .expression("(?:[^#]+#)?")
            .number("(d+.d+),([EW]),")           // longitude
            .number("(d+.d+),([NS]),")           // latitude
            .number("(d+.d+),")                  // speed
            .number("(d+.?d*)?#")                // course
            .number("(dd)(dd)(dd)#")             // date (ddmmyy)
            .number("(dd)(dd)(dd)")              // time (hhmmss)
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;
        sentence = sentence.trim();

        if (sentence.length() == 16) {

            getDeviceSession(channel, remoteAddress, sentence.substring(1, sentence.length()));

        } else {

            Parser parser = new Parser(PATTERN, sentence);
            if (!parser.matches()) {
                return null;
            }

            Position position = new Position();
            position.setProtocol(getProtocolName());

            DeviceSession deviceSession;
            if (parser.hasNext()) {
                deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
            } else {
                deviceSession = getDeviceSession(channel, remoteAddress);
            }
            if (deviceSession == null) {
                return null;
            }
            position.setDeviceId(deviceSession.getDeviceId());

            position.set("user", parser.next());
            position.setValid(parser.nextInt() > 0);
            position.set("password", parser.next());
            position.set(Position.KEY_EVENT, parser.next());
            position.set("packet", parser.next());
            position.set(Position.KEY_RSSI, parser.next());

            double lon = parser.nextDouble();
            boolean west = parser.next().equals("W");
            double lat = parser.nextDouble();
            boolean south = parser.next().equals("S");

            if (lat > 90 || lon > 180) {
                int lonDegrees = (int) (lon * 0.01);
                lon = (lon - lonDegrees * 100) / 60.0;
                lon += lonDegrees;

                int latDegrees = (int) (lat * 0.01);
                lat = (lat - latDegrees * 100) / 60.0;
                lat += latDegrees;
            }

            position.setLongitude(west ? -lon : lon);
            position.setLatitude(south ? -lat : lat);

            position.setSpeed(parser.nextDouble());
            position.setCourse(parser.nextDouble());

            int day = parser.nextInt();
            int month = parser.nextInt();
            if (day == 0 && month == 0) {
                return null; // invalid date
            }

            DateBuilder dateBuilder = new DateBuilder()
                    .setDate(parser.nextInt(), month, day)
                    .setTime(parser.nextInt(), parser.nextInt(), parser.nextInt());
            position.setTime(dateBuilder.getDate());

            return position;
        }

        return null;
    }

}
