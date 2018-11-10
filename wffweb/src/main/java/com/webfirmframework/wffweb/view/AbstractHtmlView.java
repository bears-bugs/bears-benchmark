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
 * @author WFF
 */
/**
 *
 */
package com.webfirmframework.wffweb.view;

import java.nio.charset.Charset;

import com.webfirmframework.wffweb.io.OutputBuffer;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public abstract class AbstractHtmlView implements HtmlView {

    private static final long serialVersionUID = 1_0_0L;

    private boolean preserveOutputBufferContent;

    private Charset charset = Charset.defaultCharset();

    private static final ThreadLocal<OutputBuffer> outputBufferTL = new ThreadLocal<OutputBuffer>() {

        /*
         * (non-Javadoc)
         *
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected OutputBuffer initialValue() {
            return new OutputBuffer();
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see com.webfirmframework.wffweb.tag.Base#toValueString()
     */
    @Override
    public String toHtmlString() {
        final OutputBuffer outputBuffer = outputBufferTL.get();
        if (!preserveOutputBufferContent) {
            outputBuffer.setLength(0);
        }
        develop(outputBuffer);
        return outputBuffer.toBuilderString();
    }

    @Override
    public String toHtmlString(final Charset charset) {
        final Charset previousCharset = this.charset;
        try {
            this.charset = charset;
            return toHtmlString();
        } finally {
            this.charset = previousCharset;
        }
    }

    @Override
    public String toHtmlString(final String charset) {
        final Charset previousCharset = this.charset;
        try {
            this.charset = Charset.forName(charset);
            return toHtmlString();
        } finally {
            this.charset = previousCharset;
        }
    }

    @Override
    public String toHtmlString(final boolean rebuild) {
        final OutputBuffer outputBuffer = outputBufferTL.get();
        outputBuffer.setRebuild(rebuild);
        if (!preserveOutputBufferContent) {
            outputBuffer.setLength(0);
        }
        develop(outputBuffer);
        return outputBuffer.toBuilderString();
    }

    @Override
    public String toHtmlString(final boolean rebuild, final Charset charset) {
        final Charset previousCharset = this.charset;
        try {
            this.charset = charset;
            return toHtmlString(rebuild);
        } finally {
            this.charset = previousCharset;
        }
    }

    @Override
    public String toHtmlString(final boolean rebuild, final String charset) {
        final Charset previousCharset = this.charset;
        try {
            this.charset = Charset.forName(charset);
            return toHtmlString(rebuild);
        } finally {
            this.charset = previousCharset;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    // it is not a best practice to print html string by this method because if
    // it is used in ThreadLocal class it may cause memory leak.
    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return the preserveOutputBufferContent
     */
    public boolean isPreserveOutputBufferContent() {
        return preserveOutputBufferContent;
    }

    /**
     * To preserve the content in {@code OutputBuffer} in
     * {@code HtmlView#develop(OutputBuffer)} method argument so that the new
     * content can be appended in the next invocation of
     * {@code HtmlView#develop(OutputBuffer)} method. The next invocation can be
     * done by invoking methods like {@code AbstractHtmlView#toHtmlString()} and
     * {@code AbstractHtmlView#toHtmlString(boolean)}.
     *
     * @param preserveOutputBufferContent
     *            <code>true</code> to preserve and <code>false</code> for not
     *            to preserve. The default values is <code>false</code>.
     *
     *
     */
    public void setPreserveOutputBufferContent(
            final boolean preserveOutputBufferContent) {
        this.preserveOutputBufferContent = preserveOutputBufferContent;
    }

}
