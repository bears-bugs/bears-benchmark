/*
 * Copyright 2015 MiLaboratory.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.milaboratory.core.alignment.kaligner2;

import com.milaboratory.util.IntArrayList;

import java.util.Arrays;

import static com.milaboratory.core.alignment.kaligner2.KMapper2.*;
import static java.lang.Math.*;

public final class OffsetPacksAccumulator {
    public static final int DROPPED_CLUSTER = -274653;
    public static final int FIRST_RECORD_ID = 0;
    public static final int LAST_RECORD_ID = 1;
    public static final int SCORE = 2;
    public static final int MIN_VALUE = 3;
    public static final int MAX_VALUE = 4;
    public static final int LAST_VALUE = 5;
    public static final int LAST_INDEX = 6;
    public static final int STRETCH_INDEX_MARK = 0xA0000000;
    public static final int STRETCH_INDEX_MASK = 0xE0000000;

    public static final int RECORD_SIZE = 7;
    public static final int OUTPUT_RECORD_SIZE = 3;

    final int[] slidingArray;
    final int slotCount;
    final int maxAllowedDelta;
    final int matchScore, mismatchScore, shiftScore, absoluteMinClusterScore;
    final IntArrayList results = new IntArrayList(OUTPUT_RECORD_SIZE * 2);
    int totalScore;

    public OffsetPacksAccumulator(int slotCount, int maxAllowedDelta, int matchScore,
                                  int mismatchScore, int shiftScore, int absoluteMinClusterScore) {
        this.slotCount = slotCount;
        this.maxAllowedDelta = maxAllowedDelta;
        this.slidingArray = new int[RECORD_SIZE * slotCount];
        this.matchScore = matchScore;
        this.mismatchScore = mismatchScore;
        this.shiftScore = shiftScore;
        this.absoluteMinClusterScore = absoluteMinClusterScore;
    }

    private void reset() {
        results.clear();
        totalScore = 0;
        Arrays.fill(slidingArray, Integer.MIN_VALUE);
    }

    public void calculateInitialPartitioning(IntArrayList list) {
        calculateInitialPartitioning(IntArrayList.getArrayReference(list), 0, list.size());
    }

    public void calculateInitialPartitioning(int[] data) {
        calculateInitialPartitioning(data, 0, data.length);
    }

    /**
     * Accepts array with elements in the following format:
     */
    public void calculateInitialPartitioning(int[] data, int dataFrom, int dataTo) {
        reset();

        int index, offset;
        OUTER:
        for (int recordId = dataFrom; recordId < dataTo; recordId++) {
            int record = data[recordId];
            offset = offset(record);
            index = index(record);

            // Matching existing records
            for (int i = 0; i < slidingArray.length; i += RECORD_SIZE) {
                if ((slidingArray[i + SCORE] & STRETCH_INDEX_MASK) == STRETCH_INDEX_MARK) {
                    if (slidingArray[i + MIN_VALUE] - maxAllowedDelta <= offset && offset <= slidingArray[i + MAX_VALUE] + maxAllowedDelta) {
                        int pRecordId = slidingArray[i + SCORE] ^ STRETCH_INDEX_MARK,
                                pOffset = offset(data[pRecordId]),
                                pIndex = index(data[pRecordId]),
                                minDelta = Integer.MAX_VALUE,
                                minDeltaId = -1, temp;

                        if (minDelta > (temp = abs(offset - offset(data[pRecordId])))) {
                            minDeltaId = pRecordId;
                            minDelta = temp;
                        }

                        while (pRecordId < dataTo - 1
                                && pIndex == index(data[pRecordId + 1])
                                && abs(pOffset - offset(data[pRecordId + 1])) <= maxAllowedDelta)
                            if (minDelta > (temp = abs(offset - offset(data[++pRecordId])))) {
                                minDeltaId = pRecordId;
                                minDelta = temp;
                            }

                        pOffset = offset(data[minDeltaId]);
                        slidingArray[i + FIRST_RECORD_ID] = minDeltaId;
                        slidingArray[i + LAST_RECORD_ID] = recordId;
                        slidingArray[i + LAST_VALUE] = pOffset;
                        slidingArray[i + MIN_VALUE] = pOffset;
                        slidingArray[i + MAX_VALUE] = pOffset;
                        slidingArray[i + SCORE] = matchScore;
                    }
                }
                if (inDelta(slidingArray[i + LAST_VALUE], offset, maxAllowedDelta)) {
                    // Processing exceptional cases for self-correlated K-Mers

                    // If next record has same index and better offset
                    // (closer to current island LAST_VALUE)
                    if (recordId < dataTo - 1
                            && index == index(data[recordId + 1])
                            && abs(slidingArray[i + LAST_VALUE] - offset) > abs(slidingArray[i + LAST_VALUE] - offset(data[recordId + 1])))
                        // Skip current record
                        continue OUTER;

                    // If previous record has same index and better offset
                    // (closer to current island LAST_VALUE)
                    if (recordId > dataFrom
                            && index == index(data[recordId - 1])
                            && abs(slidingArray[i + LAST_VALUE] - offset) > abs(slidingArray[i + LAST_VALUE] - offset(data[recordId - 1])))
                        // Skip current record
                        continue OUTER;

                    assert index > slidingArray[i + LAST_INDEX];

                    int scoreDelta = matchScore + (index - slidingArray[i + LAST_INDEX] - 1) * mismatchScore +
                            abs(slidingArray[i + LAST_VALUE] - offset) * shiftScore;

                    if (scoreDelta > 0) {
                        slidingArray[i + LAST_INDEX] = index;
                        slidingArray[i + LAST_RECORD_ID] = recordId;
                        slidingArray[i + MIN_VALUE] = min(slidingArray[i + MIN_VALUE], offset);
                        slidingArray[i + MAX_VALUE] = max(slidingArray[i + MAX_VALUE], offset);
                        slidingArray[i + LAST_VALUE] = offset;
                        slidingArray[i + SCORE] += scoreDelta;
                        continue OUTER;
                    }
                }
            }

            int minimalIndex = -1;
            int minimalValue = Integer.MAX_VALUE;
            for (int i = LAST_INDEX; i < slidingArray.length; i += RECORD_SIZE) {
                if (slidingArray[i] == Integer.MIN_VALUE) {
                    minimalIndex = i;
                    break;
                } else if (slidingArray[i] < minimalValue) {
                    minimalIndex = i;
                    minimalValue = slidingArray[i];
                }
            }
            minimalIndex -= LAST_INDEX;

            assert minimalIndex >= 0;

            //finishing previous record
            finished(minimalIndex);

            //create new record
            slidingArray[minimalIndex + FIRST_RECORD_ID] = recordId;
            slidingArray[minimalIndex + LAST_RECORD_ID] = recordId;
            slidingArray[minimalIndex + SCORE] = matchScore;
            slidingArray[minimalIndex + MIN_VALUE] = offset;
            slidingArray[minimalIndex + MAX_VALUE] = offset;
            slidingArray[minimalIndex + LAST_VALUE] = offset;
            slidingArray[minimalIndex + LAST_INDEX] = index;

            // If next record has same index
            while (recordId < dataTo - 1
                    && index == index(data[recordId + 1])
                    && inDelta(offset, offset(data[recordId + 1]), maxAllowedDelta)) {
                //mark slot on first iteration
                if ((slidingArray[minimalIndex + SCORE] & STRETCH_INDEX_MARK) != STRETCH_INDEX_MARK) {
                    slidingArray[minimalIndex + SCORE] = STRETCH_INDEX_MARK | recordId;
                    slidingArray[minimalIndex + LAST_VALUE] = Integer.MIN_VALUE;
                }

                assert slidingArray[minimalIndex + MAX_VALUE] < offset(data[recordId + 1]);

                slidingArray[minimalIndex + MAX_VALUE] = offset = offset(data[++recordId]);
            }
        }

        for (int i = 0; i < slidingArray.length; i += RECORD_SIZE)
            finished(i);

        recalculateScores(data, dataFrom, dataTo);
    }

    private void recalculateScores(int[] data, int dataFrom, int dataTo) {
        for (int i = 0, size = results.size(); i < size; i += OUTPUT_RECORD_SIZE) {
            int recordId = results.get(i + FIRST_RECORD_ID);
            assert recordId >= dataFrom;

            int lastRecordId = results.get(i + LAST_RECORD_ID);
            assert lastRecordId < dataTo;

            int record, index, offset, delta;
            int clusterScore = matchScore;

            int previousIndex = index = index(record = data[recordId]);

            int previousOffset = offset(record);

            while (++recordId < dataTo && index(data[recordId]) == index) ;

            --recordId;
            while (++recordId <= lastRecordId) {
                record = data[recordId];
                index = index(record);
                offset = offset(record);

                int minRecord = record;
                int minDelta = abs(offset - previousOffset);

                if (minDelta > maxAllowedDelta)
                    continue;

                boolean $ = false;
                while (recordId < lastRecordId && index(record = data[recordId + 1]) == index) {
                    ++recordId;
                    if ((delta = abs(offset(record) - previousOffset)) < minDelta) {
                        minDelta = delta;
                        minRecord = record;
                        $ = true;
                    }
                }
                if ($) offset = offset(minRecord);

                int scoreDelta = matchScore + (index - previousIndex - 1) * mismatchScore +
                        minDelta * shiftScore;

                if (scoreDelta > 0) {
                    clusterScore += scoreDelta;
                    previousIndex = index;
                    previousOffset = offset;
                }
            }

            results.set(i + SCORE, max(clusterScore, results.get(i + SCORE)));
        }
    }

    private void finished(int indexOfFinished) {
        if (slidingArray[indexOfFinished + SCORE] < absoluteMinClusterScore)
            return; //just drop

        totalScore += slidingArray[indexOfFinished + SCORE];
        results.add(slidingArray, indexOfFinished, OUTPUT_RECORD_SIZE);
    }

    public int numberOfClusters() {
        return results.size() / OUTPUT_RECORD_SIZE;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of clusters: ").append(numberOfClusters()).append("\n\n");
        int k = 0;
        for (int i = 0; i < results.size(); i += OUTPUT_RECORD_SIZE) {
            sb.append(k++ + "th cloud:\n")
                    .append("  first record id:").append(results.get(i + FIRST_RECORD_ID)).append("\n")
                    .append("  last record id:").append(results.get(i + LAST_RECORD_ID)).append("\n")
                    .append("  score:").append(results.get(i + SCORE)).append("\n\n");
        }

        return sb.toString();
    }

    public static String toString(IntArrayList results, IntArrayList seedPositions, int[] data) {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of clusters: ").append(results.size() / OUTPUT_RECORD_SIZE).append("\n\n");
        int k = 0;
        for (int i = 0; i < results.size(); i += OUTPUT_RECORD_SIZE) {
            int firstRID = results.get(i + FIRST_RECORD_ID);
            int lastRID = results.get(i + LAST_RECORD_ID);
            sb.append(k++ + "th cloud:\n")
                    .append("  first record id:").append(firstRID).append(" (").append(recordToString(data[firstRID], seedPositions)).append(")\n")
                    .append("  last record id:").append(lastRID).append(" (").append(recordToString(data[lastRID], seedPositions)).append(")\n")
                    .append("  score:").append(results.get(i + SCORE)).append("\n\n");
        }

        return sb.toString();
    }

    // 0: fromSubject toSubject | fromQuery toQuery
    // 1: fromSubject toSubject | fromQuery toQuery
}
