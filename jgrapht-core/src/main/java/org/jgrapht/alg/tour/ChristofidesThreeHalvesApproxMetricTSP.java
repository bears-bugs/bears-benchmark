/*
 * (C) Copyright 2018-2018, by Timofey Chudakov and Contributors.
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
package org.jgrapht.alg.tour;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.GraphTests;
import org.jgrapht.alg.cycle.HierholzerEulerianCycle;
import org.jgrapht.alg.interfaces.EulerianCycleAlgorithm;
import org.jgrapht.alg.interfaces.HamiltonianCycleAlgorithm;
import org.jgrapht.alg.interfaces.MatchingAlgorithm;
import org.jgrapht.alg.interfaces.SpanningTreeAlgorithm;
import org.jgrapht.alg.matching.blossom.v5.KolmogorovMinimumWeightPerfectMatching;
import org.jgrapht.alg.spanning.KruskalMinimumSpanningTree;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.Pseudograph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A $3/2$-approximation algorithm for the metric TSP problem.
 * <p>
 * The <a href="https://en.wikipedia.org/wiki/Travelling_salesman_problem">travelling salesman problem</a>
 * (TSP) asks the following question: "Given a list of cities and the distances between each pair of cities,
 * what is the shortest possible route that visits each city exactly once and returns to the origin city?".
 * In the metric TSP, the intercity distances satisfy the triangle inequality.
 * <p>
 * This is an implementation of the <a href="https://en.wikipedia.org/wiki/Christofides_algorithm">
 * Christofides algorithm</a>. The algorithms is a $3/2$-approximation assuming that the input graph
 * satisfies triangle inequality and all edge weights are nonnegative. The implementation requires the input
 * graph to be <i>undirected</i> and <i>complete</i>. The worst case running time complexity is $\mathcal{O}(V^3E)$.
 * <p>
 * The algorithm performs following steps to compute the resulting tour:
 * <ol>
 * <li>Compute a minimum spanning tree in the input graph.</li>
 * <li>Find vertices with odd degree in the MST.</li>
 * <li>Compute minimum weight perfect matching in the induced subgraph on odd degree vertices.</li>
 * <li>Add edges from the minimum weight perfect matching to the MST (forming a pseudograph).</li>
 * <li>Compute an Eulerian cycle in the obtained pseudograph and form a closed tour in this cycle.</li>
 * </ol>
 * <p>
 * The following two observations yield the $3/2$ approximation bound:
 * <ul>
 * <li>The cost of every minimum spanning tree is less than or equal to the cost of every Hamiltonian cycle
 * since after one edge removal every Hamiltonian cycle becomes a spanning tree</li>
 * <li>Twice the cost of a perfect matching in a graph is less than or equal to the cost of every Hamiltonian
 * cycle. This follows from the fact that after forming a closed tour using the edges of a perfect matching
 * the cost of the edges not from the matching is greater than or equal to the cost of the matching edges.</li>
 * </ul>
 * <p>
 * For more details, see <i>Christofides, N.: Worst-case analysis of a new heuristic for the travelling salesman
 * problem. Graduate School of Industrial Administration, Carnegie Mellon University (1976).</i>
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 * @author Timofey Chudakov
 * @author Dimitrios Michail
 */
public class ChristofidesThreeHalvesApproxMetricTSP<V, E> implements HamiltonianCycleAlgorithm<V, E> {

    /**
     * Empty constructor
     */
    public ChristofidesThreeHalvesApproxMetricTSP() {
    }

    /**
     * Computes a $3/2$-approximate tour.
     *
     * @param graph the input graph
     * @return a tour
     * @throws IllegalArgumentException if the graph is not undirected
     * @throws IllegalArgumentException if the graph is not complete
     * @throws IllegalArgumentException if the graph contains no vertices
     */
    @Override
    public GraphPath<V, E> getTour(Graph<V, E> graph) {
        if (!graph.getType().isUndirected()) {
            throw new IllegalArgumentException("Graph must be undirected");
        }
        if (!GraphTests.isComplete(graph)) {
            throw new IllegalArgumentException("Graph must be complete");
        }
        if (graph.vertexSet().isEmpty()) {
            throw new IllegalArgumentException("Graph contains no vertices");
        }

        /*
         * Special case singleton vertex
         */
        if (graph.vertexSet().size() == 1) {
            V start = graph.vertexSet().iterator().next();
            return new GraphWalk<>(
                    graph, start, start, Collections.singletonList(start), Collections.emptyList(), 0d);
        }

        int n = graph.vertexSet().size();
        // add all vertices of the graph to the auxiliary graph
        Graph<V, DefaultEdge> mstAndMatching = new Pseudograph<>(DefaultEdge.class);
        graph.vertexSet().forEach(mstAndMatching::addVertex);

        // add all edges of a minimum spanning tree to the auxiliary graph
        SpanningTreeAlgorithm<E> spanningTreeAlgorithm = new KruskalMinimumSpanningTree<>(graph);
        spanningTreeAlgorithm.getSpanningTree().getEdges().forEach(
                e -> mstAndMatching.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e)));

        // find odd degree vertices
        Set<V> oddDegreeVertices = mstAndMatching.vertexSet().stream()
                .filter(v -> (mstAndMatching.edgesOf(v).size() & 1) == 1).collect(Collectors.toSet());

        /*
         * Form an induced subgraph on odd degree vertices, find minimum weight perfect
         * matching and add its edges to the auxiliary graph
         */
        Graph<V, E> subgraph = new AsSubgraph<>(graph, oddDegreeVertices);
        MatchingAlgorithm<V, E> matchingAlgorithm = new KolmogorovMinimumWeightPerfectMatching<>(subgraph);
        matchingAlgorithm.getMatching().getEdges().forEach(e -> mstAndMatching.addEdge(graph.getEdgeSource(e), graph.getEdgeTarget(e)));

        // find an Eulerian cycle in the auxiliary graph
        EulerianCycleAlgorithm<V, DefaultEdge> eulerianCycleAlgorithm = new HierholzerEulerianCycle<>();
        GraphPath<V, DefaultEdge> eulerianCycle = eulerianCycleAlgorithm.getEulerianCycle(mstAndMatching);

        // form a closed tour from the Hamiltonian cycle
        Set<V> visited = new HashSet<>(n);
        List<V> tourVertices = eulerianCycle.getVertexList().stream().filter(visited::add).collect(Collectors.toList());
        tourVertices.add(tourVertices.get(0));

        // compute tour edges
        List<E> tourEdges = new ArrayList<>(n);
        double tourWeight = 0;
        V prev;
        V next = tourVertices.get(0);
        for (int i = 1; i <= n; i++) {
            prev = next;
            next = tourVertices.get(i);
            E edge = graph.getEdge(prev, next);
            tourEdges.add(edge);
            tourWeight += graph.getEdgeWeight(edge);
        }

        return new GraphWalk<>(graph, tourVertices.get(0), tourVertices.get(0), tourVertices, tourEdges, tourWeight);
    }
}
