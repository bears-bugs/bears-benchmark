/*
 * Copyright 2014 - 2016 Anton Tananaev (anton@traccar.org)
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
import org.traccar.helper.DateUtil;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TramigoProtocolDecoder extends BaseProtocolDecoder {

    public TramigoProtocolDecoder(TramigoProtocol protocol) {
        super(protocol);
    }

    public static final int MSG_COMPACT = 0x0100;
    public static final int MSG_FULL = 0x00FE;

    private static final String[] DIRECTIONS = new String[] {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        int protocol = buf.readUnsignedByte();
        buf.readUnsignedByte(); // version id
        int index = buf.readUnsignedShort();
        int type = buf.readUnsignedShort();
        buf.readUnsignedShort(); // length
        buf.readUnsignedShort(); // mask
        buf.readUnsignedShort(); // checksum
        long id = buf.readUnsignedInt();
        buf.readUnsignedInt(); // time

        Position position = new Position();
        position.setProtocol(getProtocolName());
        position.set(Position.KEY_INDEX, index);
        position.setValid(true);

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, String.valueOf(id));
        if (deviceSession == null) {
            return null;
        }
        position.setDeviceId(deviceSession.getDeviceId());

        if (protocol == 0x01 && (type == MSG_COMPACT || type == MSG_FULL)) {

            // need to send ack?

            buf.readUnsignedShort(); // report trigger
            buf.readUnsignedShort(); // state flag

            position.setLatitude(buf.readUnsignedInt() * 0.0000001);
            position.setLongitude(buf.readUnsignedInt() * 0.0000001);

            position.set(Position.KEY_RSSI, buf.readUnsignedShort());
            position.set(Position.KEY_SATELLITES, buf.readUnsignedShort());
            position.set(Position.KEY_SATELLITES_VISIBLE, buf.readUnsignedShort());
            position.set("gpsAntennaStatus", buf.readUnsignedShort());

            position.setSpeed(buf.readUnsignedShort() * 0.194384);
            position.setCourse((double) buf.readUnsignedShort());

            position.set(Position.KEY_ODOMETER, buf.readUnsignedInt());

            position.set(Position.KEY_BATTERY, buf.readUnsignedShort());

            position.set(Position.KEY_CHARGE, buf.readUnsignedShort());

            position.setTime(new Date(buf.readUnsignedInt() * 1000));

            // parse other data

            return position;

        } else if (protocol == 0x80) {

            if (channel != null) {
                channel.write(ChannelBuffers.copiedBuffer("gprs,ack," + index, StandardCharsets.US_ASCII));
            }

            String sentence = buf.toString(StandardCharsets.US_ASCII);

            Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+), (-?\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(sentence);
            if (!matcher.find()) {
                return null;
            }
            position.setLatitude(Double.parseDouble(matcher.group(1)));
            position.setLongitude(Double.parseDouble(matcher.group(2)));

            pattern = Pattern.compile("([NSWE]{1,2}) with speed (\\d+) km/h");
            matcher = pattern.matcher(sentence);
            if (matcher.find()) {
                for (int i = 0; i < DIRECTIONS.length; i++) {
                    if (matcher.group(1).equals(DIRECTIONS[i])) {
                        position.setCourse(i * 45.0);
                        break;
                    }
                }
                position.setSpeed(UnitsConverter.knotsFromKph(Double.parseDouble(matcher.group(2))));
            }

            pattern = Pattern.compile("(\\d{1,2}:\\d{2} \\w{3} \\d{1,2})");
            matcher = pattern.matcher(sentence);
            if (!matcher.find()) {
                return null;
            }
            DateFormat dateFormat = new SimpleDateFormat("HH:mm MMM d yyyy", Locale.ENGLISH);
            position.setTime(DateUtil.correctYear(
                    dateFormat.parse(matcher.group(1) + " " + Calendar.getInstance().get(Calendar.YEAR))));

            if (sentence.contains("Ignition on detected")) {
                position.set(Position.KEY_IGNITION, true);
            } else if (sentence.contains("Ignition off detected")) {
                position.set(Position.KEY_IGNITION, false);
            }

            return position;

        }

        return null;
    }

}
