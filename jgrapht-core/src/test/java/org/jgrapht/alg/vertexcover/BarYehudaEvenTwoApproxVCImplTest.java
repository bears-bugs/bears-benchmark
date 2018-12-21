/*
 * (C) Copyright 2018-2018, by Alexandru Valeanu and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht.alg.vertexcover;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.VertexCoverAlgorithm;

import java.util.Map;

public class BarYehudaEvenTwoApproxVCImplTest extends WeightedVertexCoverTwoApproxTest {

    @Override
    public <V, E> VertexCoverAlgorithm<V> createSolver(Graph<V, E> graph) {
        return new BarYehudaEvenTwoApproxVCImpl<>(graph);
    }

    @Override
    public <V, E> VertexCoverAlgorithm<V> createWeightedSolver(Graph<V, E> graph, Map<V, Double> vertexWeightMap) {
        return new BarYehudaEvenTwoApproxVCImpl<>(graph, vertexWeightMap);
    }
}