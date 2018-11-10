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
package com.webfirmframework.wffweb.tag.html.attribute.global;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * @author WFF
 *
 */
public class AccessKey extends AbstractAttribute implements GlobalAttributable {

    /**
     *
     */
    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.ACCESSKEY);
        init();
    }

    public AccessKey() {
        super.setAttributeValue("");
    }

    public AccessKey(final String value) {
        super.setAttributeValue(value);
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
     * @return the accessKey
     * @since 1.0.0
     * @author WFF
     */
    public String getAccessKey() {
        return super.getAttributeValue();
    }

    /**
     * @param accessKey
     *            the accessKey to set
     * @since 1.0.0
     * @author WFF
     */
    public void setAccessKey(final String accessKey) {
        super.setAttributeValue(accessKey);
    }

}
