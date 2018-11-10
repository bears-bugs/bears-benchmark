package com.webfirmframework.wffweb.tag.html.metainfo;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.BaseAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class Base extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Base.class.getName());

    private static TagType tagType = TagType.SELF_CLOSING;

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
    public Base(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(tagType, TagNameConstants.BASE, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof BaseAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of BaseAttribute");
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
     * @param selfClosing
     *            <code>true</code> to set as self closing tag and
     *            <code>false</code> for not to set as self closing tag. The
     *            default value is <code>true</code>.
     * @since 1.0.0
     * @author WFF
     */
    public static void setSelfClosing(final boolean selfClosing) {
        Base.tagType = selfClosing ? TagType.SELF_CLOSING : TagType.NON_CLOSING;
    }

    /**
     * @param nonClosing
     *            <code>true</code> to set as self closing tag and
     *            <code>false</code> for not to set as self closing tag. The
     *            default value is <code>true</code>.
     * @since 1.0.0
     * @author WFF
     */
    public static void setNonClosing(final boolean nonClosing) {
        Base.tagType = nonClosing ? TagType.NON_CLOSING : TagType.SELF_CLOSING;
    }

    /**
     * @return true if it is set as self closing tag otherwise false
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isSelfClosing() {
        return Base.tagType == TagType.SELF_CLOSING;
    }

    /**
     * @return true if it is set as non closing tag otherwise false
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isNonClosing() {
        return Base.tagType == TagType.NON_CLOSING;
    }

}
