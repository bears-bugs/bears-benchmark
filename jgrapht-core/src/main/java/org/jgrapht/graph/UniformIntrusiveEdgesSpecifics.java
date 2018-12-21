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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An uniform weights variant of the intrusive edges specifics.
 * 
 * <p>
 * The implementation optimizes the use of {@link DefaultEdge} and subclasses. For other custom user
 * edge types, a map is used to store vertex source and target.
 * 
 * @author Barak Naveh
 * @author Dimitrios Michail
 *
 * @param <V> the graph vertex type
 * @param <E> the graph edge type
 */
public class UniformIntrusiveEdgesSpecifics<V, E>
    extends
    BaseIntrusiveEdgesSpecifics<V, E, IntrusiveEdge>
    implements
    IntrusiveEdgesSpecifics<V, E>
{
    private static final long serialVersionUID = -5736320893697031114L;

    /**
     * Constructor
     * 
     * @deprecated Since, default strategies should be decided at a higher level.
     */
    @Deprecated
    public UniformIntrusiveEdgesSpecifics()
    {
        this(new LinkedHashMap<>());
    }
    
    /**
     * Constructor
     * 
     * @param map the map to use for storage
     */
    public UniformIntrusiveEdgesSpecifics(Map<E, IntrusiveEdge> map)
    {
        super(map);
    }

    @Override
    public boolean add(E e, V sourceVertex, V targetVertex)
    {
        IntrusiveEdge intrusiveEdge;
        if (e instanceof IntrusiveEdge) {
            intrusiveEdge = (IntrusiveEdge) e;
        } else {
            intrusiveEdge = new IntrusiveEdge();
        }

        intrusiveEdge.source = sourceVertex;
        intrusiveEdge.target = targetVertex;

        return edgeMap.putIfAbsent(e, intrusiveEdge) == null;
    }

    @Override
    protected IntrusiveEdge getIntrusiveEdge(E e)
    {
        if (e instanceof IntrusiveEdge) {
            return (IntrusiveEdge) e;
        }
        return edgeMap.get(e);
    }
}
