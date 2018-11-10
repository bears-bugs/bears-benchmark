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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import com.webfirmframework.wffweb.tag.html.attribute.AttributeNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractValueSetAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;

/**
 *
 * <code>autocomplete</code> attribute for the element.
 *
 * <pre>
 * This attribute indicates whether the value of the control can be automatically completed by the browser.
 * Possible values are:
 * off: The user must explicitly enter a value into this field for every use, or the document provides its own auto-completion method; the browser does not automatically complete the entry.
 * on: The browser is allowed to automatically complete the value based on values that the user has entered during previous uses, however on does not provide any further information about what kind of data the user might be expected to enter.
 * name: Full name
 * honorific-prefix: Prefix or title (e.g. "Mr.", "Ms.", "Dr.", "Mlle")
 * given-name (first name)
 * additional-name
 * family-name
 * honorific-suffix: Suffix (e.g. "Jr.", "B.Sc.", "MBASW", "II")
 * nickname
 * email
 * username
 * new-password: A new password (e.g. when creating an account or changing a password)
 * current-password
 * organization-title: Job title (e.g. "Software Engineer", "Senior Vice President", "Deputy Managing Director")
 * organization
 * street-address
 * address-line1, address-line2, address-line3, address-level4, address-level3, address-level2, address-level1
 * country
 * country-name
 * postal-code
 * cc-name: Full name as given on the payment instrument
 * cc-given-name
 * cc-additional-name
 * cc-family-name
 * cc-number: Code identifying the payment instrument (e.g. the credit card number)
 * cc-exp: Expiration date of the payment instrument
 * cc-exp-month
 * cc-exp-year
 * cc-csc: Security code for the payment instrument
 * cc-type: Type of payment instrument (e.g. Visa)
 * transaction-currency
 * transaction-amount
 * language: Preferred language; Valid BCP 47 language tag
 * bday
 * bday-day
 * bday-month
 * bday-year
 * sex: Gender identity (e.g. Female, Fa'afafine); Free-form text, no newlines
 * tel
 * url: Home page or other Web page corresponding to the company, person, address, or contact information in the other fields associated with this field
 * photo: Photograph, icon, or other image corresponding to the company, person, address, or contact information in the other fields associated with this field
 * </pre>
 *
 * If the autocomplete attribute is not specified on an input element, then the
 * browser uses the autocomplete attribute value of the {@code<input>} element's
 * form owner. The form owner is either the form element that this {@code
 * <input>} element is a descendant of, or the form element whose id is
 * specified by the form attribute of the input element. For more information,
 * see the autocomplete attribute in {@code<form>}.
 *
 * The autocomplete attribute also controls whether Firefox will, unlike other
 * browsers, persist the dynamic disabled state and (if applicable) dynamic
 * checkedness of an {@code<input>} across page loads. The persistence feature
 * is enabled by default. Setting the value of the autocomplete attribute to off
 * disables this feature; this works even when the autocomplete attribute would
 * normally not apply to the {@code<input>} by virtue of its type. See bug
 * 654072.
 *
 * For most modern browsers (including Firefox 38+, Google Chrome 34+, IE 11+)
 * setting the autocomplete attribute will not prevent a browser's password
 * manager from asking the user if they want to store login (username and
 * password) fields and, if they agree, from autofilling the login the next time
 * the user visits the page. See The autocomplete attribute and login fields.
 *
 * @author WFF
 * @since 1.0.0
 */
