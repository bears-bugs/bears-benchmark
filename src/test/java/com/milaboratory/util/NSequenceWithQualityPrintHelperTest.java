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
package com.milaboratory.util;

import cc.redberry.pipe.CUtils;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.fastq.SingleFastqReader;
import org.junit.Ignore;
import org.junit.Test;

public class NSequenceWithQualityPrintHelperTest {
    @Ignore
    @Test
    public void test1() throws Exception {
        try (SingleFastqReader reader = new SingleFastqReader(
                NSequenceWithQualityPrintHelperTest.class
                        .getResourceAsStream("/sequences/sample_r2.fastq"),
                true
        )) {
            for (SingleRead singleRead : CUtils.it(reader)) {
                NSequenceWithQualityPrintHelper helper = new NSequenceWithQualityPrintHelper(singleRead.getData(), 7, 20);
                System.out.println(helper);
            }
        }
    }
}