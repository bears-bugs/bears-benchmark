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
package com.milaboratory.primitivio.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.milaboratory.primitivio.annotations.Serializable;

@Serializable(asJson = true)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class TestJsonClass1 {
    public final int guga;
    public final String muga;

    @JsonCreator
    public TestJsonClass1(@JsonProperty("guga") int guga,
                          @JsonProperty("muga") String muga) {
        this.guga = guga;
        this.muga = muga;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestJsonClass1 that = (TestJsonClass1) o;

        if (guga != that.guga) return false;
        if (!muga.equals(that.muga)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = guga;
        result = 31 * result + muga.hashCode();
        return result;
    }
}
