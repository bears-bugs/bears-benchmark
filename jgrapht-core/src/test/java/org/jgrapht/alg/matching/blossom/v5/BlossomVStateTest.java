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
package org.jgrapht.alg.matching.blossom.v5;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import static org.jgrapht.alg.matching.blossom.v5.BlossomVOptions.InitializationType.NONE;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BlossomVState}
 *
 * @author Timofey Chudakov
 */
public class BlossomVStateTest {

    @Test
    public void testAddTreeEdge() {
        BlossomVTree tree1 = new BlossomVTree(new BlossomVNode(-1)); // positions doesn't matter here
        BlossomVTree tree2 = new BlossomVTree(new BlossomVNode(-1));
        BlossomVTreeEdge treeEdge = BlossomVTree.addTreeEdge(tree1, tree2);
        int currentDir = tree2.currentDirection;
        assertEquals(tree2, treeEdge.head[currentDir]);
        assertEquals(tree1, treeEdge.head[1 - currentDir]);
    }

    @Test
    public void testMoveEdge() {
        Graph<Integer, DefaultWeightedEdge> graph = new DefaultUndirectedWeightedGraph<>(DefaultWeightedEdge.class);
        DefaultWeightedEdge e12 = Graphs.addEdgeWithVertices(graph, 1, 2, 0);
        DefaultWeightedEdge e13 = Graphs.addEdgeWithVertices(graph, 1, 3, 0);
        DefaultWeightedEdge e23 = Graphs.addEdgeWithVertices(graph, 2, 3, 0);

        BlossomVInitializer<Integer, DefaultWeightedEdge> initializer = new BlossomVInitializer<>(graph);
        BlossomVState<Integer, DefaultWeightedEdge> state = initializer.initialize(new BlossomVOptions(NONE));
        Map<Integer, BlossomVNode> vertexMap = BlossomVDebugger.getVertexMap(state);
        Map<DefaultWeightedEdge, BlossomVEdge> edgeMap = BlossomVDebugger.getEdgeMap(state);

        BlossomVNode node1 = vertexMap.get(1);
        BlossomVNode node2 = vertexMap.get(2);
        BlossomVNode node3 = vertexMap.get(3);

        BlossomVEdge edge12 = edgeMap.get(e12);
        BlossomVEdge edge13 = edgeMap.get(e13);
        BlossomVEdge edge23 = edgeMap.get(e23);

        edge12.moveEdgeTail(node2, node3);
        assertEquals(node3, edge12.getOpposite(node1));
        assertEquals(new HashSet<>(Arrays.asList(edge12, edge13)), BlossomVDebugger.getEdgesOf(node1));
        assertEquals(new HashSet<>(Collections.singletonList(edge23)), BlossomVDebugger.getEdgesOf(node2));
        assertEquals(new HashSet<>(Arrays.asList(edge12, edge13, edge23)), BlossomVDebugger.getEdgesOf(node3));

        edge23.moveEdgeTail(node2, node1);
        assertEquals(node1, edge13.getOpposite(node3));
        assertEquals(new HashSet<>(Arrays.asList(edge12, edge13, edge23)), BlossomVDebugger.getEdgesOf(node1));
        assertEquals(new HashSet<>(), BlossomVDebugger.getEdgesOf(node2));
        assertEquals(new HashSet<>(Arrays.asList(edge12, edge13, edge23)), BlossomVDebugger.getEdgesOf(node3));
    }

}
