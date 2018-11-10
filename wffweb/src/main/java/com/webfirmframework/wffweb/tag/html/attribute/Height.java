package com.webfirmframework.wffweb.tag.html.attribute;

import com.webfirmframework.wffweb.InvalidValueException;
import com.webfirmframework.wffweb.css.CssLengthUnit;
import com.webfirmframework.wffweb.css.core.LengthUnit;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.html5.identifier.RectAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.InputAttributable;
import com.webfirmframework.wffweb.util.CssLengthUtil;

/**
 * This is a width attribute.
 *
 * @author WFF
 * @since 1.0.0
 *
 */
public class Height extends AbstractAttribute
        implements InputAttributable, RectAttributable {

    private static final long serialVersionUID = 1_0_0L;

    private String htmlString;
    private float value;
    private LengthUnit cssLengthUnit;

    {
        super.setAttributeName(AttributeNameConstants.HEIGHT);
        init();
    }

    /**
     * the value will be set as <code>100%</code>
     *
     * @since 1.0.0
     */
    public Height() {
        value = 100;
        cssLengthUnit = CssLengthUnit.PER;
        htmlString = String.valueOf((int) value) + CssLengthUnit.PER.getUnit();
        setAttributeValue(htmlString);
    }

    /**
     * eg:- <code>100%</code>
     *
     * @since 1.1.4
     */
    public Height(final String value) {
        assignValues(value);
    }

    /**
     * @param value
     * @param cssLengthUnit
     * @since 1.1.4
     */
    public Height(final float value, final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        htmlString = String.valueOf(value) + cssLengthUnit.getUnit();
        setAttributeValue(htmlString);
    }

    /**
     * @param percent
     *            the percentage value to set.
     * @since 1.1.4
     */
    public Height(final int percent) {
        value = percent;
        cssLengthUnit = CssLengthUnit.PER;
        htmlString = String.valueOf(percent) + CssLengthUnit.PER.getUnit();
        setAttributeValue(htmlString);
    }

    /**
     * @param percent
     *            the percentage value to set.
     * @since 1.0.0
     */
    public Height(final float percent) {
        value = percent;
        cssLengthUnit = CssLengthUnit.PER;
        htmlString = String.valueOf(percent) + CssLengthUnit.PER.getUnit();
        setAttributeValue(htmlString);
    }

    /**
     * @param value
     *            the value to set
     * @param cssLengthUnit
     *            the unit for the value.
     * @since 1.0.0
     */
    public Height(final int value, final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        htmlString = String.valueOf(value) + cssLengthUnit.getUnit();
        setAttributeValue(htmlString);
    }

    /**
     * @param value
     *            the value to set
     * @param lengthUnit
     *            The length unit. It may be an enum class which implements
     *            {@code LengthUnit} interface. the unit for the value.
     * @since 1.0.0
     */
    public Height(final int value, final LengthUnit lengthUnit) {
        this.value = value;
        cssLengthUnit = lengthUnit;
        htmlString = String.valueOf(value) + lengthUnit.getUnit();
        setAttributeValue(htmlString);
    }

    private void init() {
        // to override and use this method
    }

    /**
     * @param percent
     *            the percent to set
     * @since 1.0.0
     */
    public void setPercent(final float percent) {
        value = percent;
        cssLengthUnit = CssLengthUnit.PER;
        setAttributeValue(String.valueOf(percent) + cssLengthUnit.getUnit());
    }

    /**
     * @param value
     *            the value to set
     * @param cssLengthUnit
     *            the unit for the value. return the current object will be
     *            returned.
     * @since 1.0.0
     */
    public Height setValue(final int value, final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        htmlString = String.valueOf(value) + cssLengthUnit.getUnit();
        setAttributeValue(htmlString);
        return this;
    }

    /**
     * @param value
     *            the value to set
     * @param lengthUnit
     *            the unit for the value. It may be an enum class which
     *            implements {@code LengthUnit} interface. return the current
     *            object will be returned.
     * @since 1.0.0
     */
    public Height setValue(final int value, final LengthUnit lengthUnit) {
        this.value = value;
        cssLengthUnit = lengthUnit;
        htmlString = String.valueOf(value) + lengthUnit.getUnit();
        setAttributeValue(htmlString);
        return this;
    }

    /**
     * @param value
     *            the value to set
     * @param cssLengthUnit
     *            the unit for the value. return the current object will be
     *            returned.
     * @since 1.0.0
     */
    public Height setValue(final float value,
            final CssLengthUnit cssLengthUnit) {
        this.value = value;
        this.cssLengthUnit = cssLengthUnit;
        htmlString = String.valueOf(value) + cssLengthUnit.getUnit();
        setAttributeValue(htmlString);
        return this;
    }

    /**
     * @param value
     *            the value to set
     * @param lengthUnit
     *            the unit for the value. return the current object will be
     *            returned.
     * @since 1.0.0
     */
    public Height setValue(final float value, final LengthUnit lengthUnit) {
        this.value = value;
        cssLengthUnit = lengthUnit;
        htmlString = String.valueOf(value) + lengthUnit.getUnit();
        setAttributeValue(htmlString);
        return this;
    }

    /**
     * @return the value in float.
     * @since 1.0.0
     */
    public float getValue() {
        return value;
    }

    /**
     * @return the unit for the value.
     * @since 1.0.0
     */
    public LengthUnit getUnit() {
        return cssLengthUnit;
    }

    private void assignValues(final String attributeValue) {

        final Object[] lengthValueAsPremitiveAndUnit = CssLengthUtil
                .getLengthValueAsPremitiveAndUnit(attributeValue);

        if (lengthValueAsPremitiveAndUnit.length == 2) {
            value = (float) lengthValueAsPremitiveAndUnit[0];
            cssLengthUnit = (CssLengthUnit) lengthValueAsPremitiveAndUnit[1];

        } else if (lengthValueAsPremitiveAndUnit.length == 1) {
            value = (float) lengthValueAsPremitiveAndUnit[0];
            cssLengthUnit = CssLengthUnit.PER;
        } else {
            throw new InvalidValueException(
                    "the value should be in the format for eg: 100%");
        }

        htmlString = attributeValue;

        setAttributeValue(htmlString);
    }

}
