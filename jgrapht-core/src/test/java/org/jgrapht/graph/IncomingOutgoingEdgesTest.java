/*
 * (C) Copyright 2017-2018, by Dimitrios Michail and Contributors.
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
package org.jgrapht.graph;

import org.jgrapht.*;
import org.junit.*;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.Assert.*;

/**
 * Check Incoming/Outgoing edges in directed and undirected graphs.
 *
 * @author Dimitrios Michail
 */
public class IncomingOutgoingEdgesTest
{

    public static void testDirectedGraph(Supplier<Graph<Integer, DefaultEdge>> graphSupplier)
    {
        Graph<Integer, DefaultEdge> g = graphSupplier.get();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        
        assertEquals(5, g.vertexSet().size());
        assertTrue(g.vertexSet().contains(1));
        assertTrue(g.vertexSet().contains(2));
        assertTrue(g.vertexSet().contains(3));
        assertTrue(g.vertexSet().contains(4));
        assertTrue(g.vertexSet().contains(5));
        
        DefaultEdge e12 = g.addEdge(1, 2);
        DefaultEdge e23_1 = g.addEdge(2, 3);
        DefaultEdge e23_2 = g.addEdge(2, 3);
        DefaultEdge e24 = g.addEdge(2, 4);
        DefaultEdge e44 = g.addEdge(4, 4);
        DefaultEdge e55_1 = g.addEdge(5, 5);
        DefaultEdge e52 = g.addEdge(5, 2);
        DefaultEdge e55_2 = g.addEdge(5, 5);

        assertEquals(1, g.degreeOf(1));
        assertEquals(5, g.degreeOf(2));
        assertEquals(2, g.degreeOf(3));
        assertEquals(3, g.degreeOf(4));
        assertEquals(5, g.degreeOf(5));

        assertEquals(new HashSet<>(Arrays.asList(e12)), g.edgesOf(1));
        assertEquals(new HashSet<>(Arrays.asList(e12, e23_1, e23_2, e24, e52)), g.edgesOf(2));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2)), g.edgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e24, e44)), g.edgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e52, e55_1, e55_2)), g.edgesOf(5));

        assertEquals(0, g.inDegreeOf(1));
        assertEquals(2, g.inDegreeOf(2));
        assertEquals(2, g.inDegreeOf(3));
        assertEquals(2, g.inDegreeOf(4));
        assertEquals(2, g.inDegreeOf(5));

        assertEquals(new HashSet<>(), g.incomingEdgesOf(1));
        assertEquals(new HashSet<>(Arrays.asList(e12, e52)), g.incomingEdgesOf(2));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2)), g.incomingEdgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e24, e44)), g.incomingEdgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e55_1, e55_2)), g.incomingEdgesOf(5));

        assertEquals(1, g.outDegreeOf(1));
        assertEquals(3, g.outDegreeOf(2));
        assertEquals(0, g.outDegreeOf(3));
        assertEquals(1, g.outDegreeOf(4));
        assertEquals(3, g.outDegreeOf(5));

        assertEquals(new HashSet<>(Arrays.asList(e12)), g.outgoingEdgesOf(1));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2, e24)), g.outgoingEdgesOf(2));
        assertEquals(new HashSet<>(), g.outgoingEdgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e44)), g.outgoingEdgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e52, e55_1, e55_2)), g.outgoingEdgesOf(5));
    }

    /**
     * Test the most general version of the directed graph.
     */
    @Test
    public void testDirectedGraph()
    {
        testDirectedGraph(() -> new DirectedPseudograph<>(DefaultEdge.class));
    }

    /**
     * Test the most general version of the undirected graph.
     */
    public static void testUndirectedGraph(Supplier<Graph<Integer, DefaultEdge>> graphSupplier)
    {
        Graph<Integer, DefaultEdge> g = graphSupplier.get();
        g.addVertex(1);
        g.addVertex(2);
        g.addVertex(3);
        g.addVertex(4);
        g.addVertex(5);
        
        assertEquals(5, g.vertexSet().size());
        assertTrue(g.vertexSet().contains(1));
        assertTrue(g.vertexSet().contains(2));
        assertTrue(g.vertexSet().contains(3));
        assertTrue(g.vertexSet().contains(4));
        assertTrue(g.vertexSet().contains(5));
        
        DefaultEdge e12 = g.addEdge(1, 2);
        DefaultEdge e23_1 = g.addEdge(2, 3);
        DefaultEdge e23_2 = g.addEdge(2, 3);
        DefaultEdge e24 = g.addEdge(2, 4);
        DefaultEdge e44 = g.addEdge(4, 4);
        DefaultEdge e55_1 = g.addEdge(5, 5);
        DefaultEdge e52 = g.addEdge(5, 2);
        DefaultEdge e55_2 = g.addEdge(5, 5);

        assertEquals(1, g.degreeOf(1));
        assertEquals(5, g.degreeOf(2));
        assertEquals(2, g.degreeOf(3));
        assertEquals(3, g.degreeOf(4));
        assertEquals(5, g.degreeOf(5));

        assertEquals(new HashSet<>(Arrays.asList(e12)), g.edgesOf(1));
        assertEquals(new HashSet<>(Arrays.asList(e12, e23_1, e23_2, e24, e52)), g.edgesOf(2));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2)), g.edgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e24, e44)), g.edgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e52, e55_1, e55_2)), g.edgesOf(5));

        assertEquals(1, g.inDegreeOf(1));
        assertEquals(5, g.inDegreeOf(2));
        assertEquals(2, g.inDegreeOf(3));
        assertEquals(3, g.inDegreeOf(4));
        assertEquals(5, g.inDegreeOf(5));

        assertEquals(new HashSet<>(Arrays.asList(e12)), g.incomingEdgesOf(1));
        assertEquals(
            new HashSet<>(Arrays.asList(e12, e23_1, e23_2, e24, e52)), g.incomingEdgesOf(2));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2)), g.incomingEdgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e24, e44)), g.incomingEdgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e52, e55_1, e55_2)), g.incomingEdgesOf(5));

        assertEquals(1, g.outDegreeOf(1));
        assertEquals(5, g.outDegreeOf(2));
        assertEquals(2, g.outDegreeOf(3));
        assertEquals(3, g.outDegreeOf(4));
        assertEquals(5, g.outDegreeOf(5));

        assertEquals(new HashSet<>(Arrays.asList(e12)), g.outgoingEdgesOf(1));
        assertEquals(
            new HashSet<>(Arrays.asList(e12, e23_1, e23_2, e24, e52)), g.outgoingEdgesOf(2));
        assertEquals(new HashSet<>(Arrays.asList(e23_1, e23_2)), g.outgoingEdgesOf(3));
        assertEquals(new HashSet<>(Arrays.asList(e24, e44)), g.outgoingEdgesOf(4));
        assertEquals(new HashSet<>(Arrays.asList(e52, e55_1, e55_2)), g.outgoingEdgesOf(5));
    }

    /**
     * Test the most general version of the undirected graph.
     */
    @Test
    public void testUndirectedGraph()
    {
        testUndirectedGraph(() -> new Pseudograph<>(DefaultEdge.class));
    }

}
