package com.milaboratory.core.alignment.blast;

import org.junit.Test;

public class BlastAlignerParametersTest {
    @Test
    public void test1() throws Exception {
        BlastAlignerParameters bap = new BlastAlignerParameters();
        bap.chechAlphabet(null);
        bap.addEnvVariablesTo(null);
        bap.addArgumentsTo(null);
    }
}