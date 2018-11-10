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
package com.webfirmframework.wffweb.io;

import com.webfirmframework.wffweb.tag.core.AbstractTagBase;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public class OutputBuffer {

    private final StringBuilder stringBuilder;

    private boolean rebuild;

    /**
     *
     */
    public OutputBuffer() {
        stringBuilder = new StringBuilder();
    }

    /**
     *
     * @param base
     * @author WFF
     * @since 1.0.0
     */
    public void append(final AbstractTagBase base) {
        stringBuilder.append(base.toHtmlString());
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    // never modify or override this method to print html string. It may cause
    // memory leak as the object is used in ThreadLocal class,
    // check com.webfirmframework.wffweb.view.AbstractHtmlView
    @Override
    public final String toString() {
        return super.toString();
    }

    public String toBuilderString() {
        stringBuilder.trimToSize();
        return stringBuilder.toString();
    }

    /**
     * @return the rebuild
     * @since 1.0.0
     * @author WFF
     */
    public boolean isRebuild() {
        return rebuild;
    }

    /**
     * @param rebuild
     *            the rebuild to set
     * @since 1.0.0
     * @author WFF
     */
    public void setRebuild(final boolean rebuild) {
        this.rebuild = rebuild;
    }

    /**
     * @param newLength
     *            sets the length to the output buffer so that the first chars
     *            up to newLength will be kept and the remaining will be
     *            removed.
     * @since 1.0.0
     * @author WFF
     */
    public void setLength(final int newLength) {
        stringBuilder.setLength(newLength);
    }

}
