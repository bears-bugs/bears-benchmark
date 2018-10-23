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
package com.milaboratory.core.io.sequence;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class PairedRead extends MultiRead {
    public PairedRead(SingleRead... data) {
        super(data);
        if (data.length != 2)
            throw new IllegalArgumentException();
    }

    public SingleRead getR1() {
        return data[0];
    }

    public SingleRead getR2() {
        return data[1];
    }

    @Override
    public String toString() {
        return getR1().toString() + "\n" + getR2().toString();
    }
}
