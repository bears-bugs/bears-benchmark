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

/**
 * KMappingHit - class which represents single hit for {@link KMappingResult2}
 */
public final class KMappingHit2 implements java.io.Serializable {
    /**
     * Reference id (in storage)
     */
    final int id;

    /**
     * Offset values for according seeds inside intersection.
     *
     * each record = (offset << bitsForIndex) | seedIndex
     */
    final int[] seedRecords;

    /**
     * Islands boundaries.
     *
     * count = countOfIslands - 1
     */
    final int[] boundaries;

    /**
     * Mapping score value
     */
    int score;

    KMappingResult2 result;

    ///**
    // * Creates new KMappingHit
    // *
    // * @param seedOffsets offset values for according seeds inside intersection
    // * @param boundaries  boundaries of compact supports
    // * @param offset      best offset value (most popular offset)
    // * @param id          reference id (in {@link com.milaboratory.core.alignment.KMapper} storage)
    // * @param score       absolute alignment score value
    // * @param from        index of seed in seeds array of {@link KMappingResult2} from which intersection range of target and reference sequences starts
    // * @param to          index of seed in seeds array of {@link KMappingResult2} from which intersection range of target and reference sequences ends
    // */

    public KMappingHit2(int id, int[] seedRecords, int[] boundaries, int score) {
        this.id = id;
        this.seedRecords = seedRecords;
        this.boundaries = boundaries;
        this.score = score;
    }

    public int indexById(int recordId) {
        return KMapper2.index(seedRecords[recordId]);
    }

    public int offsetById(int recordId) {
        return KMapper2.offset(seedRecords[recordId]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ID: ").append(id).append("\n")
                .append("  Score: ").append(score).append("\n")
                .append("  Cluster 0:\n");
        int boundaryI = 0;
        int i = 0;
        for (int seedRecord : seedRecords) {
            if (boundaryI < boundaries.length && boundaries[boundaryI] == i) {
                boundaryI++;
                sb.append("  Cluster ").append(boundaryI).append(":\n");
            }
            int index = KMapper2.index(seedRecord);
            int offset = KMapper2.offset(seedRecord);
            sb.append("  Q ").append(result == null ? "null" : result.getSeedPosition(index)).append(" -> T ").append(result == null ? "null" : (offset + result.getSeedPosition(index))).append(" - ").append(offset).append("\n");
            i++;
        }
        return sb.toString();
    }
}
