package com.milaboratory.util;

import cc.redberry.pipe.OutputPort;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class IntCombinations implements OutputPort<int[]> {
    final int[] combination;
    private final int n, k;
    private boolean onFirst = true;

    public IntCombinations(int n, int k) {
        if (n < k)
            throw new IllegalArgumentException(" n < k ");
        this.n = n;
        this.k = k;
        this.combination = new int[k];
        reset();
    }


    public void reset() {
        onFirst = true;
        for (int i = 0; i < k; ++i)
            combination[i] = i;
    }


    private boolean isLast() {
        for (int i = 0; i < k; ++i)
            if (combination[i] != i + n - k)
                return false;
        return true;
    }


    @Override
    public int[] take() {
        if (onFirst)
            onFirst = false;
        else {
            if (isLast())
                return null;

            int i;
            for (i = k - 1; i >= 0; --i)
                if (combination[i] != i + n - k)
                    break;
            int m = ++combination[i++];
            for (; i < k; ++i)
                combination[i] = ++m;
        }
        return combination;
    }
}
