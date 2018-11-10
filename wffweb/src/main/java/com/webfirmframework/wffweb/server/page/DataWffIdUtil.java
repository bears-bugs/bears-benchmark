/*
 * Copyright 2014-2018 Web Firm Framework
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
package com.webfirmframework.wffweb.server.page;

import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Deque;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.html5.attribute.global.DataWffId;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;

final class DataWffIdUtil {

    private DataWffIdUtil() {
        throw new AssertionError();
    }

    static byte[] getDataWffIdBytes(final String dataWffId)
            throws UnsupportedEncodingException {

        // the first byte represents C for Client and S for Server and the
        // remaining string is an integer value
        final byte sOrC = dataWffId.getBytes("UTF-8")[0];

        final byte[] intBytes = WffBinaryMessageUtil.getOptimizedBytesFromInt(
                Integer.parseInt(dataWffId.substring(1)));

        final byte[] dataWffIdBytes = new byte[1 + intBytes.length];
        dataWffIdBytes[0] = sOrC;
        System.arraycopy(intBytes, 0, dataWffIdBytes, 1, intBytes.length);

        return dataWffIdBytes;
    }

    /**
     * @param abstractHtml
     * @return array containing tagName bytes and dataWffIdBytes of the given
     *         argument or its parent.
     * @throws UnsupportedEncodingException
     * @since 2.0.0
     * @author WFF
     */
    static byte[][] getTagNameAndWffId(final AbstractHtml abstractHtml)
            throws UnsupportedEncodingException {

        final Deque<AbstractHtml> parentStack = new ArrayDeque<>();
        parentStack.push(abstractHtml);

        AbstractHtml parent;
        while ((parent = parentStack.poll()) != null) {

            if (parent.getTagName() != null && !parent.getTagName().isEmpty()) {
                final DataWffId dataWffId = parent.getDataWffId();

                final byte[] dataWffIdBytes = DataWffIdUtil
                        .getDataWffIdBytes(dataWffId.getValue());

                return new byte[][] { parent.getTagName().getBytes("UTF-8"),
                        dataWffIdBytes };
            }

            final AbstractHtml parentOfParent = parent.getParent();
            if (parentOfParent != null) {

                parentStack.push(parentOfParent);
            }
        }

        return null;
    }

    /**
     * @return
     * @throws UnsupportedEncodingException
     * @since 2.1.13
     * @author WFF
     */
    static byte[][] getTagNameAndWffIdForNoTag()
            throws UnsupportedEncodingException {
        // there is no DataWffId attribute for NoTag
        return new byte[2][0];
    }
}
