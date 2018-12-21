/*
 * (C) Copyright 2003-2018, by Barak Naveh and Contributors.
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

import org.jgrapht.graph.builder.*;
import org.jgrapht.util.*;

import java.util.function.*;

/**
 * A directed weighted pseudograph. A directed weighted pseudograph is a non-simple directed graph
 * in which both graph loops and multiple (parallel) edges are permitted, and edges have weights.
 * 
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 * 
 */
public class DirectedWeightedPseudograph<V, E>
    extends
    DirectedPseudograph<V, E>
{
    private static final long serialVersionUID = -4775269773843490859L;

    /**
     * Creates a new weighted graph.
     *
     * @param edgeClass class on which to base the edge supplier
     */
    public DirectedWeightedPseudograph(Class<? extends E> edgeClass)
    {
        this(null, SupplierUtil.createSupplier(edgeClass));
    }

    /**
     * Creates a new weighted graph.
     * 
     * @param vertexSupplier the vertex supplier, can be null
     * @param edgeSupplier the edge supplier, can be null
     */
    public DirectedWeightedPseudograph(Supplier<V> vertexSupplier, Supplier<E> edgeSupplier)
    {
        super(vertexSupplier, edgeSupplier, true);
    }

    /**
     * Create a builder for this kind of graph.
     * 
     * @param edgeClass class on which to base factory for edges
     * @param <V> the graph vertex type
     * @param <E> the graph edge type
     * @return a builder for this kind of graph
     */
    public static <V,
        E> GraphBuilder<V, E, ? extends DirectedWeightedPseudograph<V, E>> createBuilder(
            Class<? extends E> edgeClass)
    {
        return new GraphBuilder<>(new DirectedWeightedPseudograph<>(edgeClass));
    }

    /**
     * Create a builder for this kind of graph.
     * 
     * @param edgeSupplier the edge supplier
     * @param <V> the graph vertex type
     * @param <E> the graph edge type
     * @return a builder for this kind of graph
     */
    public static <V,
        E> GraphBuilder<V, E, ? extends DirectedWeightedPseudograph<V, E>> createBuilder(
            Supplier<E> edgeSupplier)
    {
        return new GraphBuilder<>(new DirectedWeightedPseudograph<>(null, edgeSupplier));
    }

}

