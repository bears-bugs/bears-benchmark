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
package com.milaboratory.core.clustering;

import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.tree.NeighborhoodIterator;
import com.milaboratory.core.tree.TreeSearchParameters;

import java.util.Comparator;

public interface ClusteringStrategy<T, S extends Sequence<S>>
        extends Comparator<T>, java.io.Serializable {
    boolean canAddToCluster(Cluster<T> cluster, T minorObject,
                            NeighborhoodIterator<S, T[]> iterator);

    TreeSearchParameters getSearchParameters();

    int getMaxClusterDepth();
}
