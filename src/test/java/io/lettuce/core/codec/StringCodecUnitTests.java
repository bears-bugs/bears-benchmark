/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.codec;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import io.lettuce.core.protocol.LettuceCharsets;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Mark Paluch
 */
class StringCodecUnitTests {

    private String teststring = "hello üäü~∑†®†ª€∂‚¶¢ Wørld";
    private String teststringPlain = "hello uufadsfasdfadssdfadfs";

    @Test
    void encodeUtf8Buf() {

        StringCodec codec = new StringCodec(LettuceCharsets.UTF8);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encode(teststring, buffer);

        assertThat(buffer.toString(StandardCharsets.UTF_8)).isEqualTo(teststring);
    }

    @Test
    void encodeAsciiBuf() {

        StringCodec codec = new StringCodec(LettuceCharsets.ASCII);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encode(teststringPlain, buffer);

        assertThat(buffer.toString(LettuceCharsets.ASCII)).isEqualTo(teststringPlain);
    }

    @Test
    void encodeIso88591Buf() {

        StringCodec codec = new StringCodec(StandardCharsets.ISO_8859_1);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encodeValue(teststringPlain, buffer);

        assertThat(buffer.toString(StandardCharsets.ISO_8859_1)).isEqualTo(teststringPlain);
    }

    @Test
    void encodeAndDecodeUtf8Buf() {

        StringCodec codec = new StringCodec(LettuceCharsets.UTF8);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encodeKey(teststring, buffer);

        assertThat(codec.decodeKey(buffer.nioBuffer())).isEqualTo(teststring);
    }

    @Test
    void encodeAndDecodeUtf8() {

        StringCodec codec = new StringCodec(LettuceCharsets.UTF8);
        ByteBuffer byteBuffer = codec.encodeKey(teststring);

        assertThat(codec.decodeKey(byteBuffer)).isEqualTo(teststring);
    }

    @Test
    void encodeAndDecodeAsciiBuf() {

        StringCodec codec = new StringCodec(LettuceCharsets.ASCII);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encode(teststringPlain, buffer);

        assertThat(codec.decodeKey(buffer.nioBuffer())).isEqualTo(teststringPlain);
    }

    @Test
    void encodeAndDecodeIso88591Buf() {

        StringCodec codec = new StringCodec(StandardCharsets.ISO_8859_1);

        ByteBuf buffer = Unpooled.buffer(1234);
        codec.encode(teststringPlain, buffer);

        assertThat(codec.decodeKey(buffer.nioBuffer())).isEqualTo(teststringPlain);
    }

    @Test
    void estimateSize() {

        assertThat(new StringCodec(LettuceCharsets.UTF8).estimateSize(teststring)).isEqualTo((int) (teststring.length() * 1.1));
        assertThat(new StringCodec(LettuceCharsets.ASCII).estimateSize(teststring)).isEqualTo(teststring.length());
        assertThat(new StringCodec(StandardCharsets.ISO_8859_1).estimateSize(teststring)).isEqualTo(teststring.length());
    }
}
