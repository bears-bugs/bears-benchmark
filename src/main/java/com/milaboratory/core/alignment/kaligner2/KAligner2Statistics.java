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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.milaboratory.util.AtomicEnumHistogram;
import com.milaboratory.util.AtomicHistogram;
import com.milaboratory.util.GlobalObjectMappers;
import com.milaboratory.util.IntArrayList;

import java.util.concurrent.atomic.AtomicLong;

import static com.milaboratory.core.alignment.kaligner2.OffsetPacksAccumulator.DROPPED_CLUSTER;
import static com.milaboratory.core.alignment.kaligner2.OffsetPacksAccumulator.FIRST_RECORD_ID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        getterVisibility = JsonAutoDetect.Visibility.NONE)
public final class KAligner2Statistics {
    @JsonIgnore
    final ThreadLocal<State> currentState = new ThreadLocal<State>() {
        @Override
        protected State initialValue() {
            return new State();
        }
    };
    public final AtomicLong inputQueries = new AtomicLong();
    public final AtomicHistogram allInitialRecordsCount = new AtomicHistogram(0, 300),
            topInitialRecordsCount = new AtomicHistogram(0, 300);
    public final AtomicHistogram allInitialClusters = new AtomicHistogram(0, 20),
            topInitialClusters = new AtomicHistogram(0, 20);
    public final AtomicHistogram allRemovedByTrimming = new AtomicHistogram(0, 20),
            topRemovedByTrimming = new AtomicHistogram(0, 20);
    public final AtomicHistogram allRemovedByUntangling = new AtomicHistogram(0, 20),
            topRemovedByUntangling = new AtomicHistogram(0, 20);
    public final AtomicHistogram allUntangledClusters = new AtomicHistogram(0, 20),
            topUntangledClusters = new AtomicHistogram(0, 20);
    public final AtomicEnumHistogram<ClusterTrimmingType> allTrimming = new AtomicEnumHistogram<>(ClusterTrimmingType.class),
            topTrimming = new AtomicEnumHistogram<>(ClusterTrimmingType.class);
    public final AtomicLong rerun = new AtomicLong(), changeOfTop1 = new AtomicLong(), changeOfTop2 = new AtomicLong();
    public final AtomicHistogram numberOfMappingHits = new AtomicHistogram(0, 20),
            numberOfAlignmentsHits = new AtomicHistogram(0, 20);
    public final AtomicHistogram filteredHitsByAlignments = new AtomicHistogram(0, 50);

    // Timing
    public final AtomicHistogram seedExtractionTime = new AtomicHistogram(0, 10000, 400),
            hitCalculationTime = new AtomicHistogram(0, 10000, 400),
            mapperTotalTime = new AtomicHistogram(0, 10000, 400),
            alignerTime = new AtomicHistogram(0, 10000, 400),
            totalTime = new AtomicHistogram(0, 10000, 400);


    public void nextQuery() {
        State state = currentState.get();
        state.reset();
        inputQueries.incrementAndGet();
    }

    public void afterCandidatesArrayDone(IntArrayList[] candidates) {
        State state = currentState.get();

        state.previousStep = System.nanoTime();
        seedExtractionTime.add((state.previousStep - state.start) / 1000.0);

        // Collecting statistics and calculating top candidate index
        int top = -1;
        for (int i = 0; i < candidates.length; i++) {
            IntArrayList candidate = candidates[i];
            if (candidate == null) {
                allInitialRecordsCount.add(0);
                continue;
            } else
                allInitialRecordsCount.add(candidate.size());

            if (top == -1 || (candidates[top].size() < candidate.size()))
                top = i;
        }

        // Saving top in stat
        state.topByRecordsCount = top;

        // Collecting information on number of records in top record
        if (top == -1)
            topInitialRecordsCount.add(0);
        else
            topInitialRecordsCount.add(candidates[top].size());
    }

    public void initialClusters(int id, IntArrayList results) {
        State state = currentState.get();

        // Saving current id
        state.currentTargetIndex = id;

        // Counting clusters
        int clusters = countClusters(results);

        // Saving for further use
        state.clusters = clusters;
        allInitialClusters.add(clusters);
        if (state.isCurrentTop())
            topInitialClusters.add(clusters);
    }

    public void trimmingEvent(ClusterTrimmingType type) {
        State state = currentState.get();

        allTrimming.add(type);
        if (state.isCurrentTop())
            topTrimming.add(type);
    }

    public void afterTrimming(int id, IntArrayList results) {
        State state = currentState.get();

        // Assert
        assert state.currentTargetIndex == id;

        // Counting clusters
        int clusters = countClusters(results);

        // Collecting statistics about deltas
        allRemovedByTrimming.add(state.clusters - clusters);
        if (state.isCurrentTop())
            topRemovedByTrimming.add(state.clusters - clusters);

        state.clusters = clusters;
    }

    public void afterUntangling(IntArrayList untangled) {
        State state = currentState.get();

        int clusters = untangled.size();

        // Collecting statistics about deltas
        allRemovedByUntangling.add(state.clusters - clusters);
        if (state.isCurrentTop())
            topRemovedByUntangling.add(state.clusters - clusters);

        // Collecting statistics about final number of clusters
        allUntangledClusters.add(clusters);
        if (state.isCurrentTop())
            topUntangledClusters.add(clusters);
    }

    public void reRunBecauseOfMicroTangling() {
        rerun.incrementAndGet();
    }

    public void kMappingResults(KMappingResult2 result) {
        State state = currentState.get();

        long timestamp = System.nanoTime();
        hitCalculationTime.add((timestamp - state.previousStep) / 1000.0);
        mapperTotalTime.add((timestamp - state.start) / 1000.0);
        state.previousStep = timestamp;

        if (result.hits == null) {
            numberOfMappingHits.add(0);
            state.mappingHits = 0;
            return;
        }

        numberOfMappingHits.add(result.hits.size());
        state.mappingHits = result.hits.size();

        int top;
        if (result.hits.isEmpty())
            top = -1;
        else
            top = result.hits.get(0).id;

        if (top != state.topByRecordsCount)
            changeOfTop1.incrementAndGet();

        state.topByMappingScore = top;
    }

    public void kAlignerResult(KAlignmentResult2 result) {
        State state = currentState.get();

        long timestamp = System.nanoTime();
        alignerTime.add((timestamp - state.previousStep) / 1000.0);
        totalTime.add((timestamp - state.start) / 1000.0);

        int hits = result.getHits().size();

        numberOfAlignmentsHits.add(hits);

        filteredHitsByAlignments.add(state.mappingHits - hits);
    }

    @Override
    public String toString() {
        try {
            return GlobalObjectMappers.toOneLine(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static int countClusters(IntArrayList results) {
        int clusters = 0;
        for (int i = 0; i < results.size(); i += OffsetPacksAccumulator.OUTPUT_RECORD_SIZE)
            if (results.get(i + FIRST_RECORD_ID) != DROPPED_CLUSTER)
                ++clusters;

        return clusters;
    }

    public static final class State {
        volatile int topByRecordsCount, topByMappingScore, currentTargetIndex,
                clusters, mappingHits;
        volatile long start, previousStep;

        public boolean isCurrentTop() {
            return topByRecordsCount == currentTargetIndex;
        }

        void reset() {
            topByRecordsCount = -1;
            currentTargetIndex = -1;
            start = System.nanoTime();
        }
    }

    public enum ClusterTrimmingType {
        TrimLeftQuery, TrimLeftTarget, TrimRightQuery, TrimRightTarget
    }
}
