/*
 * Copyright 2013 - 2015 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.regex.Pattern;

public class EasyTrackProtocolDecoder extends BaseProtocolDecoder {

    public EasyTrackProtocolDecoder(EasyTrackProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .text("*").expression("..,")         // manufacturer
            .number("(d+),")                     // imei
            .expression("([^,]{2}),")            // command
            .expression("([AV]),")               // validity
            .number("(xx)(xx)(xx),")             // date (yymmdd)
            .number("(xx)(xx)(xx),")             // time (hhmmss)
            .number("(x)(x{7}),")                // latitude
            .number("(x)(x{7}),")                // longitude
            .number("(x{4}),")                   // speed
            .number("(x{4}),")                   // course
            .number("(x{8}),")                   // status
            .number("(x+),")                     // signal
            .number("(d+),")                     // power
            .number("(x{4}),")                   // oil
            .number("(x+),?")                    // odometer
            .number("(d+)?")                     // altitude
            .any()
            .compile();

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        Parser parser = new Parser(PATTERN, (String) msg);
        if (!parser.matches()) {
            return null;
        }

        Position position = new Position();
        position.setProtocol(getProtocolName());

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        position.set(Position.KEY_COMMAND, parser.next());

        position.setValid(parser.next().equals("A"));

        DateBuilder dateBuilder = new DateBuilder()
                .setDate(parser.nextHexInt(0), parser.nextHexInt(0), parser.nextHexInt(0))
                .setTime(parser.nextHexInt(0), parser.nextHexInt(0), parser.nextHexInt(0));
        position.setTime(dateBuilder.getDate());

        if (BitUtil.check(parser.nextHexInt(0), 3)) {
            position.setLatitude(-parser.nextHexInt(0) / 600000.0);
        } else {
            position.setLatitude(parser.nextHexInt(0) / 600000.0);
        }

        if (BitUtil.check(parser.nextHexInt(0), 3)) {
            position.setLongitude(-parser.nextHexInt(0) / 600000.0);
        } else {
            position.setLongitude(parser.nextHexInt(0) / 600000.0);
        }

        position.setSpeed(parser.nextHexInt(0) / 100.0);
        position.setCourse(parser.nextHexInt(0) / 100.0);

        position.set(Position.KEY_STATUS, parser.next());
        position.set("signal", parser.next());
        position.set(Position.KEY_POWER, parser.nextDouble(0));
        position.set("oil", parser.nextHexInt(0));
        position.set(Position.KEY_ODOMETER, parser.nextHexInt(0) * 100);

        position.setAltitude(parser.nextDouble(0));

        return position;
    }

}
