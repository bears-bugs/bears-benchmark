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
 *
 * <code>preload</code> attribute for the element.
 *
 * <pre>
 *
 * This enumerated attribute is intended to provide a hint to the browser about what the author thinks will lead to the best user experience. It may have one of the following values:
 *
 *  none: indicates that the audio should not be preloaded;
 *  metadata: indicates that only audio metadata (e.g. length) is fetched;
 *  auto: indicates that the whole audio file could be downloaded, even if the user is not expected to use it;
 *  the empty string: synonym of the auto value.
 *
 * If not set, its default value is browser-defined (i.e. each browser may have its own default value). The spec advises it to be set to metadata.
 *
 * </pre>
 *
 * @author WFF
 * @since 1.0.0
 */
public class Preload extends AbstractAttribute implements AudioAttributable {

    public static final String AUTO = "auto";

    public static final String METADATA = "metadata";

    public static final String NONE = "none";

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.PRELOAD);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Preload(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String value) {
        super.setAttributeValue(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     * @param value
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    public void setValue(final boolean updateClient, final String value) {
        super.setAttributeValue(updateClient, value);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
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

}
