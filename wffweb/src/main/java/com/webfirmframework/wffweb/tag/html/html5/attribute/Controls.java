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

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.html5.identifier.AudioAttributable;

/**
 * {@code <element controls> }
 *
 * If this attribute is present, the browser will offer controls to allow the
 * user to control audio playback, including volume, seeking, and pause/resume
 * playback.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Controls extends AbstractAttribute implements AudioAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private Boolean controls;

    {
        super.setAttributeName(AttributeNameConstants.CONTROLS);
        init();
    }

    public Controls() {
        setAttributeValue(null);
    }

    public Controls(final Boolean controls) {
        if (controls == null) {
            setAttributeValue(null);
        } else {
            setAttributeValue(String.valueOf(controls));
        }
        this.controls = controls;
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
     * @return the controls
     * @author WFF
     * @since 1.0.0
     */
    public boolean isControls() {
        return controls == null || controls.booleanValue() ? true : false;
    }

    /**
     * @param controls
     *            the controls to set. {@code null} will remove the value.
     * @author WFF
     * @since 1.0.0
     */
    public void setControls(final Boolean controls) {
        if (controls == null) {
            setAttributeValue(null);
        } else {
            setAttributeValue(String.valueOf(controls));
        }
        this.controls = controls;
    }

}
