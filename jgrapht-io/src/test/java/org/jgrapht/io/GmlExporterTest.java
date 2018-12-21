/*
 * (C) Copyright 2006-2017, by John V Sichi, Dimitrios Michail and Contributors.
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
package org.jgrapht.io;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.junit.*;

import java.io.*;

import static org.junit.Assert.*;

/**
 * @author John V. Sichi
 * @author Dimitrios Michail
 */
public class GmlExporterTest
{
    // ~ Static fields/initializers
    // ---------------------------------------------

    private static final String V1 = "v1";
    private static final String V2 = "v2";
    private static final String V3 = "v3";
    private static final String V4 = "v4";
    private static final String V5 = "v5";

    private static final String NL = System.getProperty("line.separator");

    // @formatter:off
    private static final String UNDIRECTED =
            "Creator \"JGraphT GML Exporter\"" + NL
            + "Version 1" + NL
            + "graph" + NL
            + "[" + NL
            + "\tlabel \"\"" + NL
            + "\tdirected 0" + NL
            + "\tnode" + NL
            + "\t[" + NL
            + "\t\tid 1" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL
            + "\t\tid 2" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL
            + "\t\tid 3" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL
            + "\t\tid 1" + NL
            + "\t\tsource 1" + NL
            + "\t\ttarget 2" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL
            + "\t\tid 2" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 1" + NL
            + "\t]" + NL
            + "]" + NL;
    
    private static final String UNDIRECTED_WEIGHTED
            = "Creator \"JGraphT GML Exporter\"" + NL
            + "Version 1" + NL
            + "graph" + NL
            + "[" + NL
            + "\tlabel \"\"" + NL
            + "\tdirected 0" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 3" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t\tsource 1" + NL
            + "\t\ttarget 2" + NL
            + "\t\tweight 2.0" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 1" + NL
            + "\t\tweight 5.0" + NL
            + "\t]" + NL
            + "]" + NL;
    
    private static final String UNDIRECTED_WEIGHTED_WITH_EDGE_LABELS
            = "Creator \"JGraphT GML Exporter\"" + NL
            + "Version 1" + NL
            + "graph" + NL
            + "[" + NL
            + "\tlabel \"\"" + NL
            + "\tdirected 0" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 3" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t\tsource 1" + NL
            + "\t\ttarget 2" + NL
            + "\t\tlabel \"(v1 : v2)\"" + NL
            + "\t\tweight 2.0" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 1" + NL
            + "\t\tlabel \"(v3 : v1)\"" + NL            
            + "\t\tweight 5.0" + NL
            + "\t]" + NL
            + "]" + NL;
    
    private static final String UNDIRECTED_WITH_VERTEX_LABELS
            = "Creator \"JGraphT GML Exporter\"" + NL
            + "Version 1" + NL
            + "graph" + NL
            + "[" + NL
            + "\tlabel \"\"" + NL
            + "\tdirected 0" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t\tlabel \"v1\"" + NL            
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t\tlabel \"v2\"" + NL            
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 3" + NL
            + "\t\tlabel \"v3\"" + NL            
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t\tsource 1" + NL
            + "\t\ttarget 2" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 1" + NL
            + "\t]" + NL
            + "]" + NL;
    
    private static final String DIRECTED
            = "Creator \"JGraphT GML Exporter\"" + NL
            + "Version 1" + NL
            + "graph" + NL
            + "[" + NL            
            + "\tlabel \"\"" + NL
            + "\tdirected 1" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 3" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 4" + NL
            + "\t]" + NL
            + "\tnode" + NL
            + "\t[" + NL            
            + "\t\tid 5" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 1" + NL
            + "\t\tsource 1" + NL
            + "\t\ttarget 2" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 2" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 1" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 3" + NL
            + "\t\tsource 2" + NL
            + "\t\ttarget 3" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 4" + NL
            + "\t\tsource 3" + NL
            + "\t\ttarget 4" + NL
            + "\t]" + NL
            + "\tedge" + NL
            + "\t[" + NL            
            + "\t\tid 5" + NL
            + "\t\tsource 4" + NL
            + "\t\ttarget 5" + NL
            + "\t]" + NL
            + "]" + NL;
    // @formatter:on

