package com.milaboratory.core.alignment.benchmark;

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.Processor;
import com.milaboratory.core.alignment.batch.BatchAlignerWithBaseParameters;
import com.milaboratory.core.alignment.AlignmentUtils;
import com.milaboratory.core.alignment.batch.AlignmentHit;
import com.milaboratory.core.alignment.batch.AlignmentResult;
import com.milaboratory.core.alignment.batch.BatchAlignerWithBase;
import com.milaboratory.core.alignment.kaligner2.KAligner2Statistics;
import com.milaboratory.core.sequence.NucleotideSequence;
import com.milaboratory.util.RandomUtil;

public final class Benchmark<T extends BatchAlignerWithBaseParameters>
        implements Processor<BenchmarkInput<T>, BenchmarkResults> {
    final long maxExecutionTime;
    final long maxNoHits;
    ExceptionListener exceptionListener;

    public Benchmark(long maxExecutionTime) {
        this(maxExecutionTime, Integer.MAX_VALUE);
    }

    public Benchmark(long maxExecutionTime, long maxNoHits) {
        this.maxExecutionTime = maxExecutionTime;
        this.maxNoHits = maxNoHits;
    }

    public void setExceptionListener(ExceptionListener exceptionListener) {
        this.exceptionListener = exceptionListener;
    }

    @Override
    public BenchmarkResults process(BenchmarkInput input) {
        RandomUtil.reseedThreadLocal(input.challenge.seed);
        KAligner2Statistics stat = new KAligner2Statistics();
        BatchAlignerWithBase<NucleotideSequence, Integer, ? extends AlignmentHit> aligner = input.params.createAligner();
        NucleotideSequence[] db = input.challenge.getDB();
        for (int i = 0; i < db.length; i++)
            aligner.addReference(db[i], i);

        long executionTime = 0;
        int processedQueries = 0;
        int processedGoodQueries = 0;
        int falsePositives = 0;
        int mismatched = 0;
        int noHits = 0;
        int scoreError = 0;

        long start = System.nanoTime();

        OUTER:
        for (KAlignerQuery query : CUtils.it(input.challenge.queries())) {
            if (System.nanoTime() - start > maxExecutionTime)
                break;

            if (noHits > maxNoHits)
                break;

            long seed = RandomUtil.reseedThreadLocal();
            try {
                long b = System.nanoTime();
                AlignmentResult<? extends AlignmentHit> result = aligner.align(query.query);
                executionTime += (System.nanoTime() - b);

                ++processedQueries;
                if (query.isFalse()) {
                    if (result.hasHits())
                        ++falsePositives;
                } else {
                    ++processedGoodQueries;


                    if (!result.hasHits()) {
                        ++noHits;
                        continue;
                    }
                    for (AlignmentHit hit : result.getHits())
                        if (!query.query.getRange(hit.getAlignment().getSequence2Range())
                                .equals(AlignmentUtils.getAlignedSequence2Part(hit.getAlignment()))) {
                            System.out.println("Expected:");
                            System.out.println(query.expectedAlignment.getAlignmentHelper());
                            System.out.println("Actual:");
                            System.out.println(hit.getAlignment().getAlignmentHelper());
                            throw new RuntimeException("Wrong answer.");
                        }

                    float topScore = result.getHits().get(0).getAlignment().getScore();
                    for (AlignmentHit hit : result.getHits()) {
                        if (hit.getAlignment().getScore() != topScore)
                            break;
                        if (hit.getRecordPayload().equals(query.targetId)) {
                            if (topScore < 0.95 * query.expectedAlignment.getScore()) {
//                                System.out.println("\n\n\n======================\n\n");
//                                System.out.println("expected score: " + query.expectedAlignment.getScore());
//                                System.out.println(query.expectedAlignment.getAlignmentHelper());
//                                System.out.println("\n");
//                                System.out.println("actual score: " + hit.getAlignment().getScore());
//                                System.out.println(hit.getAlignment().getAlignmentHelper());
                                ++scoreError;
                            }
                            continue OUTER;
                        }
                    }
                    ++mismatched;
                }
            } catch (Exception e) {
                if (exceptionListener != null)
                    exceptionListener.onException(new ExceptionData(seed, e, db, query.query, input));
                else
                    throw e;
            }
        }

        return new BenchmarkResults(input, stat, executionTime, processedQueries, processedGoodQueries, falsePositives,
                mismatched, noHits, scoreError);
    }

    public interface ExceptionListener {
        void onException(ExceptionData exceptionData);
    }

    public static final class ExceptionData {
        public final long seed;
        public final Throwable exception;
        public final NucleotideSequence[] db;
        public final NucleotideSequence query;
        public final BenchmarkInput input;

        public ExceptionData(long seed, Throwable exception, NucleotideSequence[] db, NucleotideSequence query, BenchmarkInput input) {
            this.seed = seed;
            this.exception = exception;
            this.db = db;
            this.query = query;
            this.input = input;
        }
    }
}
