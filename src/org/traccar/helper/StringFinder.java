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
package org.traccar.helper;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferIndexFinder;

import java.nio.charset.StandardCharsets;

public class StringFinder implements ChannelBufferIndexFinder {

    private String string;

    public StringFinder(String string) {
        this.string = string;
    }

    @Override
    public boolean find(ChannelBuffer buffer, int guessedIndex) {

        if (buffer.writerIndex() - guessedIndex < string.length()) {
            return false;
        }

        return string.equals(buffer.toString(guessedIndex, string.length(), StandardCharsets.US_ASCII));
    }

}
