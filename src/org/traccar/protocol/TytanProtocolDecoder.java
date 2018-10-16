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
import org.traccar.helper.BitUtil;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TytanProtocolDecoder extends BaseProtocolDecoder {

    public TytanProtocolDecoder(TytanProtocol protocol) {
        super(protocol);
    }

    private void decodeExtraData(Position position, ChannelBuffer buf, int end) {
        while (buf.readerIndex() < end) {

            int type = buf.readUnsignedByte();
            int length = buf.readUnsignedByte();
            if (length == 255) {
                length += buf.readUnsignedByte();
            }

            int n;

            switch (type) {
                case 2:
                    position.set(Position.KEY_TRIP_ODOMETER, buf.readUnsignedMedium());
                    break;
                case 5:
                    position.set(Position.KEY_INPUT, buf.readUnsignedByte());
                    break;
                case 6:
                    n = buf.readUnsignedByte() >> 4;
                    if (n < 2) {
                        position.set(Position.PREFIX_ADC + n, buf.readFloat());
                    } else {
                        position.set("di" + (n - 2), buf.readFloat());
                    }
                    break;
                case 7:
                    int alarm = buf.readUnsignedByte();
                    buf.readUnsignedByte();
                    if (BitUtil.check(alarm, 5)) {
                        position.set(Position.KEY_ALARM, Position.ALARM_GENERAL);
                    }
                    break;
                case 8:
                    position.set("antihijack", buf.readUnsignedByte());
                    break;
                case 9:
                    position.set("unauthorized", ChannelBuffers.hexDump(buf.readBytes(8)));
                    break;
                case 10:
                    position.set("authorized", ChannelBuffers.hexDump(buf.readBytes(8)));
                    break;
                case 24:
                    for (int i = 0; i < length / 2; i++) {
                        position.set(Position.PREFIX_TEMP + buf.readUnsignedByte(), buf.readByte());
                    }
                    break;
                case 28:
                    position.set("weight", buf.readUnsignedShort());
                    buf.readUnsignedByte();
                    break;
                case 90:
                    position.set(Position.KEY_POWER, buf.readFloat());
                    break;
                case 101:
                    position.set(Position.KEY_OBD_SPEED, buf.readUnsignedByte());
                    break;
                case 102:
                    position.set(Position.KEY_RPM, buf.readUnsignedByte() * 50);
                    break;
                case 107:
                    int fuel = buf.readUnsignedShort();
                    int fuelFormat = fuel >> 14;
                    if (fuelFormat == 1) {
                        position.set(Position.KEY_FUEL, (fuel & 0x3fff) * 0.4 + "%");
                    } else if (fuelFormat == 2) {
                        position.set(Position.KEY_FUEL, (fuel & 0x3fff) * 0.5 + " l");
                    } else if (fuelFormat == 3) {
                        position.set(Position.KEY_FUEL, (fuel & 0x3fff) * -0.5 + " l");
                    }
                    break;
                case 108:
                    position.set(Position.KEY_OBD_ODOMETER, buf.readUnsignedInt() * 5);
                    break;
                case 150:
                    position.set("door", buf.readUnsignedByte());
                    break;
                default:
                    buf.skipBytes(length);
                    break;
            }
        }
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        buf.readUnsignedByte(); // protocol
        buf.readUnsignedShort(); // length
        int index = buf.readUnsignedByte() >> 3;

        if (channel != null) {
            ChannelBuffer response = ChannelBuffers.copiedBuffer(
                    "^" + index, StandardCharsets.US_ASCII);
            channel.write(response, remoteAddress);
        }

        String id = String.valueOf(buf.readUnsignedInt());
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, id);
        if (deviceSession == null) {
            return null;
        }

        List<Position> positions = new LinkedList<>();

        while (buf.readableBytes() > 2) {

            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            int end = buf.readerIndex() + buf.readUnsignedByte();

            position.setTime(new Date(buf.readUnsignedInt() * 1000));

            int flags = buf.readUnsignedByte();
            position.set(Position.KEY_SATELLITES, BitUtil.from(flags, 2));
            position.setValid(BitUtil.to(flags, 2) > 0);

            // Latitude
            double lat = buf.readUnsignedMedium();
            lat = lat * -180 / 16777216 + 90;
            position.setLatitude(lat);

            // Longitude
            double lon = buf.readUnsignedMedium();
            lon = lon * 360 / 16777216 - 180;
            position.setLongitude(lon);

            // Status
            flags = buf.readUnsignedByte();
            position.set(Position.KEY_IGNITION, BitUtil.check(flags, 0));
            position.set(Position.KEY_RSSI, BitUtil.between(flags, 2, 5));
            position.setCourse((BitUtil.from(flags, 5) * 45 + 180) % 360);

            // Speed
            int speed = buf.readUnsignedByte();
            if (speed < 250) {
                position.setSpeed(UnitsConverter.knotsFromKph(speed));
            }

            decodeExtraData(position, buf, end);

            positions.add(position);
        }

        return positions;
    }

}
