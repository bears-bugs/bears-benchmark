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
package com.webfirmframework.wffweb.tag.core;

import java.io.Serializable;
import java.nio.charset.Charset;

public interface TagBase extends Serializable, Cloneable {
    /**
     * gets the html string of the tag including the child tags/values. rebuilds
     * the html string if the child tags/values or attributes have been
     * modified.
     *
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString();

    /**
     * gets the html string of the tag including the child tags/values using the
     * given charset. rebuilds the html string if the child tags/values or
     * attributes have been modified.
     *
     * @param charset
     *            the charset to set for the returning value, eg:
     *            {@code StandardCharsets.UTF_8}
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString(Charset charset);

    /**
     * gets the html string of the tag including the child tags/values using the
     * given charset. rebuilds the html string if the child tags/values or
     * attributes have been modified.
     *
     * @param charset
     *            the charset to set for the returning value, eg:
     *            {@code StandardCharsets.UTF_8.name()}
     *
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString(String charset);

    /**
     * rebuilds the html string of the tag including the child tags/values if
     * parameter is true, otherwise returns the html string prebuilt and kept in
     * the cache.
     *
     * @param rebuild
     *            true to rebuild &amp; false to return previously built string.
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString(boolean rebuild);

    /**
     * rebuilds the html string of the tag including the child tags/values if
     * parameter is true, otherwise returns the html string prebuilt and kept in
     * the cache.
     *
     * @param rebuild
     *            true to rebuild &amp; false to return previously built string.
     * @param charset
     *            the charset to set for the returning value, eg:
     *            {@code StandardCharsets.UTF_8}
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     *
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString(boolean rebuild, Charset charset);

    /**
     * rebuilds the html string of the tag including the child tags/values if
     * parameter is true, otherwise returns the html string prebuilt and kept in
     * the cache.
     *
     * @param rebuild
     *            true to rebuild &amp; false to return previously built string.
     * @param charset
     *            the charset to set for the returning value, eg:
     *            {@code StandardCharsets.UTF_8.name()}
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     *
     * @since 1.0.0
     * @author WFF
     */
    public abstract String toHtmlString(boolean rebuild, String charset);

    /**
     * gets the html string of the tag including the child tags/values. rebuilds
     * the html string if the child tags/values or attributes have been
     * modified.
     *
     * @return {@code String} equalent to the html string of the tag including
     *         the child tags.
     * @since 1.0.0
     * @author WFF
     */
    @Override
    public abstract String toString();

}
