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

import java.util.UUID;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * @author WFF
 *
 */
public class Id extends AbstractAttribute implements GlobalAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private UUID uuid;

    {
        super.setAttributeName(AttributeNameConstants.ID);
        init();
    }

    /**
     * sets with empty value as id=""
     *
     * @since 1.0.0
     * @author WFF
     */
    public Id() {
        super.setAttributeValue("");
    }

    /**
     * value for the id attribute.
     *
     * @param value
     * @since 1.0.0
     * @author WFF
     */
    public Id(final String value) {
        super.setAttributeValue(value);
    }

    /**
     * value for the id attribute.
     *
     * @param uuid
     * @since 1.0.0
     * @author WFF
     */
    public Id(final UUID uuid) {
        this.uuid = uuid;
        super.setAttributeValue(uuid.toString());
    }

    /**
     * value for the id attribute.
     *
     * @param value
     * @since 1.0.0
     * @author WFF
     */
    public Id(final int value) {
        super.setAttributeValue(String.valueOf(value));
    }

    /**
     * value for the id attribute.
     *
     * @param value
     * @since 1.0.0
     * @author WFF
     */
    public Id(final float value) {
        super.setAttributeValue(String.valueOf(value));
    }

    /**
     * value for the id attribute.
     *
     * @param value
     * @since 1.0.0
     * @author WFF
     */
    public Id(final long value) {
        super.setAttributeValue(String.valueOf(value));
    }

    /**
     * value for the id attribute.
     *
     * @param value
     * @since 1.0.0
     * @author WFF
     */
    public Id(final double value) {
        super.setAttributeValue(String.valueOf(value));
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
     * value for the id attribute.
     *
     * @param id
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final String id) {
        if (id != null) {
            super.setAttributeValue(id);
            uuid = null;
        }
    }

    /**
     * value for the id attribute.
     *
     * @param id
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final int id) {
        super.setAttributeValue(String.valueOf(id));
        uuid = null;
    }

    /**
     * value for the id attribute.
     *
     * @param id
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final long id) {
        super.setAttributeValue(String.valueOf(id));
        uuid = null;
    }

    /**
     * value for the id attribute.
     *
     * @param id
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final float id) {
        super.setAttributeValue(String.valueOf(id));
        uuid = null;
    }

    /**
     * value for the id attribute.
     *
     * @param id
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final double id) {
        super.setAttributeValue(String.valueOf(id));
        uuid = null;
    }

    /**
     * value for the id attribute.
     *
     * @param uuid
     * @since 1.0.0
     * @author WFF
     */
    public void setValue(final UUID uuid) {
        if (uuid != null) {
            super.setAttributeValue(uuid.toString());
            this.uuid = uuid;
        }
    }

    /**
     * @return the value for the attribute set
     * @since 1.0.0
     * @author WFF
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * @return the {@code UUID} set by {@code Id#setValue(UUID)},
     *         {@code Id#setUuid(UUID)} or {@code Id#Id(UUID)}.
     * @since 1.0.0
     * @author WFF
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * To set {@code UUID} as id value.
     *
     * @param uuid
     *            the {@code UUID} object
     * @since 1.0.0
     * @author WFF
     */
    public void setUuid(final UUID uuid) {
        super.setAttributeValue(uuid.toString());
        this.uuid = uuid;
    }
}
