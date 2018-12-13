/*
 * Copyright 2017 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class SiwiProtocolDecoder extends BaseProtocolDecoder {

    public SiwiProtocolDecoder(SiwiProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("$").expression("[A-Z]+,")     // header
            .number("(d+),")                     // device id
            .number("d+,")                       // unit no
            .expression("([A-Z]),")              // reason
            .number("d+,")                       // command code
            .number("[^,]*,")                    // command value
            .expression("([01]),")               // ignition
            .expression("[01],")                 // power cut
            .expression("[01],")                 // box open
            .number("d+,")                       // message key
            .number("(d+),")                     // odometer
            .number("(d+),")                     // speed
            .number("(d+),")                     // satellites
            .expression("([AV]),")               // valid
            .number("(-?d+.d+),")                // latitude
            .number("(-?d+.d+),")                // longitude
            .number("(-?d+),")                   // altitude
            .number("(d+),")                     // course
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(dd)(dd)(dd),")             // date (ddmmyy)
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        Parser parser = new Parser(PATTERN, (String) msg);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        Position position = new Position();
        position.setProtocol(getProtocolName());
        position.setDeviceId(deviceSession.getDeviceId());

        position.set(Position.KEY_EVENT, parser.next());
        position.set(Position.KEY_IGNITION, parser.next().equals("1"));
        position.set(Position.KEY_ODOMETER, parser.nextInt(0));

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextInt(0)));

        position.set(Position.KEY_SATELLITES, parser.nextInt(0));

        position.setValid(parser.next().equals("A"));
        position.setLatitude(parser.nextDouble(0));
        position.setLongitude(parser.nextDouble(0));
        position.setAltitude(parser.nextDouble(0));
        position.setCourse(parser.nextInt(0));

        position.setTime(parser.nextDateTime(Parser.DateTimeFormat.HMS_DMY, "IST"));

        return position;
    }

}
