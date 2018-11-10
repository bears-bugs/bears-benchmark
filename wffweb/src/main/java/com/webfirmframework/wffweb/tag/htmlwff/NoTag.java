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
package com.webfirmframework.wffweb.tag.htmlwff;

import java.util.Collection;
import java.util.List;

import com.webfirmframework.wffweb.tag.html.AbstractHtml;

/**
 * It's a tag which makes child content without any opening closing tag.
 *
 * @author WFF
 * @since 1.0.0
 */
public class NoTag extends AbstractHtml {

    // TODO This class needs to be tested properly

    private static final long serialVersionUID = 1_0_0L;

    {
        init();
    }

    /**
     *
     * @param base
     *            i.e. parent tag of this tag
     * @param children
     *            An array of {@code AbstractHtml}
     *
     * @since 1.0.0
     */
    public NoTag(final AbstractHtml base, final AbstractHtml... children) {
        super(base, children);
    }

    /**
     *
     * @param base
     *            i.e. parent tag of this tag
     * @param children
     *            An array of {@code AbstractHtml}
     *
     * @since 1.0.0
     */
    public NoTag(final AbstractHtml base,
            final Collection<? extends AbstractHtml> children) {
        super(base, children);
    }

    /**
     *
     * @param base
     *            i.e. parent tag of this tag
     * @param childContent
     *
     * @since 1.0.0
     */
    public NoTag(final AbstractHtml base, final String childContent) {
        super(base, childContent);
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
     * adds {@code AbstractHtml}s as children.
     *
     * @param children
     * @since 1.0.0
     * @author WFF
     */
    public void addChildren(final List<AbstractHtml> children) {
        super.appendChildren(children);
    }

    /**
     * adds {@code AbstractHtml}s as children.
     *
     * @param child
     * @since 1.0.0
     * @author WFF
     */
    public void addChild(final AbstractHtml child) {
        super.appendChild(child);
    }

    /**
     * adds {@code AbstractHtml}s as children.
     *
     * @param children
     * @since 1.0.0
     * @author WFF
     */
    public void removeChildren(final List<AbstractHtml> children) {
        super.removeChildren(children);
    }

    /**
     * adds {@code AbstractHtml}s as children.
     *
     * @param child
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public boolean removeChild(final AbstractHtml child) {
        return super.removeChild(child);
    }

    /**
     * removes the the child content.
     *
     * @param child
     * @since 1.0.0
     * @author WFF
     */
    public void removeChild(final String child) {
        final StringBuilder htmlMiddleSB = getHtmlMiddleSB();
        final String sb = htmlMiddleSB.toString();
        final String replaced = sb.replace(child, "");
        final int lastIndex = htmlMiddleSB.length() - 1;
        htmlMiddleSB.delete(0, lastIndex);
        htmlMiddleSB.append(replaced);
    }

    /**
     * adds {@code AbstractHtml}s as children.
     *
     * @param child
     * @since 1.0.0
     * @author WFF
     */
    public void addChild(final String child) {
        super.getChildren().add(new NoTag(this, child));
    }

    /**
     * adds {@code AbstractHtml}s as children.
     *
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public String getChildContent() {
        return getHtmlMiddleSB().toString();
    }
}
