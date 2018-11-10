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
package com.webfirmframework.wffweb.util;

import java.util.Objects;

/**
 *
 * @author WFF
 * @since 1.0.0
 */
public final class ObjectUtil {
    private ObjectUtil() {
        throw new AssertionError();
    }

    /**
     *
     * @author WFF
     * @return true if all objects are equal or null.
     * @since 1.0.0
     */
    public static boolean isEqual(final Object... objects) {
        if (objects == null || objects.length < 2) {
            return false;
        }
        boolean previousNull = false;
        boolean previousNotNull = false;
        Object previousObject = null;
        for (final Object object : objects) {
            if (object == null) {
                if (previousNotNull) {
                    return false;
                }
                previousNull = true;
            } else if (previousNull) {
                return false;
            } else {
                if (previousNotNull && !object.equals(previousObject)) {
                    return false;
                }
                previousNotNull = true;
                previousObject = object;
            }
        }
        // previousNull will be true if all values are null
        return true;
    }

    /**
     *
     * @author WFF
     * @return true if two objects are equal or null.
     * @since 1.0.0
     */
    public static boolean isEqual(final Object object1, final Object object2) {
        return Objects.equals(object1, object2);
    }

}
