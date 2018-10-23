package com.milaboratory.core.alignment.blast;

public enum BlastStrand {
    Both, Minus, Plus;

    public String getOptionValue() {
        return this.name().toLowerCase();
    }
}
