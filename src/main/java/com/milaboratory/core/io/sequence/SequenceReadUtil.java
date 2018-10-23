package com.milaboratory.core.io.sequence;

public final class SequenceReadUtil {
    private SequenceReadUtil() {}

    public static SequenceRead setReadId(long readId, SequenceRead read) {
        if (readId == read.getId())
            return read;

        if (read instanceof SingleReadLazy)
            return ((SingleReadLazy) read).setReadId(readId);

        if (read.numberOfReads() == 1) {
            SingleRead sRead = read.getRead(0);
            return new SingleReadImpl(readId, sRead.getData(), sRead.getDescription());
        }

        int nReads = read.numberOfReads();
        SingleRead[] sReads = new SingleRead[nReads];
        for (int i = 0; i < sReads.length; i++)
            sReads[i] = (SingleRead) setReadId(readId, read.getRead(i));

        if (nReads == 2)
            return new PairedRead(sReads);
        else
            return new MultiRead(sReads);
    }
}
