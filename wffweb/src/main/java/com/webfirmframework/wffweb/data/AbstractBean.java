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
package com.webfirmframework.wffweb.data;

import com.webfirmframework.wffweb.informer.StateChangeInformer;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public abstract class AbstractBean<FORCLASS> implements Bean {

    private static final long serialVersionUID = 1_0_0L;

    private StateChangeInformer<Bean> stateChangeInformer;

    private boolean alreadyInUse;

    /**
     * @return the stateChangeInformer
     * @since 1.0.0
     * @author WFF
     */
    public StateChangeInformer<Bean> getStateChangeInformer() {
        return stateChangeInformer;
    }

    /**
     * @param stateChangeInformer
     *            the stateChangeInformer to set
     * @since 1.0.0
     * @author WFF
     */
    public void setStateChangeInformer(
            final StateChangeInformer<Bean> stateChangeInformer) {
        this.stateChangeInformer = stateChangeInformer;
    }

    /**
     * @return true if the object is already used by any object.
     * @since 1.0.0
     * @author WFF
     */
    public boolean isAlreadyInUse() {
        return alreadyInUse;
    }

    /**
     * sets the if the object is already in use. It will also set null for
     * stateChangeInformer if the given argument is false.
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

}
