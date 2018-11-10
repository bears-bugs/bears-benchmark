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
package com.webfirmframework.wffweb.css.core;

import com.webfirmframework.wffweb.clone.CloneUtil;
import com.webfirmframework.wffweb.informer.StateChangeInformer;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public abstract class AbstractCssProperty<EXTENDEDCLASS extends AbstractCssProperty<EXTENDEDCLASS>>
        implements CssProperty {

    private static final long serialVersionUID = 1_0_0L;

    private StateChangeInformer<CssProperty> stateChangeInformer;

    private boolean alreadyInUse;

    /**
     * @param value
     *            to set the value portion in style, eg <code>center</code> for
     *            style <code>align-content: center</code>.
     * @since 1.0.0
     * @author WFF
     * @return the current object.
     */
    public abstract EXTENDEDCLASS setCssValue(String value);

    /**
     * @return the stateChangeInformer
     * @since 1.0.0
     * @author WFF
     */
    public StateChangeInformer<CssProperty> getStateChangeInformer() {
        return stateChangeInformer;
    }

    /**
     * @param stateChangeInformer
     *            the stateChangeInformer to set
     * @since 1.0.0
     * @author WFF
     */
    public void setStateChangeInformer(
            final StateChangeInformer<CssProperty> stateChangeInformer) {
        this.stateChangeInformer = stateChangeInformer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public EXTENDEDCLASS clone() throws CloneNotSupportedException {
        // TODO optimize later as we can create a new object (by
        // this.newInstance()) and assign cssValue using setCssValue. Confirm
        // and modify based on the performance test result.
        return (EXTENDEDCLASS) CloneUtil
                .<AbstractCssProperty<EXTENDEDCLASS>> deepClone(this);
    }

    /**
     * @return true if the object is already used by {@code Style} class.
     * @since 1.0.0
     * @author WFF
     */
    public boolean isAlreadyInUse() {
        return alreadyInUse;
    }

    /**
     * To set whether this object is used by any other object. It also
     * dereferences stateChangeInformer object if the given argument is false.
     *
     * @param alreadyInUse
     *            the alreadyInUse to set
     * @since 1.0.0
     * @author WFF
     */
    public void setAlreadyInUse(final boolean alreadyInUse) {
        this.alreadyInUse = alreadyInUse;
        if (!alreadyInUse) {
            stateChangeInformer = null;
        }
    }

    /**
     * @return the css string of the property
     * @since 1.1.2
     * @author WFF
     */
    public String toCssString() {
        return getCssName() + ':' + getCssValue();
    }

}
