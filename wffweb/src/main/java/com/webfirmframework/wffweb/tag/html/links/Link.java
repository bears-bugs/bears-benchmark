package com.webfirmframework.wffweb.tag.html.links;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.LinkAttributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class Link extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Link.class.getName());

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
    public Link(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(tagType, TagNameConstants.LINK, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof LinkAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of LinkAttribute");
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
        Link.tagType = selfClosing ? TagType.SELF_CLOSING : TagType.NON_CLOSING;
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
        Link.tagType = nonClosing ? TagType.NON_CLOSING : TagType.SELF_CLOSING;
    }

    /**
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isSelfClosing() {
        return Link.tagType == TagType.SELF_CLOSING;
    }

    /**
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public static boolean isNonClosing() {
        return Link.tagType == TagType.NON_CLOSING;
    }

}
