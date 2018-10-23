package com.milaboratory.primitivio;

import cc.redberry.pipe.OutputPortCloseable;

public final class PipeDataInputReader<O> implements OutputPortCloseable<O> {
    private final Class<O> oClass;
    private final PrimitivI input;
    private long count;

    public PipeDataInputReader(Class<O> oClass, PrimitivI input) {
        this(oClass, input, Long.MAX_VALUE);
    }

    public PipeDataInputReader(Class<O> oClass, PrimitivI input, long count) {
        this.oClass = oClass;
        this.input = input;
        this.count = count;
    }

    @Override
    public void close() {
        input.close();
    }

    @Override
    public synchronized O take() {
        if (count <= 0)
            return null;
        O obj = input.readObject(oClass);
        --count;
        return obj;
    }
}
