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

import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.Date;
import java.util.regex.Pattern;

public class GpsmtaProtocolDecoder extends BaseProtocolDecoder {

    public GpsmtaProtocolDecoder(GpsmtaProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .expression("([^ ]+) ")              // uid
            .number("(d+) ")                     // time (unix time)
            .number("(d+.d+) ")                  // latitude
            .number("(d+.d+) ")                  // longitude
            .number("(d+) ")                     // speed
            .number("(d+) ")                     // course
            .number("(d+) ")                     // accuracy
            .number("(d+) ")                     // altitude
            .number("(d+) ")                     // flags
            .number("(d+) ")                     // battery
            .number("(d+) ")                     // temperature
            .number("(d)")                       // changing status
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

        String time = parser.next();
        position.setTime(new Date(Long.parseLong(time) * 1000));

        position.setLatitude(parser.nextDouble(0));
        position.setLongitude(parser.nextDouble(0));
        position.setSpeed(parser.nextInt(0));
        position.setCourse(parser.nextInt(0));
        position.setAccuracy(parser.nextInt(0));
        position.setAltitude(parser.nextInt(0));

        position.set(Position.KEY_STATUS, parser.nextInt(0));
        position.set(Position.KEY_BATTERY, parser.nextInt(0));
        position.set(Position.PREFIX_TEMP + 1, parser.nextInt(0));
        position.set(Position.KEY_CHARGE, parser.nextInt(0) == 1);

        if (channel != null) {
            channel.write(time, remoteAddress);
        }

        return position;
    }

}
