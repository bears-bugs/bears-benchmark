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
package com.milaboratory.core.alignment.kaligner1;

import com.milaboratory.core.alignment.AlignmentTest;
import com.milaboratory.core.alignment.LinearGapAlignmentScoring;
import com.milaboratory.core.alignment.batch.BatchAlignerWithBaseParameters;
import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.util.GlobalObjectMappers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class KAlignerParametersTest extends AlignmentTest {
    private static final KAlignerParameters gParams = new KAlignerParameters(5, false, false,
            1.5f, 0.75f, 1.0f, -0.1f, -0.3f, 4, 10, 15, 2, -10,
            40.0f, 0.87f, 7,
            LinearGapAlignmentScoring.getNucleotideBLASTScoring());

    @Test
    public void test1() throws Exception {
        Assert.assertTrue(gParams.equals(gParams.clone()));
        check(gParams);
        for (String key : KAlignerParameters.getAvailableNames())
            check(KAlignerParameters.getByName(key));
    }

    @Test
    public void test2() throws Exception {
        Object se = gParams;
        IOTestUtil.assertJavaSerialization(se);
    }

    private void check(KAlignerParameters params) throws IOException {
        String seialized = GlobalObjectMappers.PRETTY.writeValueAsString(params);
        KAlignerParameters deserialized = (KAlignerParameters) GlobalObjectMappers.PRETTY.readValue(seialized, BatchAlignerWithBaseParameters.class);
        Assert.assertTrue(deserialized.equals(params));
    }
}
