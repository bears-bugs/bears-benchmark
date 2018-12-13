/*
 * Copyright 2014 Anton Tananaev (anton@traccar.org)
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
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.traccar.BaseProtocolDecoder;
import org.traccar.DeviceSession;
import org.traccar.helper.BitUtil;
import org.traccar.helper.DateBuilder;
import org.traccar.model.Position;

import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class PiligrimProtocolDecoder extends BaseProtocolDecoder {

    public PiligrimProtocolDecoder(PiligrimProtocol protocol) {
        super(protocol);
    }

    private void sendResponse(Channel channel, String message) {
        if (channel != null) {
            HttpResponse response = new DefaultHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            response.setContent(ChannelBuffers.copiedBuffer(
                    ByteOrder.BIG_ENDIAN, message, StandardCharsets.US_ASCII));
            channel.write(response);
        }
    }

    public static final int MSG_GPS = 0xF1;
    public static final int MSG_GPS_SENSORS = 0xF2;
    public static final int MSG_EVENTS = 0xF3;

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        HttpRequest request = (HttpRequest) msg;
        String uri = request.getUri();

        if (uri.startsWith("/config")) {

            sendResponse(channel, "CONFIG: OK");

        } else if (uri.startsWith("/addlog")) {

            sendResponse(channel, "ADDLOG: OK");

        } else if (uri.startsWith("/inform")) {

            sendResponse(channel, "INFORM: OK");

        } else if (uri.startsWith("/bingps")) {

            sendResponse(channel, "BINGPS: OK");

            QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
            DeviceSession deviceSession = getDeviceSession(
                    channel, remoteAddress, decoder.getParameters().get("imei").get(0));
            if (deviceSession == null) {
                return null;
            }

            List<Position> positions = new LinkedList<>();
            ChannelBuffer buf = request.getContent();

            while (buf.readableBytes() > 2) {

                buf.readUnsignedByte(); // header
                int type = buf.readUnsignedByte();
                buf.readUnsignedByte(); // length

                if (type == MSG_GPS || type == MSG_GPS_SENSORS) {

                    Position position = new Position();
                    position.setProtocol(getProtocolName());
                    position.setDeviceId(deviceSession.getDeviceId());

                    DateBuilder dateBuilder = new DateBuilder()
                            .setDay(buf.readUnsignedByte())
                            .setMonth(buf.getByte(buf.readerIndex()) & 0x0f)
                            .setYear(2010 + (buf.readUnsignedByte() >> 4))
                            .setTime(buf.readUnsignedByte(), buf.readUnsignedByte(), buf.readUnsignedByte());
                    position.setTime(dateBuilder.getDate());

                    double latitude = buf.readUnsignedByte();
                    latitude += buf.readUnsignedByte() / 60.0;
                    latitude += buf.readUnsignedByte() / 6000.0;
                    latitude += buf.readUnsignedByte() / 600000.0;

                    double longitude = buf.readUnsignedByte();
                    longitude += buf.readUnsignedByte() / 60.0;
                    longitude += buf.readUnsignedByte() / 6000.0;
                    longitude += buf.readUnsignedByte() / 600000.0;

                    int flags = buf.readUnsignedByte();
                    if (BitUtil.check(flags, 0)) {
                        latitude = -latitude;
                    }
                    if (BitUtil.check(flags, 1)) {
                        longitude = -longitude;
                    }
                    position.setLatitude(latitude);
                    position.setLongitude(longitude);

                    int satellites = buf.readUnsignedByte();
                    position.set(Position.KEY_SATELLITES, satellites);
                    position.setValid(satellites >= 3);

                    position.setSpeed(buf.readUnsignedByte());

                    double course = buf.readUnsignedByte() << 1;
                    course += (flags >> 2) & 1;
                    course += buf.readUnsignedByte() / 100.0;
                    position.setCourse(course);

                    if (type == MSG_GPS_SENSORS) {
                        double power = buf.readUnsignedByte();
                        power += buf.readUnsignedByte() << 8;
                        position.set(Position.KEY_POWER, power * 0.01);

                        double battery = buf.readUnsignedByte();
                        battery += buf.readUnsignedByte() << 8;
                        position.set(Position.KEY_BATTERY, battery * 0.01);

                        buf.skipBytes(6);
                    }

                    positions.add(position);

                } else if (type == MSG_EVENTS) {

                    buf.skipBytes(13);
                }
            }

            return positions;
        }

        return null;
    }

}
