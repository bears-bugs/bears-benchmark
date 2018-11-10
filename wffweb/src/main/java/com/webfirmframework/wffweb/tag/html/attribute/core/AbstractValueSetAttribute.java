/*
 * Copyright 2014-2017 Web Firm Framework
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
package com.webfirmframework.wffweb.tag.html.attribute.core;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;

import com.webfirmframework.wffweb.util.StringUtil;

/**
 * @author WFF
 * @since 2.1.15
 */
public abstract class AbstractValueSetAttribute extends AbstractAttribute {

    private static final long serialVersionUID = 1L;

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
    @Override
    protected void setAttributeValue(final boolean updateClient,
            final String value) {
        if (value != null) {
            final Collection<String> allValues = extractValues(value);
            super.replaceAllInAttributeValueSet(updateClient, allValues);
        }
    }

    /*
     * splits by space
     */
    private static Collection<String> extractValues(final String value) {
        final String[] inputValues = StringUtil.splitBySpace(value);
        final Collection<String> allValues = new ArrayDeque<>();
        for (final String each : inputValues) {
            // As per Collections.addAll's doc it is faster than
            // allValues.addAll(Arrays.asList(values));
            Collections.addAll(allValues, StringUtil.strip(each));
        }
        return allValues;
    }

    /**
     * sets the value for this attribute
     *
     *
     * @param value
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    @Override
    protected void setAttributeValue(final String value) {
        if (value != null) {
            replaceAllInAttributeValueSet(value);
        }
    }

    /**
     * Checks if the value is a part of this attribute value.
     *
     * @param value
     *            the value to be checked
     * @return true if the given value is contained in the value string of this
     *         attribute.
     * @since 2.1.15
     * @author WFF
     */
    public boolean contains(final String value) {
        return super.getAttributeValueSet().contains(value);
    }

    /**
     * Checks if the values are part of this attribute value.
     *
     * @param values
     *            the values to be checked
     * @return true if the given values are contained in the value string of
     *         this attribute.
     * @since 2.1.15
     * @author WFF
     */
    public boolean containsAll(final Collection<String> values) {
        return super.getAttributeValueSet().containsAll(values);
    }

    @Override
    public String getAttributeValue() {
        return StringUtil.join(' ', getAttributeValueSet());
    }

    /**
     * @param attrValues
     * @since 2.1.15
     * @author WFF
     */
    protected void addAllToAttributeValueSet(final String... attrValues) {
        if (attrValues != null) {

            final Collection<String> allValues = new ArrayDeque<>();
            for (final String attrValue : attrValues) {
                String trimmmedValue = null;
                if (attrValue != null
                        && !(trimmmedValue = StringUtil.strip(attrValue))
                                .isEmpty()) {
                    // As per Collections.addAll's doc it is faster than
                    // allValues.addAll(Arrays.asList(values));
                    Collections.addAll(allValues,
                            StringUtil.splitBySpace(trimmmedValue));
                }
            }

            super.addAllToAttributeValueSet(allValues);
        }
    }

    /**
     * Removes all values from the attributeValueSet and adds the given
     * attribute values.
     *
     * @param attrValues
     * @since 3.0.1
     */
    protected void replaceAllInAttributeValueSet(final String... attrValues) {
        replaceAllInAttributeValueSet(true, attrValues);
    }

    /**
     * Removes all values from the attributeValueSet and adds the given
     * attribute values.
     *
     * @param updateClient
     * @param attrValues
     * @since 3.0.1
     */
    protected void replaceAllInAttributeValueSet(final boolean updateClient,
            final String... attrValues) {
        if (attrValues != null) {

            final Collection<String> allValues = new ArrayDeque<>();
            for (final String attrValue : attrValues) {
                if (attrValue != null) {
                    allValues.addAll(extractValues(attrValue));
                }
            }

            super.replaceAllInAttributeValueSet(updateClient, allValues);
        }
    }

}
