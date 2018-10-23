package com.milaboratory.core.alignment.blast;

import org.junit.Assume;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicBoolean;

public class BlastTest {
    static final AtomicBoolean first = new AtomicBoolean(true);

    @Before
    public void setUp() throws Exception {
        Assume.assumeTrue(Blast.isBlastAvailable());
        if (first.compareAndSet(true, false))
            System.out.println("Blast: " + Blast.getBlastCommand(Blast.CMD_BLASTN, false));
    }
}
