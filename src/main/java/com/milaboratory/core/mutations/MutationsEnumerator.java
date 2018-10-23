package com.milaboratory.core.mutations;

/**
 * @author Dmitry Bolotin
 * @author Stanislav Poslavsky
 */
public final class MutationsEnumerator {
    private final int[] mutations;
    private int offset = 0;
    private int length = 0;
    private MutationType type;

    public MutationsEnumerator(Mutations mutations) {
        this.mutations = mutations.mutations;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public MutationType getType() {
        return type;
    }

    public boolean next() {
        if (offset + length >= mutations.length)
            return false;
        offset += length;
        length = 1;
        type = Mutation.getType(mutations[offset]);
        if (type == MutationType.Insertion) {
            MutationType t;
            while (offset + length < mutations.length) {
                t = Mutation.getType(mutations[offset + length]);
                if (t == type && Mutation.getPosition(mutations[offset])
                        == Mutation.getPosition(mutations[offset + length]))
                    ++length;
                else break;
            }
        }
        return true;
    }
}
