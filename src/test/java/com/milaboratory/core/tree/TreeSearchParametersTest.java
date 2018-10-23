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
package com.milaboratory.core.tree;

import com.milaboratory.util.GlobalObjectMappers;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TreeSearchParametersTest {
    @Test
    public void test1() throws Exception {
        TreeSearchParameters params = new TreeSearchParameters(1, 1, 1);
        String str = GlobalObjectMappers.PRETTY.writeValueAsString(params);
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
        assertEquals(params, deser);
    }

    @Test
    public void test2() throws Exception {
        TreeSearchParameters params = new TreeSearchParameters(1, 1, 1, 1);
        String str = GlobalObjectMappers.PRETTY.writeValueAsString(params);
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
        assertEquals(params, deser);
    }

    @Test
    public void test3() throws Exception {
        TreeSearchParameters params = new TreeSearchParameters(1, 1, 2, 0.2, 0.3, 0.4, 0.7);
        String str = GlobalObjectMappers.PRETTY.writeValueAsString(params);
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
        assertEquals(params, deser);
    }

    @Test
    public void test4() throws Exception {
        String str = "{\"maxSubstitutions\":1,\"substitutionPenalty\":0.2,\"maxPenalty\":0.6}";
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
        TreeSearchParameters params = new TreeSearchParameters(1, 0, 0, 0.2, 0.0, 0.0, 0.6);
        assertEquals(params, deser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test5() throws Exception {
        String str = "{\"maxSubstitutions\":1,\"maxDeletions\":1,\"substitutionPenalty\":0.2,\"maxPenalty\":0.7}";
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
    }

    @Test
    public void test6() throws Exception {
        String str = "\"oneMismatch\"";
        TreeSearchParameters deser = GlobalObjectMappers.PRETTY.readValue(str, TreeSearchParameters.class);
        TreeSearchParameters params = TreeSearchParameters.ONE_MISMATCH;
        assertEquals(params, deser);
    }

    @Test
    public void test7() throws Exception {
        String str = GlobalObjectMappers.PRETTY.writeValueAsString(TreeSearchParameters.ONE_MISMATCH);
        assertEquals("\"oneMismatch\"", str);
    }
}
