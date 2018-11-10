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
package com.webfirmframework.wffweb.server.page.action;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.server.page.Task;
import com.webfirmframework.wffweb.util.WffBinaryMessageUtil;
import com.webfirmframework.wffweb.util.data.NameValue;

public enum BrowserPageAction {

    RELOAD(Task.RELOAD_BROWSER,
            null), RELOAD_FROM_CACHE(Task.RELOAD_BROWSER_FROM_CACHE, null);

    private static final Logger LOGGER = Logger
            .getLogger(BrowserPageAction.class.getName());

    private byte[] actionBytes;

    private BrowserPageAction(final Task task, final String js) {
        init(task, js);
    }

    private void init(final Task task, final String js) {
        try {

            if (js != null) {

                final NameValue nameValue = new NameValue();
                nameValue.setName(js.getBytes("UTF-8"));
                nameValue.setValues(new byte[][] {});

                actionBytes = WffBinaryMessageUtil.VERSION_1
                        .getWffBinaryMessageBytes(
                                Task.RELOAD_BROWSER.getTaskNameValue(),
                                nameValue);
            } else {
                actionBytes = WffBinaryMessageUtil.VERSION_1
                        .getWffBinaryMessageBytes(
                                Task.RELOAD_BROWSER.getTaskNameValue());
            }

        } catch (final UnsupportedEncodingException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }

    /**
     * @return the bytes for the browser action
     * @since 2.0.3
     * @author WFF
     */
    public byte[] getActionBytes() {
        return actionBytes;
    }

    /**
     * @return the action {@code ByteBuffer} for the browser action
     * @since 2.0.3
     * @author WFF
     */
    public ByteBuffer getActionByteBuffer() {
        return ByteBuffer.wrap(actionBytes);
    }

    /**
     * Gets the action {@code ByteBuffer} for executing the given JavaScript
     *
     * @param js
     *            JavaScript to execute in the browser
     * @return the action {@code ByteBuffer} for executing the given JavaScript
     *         in the browser.
     * @throws UnsupportedEncodingException
     * @since 2.1.0
     * @author WFF
     */
    public static ByteBuffer getActionByteBufferForExecuteJS(final String js)
            throws UnsupportedEncodingException {
        return ByteBuffer.wrap(getActionBytesForExecuteJS(js));
    }

    /**
     * Gets the action bytes for executing the given JavaScript in the browser.
     *
     * @param js
     *            JavaScript to execute in the browser
     * @return the action bytes for executing the given JavaScript in the
     *         browser.
     * @throws UnsupportedEncodingException
     * @since 2.1.0
     * @author WFF
     */
    public static byte[] getActionBytesForExecuteJS(final String js)
            throws UnsupportedEncodingException {

        final NameValue taskNameValue = Task.EXECURE_JS.getTaskNameValue();
        final byte[][] taskValue = taskNameValue.getValues();
        final byte[][] values = new byte[taskValue.length + 1][0];

        System.arraycopy(taskValue, 0, values, 0, taskValue.length);

        values[taskValue.length] = js.getBytes("UTF-8");

        taskNameValue.setValues(values);

        return WffBinaryMessageUtil.VERSION_1
                .getWffBinaryMessageBytes(taskNameValue);
    }

}
