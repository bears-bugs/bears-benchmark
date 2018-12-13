/*
 * Copyright 2013 Anton Tananaev (anton@traccar.org)
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
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

public class AplicomFrameDecoder extends FrameDecoder {

    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buf) throws Exception {

        // Skip Alive message
        while (buf.readable() && Character.isDigit(buf.getByte(buf.readerIndex()))) {
            buf.readByte();
        }

        // Check minimum length
        if (buf.readableBytes() < 11) {
            return null;
        }

        // Read flags
        int version = buf.getUnsignedByte(buf.readerIndex() + 1);
        int offset = 1 + 1 + 3;
        if ((version & 0x80) != 0) {
            offset += 4;
        }

        // Get data length
        int length = buf.getUnsignedShort(buf.readerIndex() + offset);
        offset += 2;
        if ((version & 0x40) != 0) {
            offset += 3;
        }
        length += offset; // add header

        // Return buffer
        if (buf.readableBytes() >= length) {
            return buf.readBytes(length);
        }

        return null;
    }

}
