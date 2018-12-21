/*
 * (C) Copyright 2016-2018, by Dimitrios Michail and Contributors.
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
package org.jgrapht.alg.interfaces;

import java.util.*;

/**
 * An interface for all algorithms which assign scores to vertices of a graph.
 * 
 * @param <V> the vertex type
 * @param <D> the score type
 * 
 * @author Dimitrios Michail
 */
public interface VertexScoringAlgorithm<V, D>
{

    /**
     * Get a map with the scores of all vertices
     * 
     * @return a map with all scores
     */
    Map<V, D> getScores();

    /**
     * Get a vertex score
     * 
     * @param v the vertex
     * @return the score
     */
    D getVertexScore(V v);

}
