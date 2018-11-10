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
import com.webfirmframework.wffweb.tag.html.identifier.AAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;

/**
 * This attribute, if present, indicates that the author intends the hyperlink
 * to be used for downloading a resource so that when the user clicks on the
 * link they will be prompted to save it as a local file. If the attribute has a
 * value, the value will be used as the pre-filled file name in the Save prompt
 * that opens when the user clicks on the link (the user can change the name
 * before actually saving the file of course). There are no restrictions on
 * allowed values (though / and \ will be converted to underscores, preventing
 * specific path hints), but you should consider that most file systems have
 * limitations with regard to what punctuation is supported in file names, and
 * browsers are likely to adjust file names accordingly.
 *
 * @author WFF
 * @since 1.0.0
 */
public class Download extends AbstractAttribute
        implements AAttributable, AreaAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.DOWNLOAD);
        init();
    }

    /**
     *
     * This attribute, if present, indicates that the author intends the
     * hyperlink to be used for downloading a resource so that when the user
     * clicks on the link they will be prompted to save it as a local file. If
     * the attribute has a value, the value will be used as the pre-filled file
     * name in the Save prompt that opens when the user clicks on the link (the
     * user can change the name before actually saving the file of course).
     * There are no restrictions on allowed values (though / and \ will be
     * converted to underscores, preventing specific path hints), but you should
     * consider that most file systems have limitations with regard to what
     * punctuation is supported in file names, and browsers are likely to adjust
     * file names accordingly.
     *
     * It creates attribute without value therefore the original filename (eg:
     * from {@code <a>} tag) will be used for the downloading file.
     *
     * @since 1.0.0
     * @author WFF
     */
    public Download() {
        setAttributeValue(null);
    }

    /**
     * This attribute, if present, indicates that the author intends the
     * hyperlink to be used for downloading a resource so that when the user
     * clicks on the link they will be prompted to save it as a local file. If
     * the attribute has a value, the value will be used as the pre-filled file
     * name in the Save prompt that opens when the user clicks on the link (the
     * user can change the name before actually saving the file of course).
     * There are no restrictions on allowed values (though / and \ will be
     * converted to underscores, preventing specific path hints), but you should
     * consider that most file systems have limitations with regard to what
     * punctuation is supported in file names, and browsers are likely to adjust
     * file names accordingly.
     *
     * @param filename
     *            the filename for the downloading file.
     * @since 1.0.0
     * @author WFF
     */
    public Download(final String filename) {
        setAttributeValue(filename);
    }

    /**
     * sets the value for this attribute
     *
     * @param filename
     *            the filename for the downloading file.
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String filename) {
        super.setAttributeValue(filename);
    }

    /**
     * gets the value of this attribute
     *
     * @return the filename for the downloading file.
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
