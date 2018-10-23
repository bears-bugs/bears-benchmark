package com.milaboratory.core.sequence;

import com.milaboratory.core.io.util.IOTestUtil;
import com.milaboratory.test.TestUtil;
import org.junit.Test;

public class NSequenceWithQualityTest {
    @Test
    public void test1() throws Exception {
        Object se = new NSequenceWithQuality(new NucleotideSequence("AACCTTGACC"), new SequenceQuality("++++++++++"));
        IOTestUtil.assertJavaSerialization(se);
    }

    @Test
    public void test2() throws Exception {
        NSequenceWithQuality se = new NSequenceWithQuality(new NucleotideSequence("AACCTTGACC"), new SequenceQuality("++++++++++"));
        TestUtil.assertJson(se);
    }
}