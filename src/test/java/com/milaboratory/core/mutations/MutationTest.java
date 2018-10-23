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
package com.milaboratory.core.mutations;

import com.milaboratory.core.sequence.AminoAcidSequence;
import com.milaboratory.core.sequence.NucleotideSequence;
import org.junit.Assert;
import org.junit.Test;

import static com.milaboratory.core.mutations.Mutation.*;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public class MutationTest {

    @Test
    public void test1() throws Exception {
        int code = Mutation.createSubstitution(49, 3, 1);
        Assert.assertEquals(3, getFrom(code));
        Assert.assertEquals(1, getTo(code));
        Assert.assertEquals(RAW_MUTATION_TYPE_SUBSTITUTION,
                getRawTypeCode(code));
    }

    @Test
    public void exportRegexps() throws Exception {
        System.out.println(MutationsUtil.getMutationPatternStringForAlphabet(NucleotideSequence.ALPHABET));
        System.out.println(MutationsUtil.getMutationPatternStringForAlphabet(AminoAcidSequence.ALPHABET));
    }
}