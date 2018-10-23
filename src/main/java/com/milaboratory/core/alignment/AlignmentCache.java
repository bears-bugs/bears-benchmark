/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.core.alignment;

import java.util.Arrays;

/**
 * AlignmentCache - class which is providing cached array for {@link com.milaboratory.core.alignment.BandedLinearAligner}.
 *
 * <p>This cache helps to avoid creating new alignment array every time banded alignment is performed.</p>
 */
public final class AlignmentCache {
    private AlignmentCache() {
    }

    static {
        if (System.getProperty("enableAlignmentCache") != null)
            enable();
    }

    /**
     * CachedArray Provider
     */
    private static final ThreadLocal<CachedArrayProvider> local = new ThreadLocal<CachedArrayProvider>() {
        @Override
        protected CachedArrayProvider initialValue() {
            return new CachedArrayProvider();
        }
    };
    /**
     * Flag which is indicating whether AlignemntCache is on or off
     */
    private static boolean enabled = false;
    /**
     * Maximum number of CachedArrays in AlignemtnCache
     */
    private static int limit = 20;

    /**
     * Enables CachedArray system
     */
    public static void enable() {
        enabled = true;
    }

    /**
     * Returns available CachedIntArray if AlignemntCache is on or creates new CachedIntArray otherwise
     *
     * @return CachedIntArray
     */
    public static CachedIntArray get() {
        if (enabled)
            return local.get().get();
        else
            return new CachedIntArray();
    }

    /**
     * Release CacheIntArray
     */
    public static void release() {
        if (enabled)
            local.get().release();
    }

    private static final class CachedArrayProvider {
        int pointer = 0;
        CachedIntArray[] arrays = new CachedIntArray[3];

        public CachedIntArray get() {
            if (arrays.length == pointer) {
                if (limit < pointer)
                    throw new RuntimeException("Too many caches.");
                arrays = Arrays.copyOf(arrays, pointer + 2);
            }

            CachedIntArray cia;
            if ((cia = arrays[pointer]) == null)
                cia = arrays[pointer] = new CachedIntArray();

            ++pointer;

            return cia;
        }

        public void release() {
            if (--pointer < 0)
                throw new IllegalStateException("All caches already released.");
        }
    }
}