    // ~ Methods
    // ----------------------------------------------------------------

    @Test
    public void testUndirected()
        throws UnsupportedEncodingException,
        ExportException
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);

        GmlExporter<String, DefaultEdge> exporter = new GmlExporter<String, DefaultEdge>();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED, res);
    }

    @Test
    public void testUnweightedUndirected()
        throws UnsupportedEncodingException,
        ExportException
    {
        Graph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);

        GmlExporter<String, DefaultEdge> exporter = new GmlExporter<>();
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED, res);
    }

    @Test
    public void testDirected()
        throws UnsupportedEncodingException,
        ExportException
    {
        Graph<String, DefaultEdge> g = new SimpleDirectedGraph<>(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        g.addVertex(V4);
        g.addVertex(V5);
        g.addEdge(V1, V2);
        g.addEdge(V3, V1);
        g.addEdge(V2, V3);
        g.addEdge(V3, V4);
        g.addEdge(V4, V5);

        GmlExporter<String, DefaultEdge> exporter = new GmlExporter<>();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(DIRECTED, res);
    }

    @Test
    public void testWeightedUndirected()
        throws UnsupportedEncodingException,
        ExportException
    {
        SimpleGraph<String, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        DefaultWeightedEdge e1 = g.addEdge(V1, V2);
        g.setEdgeWeight(e1, 2.0);
        DefaultWeightedEdge e2 = g.addEdge(V3, V1);
        g.setEdgeWeight(e2, 5.0);

        GmlExporter<String, DefaultWeightedEdge> exporter = new GmlExporter<>();
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED_WEIGHTED, res);
    }

    @Test
    public void testWeightedUndirectedWithEdgeLabels()
        throws UnsupportedEncodingException,
        ExportException
    {
        SimpleGraph<String, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        DefaultWeightedEdge e1 = g.addEdge(V1, V2);
        g.setEdgeWeight(e1, 2.0);
        DefaultWeightedEdge e2 = g.addEdge(V3, V1);
        g.setEdgeWeight(e2, 5.0);

        GmlExporter<String, DefaultWeightedEdge> exporter = new GmlExporter<>();
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED_WEIGHTED_WITH_EDGE_LABELS, res);
    }

    @Test
    public void testUndirectedWithVertexLabels()
        throws UnsupportedEncodingException,
        ExportException
    {
        SimpleGraph<String, DefaultWeightedEdge> g =
            new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addVertex(V3);
        DefaultWeightedEdge e1 = g.addEdge(V1, V2);
        g.setEdgeWeight(e1, 2.0);
        DefaultWeightedEdge e2 = g.addEdge(V3, V1);
        g.setEdgeWeight(e2, 5.0);

        GmlExporter<String, DefaultWeightedEdge> exporter = new GmlExporter<>();
        exporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, true);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exporter.exportGraph(g, os);
        String res = new String(os.toByteArray(), "UTF-8");
        assertEquals(UNDIRECTED_WITH_VERTEX_LABELS, res);
    }

    @Test
    public void testParameters()
    {
        GmlExporter<String, DefaultWeightedEdge> exporter =
            new GmlExporter<String, DefaultWeightedEdge>();
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS));
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS));
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, true);
        assertTrue(exporter.isParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS, false);
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_VERTEX_LABELS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS, true);
        assertTrue(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS, false);
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_LABELS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, true);
        assertTrue(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS));
        exporter.setParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS, false);
        assertFalse(exporter.isParameter(GmlExporter.Parameter.EXPORT_EDGE_WEIGHTS));
    }

}

