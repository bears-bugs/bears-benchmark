package com.milaboratory.core.alignment.blast;

import com.milaboratory.core.Range;
import com.milaboratory.core.alignment.Alignment;
import com.milaboratory.core.alignment.batch.AlignmentHitImpl;
import com.milaboratory.core.sequence.Sequence;

public class BlastHit<S extends Sequence<S>, P> extends AlignmentHitImpl<S, P> {
    private final double score, bitScore, eValue;
    private final String subjectId, subjectTitle;
    private final Range subjectRange;

    public BlastHit(Alignment<S> alignment, P recordPayload, BlastHit<S, ?> hit) {
        super(alignment, recordPayload);
        this.score = hit.getScore();
        this.bitScore = hit.getBitScore();
        this.eValue = hit.getEValue();
        this.subjectRange = hit.getSubjectRange();
        this.subjectId = hit.getSubjectId();
        this.subjectTitle = hit.getSubjectTitle();
    }

    public BlastHit(Alignment<S> alignment, P recordPayload, double score, double bitScore, double eValue,
                    Range subjectRange, String subjectId, String subjectTitle) {
        super(alignment, recordPayload);
        this.score = score;
        this.bitScore = bitScore;
        this.eValue = eValue;
        this.subjectRange = subjectRange;
        this.subjectId = subjectId;
        this.subjectTitle = subjectTitle;
    }

    public Range getSubjectRange() {
        return subjectRange;
    }

    public double getScore() {
        return score;
    }

    public double getBitScore() {
        return bitScore;
    }

    public double getEValue() {
        return eValue;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    @Override
    public String toString() {
        return "Record: " + getRecordPayload() + "\n" +
                "EValue = " + eValue + ";  Score = " + score + ";  BitScore = " + bitScore + " \n" +
                getAlignment().getAlignmentHelper().toString();
    }
}
