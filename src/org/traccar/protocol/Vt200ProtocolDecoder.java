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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.helper.BcdUtil;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.Position;

import java.net.SocketAddress;

public class Vt200ProtocolDecoder extends BaseProtocolDecoder {

    public Vt200ProtocolDecoder(Vt200Protocol protocol) {
        super(protocol);
    }

    private static double decodeCoordinate(int value) {
        int degrees = value / 1000000;
        int minutes = value % 1000000;
        return degrees + minutes * 0.0001 / 60;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        ChannelBuffer buf = (ChannelBuffer) msg;

        buf.skipBytes(1); // header

        String id = ChannelBuffers.hexDump(buf.readBytes(6));
        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, id);
        if (deviceSession == null) {
            return null;
        }

        int type = buf.readUnsignedShort();
        buf.readUnsignedShort(); // length

        if (type == 0x2084) {

            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            buf.readUnsignedByte(); // data type
            buf.readUnsignedShort(); // trip id

            DateBuilder dateBuilder = new DateBuilder();
            dateBuilder.setDateReverse(
                    BcdUtil.readInteger(buf, 2), BcdUtil.readInteger(buf, 2), BcdUtil.readInteger(buf, 2));
            dateBuilder.setTime(
                    BcdUtil.readInteger(buf, 2), BcdUtil.readInteger(buf, 2), BcdUtil.readInteger(buf, 2));
            position.setTime(dateBuilder.getDate());

            position.setLatitude(decodeCoordinate(BcdUtil.readInteger(buf, 8)));
            position.setLongitude(decodeCoordinate(BcdUtil.readInteger(buf, 9)));

            int flags = buf.readUnsignedByte();
            position.setValid(BitUtil.check(flags, 0));
            if (!BitUtil.check(flags, 1)) {
                position.setLatitude(-position.getLatitude());
            }
            if (!BitUtil.check(flags, 1)) {
                position.setLongitude(-position.getLongitude());
            }

            position.setSpeed(UnitsConverter.knotsFromKph(buf.readUnsignedByte()));
            position.setCourse(buf.readUnsignedByte() * 2);

            position.set(Position.KEY_SATELLITES, buf.readUnsignedByte());
            position.set(Position.KEY_RSSI, buf.readUnsignedByte());
            position.set(Position.KEY_ODOMETER, buf.readUnsignedInt() * 1000);
            position.set(Position.KEY_STATUS, buf.readUnsignedInt());

            // additional data

            return position;

        }

        return null;
    }

}
