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

import com.milaboratory.util.GlobalObjectMappers;
import org.junit.Assert;
import org.junit.Test;

public class TestJsonClass1Test {
    @Test
    public void test1() throws Exception {
        TestJsonClass1 c1 = new TestJsonClass1(1, "FER");
        String str = GlobalObjectMappers.ONE_LINE.writeValueAsString(c1);
        Assert.assertEquals(c1, GlobalObjectMappers.ONE_LINE.readValue(str, TestJsonClass1.class));
    }
}