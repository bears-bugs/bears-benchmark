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
package com.webfirmframework.wffweb.tag.html;

import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.HtmlAttributable;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class Html extends DocType {

    private static final long serialVersionUID = 1_0_0L;

    public static final Logger LOGGER = Logger.getLogger(Html.class.getName());

    {
        init();
    }

    /**
     * Represents the root of an HTML or XHTML document. All other tags must be
     * descendants of this tag in case of building a complete HTML document. All
     * tags can also be used independently i.e. without being a child of this
     * tag.
     *
     * @param base
     *            i.e. parent tag of this tag
     * @param attributes
     *            An array of {@code AbstractAttribute}
     *
     * @author WFF
     * @since 1.0.0
     */
    public Html(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(TagNameConstants.HTML, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof HtmlAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of HtmlAttribute");
            }
        }
    }

    /**
     * constructor for testing
     *
     * @since 1.0.0
     * @deprecated this constructor is used for testing purpose, it should not
     *             be used/consumed for development because it may be removed
     *             later.
     */
    // TODO should be removed later
    @Deprecated
    public Html(final int id, final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(Html.class.getSimpleName().toLowerCase() + String.valueOf(id),
                base, attributes);
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

    @Override
    public Html clone() throws CloneNotSupportedException {
        return (Html) super.clone();
    }
}
