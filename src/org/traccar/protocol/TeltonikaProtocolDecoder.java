/*
 * Copyright 2013 - 2016 Anton Tananaev (anton@traccar.org)
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
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class TeltonikaProtocolDecoder extends BaseProtocolDecoder {

    public TeltonikaProtocolDecoder(TeltonikaProtocol protocol) {
        super(protocol);
    }

    private void parseIdentification(Channel channel, SocketAddress remoteAddress, ChannelBuffer buf) {

        int length = buf.readUnsignedShort();
        String imei = buf.toString(buf.readerIndex(), length, StandardCharsets.US_ASCII);
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, imei);

        if (channel != null) {
            ChannelBuffer response = ChannelBuffers.directBuffer(1);
            if (deviceSession != null) {
                response.writeByte(1);
            } else {
                response.writeByte(0);
            }
            channel.write(response);
        }
    }

    public static final int CODEC_GH3000 = 0x07;
    public static final int CODEC_FM4X00 = 0x08;
    public static final int CODEC_12 = 0x0C;

    private void decodeSerial(Position position, ChannelBuffer buf) {

        getLastLocation(position, null);

        position.set(Position.KEY_TYPE, buf.readUnsignedByte());

        position.set("command", buf.readBytes(buf.readInt()).toString(StandardCharsets.US_ASCII));

    }

    private void decodeParameter(Position position, int id, long value) {
        switch (id) {
            case 9:
                position.set(Position.PREFIX_ADC + 1, value);
                break;
            case 66:
                position.set(Position.KEY_POWER, value);
                break;
            case 68:
                position.set(Position.KEY_BATTERY, value);
                break;
            case 85:
                position.set(Position.KEY_RPM, value);
                break;
            case 182:
                position.set(Position.KEY_HDOP, value);
                break;
            case 239:
                position.set(Position.KEY_IGNITION, value == 1);
                break;
            default:
                position.set(Position.PREFIX_IO + id, value);
                break;
        }
    }

    private void decodeLocation(Position position, ChannelBuffer buf, int codec) {

        int globalMask = 0x0f;

        if (codec == CODEC_GH3000) {

            long time = buf.readUnsignedInt() & 0x3fffffff;
            time += 1167609600; // 2007-01-01 00:00:00

            globalMask = buf.readUnsignedByte();
            if (BitUtil.check(globalMask, 0)) {

                position.setTime(new Date(time * 1000));

                int locationMask = buf.readUnsignedByte();

                if (BitUtil.check(locationMask, 0)) {
                    position.setLatitude(buf.readFloat());
                    position.setLongitude(buf.readFloat());
                }

                if (BitUtil.check(locationMask, 1)) {
                    position.setAltitude(buf.readUnsignedShort());
                }

                if (BitUtil.check(locationMask, 2)) {
                    position.setCourse(buf.readUnsignedByte() * 360.0 / 256);
                }

                if (BitUtil.check(locationMask, 3)) {
                    position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedByte()));
                }

                if (BitUtil.check(locationMask, 4)) {
                    int satellites = buf.readUnsignedByte();
                    position.set(Position.KEY_SATELLITES, satellites);
                    position.setValid(satellites >= 3);
                }

                if (BitUtil.check(locationMask, 5)) {
                    position.setNetwork(new Network(
                            CellTower.fromLacCid(buf.readUnsignedShort(), buf.readUnsignedShort())));
                }

                if (BitUtil.check(locationMask, 6)) {
                    buf.readUnsignedByte(); // rssi
                }

                if (BitUtil.check(locationMask, 7)) {
                    position.set("operator", buf.readUnsignedInt());
                }

            } else {

                getLastLocation(position, new Date(time * 1000));

            }

        } else {

            position.setTime(new Date(buf.readLong()));

            position.set("priority", buf.readUnsignedByte());

            position.setLongitude(buf.readInt() / 10000000.0);
            position.setLatitude(buf.readInt() / 10000000.0);
            position.setAltitude(buf.readShort());
            position.setCourse(buf.readUnsignedShort());

            int satellites = buf.readUnsignedByte();
            position.set(Position.KEY_SATELLITES, satellites);

            position.setValid(satellites != 0);

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedShort()));

            position.set(Position.KEY_EVENT, buf.readUnsignedByte());

            buf.readUnsignedByte(); // total IO data records

        }

        // Read 1 byte data
        if (BitUtil.check(globalMask, 1)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf.readUnsignedByte());
            }
        }

        // Read 2 byte data
        if (BitUtil.check(globalMask, 2)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf.readUnsignedShort());
            }
        }

        // Read 4 byte data
        if (BitUtil.check(globalMask, 3)) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf.readUnsignedInt());
            }
        }

        // Read 8 byte data
        if (codec == CODEC_FM4X00) {
            int cnt = buf.readUnsignedByte();
            for (int j = 0; j < cnt; j++) {
                decodeParameter(position, buf.readUnsignedByte(), buf.readLong());
            }
        }

    }

    private List<Position> parseData(Channel channel, SocketAddress remoteAddress, ChannelBuffer buf) {
        List<Position> positions = new LinkedList<>();

        buf.skipBytes(4); // marker
        buf.readUnsignedInt(); // data length
        int codec = buf.readUnsignedByte();
        int count = buf.readUnsignedByte();

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress);
        if (deviceSession == null) {
            return null;
        }

        for (int i = 0; i < count; i++) {
            Position position = new Position();
            position.setProtocol(getProtocolName());

            position.setDeviceId(deviceSession.getDeviceId());

            if (codec == CODEC_12) {
                decodeSerial(position, buf);
            } else {
                decodeLocation(position, buf, codec);
            }

            positions.add(position);
        }

        if (channel != null) {
            ChannelBuffer response = ChannelBuffers.directBuffer(4);
            response.writeInt(count);
            channel.write(response);
        }

        return positions;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        if (buf.getUnsignedShort(0) > 0) {
            parseIdentification(channel, remoteAddress, buf);
        } else {
            return parseData(channel, remoteAddress, buf);
        }

        return null;
    }

}
