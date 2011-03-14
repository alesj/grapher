/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.grapher.analysis;

import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.graph.GVertex;
import org.jboss.util.graph.Edge;
import org.jboss.util.graph.Vertex;
import org.jgraph.JGraph;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Get all disjunct max sub graphs of given graph,
 * and find its potential roots; vertices that have no incoming edges.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SubGraphRenderer extends AbstractHtmlRenderer {
    @Override
    protected void doAnalyse(JGraph graph, DependencyType dtype, OutputStream writer) throws IOException {
        List<Set<Vertex>> groups = new ArrayList<Set<Vertex>>();
        Set<Vertex> visited = new HashSet<Vertex>();

        Object[] cells = graph.getRoots();
        for (Object cell : cells) {
            if (cell instanceof GVertex) {
                GVertex gv = (GVertex) cell;
                Vertex v = gv.getVertex();
                if (visited.contains(v) == false) {
                    Set<Vertex> group = new HashSet<Vertex>();
                    groups.add(group);

                    visitVertex(v, group);

                    visited.addAll(group);
                }
            }
        }

        int i = 1;
        for (Set<Vertex> sv : groups) {
            writer.write(("<ul>Group #" + (i++)).getBytes());
            List<Vertex> roots = new ArrayList<Vertex>();
            for (Vertex v : sv) {
                if (dtype != null && dtype.isRoot(v))
                    roots.add(v);

                writer.write(("<li>" + v + "</li>").getBytes());
            }
            writer.write("</ul>".getBytes());
            if (sv.size() > 1 && roots.isEmpty() == false) {
                writer.write(("<ul>Roots: ").getBytes());
                for (Vertex rv : roots) {
                    writer.write(("<li>" + rv + "</li>").getBytes());
                }
                writer.write("</ul><p/>----------------<p/><p/>".getBytes());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void visitVertex(Vertex current, Set<Vertex> group) {
        if (group.contains(current))
            return;

        group.add(current);

        List<Edge> incoming = current.getIncomingEdges();
        if (incoming != null && incoming.isEmpty() == false) {
            for (Edge e : incoming) {
                Vertex v = e.getFrom();
                visitVertex(v, group);
            }
        }

        List<Edge> outgoing = current.getOutgoingEdges();
        if (outgoing != null && outgoing.isEmpty() == false) {
            for (Edge e : outgoing) {
                Vertex v = e.getTo();
                visitVertex(v, group);
            }
        }
    }
}
