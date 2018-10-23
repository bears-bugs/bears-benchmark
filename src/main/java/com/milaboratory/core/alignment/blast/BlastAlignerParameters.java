package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.sequence.Alphabet;

import java.util.List;

import static java.lang.Integer.MIN_VALUE;
import static java.util.Arrays.asList;

public class BlastAlignerParameters {
    private int batchSize = MIN_VALUE;
    private BlastTask blastTask = null;
    private double eValue = Double.NaN;
    private int wordSize = MIN_VALUE, gapOpen = MIN_VALUE, gapExtend = MIN_VALUE;
    private int penalty = MIN_VALUE, reward = MIN_VALUE, numAlignments = MIN_VALUE;
    private int numThreads = MIN_VALUE;
    private String matrix = null;

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getNumAlignments() {
        return numAlignments;
    }

    public void setNumAlignments(int numAlignments) {
        this.numAlignments = numAlignments;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public BlastTask getBlastTask() {
        return blastTask;
    }

    public void setBlastTask(BlastTask blastTask) {
        this.blastTask = blastTask;
    }

    public double getEValue() {
        return eValue;
    }

    public void setEValue(double eValue) {
        this.eValue = eValue;
    }

    public int getWordSize() {
        return wordSize;
    }

    public void setWordSize(int wordSize) {
        this.wordSize = wordSize;
    }

    public int getGapOpen() {
        return gapOpen;
    }

    public void setGapOpen(int gapOpen) {
        this.gapOpen = gapOpen;
    }

    public int getGapExtend() {
        return gapExtend;
    }

    public void setGapExtend(int gapExtend) {
        this.gapExtend = gapExtend;
    }

    public String getMatrix() {
        return matrix;
    }

    public void setMatrix(String matrix) {
        this.matrix = matrix;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void chechAlphabet(Alphabet<?> alphabet) {
        if (blastTask != null && alphabet != blastTask.alphabet)
            throw new IllegalArgumentException("The \"" + blastTask.value + "\" is not compatible with alphabet " + alphabet);
    }

    public void addEnvVariablesTo(ProcessBuilder processBuilder) {
        if (batchSize != MIN_VALUE)
            processBuilder.environment().put("BATCH_SIZE", Integer.toString(batchSize));
    }

    public void addArgumentsTo(List<String> cmd) {
        if (blastTask != null)
            cmd.addAll(asList("-task", blastTask.value));
        if (!Double.isNaN(eValue))
            cmd.addAll(asList("-evalue", Double.toString(eValue)));
        if (wordSize != MIN_VALUE)
            cmd.addAll(asList("-word_size", Integer.toString(wordSize)));
        if (gapOpen != MIN_VALUE)
            cmd.addAll(asList("-gapopen", Integer.toString(gapOpen)));
        if (gapExtend != MIN_VALUE)
            cmd.addAll(asList("-gapextend", Integer.toString(gapExtend)));
        if (penalty != MIN_VALUE)
            cmd.addAll(asList("-penalty", Integer.toString(penalty)));
        if (reward != MIN_VALUE)
            cmd.addAll(asList("-reward", Integer.toString(reward)));
        if (numAlignments != MIN_VALUE)
            cmd.addAll(asList("-num_alignments", Integer.toString(numAlignments)));
        if (numThreads != MIN_VALUE)
            cmd.addAll(asList("-num_threads", Integer.toString(numThreads)));
        if (matrix != null)
            cmd.addAll(asList("-matrix", matrix));
    }
}
