package com.webfirmframework.wffweb.tag.html;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.H3Attributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class H3 extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(H3.class.getName());

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
    public H3(final AbstractHtml base, final AbstractAttribute... attributes) {
        super(TagNameConstants.H3, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof H3Attributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of H3Attribute");
            }
        }
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
