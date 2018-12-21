/*
 * (C) Copyright 2003-2018, by Linda Buisman and Contributors.
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
import org.jgrapht.Graphs;
import org.jgrapht.alg.interfaces.VertexCoverAlgorithm;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import java.util.Map;

import static org.jgrapht.alg.vertexcover.VertexCoverTestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GreedyVCImplTest {

    public <V, E> VertexCoverAlgorithm<V> createSolver(Graph<V, E> graph) {
        return new GreedyVCImpl<>(graph);
    }

    public <V, E> VertexCoverAlgorithm<V> createWeightedSolver(Graph<V, E> graph, Map<V, Double> vertexWeightMap) {
        return new GreedyVCImpl<>(graph, vertexWeightMap);
    }

    // ------- Greedy algorithms ------

    /**
     * Test greedy algorithm for the minimum vertex cover problem.
     */
    @Test
    public void testFindGreedyCover()
    {
        for (int i = 0; i < TEST_REPEATS; i++) {
            Graph<Integer, DefaultEdge> g = createRandomPseudoGraph(TEST_GRAPH_SIZE);
            VertexCoverAlgorithm<Integer> mvc = createSolver(Graphs.undirectedGraph(g));

            VertexCoverAlgorithm.VertexCover<Integer> vertexCover = mvc.getVertexCover();
            assertTrue(isCover(g, vertexCover));
            assertEquals(vertexCover.getWeight(), 1.0 * vertexCover.size(),0);
        }
    }

    /**
     * Test greedy algorithm for the minimum weighted vertex cover problem.
     */
    @Test
    public void testFindGreedyWeightedCover()
    {

        for (int i = 0; i < TEST_REPEATS; i++) {
            Graph<Integer, DefaultEdge> g = createRandomPseudoGraph(TEST_GRAPH_SIZE);
            Map<Integer, Double> vertexWeights = WeightedVertexCoverTest.getRandomVertexWeights(g);

            VertexCoverAlgorithm<Integer> mvc = createWeightedSolver(Graphs.undirectedGraph(g), vertexWeights);

            VertexCoverAlgorithm.VertexCover<Integer> vertexCover = mvc.getVertexCover();
            assertTrue(isCover(g, vertexCover));
            assertEquals(
                    vertexCover.getWeight(),
                    vertexCover.stream().mapToDouble(vertexWeights::get).sum(),0);
        }
    }
}