package com.webfirmframework.wffweb.tag.html.formsandinputs;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.OptionAttributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class Option extends AbstractHtml {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger
            .getLogger(Option.class.getName());

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
    public Option(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(TagNameConstants.OPTION, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof OptionAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of OptionAttribute");
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

}