public class AutoComplete extends AbstractValueSetAttribute
        implements InputAttributable {

    public static final String ON = "on";

    public static final String OFF = "off";

    /**
     * Eg:- Full name
     */
    public static final String NAME = "name";

    /**
     * Prefix or title (e.g. "Mr.", "Ms.", "Dr.", "Mlle").
     */
    public static final String HONORIFIC_PREFIX = "honorific-prefix";

    /**
     * First name
     */
    public static final String GIVEN_NAME = "given-name";

    /**
     * Middle name
     */
    public static final String ADDITIONAL_NAME = "additional-name";

    /**
     * Last name
     */
    public static final String FAMILY_NAME = "family-name";

    /**
     * Suffix (e.g. "Jr.", "B.Sc.", "MBASW", "II").
     */
    public static final String HONORIFIC_SUFFIX = "honorific-suffix";

    public static final String NICKNAME = "nickname";

    public static final String EMAIL = "email";

    public static final String USERNAME = "username";

    /**
     * A new password (e.g. when creating an account or changing a password).
     */
    public static final String NEW_PASSWORD = "new-password";

    public static final String CURRENT_PASSWORD = "current-password";

    /**
     * Job title (e.g. "Software Engineer", "Senior Vice President", "Deputy
     * Managing Director").
     */
    public static final String ORGANIZATION_TITLE = "organization-title";

    public static final String ORGANIZATION = "organization";

    public static final String STREET_ADDRESS = "street-address";

    public static final String ADDRESS_LINE1 = "address-line1";

    public static final String ADDRESS_LINE2 = "address-line2";

    public static final String ADDRESS_LINE3 = "address-line3";

    public static final String ADDRESS_LEVEL4 = "address-level4";

    public static final String ADDRESS_LEVEL3 = "address-level3";

    public static final String ADDRESS_LEVEL2 = "address-level2";

    public static final String ADDRESS_LEVEL1 = "address-level1";

    public static final String COUNTRY = "country";

    public static final String COUNTRY_NAME = "country-name";

    public static final String POSTAL_CODE = "postal-code";

    /**
     * Full name as given on the payment instrument.
     */
    public static final String CC_NAME = "cc-name";

    public static final String CC_GIVEN_NAME = "cc-given-name";

    public static final String CC_ADDITIONAL_NAME = "cc-additional-name";

    public static final String CC_FAMILY_NAME = "cc-family-name";

    /**
     * Code identifying the payment instrument (e.g. the credit card number).
     */
    public static final String CC_NUMBER = "cc-number";

    /**
     * Expiration date of the payment instrument.
     */
    public static final String CC_EXP = "cc-exp";

    public static final String CC_EXP_MONTH = "cc-exp-month";

    public static final String CC_EXP_YEAR = "cc-exp-year";

    /**
     * Security code for the payment instrument.
     */
    public static final String CC_CSC = "cc-csc";

    /**
     * Type of payment instrument (e.g. Visa).
     */
    public static final String CC_TYPE = "cc-type";

    public static final String TRANSACTION_CURRENCY = "transaction-currency";

    public static final String TRANSACTION_AMOUNT = "transaction-amount";

    /**
     * Preferred language; a valid BCP 47 language tag.
     */
    public static final String LANGUAGE = "language";

    /**
     * birthday
     */
    public static final String BDAY = "bday";

    public static final String BDAY_DAY = "bday-day";

    public static final String BDAY_MONTH = "bday-month";

    public static final String BDAY_YEAR = "bday-year";

    /**
     * Gender identity (e.g. Female, Fa'afafine), free-form text, no newlines.
     */
    public static final String SEX = "sex";

    /**
     * full telephone number, including country code
     */
    public static final String TEL = "tel";

    public static final String TEL_COUNTRY_CODE = "tel-country-code";

    public static final String TEL_NATIONAL = "tel-national";

    public static final String TEL_AREA_CODE = "tel-area-code";

    public static final String TEL_LOCAL = "tel-local";

    public static final String TEL_LOCAL_PREFIX = "tel-local-prefix";

    public static final String TEL_LOCAL_SUFFIX = "tel-local-suffix";

    public static final String TEL_EXTENSION = "tel-extension";

    /**
     * Home page or other Web page corresponding to the company, person,
     * address, or contact information in the other fields associated with this
     * field.
     */
    public static final String URL = "url";

    /**
     * Photograph, icon, or other image corresponding to the company, person,
     * address, or contact information in the other fields associated with this
     * field.
     */
    public static final String PHOTO = "photo";

    /**
     * URL representing an instant messaging protocol endpoint (for example,
     * "aim:goim?screenname=example" or "xmpp:fred@example.net")
     */
    public static final String IMPP = "impp";

    /**
     * the field is for contacting someone at their residence
     */
    public static final String HOME = "home";

    /**
     * the field is for contacting someone at their workplace
     */
    public static final String WORK = "work";

    /**
     * the field is for contacting someone regardless of location
     */
    public static final String MOBILE = "mobile";

    /**
     * the field describes a fax machine's contact details
     */
    public static final String FAX = "fax";

    /**
     * the field describes a pager's or beeper's contact details
     */
    public static final String PAGER = "pager";

    private static final long serialVersionUID = 1_0_0L;

    {
        super.setAttributeName(AttributeNameConstants.AUTOCOMPLETE);
        init();
    }

    /**
     *
     * @since 2.1.15
     * @author WFF
     */
    public AutoComplete() {
    }

    /**
     *
     * @param value
     *            the value for the attribute. The value string can contain
     *            values separated by space.
     * @since 1.0.0
     */
    public AutoComplete(final String value) {
        super.addAllToAttributeValueSet(value);
    }

    /**
     *
     * @param values
     *            the value for the attribute. The value string can contain
     *            values separated by space.
     * @since 2.1.15
     */
    public AutoComplete(final String... values) {
        super.addAllToAttributeValueSet(values);
    }

    /**
     * removes the value
     *
     * @param value
     * @since 2.1.15
     * @author WFF
     */
    public void removeValue(final String value) {
        super.removeFromAttributeValueSet(value);
    }

    /**
     * removes the values
     *
     * @param values
     * @since 2.1.15
     * @author WFF
     */
    public void removeValues(final Collection<String> values) {
        super.removeAllFromAttributeValueSet(values);
    }

    /**
     * adds the values to the last
     *
     * @param values
     * @since 2.1.15
     * @author WFF
     */
    public void addValues(final Collection<String> values) {
        super.addAllToAttributeValueSet(values);
    }

    /**
     * adds the value to the last
     *
     * @param value
     * @since 2.1.15
     * @author WFF
     */
    public void addValue(final String value) {
        super.addToAttributeValueSet(value);
    }

    /**
     * sets the value for this attribute
     *
     * @param value
     *            the value for the attribute.
     * @since 1.0.0
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
     * @param attributeValue
     *            the value for the attribute.
     * @since 2.1.15
     * @author WFF
     */
    public void setValue(final boolean updateClient,
            final String attributeValue) {
        super.setAttributeValue(updateClient, attributeValue);
    }

    /**
     * gets the value of this attribute
     *
     * @return the value of the attribute
     * @since 1.0.0
     */
    public String getValue() {
        return super.getAttributeValue();
    }

    /**
     * @return a new copy of set of values
     * @since 2.1.15
     * @author WFF
     */
    public Set<String> getValueSet() {
        return new LinkedHashSet<>(super.getAttributeValueSet());
    }

    /**
     * invokes only once per object
     *
     * @since 1.0.0
     */
    protected void init() {
        // to override and use this method
    }

}
