/*
 * Copyright 2014 - 2015 Anton Tananaev (anton@traccar.org)
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

public class TrackboxProtocolDecoder extends BaseProtocolDecoder {

    public TrackboxProtocolDecoder(TrackboxProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .number("(dd)(dd)(dd).(ddd),")       // time (hhmmss.sss)
            .number("(dd)(dd.dddd)([NS]),")      // latitude
            .number("(ddd)(dd.dddd)([EW]),")     // longitude
            .number("(d+.d),")                   // hdop
            .number("(-?d+.?d*),")               // altitude
            .number("(d),")                      // fix type
            .number("(d+.d+),")                  // course
            .number("d+.d+,")                    // speed (kph)
            .number("(d+.d+),")                  // speed (knots)
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .number("(d+)")                      // satellites
            .compile();

    private void sendResponse(Channel channel) {
        if (channel != null) {
            channel.write("=OK=\r\n");
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;

        if (sentence.startsWith("a=connect")) {
            String id = sentence.substring(sentence.indexOf("i=") + 2);
            if (getDeviceSession(channel, remoteAddress, id) != null) {
                sendResponse(channel);
            }
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        Parser parser = new Parser(PATTERN, sentence);
        if (!parser.matches()) {
            return null;
        }
        sendResponse(channel);

        Position position = new Position();
        position.setProtocol(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        DateBuilder dateBuilder = new DateBuilder()
                .setTime(parser.nextInt(), parser.nextInt(), parser.nextInt(), parser.nextInt());

        position.setLatitude(parser.nextCoordinate());
        position.setLongitude(parser.nextCoordinate());

        position.set(Position.KEY_HDOP, parser.next());

        position.setAltitude(parser.nextDouble());

        int fix = parser.nextInt();
        position.set(Position.KEY_GPS, fix);
        position.setValid(fix > 0);

        position.setCourse(parser.nextDouble());
        position.setSpeed(parser.nextDouble());

        dateBuilder.setDateReverse(parser.nextInt(), parser.nextInt(), parser.nextInt());
        position.setTime(dateBuilder.getDate());

        position.set(Position.KEY_SATELLITES, parser.next());

        return position;
    }

}
