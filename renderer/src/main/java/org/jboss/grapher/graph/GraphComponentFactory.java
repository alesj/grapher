/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.grapher.graph;

import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;

/**
 * Create graph components.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface GraphComponentFactory {
    /**
     * Create vertex.
     *
     * @param label the vertex label
     * @return new graph vertex
     */
    GraphCell createVertex(Object label);

    /**
     * Create vertex.
     *
     * @param target the target
     * @return new graph vertex
     */
    GraphCell createVertex(DependencyTarget target);

    /**
     * Enhance existing vertex.
     *
     * @param vertex  the vertex to enhance
     * @param target  the target
     */
    void enhanceVertex(GraphCell vertex, DependencyTarget target);

    /**
     * Create edge.
     *
     * @param label  the edge label
     * @param target the edge target
     * @param source the edge source
     * @param cs     the connection set
     * @return new edge
     */
    Edge createEdge(Object label, GraphCell target, GraphCell source, ConnectionSet cs);
}
