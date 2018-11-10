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
import com.webfirmframework.wffweb.tag.html.identifier.AAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.AreaAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.ScriptAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.StyleAttributable;

/**
 *
 * <code>type</code> attribute for the element.<br>
 *
 * <i>type</i> attribute for <i>a</i> element :- <br>
 *
 * This attribute specifies the media type in the form of a MIME type for the
 * link target. Generally, this is provided strictly as advisory information;
 * however, in the future a browser might add a small icon for multimedia types.
 * For example, a browser might add a small speaker icon when type is set to
 * audio/wav. For a complete list of recognized MIME types, see
 * http://www.w3.org/TR/html4/references.html#ref-MIMETYPES. Use this attribute
 * only if the href attribute is present. <br>
 * <br>
 *
 *
 * <i>type</i> attribute for <i>area</i> element :- <br>
 * This attribute specifies the media type in the form of a MIME type for the
 * link target. Generally, this is provided strictly as advisory information;
 * however, in the future a browser might add a small icon for multimedia types.
 * For example, a browser might add a small speaker icon when type is set to
 * audio/wav. For a complete list of recognized MIME types, see
 * http://www.w3.org/TR/html4/references.html#ref-MIMETYPES. Use this attribute
 * only if the href attribute is present. <br>
 * <br>
 *
 * <i>type</i> attribute for <i>area</i> element :- <br>
 *
 * <pre>
 * The type of control to display. The default type is text, if this attribute is not specified. Possible values are:
 * button: A push button with no default behavior.
 * checkbox: A check box. You must use the value attribute to define the value submitted by this item. Use the checked attribute to indicate whether this item is selected. You can also use the indeterminate attribute to indicate that the checkbox is in an indeterminate state (on most platforms, this draws a horizontal line across the checkbox).
 * color: HTML5 A control for specifying a color. A color picker's UI has no required features other than accepting simple colors as text (more info).
 * date: HTML5 A control for entering a date (year, month, and day, with no time).
 * datetime: HTML5   A control for entering a date and time (hour, minute, second, and fraction of a second) based on UTC time zone. This feature has been removed from WHATWG HTML.
 * datetime-local: HTML5 A control for entering a date and time, with no time zone.
 * email: HTML5 A field for editing an e-mail address. The input value is validated to contain either the empty string or a single valid e-mail address before submitting. The :valid and :invalid CSS pseudo-classes are applied as appropriate.
 * file: A control that lets the user select a file. Use the accept attribute to define the types of files that the control can select.
 * hidden: A control that is not displayed, but whose value is submitted to the server.
 * image: A graphical submit button. You must use the src attribute to define the source of the image and the alt attribute to define alternative text. You can use the height and width attributes to define the size of the image in pixels.
 * month: HTML5 A control for entering a month and year, with no time zone.
 * number: HTML5 A control for entering a floating point number.
 * password: A single-line text field whose value is obscured. Use the maxlength attribute to specify the maximum length of the value that can be entered.
 * radio: A radio button. You must use the value attribute to define the value submitted by this item. Use the checked attribute to indicate whether this item is selected by default. Radio buttons that have the same value for the name attribute are in the same "radio button group"; only one radio button in a group can be selected at a time.
 * range: HTML5 A control for entering a number whose exact value is not important. This type control uses the following default values if the corresponding attributes are not specified:
 * min: 0
 * max: 100
 * value: min + (max-min)/2, or min if max is less than min
 * step: 1
 * reset: A button that resets the contents of the form to default values.
 * search: HTML5 A single-line text field for entering search strings; line-breaks are automatically removed from the input value.
 * submit: A button that submits the form.
 * tel: HTML5 A control for entering a telephone number; line-breaks are automatically removed from the input value, but no other syntax is enforced. You can use attributes such as pattern and maxlength to restrict values entered in the control. The :valid and :invalid CSS pseudo-classes are applied as appropriate.
 * text: A single-line text field; line-breaks are automatically removed from the input value.
 * time: HTML5 A control for entering a time value with no time zone.
 * url: HTML5 A field for editing a URL. The input value is validated to contain either the empty string or a valid absolute URL before submitting. Line-breaks and leading or trailing whitespace are automatically removed from the input value. You can use attributes such as pattern and maxlength to restrict values entered in the control. The :valid and :invalid CSS pseudo-classes are applied as appropriate.
 * week: HTML5 A control for entering a date consisting of a week-year number and a week number with no time zone.
 *
 *
 * </pre>
 *
 * @author WFF
 *
 */
public class Type extends AbstractAttribute
        implements AAttributable, AreaAttributable, InputAttributable,
        ScriptAttributable, StyleAttributable {

    private static final long serialVersionUID = 1_0_0L;

    public static final String BUTTON = "button";

    public static final String CHECKBOX = "checkbox";

    public static final String COLOR = "color";

    public static final String DATE = "date";

    public static final String DATETIME = "datetime";

    public static final String DATETIME_LOCAL = "datetime-local";

    public static final String EMAIL = "email";

    public static final String FILE = "file";

    public static final String HIDDEN = "hidden";

    public static final String IMAGE = "image";

    public static final String MONTH = "month";

    public static final String NUMBER = "number";

    public static final String PASSWORD = "password";

    public static final String RADIO = "radio";

    public static final String RANGE = "range";

    public static final String RESET = "reset";

    public static final String SEARCH = "search";

    public static final String SUBMIT = "submit";

    public static final String TEL = "tel";

    public static final String TEXT = "text";

    public static final String TIME = "time";

    public static final String URL = "url";

    public static final String WEEK = "week";

    public static final String TEXT_JAVASCRIPT = "text/javascript";

    public static final String TEXT_ECMASCRIPT = "text/ecmascript";

    public static final String APPLICATION_ECMASCRIPT = "application/ecmascript";

    public static final String APPLICATION_JAVASCRIPT = "application/javascript";

    public static final String TEXT_CSS = "text/css";

    {
        super.setAttributeName(AttributeNameConstants.TYPE);
        init();
    }

    /**
     *
     * @param value
     *            the value for the attribute
     * @since 1.0.0
     * @author WFF
     */
    public Type(final String value) {
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
