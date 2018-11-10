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
package com.webfirmframework.wffweb.tag.html.html5.attribute;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.html5.identifier.AudioAttributable;

/**
 * {@code <element loop> }
 *
 * A Boolean attribute; if specified, will automatically seek back to the start
 * upon reaching the end of the audio.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Loop extends AbstractAttribute implements AudioAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private Boolean loop;

    {
        super.setAttributeName(AttributeNameConstants.LOOP);
        init();
    }

    public Loop() {
        setAttributeValue(null);
    }

    /**
     * @param value
     *            the value may be <code>loop</code>
     * @since 1.1.4
     * @author WFF
     */
    public Loop(final String value) {
        if ("loop".equals(value)) {
            loop = true;
            super.setAttributeValue("loop");
        } else if ("true".equals(value)) {
            loop = true;
            super.setAttributeValue("true");
        } else if ("false".equals(value)) {
            loop = false;
            super.setAttributeValue("false");
        } else {
            throw new InvalidValueException("the value must be loop");
        }
    }

    public Loop(final Boolean loop) {
        if (loop == null) {
            super.setAttributeValue(null);
        } else {
            super.setAttributeValue(
                    loop.booleanValue() ? "loop" : String.valueOf(loop));
        }
        this.loop = loop;
    }

    /**
     * invokes only once per object
     *
     * @author WFF
     * @since 1.0.0
     */
    protected void init() {
        // to override and use this method
    }

    /**
     * @return the loop
     * @author WFF
     * @since 1.0.0
     */
    public boolean isLoop() {
        return loop == null || loop.booleanValue() ? true : false;
    }

    /**
     * @param loop
     *            the loop to set. {@code null} will remove the value.
     * @author WFF
     * @since 1.0.0
     */
    public void setLoop(final Boolean loop) {
        if (loop == null) {
            super.setAttributeValue(null);
        } else {
            super.setAttributeValue(
                    loop.booleanValue() ? "loop" : String.valueOf(loop));
        }
        this.loop = loop;
    }

}
