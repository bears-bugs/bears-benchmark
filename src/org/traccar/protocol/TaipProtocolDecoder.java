/*
 * Copyright 2013 - 2017 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.Checksum;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.DateUtil;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.util.Date;
import java.util.regex.Pattern;

public class TaipProtocolDecoder extends BaseProtocolDecoder {

    public TaipProtocolDecoder(TaipProtocol protocol) {
        super(protocol);
    }

    private static final Pattern PATTERN = new PatternBuilder()
            .groupBegin()
            .expression("R[EP]V")                // type
            .groupBegin()
            .number("(dd)")                      // event
            .number("(dddd)")                    // week
            .number("(d)")                       // day
            .groupEnd("?")
            .number("(d{5})")                    // seconds
            .or()
            .expression("(?:RGP|RCQ|RBR)")       // type
            .number("(dd)?")                     // event
            .number("(dd)(dd)(dd)")              // date (mmddyy)
            .number("(dd)(dd)(dd)")              // time (hhmmss)
            .groupEnd()
            .groupBegin()
            .number("([-+]dd)(d{5})")            // latitude
            .number("([-+]ddd)(d{5})")           // longitude
            .or()
            .number("([-+])(dd)(dd.dddd)")       // latitude
            .number("([-+])(ddd)(dd.dddd)")      // longitude
            .groupEnd()
            .number("(ddd)")                     // speed
            .number("(ddd)")                     // course
            .groupBegin()
            .number("(xx)")                      // input
            .number("(xx)")                      // satellites
            .number("(ddd)")                     // battery
            .number("(x{8})")                    // odometer
            .number("[01]")                      // gps power
            .groupEnd("?")
            .number("(d)")                       // fix mode
            .any()
            .compile();

    private Date getTime(long week, long day, long seconds) {
        DateBuilder dateBuilder = new DateBuilder()
                .setDate(1980, 1, 6)
                .addMillis(((week * 7 + day) * 24 * 60 * 60 + seconds) * 1000);
        return dateBuilder.getDate();
    }

    private Date getTime(long seconds) {
        DateBuilder dateBuilder = new DateBuilder(new Date())
                .setTime(0, 0, 0, 0)
                .addMillis(seconds * 1000);
        return DateUtil.correctDay(dateBuilder.getDate());
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = (String) msg;

        int beginIndex = sentence.indexOf('>');
        if (beginIndex != -1) {
            sentence = sentence.substring(beginIndex + 1);
        }

        Parser parser = new Parser(PATTERN, sentence);
        if (!parser.matches()) {
            return null;
        }

        Position position = new Position();
        position.setProtocol(getProtocolName());

        if (parser.hasNext(3)) {
            position.set(Position.KEY_EVENT, parser.nextInt());
            position.setTime(getTime(parser.nextInt(), parser.nextInt(), parser.nextInt()));
        } else if (parser.hasNext()) {
            position.setTime(getTime(parser.nextInt()));
        }

        if (parser.hasNext()) {
            position.set(Position.KEY_EVENT, parser.nextInt());
        }

        if (parser.hasNext(6)) {
            position.setTime(parser.nextDateTime(Parser.DateTimeFormat.DMY_HMS));
        }

        if (parser.hasNext(4)) {
            position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_DEG));
            position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.DEG_DEG));
        }
        if (parser.hasNext(6)) {
            position.setLatitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN));
            position.setLongitude(parser.nextCoordinate(Parser.CoordinateFormat.HEM_DEG_MIN));
        }

        position.setSpeed(UnitsConverter.knotsFromMph(parser.nextDouble()));
        position.setCourse(parser.nextDouble());

        if (parser.hasNext(4)) {
            position.set(Position.KEY_INPUT, parser.nextInt(16));
            position.set(Position.KEY_SATELLITES, parser.nextInt(16));
            position.set(Position.KEY_BATTERY, parser.nextInt());
            position.set(Position.KEY_ODOMETER, parser.nextLong(16));
        }

        position.setValid(parser.nextInt() != 0);

        String[] attributes = null;
        beginIndex = sentence.indexOf(';');
        if (beginIndex != -1) {
            int endIndex = sentence.indexOf('<', beginIndex);
            if (endIndex == -1) {
                endIndex = sentence.length();
            }
            attributes = sentence.substring(beginIndex, endIndex).split(";");
        }

        String uniqueId = null;
        DeviceSession deviceSession = null;
        String messageIndex = null;

        if (attributes != null) {
            for (String attribute : attributes) {
                int index = attribute.indexOf('=');
                if (index != -1) {
                    String key = attribute.substring(0, index).toLowerCase();
                    String value = attribute.substring(index + 1);
                    switch (key) {

                        case "id":
                            uniqueId = value;
                            deviceSession = getDeviceSession(channel, remoteAddress, value);
                            if (deviceSession != null) {
                                position.setDeviceId(deviceSession.getDeviceId());
                            }
                            break;

                        case "sv":
                            position.set(Position.KEY_SATELLITES, value);
                            break;

                        case "bl":
                            position.set(Position.KEY_BATTERY, value);
                            break;

                        case "vo":
                            position.set(Position.KEY_ODOMETER, Long.parseLong(value));
                            break;

                        default:
                            position.set(key, value);
                            break;

                    }
                } else if (attribute.startsWith("#")) {
                    messageIndex = attribute;
                }
            }
        }

        if (deviceSession != null) {
            if (channel != null) {
                if (messageIndex != null) {
                    String response = ">ACK;" + messageIndex + ";ID=" + uniqueId + ";*";
                    response += String.format("%02X", Checksum.xor(response)) + "<";
                    channel.write(response, remoteAddress);
                } else {
                    channel.write(uniqueId, remoteAddress);
                }
            }

            return position;
        }

        return null;
    }

}
