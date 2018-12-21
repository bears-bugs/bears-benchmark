/*
 * (C) Copyright 2018-2018, by Dimitrios Michail and Contributors.
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
package org.jgrapht.opt.graph.fastutil;

import org.jgrapht.graph.DefaultGraphType;
import org.jgrapht.graph.IncomingOutgoingEdgesTest;
import org.jgrapht.opt.graph.fastutil.FastutilMapIntVertexGraph;
import org.jgrapht.util.SupplierUtil;
import org.junit.Test;

/**
 * Tests for {@link FastutilMapIntVertexGraph}.
 * 
 * @author Dimitrios Michail
 */
public class FastUtilMapIntVertexGraphTest
{
    /**
     * Test in-out edges of directed graph
     */
    @Test
    public void testDirectedGraph()
    {
        IncomingOutgoingEdgesTest.testDirectedGraph(
            () -> new FastutilMapIntVertexGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.directedPseudograph()));
    }

    /**
     * Test in-out edges of undirected graph
     */
    @Test
    public void testUndirectedGraph()
    {
        IncomingOutgoingEdgesTest.testUndirectedGraph(
            () -> new FastutilMapIntVertexGraph<>(
                SupplierUtil.createIntegerSupplier(), SupplierUtil.createDefaultEdgeSupplier(),
                DefaultGraphType.pseudograph()));
    }

}
