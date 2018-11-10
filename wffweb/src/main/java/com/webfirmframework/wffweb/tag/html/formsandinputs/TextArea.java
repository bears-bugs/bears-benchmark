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

import java.util.List;
import java.util.logging.Logger;

import com.webfirmframework.wffweb.settings.WffConfiguration;
import com.webfirmframework.wffweb.tag.html.AbstractHtml;
import com.webfirmframework.wffweb.tag.html.TagNameConstants;
import com.webfirmframework.wffweb.tag.html.attribute.core.AbstractAttribute;
import com.webfirmframework.wffweb.tag.html.identifier.GlobalAttributable;
import com.webfirmframework.wffweb.tag.html.identifier.TextAreaAttributable;
import com.webfirmframework.wffweb.tag.htmlwff.NoTag;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class TextArea extends AbstractHtml {

    private static final long serialVersionUID = 1_0_1L;

    public static final Logger LOGGER = Logger
            .getLogger(TextArea.class.getName());

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
    public TextArea(final AbstractHtml base,
            final AbstractAttribute... attributes) {
        super(TagNameConstants.TEXTAREA, base, attributes);
        if (WffConfiguration.isDirectionWarningOn()) {
            warnForUnsupportedAttributes(attributes);
        }
    }

    private static void warnForUnsupportedAttributes(
            final AbstractAttribute... attributes) {
        for (final AbstractAttribute abstractAttribute : attributes) {
            if (!(abstractAttribute != null
                    && (abstractAttribute instanceof TextAreaAttributable
                            || abstractAttribute instanceof GlobalAttributable))) {
                LOGGER.warning(abstractAttribute
                        + " is not an instance of TextAreaAttribute");
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
     * sets the child text for this tag.
     *
     * @param text
     *            String which needs to be shown as a child of this tag.
     * @since 2.1.4
     */
    public void setChildText(final String text) {
        if (text == null || text.isEmpty()) {
            removeAllChildren();
        } else {
            addInnerHtml(new NoTag(null, text));
        }
    }

    /**
     * sets the child text for this tag.
     *
     * @param updateClient
     *            true to update client browser page if it is available. The
     *            default value is true but it will be ignored if there is no
     *            client browser page.
     *
     * @param text
     *            String which needs to be shown as a child of this tag.
     * @since 2.1.15
     */
    public void setChildText(final boolean updateClient, final String text) {
        if (text == null || text.isEmpty()) {
            removeAllChildren();
        } else {
            addInnerHtmls(updateClient, new NoTag(null, text));
        }
    }

    /**
     * gets the child text set for this tag.
     *
     * @return the child text set for this tag in string format. If there is no
     *         children/child text then will return empty string.
     * @since 2.1.4
     */
    public String getChildText() {
        final List<AbstractHtml> children = getChildren();
        if (children.size() > 0) {
            final StringBuilder builder = new StringBuilder();
            for (final AbstractHtml child : children) {
                builder.append(child.toHtmlString());
            }
            return builder.toString();
        }
        return "";
    }

}
