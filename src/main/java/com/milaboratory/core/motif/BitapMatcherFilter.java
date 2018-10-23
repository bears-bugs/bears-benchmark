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
package com.milaboratory.core.motif;

import java.util.Arrays;

import static java.lang.System.arraycopy;

public final class BitapMatcherFilter implements BitapMatcher, java.io.Serializable {
    final BitapMatcher nestedMatcher;
    final int[] positionsBuffer;
    final int[] errorsBuffer;
    //int currentIndex = -1;

    public BitapMatcherFilter(BitapMatcher nestedMatcher) {
        this.nestedMatcher = nestedMatcher;
        this.positionsBuffer = new int[3];
        Arrays.fill(this.positionsBuffer, -1);
        this.errorsBuffer = new int[3];
        Arrays.fill(this.errorsBuffer, -1);
        next();
    }

    private void next() {
        arraycopy(positionsBuffer, 1, positionsBuffer, 0, 2);
        arraycopy(errorsBuffer, 1, errorsBuffer, 0, 2);
        int pos = nestedMatcher.findNext();
        if (pos == -1) {
            positionsBuffer[2] = -1;
            errorsBuffer[2] = -1;
        } else {
            positionsBuffer[2] = pos;
            errorsBuffer[2] = nestedMatcher.getNumberOfErrors();
        }
    }

    @Override
    public int findNext() {
        while (true) {
            next();
            if (positionsBuffer[0] != -1 &&
                    Math.abs(positionsBuffer[0] - positionsBuffer[1]) == 1
                    && errorsBuffer[0] + 1 == errorsBuffer[1])
                continue;
            if (positionsBuffer[2] != -1 &&
                    Math.abs(positionsBuffer[1] - positionsBuffer[2]) == 1
                    && errorsBuffer[1] == errorsBuffer[2] + 1)
                continue;
            return positionsBuffer[1];
        }
    }

    @Override
    public int getNumberOfErrors() {
        return errorsBuffer[1];
    }
}
