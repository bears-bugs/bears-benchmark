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
package com.milaboratory.core.alignment.kaligner2;

import com.milaboratory.core.alignment.AffineGapAlignmentScoring;
import com.milaboratory.core.alignment.batch.BatchAlignerWithBaseParameters;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.test.TestUtil;
import org.junit.Test;

/**
 * Created by dbolotin on 27/10/15.
 */
public class KAlignerParameters2Test {
    public static final AffineGapAlignmentScoring<NucleotideSequence> scoring = new AffineGapAlignmentScoring<>(
            NucleotideSequence.ALPHABET, 10, -7, -11, -2);
    public static final KAlignerParameters2 gParams = new KAlignerParameters2(
            9, 3, true, true,
            15, -10, 15, 0f, 13, -7, -3,
            3, 6, 4, 3, 3, 3,
            0, 70, 0.8f, 5, scoring);

    @Test
    public void test1() throws Exception {
        TestUtil.assertJson(gParams, BatchAlignerWithBaseParameters.class);
    }
}