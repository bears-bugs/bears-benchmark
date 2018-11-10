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
 */
package com.webfirmframework.wffweb.tag.html.formsandinputs;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.Type;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.FormAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class Form extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Form.class.getName());

    {
        init();
    }

    /**
     *
     * @param base
     *            i.e. parent tag of this tag
     * @param attributes
     *            An array of {@code AbstractAttribute}
     *
     * @since 1.0.0
     */
    public Form(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(TagNameConstants.FORM, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof FormAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of FormAttribute");
            }
        }
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
     * prepares and gets the js object for the given tag names under this form
     * tag. This js object may be used to return in onsubmit attribute.
     *
     * @param onlyForTagNames
     *
     *            TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     *            TagNameConstants.SELECT. Pass object of {@code List},
     *            {@code HashSet} based on the number of elements in it.
     *
     * @return the js object string for the given tag names. The returned js
     *         string will be as {name1.name1.value} where name1 is the value of
     *         name attribute of the field.
     * @since 2.1.8
     * @since 2.1.13 changed {@code Collection<Set> onlyForTagNames} to
     *        {@code Collection<String> onlyForTagNames}.
     * @author WFF
     */
    public String getNameBasedJsObject(
            final Collection<String> onlyForTagNames) {

        // "{" should not be changed to '{'
        final StringBuilder jsObjectBuilder = new StringBuilder("{");
        final Set<String> appendedValues = new HashSet<>();

        loopThroughAllNestedChildren(child -> {

            final String tagName = child.getTagName();

            if (onlyForTagNames.contains(tagName)) {

                final AbstractAttribute nameAttr = child
                        .getAttributeByName(AttributeNameConstants.NAME);
                final AbstractAttribute typeAttr = child
                        .getAttributeByName(AttributeNameConstants.TYPE);

                if (nameAttr != null) {
                    final String nameAttrValue = nameAttr.getAttributeValue();

                    if (!appendedValues.contains(nameAttrValue)) {

                        if (typeAttr != null) {
                            final String typeAttrValue = typeAttr
                                    .getAttributeValue();
                            if (Type.CHECKBOX.equals(typeAttrValue)
                                    || Type.RADIO.equals(typeAttrValue)) {
                                jsObjectBuilder.append(nameAttrValue)
                                        .append(':').append(nameAttrValue)
                                        .append(".checked,");
                                appendedValues.add(nameAttrValue);

                            } else {
                                jsObjectBuilder.append(nameAttrValue)
                                        .append(':').append(nameAttrValue)
                                        .append(".value,");
                                appendedValues.add(nameAttrValue);

                            }
                        } else {
                            jsObjectBuilder.append(nameAttrValue).append(':')
                                    .append(nameAttrValue).append(".value,");
                            appendedValues.add(nameAttrValue);

                        }

                    }
                }

            }
            return true;
        }, false, this);

        return jsObjectBuilder.replace(jsObjectBuilder.length() - 1,
                jsObjectBuilder.length(), "}").toString();
    }

    /**
     * prepares and gets the js object for the given tag names under this form
     * tag. This js object may be used to return in onsubmit attribute.
     *
     *
     * @param onlyForTagNames
     *
     *            TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     *            TagNameConstants.SELECT Pass object of {@code List},
     *            {@code HashSet} based on the number of elements in it.
     * @return the js object string for the given tag names. The returned js
     *         string will be as {name1.name1.value} where name1 is the value of
     *         name attribute of the field.
     * @since 2.1.13
     * @author WFF
     */
    public String getIdBasedJsObject(final Collection<String> onlyForTagNames) {

        // "{" should not be changed to '{' if passing arg in constructor of
        // StringBuilder
        final StringBuilder jsObjectBuilder = new StringBuilder("{");
        final Set<String> appendedValues = new HashSet<>();

        loopThroughAllNestedChildren(child -> {

            final String tagName = child.getTagName();

            if (onlyForTagNames.contains(tagName)) {

                final AbstractAttribute idAttr = child
                        .getAttributeByName(AttributeNameConstants.ID);

                final AbstractAttribute typeAttr = child
                        .getAttributeByName(AttributeNameConstants.TYPE);

                if (idAttr != null) {
                    final String idAttrValue = idAttr.getAttributeValue();

                    if (!appendedValues.contains(idAttrValue)) {

                        final String docById = "document.getElementById('"
                                + idAttrValue + "')";

                        if (typeAttr != null) {

                            final String typeAttrValue = typeAttr
                                    .getAttributeValue();

                            if (Type.CHECKBOX.equals(typeAttrValue)
                                    || Type.RADIO.equals(typeAttrValue)) {

                                jsObjectBuilder.append(idAttrValue).append(':')
                                        .append(docById).append(".checked,");
                                appendedValues.add(idAttrValue);

                            } else {
                                jsObjectBuilder.append(idAttrValue).append(':')
                                        .append(docById).append(".value,");
                                appendedValues.add(idAttrValue);

                            }
                        } else {
                            jsObjectBuilder.append(idAttrValue).append(':')
                                    .append(docById).append(".value,");
                            appendedValues.add(idAttrValue);

                        }

                    }
                }

            }
            return true;
        }, false, this);

        return jsObjectBuilder.replace(jsObjectBuilder.length() - 1,
                jsObjectBuilder.length(), "}").toString();
    }

    /**
     *
     * prepares and gets the js object for the input tag names
     * (TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     * TagNameConstants.SELECT) under this form tag. <br>
     * NB:- If there are any missing input tag types, please inform
     * webfirmframework to update this method. <br>
     * This js object may be used to return in onsubmit attribute.
     *
     * @return the js object string for field names of TagNameConstants.INPUT,
     *         TagNameConstants.TEXTAREA and TagNameConstants.SELECT. The
     *         returned js string will be as {name1.name1.value} where name1 is
     *         the value of name attribute of the field. If the input type is
     *         checkbox/radio then checked property will be included instead of
     *         value property.
     * @since 2.1.8
     * @author WFF
     */
    public String getNameBasedJsObject() {

        final Set<String> tagNames = new HashSet<>();
        tagNames.add(TagNameConstants.INPUT);
        tagNames.add(TagNameConstants.TEXTAREA);
        tagNames.add(TagNameConstants.SELECT);

        return getNameBasedJsObject(tagNames);
    }

    /**
     *
     * prepares and gets the js object for the input tag names
     * (TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     * TagNameConstants.SELECT) under this form tag. <br>
     * NB:- If there are any missing input tag types, please inform
     * webfirmframework to update this method. <br>
     * This js object may be used to return in onsubmit attribute.
     *
     * @return the js object string for field names of TagNameConstants.INPUT,
     *         TagNameConstants.TEXTAREA and TagNameConstants.SELECT. The
     *         returned js string will be as {name1.name1.value} where name1 is
     *         the value of name attribute of the field. If the input type is
     *         checkbox/radio then checked property will be included instead of
     *         value property.
     * @since 2.1.13
     * @author WFF
     */
    public String getIdBasedJsObject() {
        final Set<String> tagNames = new HashSet<>();
        tagNames.add(TagNameConstants.INPUT);
        tagNames.add(TagNameConstants.TEXTAREA);
        tagNames.add(TagNameConstants.SELECT);
        return getIdBasedJsObject(tagNames);
    }

    /**
     *
     * prepares and gets the js object for the input tag names
     * (TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     * TagNameConstants.SELECT) under this form tag. <br>
     * NB:- If there are any missing input tag types, please inform
     * webfirmframework to update this method. <br>
     * This js object may be used to return in onsubmit attribute.
     *
     *
     * @param additionalTagNames
     *
     *            TagNameConstants.INPUT, TagNameConstants.TEXTAREA and
     *            TagNameConstants.SELECT Pass object of {@code List},
     *            {@code HashSet} based on the number of elements in it.
     *
     * @return the js object string for field names of TagNameConstants.INPUT,
     *         TagNameConstants.TEXTAREA and TagNameConstants.SELECT. The
     *         returned js string will be as {name1.name1.value} where name1 is
     *         the value of name attribute of the field. If the input type is
     *         checkbox/radio then checked property will be included instead of
     *         value property.
     * @since 2.1.13
     * @author WFF
     */
    public String getIdBasedJsObjectPlus(
            final Collection<String> additionalTagNames) {

        final Set<String> tagNames = new HashSet<>();
        tagNames.addAll(additionalTagNames);
        tagNames.add(TagNameConstants.INPUT);
        tagNames.add(TagNameConstants.TEXTAREA);
        tagNames.add(TagNameConstants.SELECT);

        return getIdBasedJsObject(tagNames);
    }

}
