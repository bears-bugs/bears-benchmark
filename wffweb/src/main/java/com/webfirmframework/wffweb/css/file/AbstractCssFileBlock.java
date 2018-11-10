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
package com.webfirmframework.wffweb.css.file;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.webfirmframework.wffweb.clone.CloneUtil;
import com.webfirmframework.wffweb.css.core.CssProperty;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public abstract class AbstractCssFileBlock implements CssFileBlock {

    private static final long serialVersionUID = 1_0_0L;

    private final Set<CssProperty> cssProperties;

    private final Map<String, CssProperty> cssPropertiesAsMap;

    private final Set<CssFile> cssFiles;

    private String selectors;

    private volatile boolean modified;

    private volatile boolean loadedOnce;

    private boolean excludeCssBlock;

    @SuppressWarnings("unused")
    private AbstractCssFileBlock() {
    }

    public AbstractCssFileBlock(final String selectors) {
        this.selectors = selectors;
    }

    {
        cssFiles = new LinkedHashSet<>();

        cssPropertiesAsMap = new LinkedHashMap<>();

        cssProperties = new LinkedHashSet<CssProperty>() {

            private static final long serialVersionUID = 1_0_0L;

            private StringBuilder toStringBuilder;

            {
                toStringBuilder = new StringBuilder();
            }

            @Override
            public boolean add(final CssProperty cssProperty) {
                final boolean added = super.add(cssProperty);
                if (added) {
                    setModified(added);
                    cssPropertiesAsMap.put(cssProperty.getCssName(),
                            cssProperty);
                }
                return added;
            }

            @Override
            public boolean addAll(
                    final Collection<? extends CssProperty> cssProperties) {
                final boolean addedAll = super.addAll(cssProperties);
                if (addedAll) {
                    setModified(addedAll);
                    for (final CssProperty cssProperty : cssProperties) {
                        cssPropertiesAsMap.put(cssProperty.getCssName(),
                                cssProperty);
                    }
                }
                return addedAll;
            }

            @Override
            public boolean remove(final Object o) {
                final boolean removed = super.remove(o);
                if (removed) {
                    setModified(removed);
                    if (o instanceof CssProperty) {
                        cssPropertiesAsMap
                                .remove(((CssProperty) o).getCssName());
                    }
                }
                return removed;
            }

            @Override
            public boolean removeAll(final Collection<?> c) {
                final boolean removedAll = super.removeAll(c);
                if (removedAll) {
                    setModified(removedAll);
                    for (final Object object : c) {
                        if (object instanceof CssProperty) {
                            cssPropertiesAsMap.remove(
                                    ((CssProperty) object).getCssName());
                        }
                    }
                }
                return removedAll;
            }

            @Override
            public void clear() {
                setModified(true);
                super.clear();
                cssPropertiesAsMap.clear();
            }

            @Override
            public String toString() {
                if (modified) {
                    synchronized (toStringBuilder) {
                        if (modified) {
                            toStringBuilder.delete(0, toStringBuilder.length());
                            for (final CssProperty cssProperty : this) {
                                toStringBuilder.append(cssProperty.getCssName())
                                        .append(':')
                                        .append(cssProperty.getCssValue())
                                        .append(';');
                            }
                            setModified(false);
                        }
                    }
                }
                return toStringBuilder.toString();
            }
        };

    }

    protected abstract void load(Set<CssProperty> cssProperties);

    void addCssFile(final CssFile cssFile) {
        cssFiles.add(cssFile);
    }

    void removeCssFile(final CssFile cssFile) {
        cssFiles.remove(cssFile);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return
     * @since 1.0.0
     * @author WFF
     */
    public String toCssString() {
        if (!loadedOnce) {
            synchronized (cssProperties) {
                if (!loadedOnce) {
                    cssProperties.clear();
                    load(cssProperties);
                    loadedOnce = true;
                    setModified(true);
                }
            }
        }
        return selectors + "{" + cssProperties.toString() + "}";
    }

    /**
     * @param rebuild
     * @return the css string.
     * @since 1.0.0
     * @author WFF
     */
    public String toCssString(final boolean rebuild) {
        if (rebuild || !loadedOnce) {
            synchronized (cssProperties) {
                if (rebuild || !loadedOnce) {
                    cssProperties.clear();
                    load(cssProperties);
                    loadedOnce = true;
                    setModified(true);
                }
            }
        }
        return selectors + "{" + cssProperties.toString() + "}";
    }

    @Override
    public AbstractCssFileBlock clone() throws CloneNotSupportedException {
        return CloneUtil.deepClone(this);
    }

    /**
     * @return the cssProperties
     * @since 1.0.0
     * @author WFF
     */
    public Set<CssProperty> getCssProperties() {
        if (!loadedOnce) {
            synchronized (cssProperties) {
                if (!loadedOnce) {
                    cssProperties.clear();
                    load(cssProperties);
                    loadedOnce = true;
                    setModified(true);
                }
            }
        }
        return cssProperties;
    }

    /**
     * @param modified
     *            the modified to set
     * @since 1.0.0
     * @author WFF
     */
    private void setModified(final boolean modified) {
        if (modified) {
            for (final CssFile cssFile : cssFiles) {
                cssFile.setModified(true);
            }
        }
        this.modified = modified;
    }

    /**
     * @return the selectors
     * @since 1.0.0
     * @author WFF
     */
    public String getSelectors() {
        return selectors;
    }

    /**
     * rebuild true to rebuild, the load method will be invoked again.
     *
     * @return the cssProperties as map with key as the cssName and value as
     *         {@code CssProperty}.
     * @since 1.0.0
     * @author WFF
     */
    Map<String, CssProperty> getCssPropertiesAsMap(final boolean rebuild) {
        if (rebuild || !loadedOnce) {
            synchronized (cssProperties) {
                if (rebuild || !loadedOnce) {
                    cssProperties.clear();
                    load(cssProperties);
                    loadedOnce = true;
                    setModified(true);
                }
            }
        }
        return cssPropertiesAsMap;
    }

    /**
     * @return the excludeCssBlock true if the css block has been excluded, i.e.
     *         it will not be contained in the generated css.
     */
    public boolean isExcludeCssBlock() {
        return excludeCssBlock;
    }

    /**
     * @param excludeCssBlock
     *            the excludeCssBlock to set. If it is set to true, then this
     *            css block will not be contained in the generated css.
     */
    protected void setExcludeCssBlock(final boolean excludeCssBlock) {
        this.excludeCssBlock = excludeCssBlock;
    }
}
