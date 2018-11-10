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
package com.webfirmframework.wffweb.tag.html.attribute;

import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;

/**
 *
 * <code>coords</code> attribute for the element.
 *
 * A set of values specifying the coordinates of the hot-spot region. The number
 * and meaning of the values depend upon the value specified for the shape
 * attribute. For a rect or rectangle shape, the coords value is two x,y pairs:
 * left, top, right, and bottom. For a circle shape, the value is x,y,r where
 * x,y is a pair specifying the center of the circle and r is a value for the
 * radius. For a poly or polygon&lt; shape, the value is a set of x,y pairs for
 * each point in the polygon: x1,y1,x2,y2,x3,y3, and so on. In HTML4, the values
 * are numbers of pixels or percentages, if a percent sign (%) is appended; in
 * HTML5, the values are numbers of CSS pixels.
 *
 * @author WFF
 * @since 1.0.0
 */
public class CoOrds extends AbstractAttribute implements AreaAttributable {

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.COORDS);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public CoOrds(final String value) {
        setAttributeValue(value);
    }

    /**
     * sets the value for this attribute.
     *
     * Specifies an alternate text for the area. Required if the href attribute
     * is present
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
