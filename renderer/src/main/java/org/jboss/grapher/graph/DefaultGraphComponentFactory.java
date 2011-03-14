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

import org.jboss.grapher.map.ComponentTypes;
import org.jboss.grapher.routing.ParallelSplineRouter;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;

import java.awt.*;

/**
 * Create graph components.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultGraphComponentFactory implements GraphComponentFactory {
    /**
     * The vertex color
     */
    protected Color COLOR = Color.BLACK;

    /**
     * The edge routing
     */
    protected Edge.Routing routing;

    public DefaultGraphComponentFactory() {
        routing = new ParallelSplineRouter();
    }

    /**
     * Create vertex.
     *
     * @param label the vertex label
     * @param color the vertex color
     * @return new vertex
     */
    protected GraphCell createVertex(Object label, Color color) {
        DefaultGraphCell cell = new ColorableGraphCell(label, color);
        cell.addPort();
        return cell;
    }

    public GraphCell createVertex(Object label) {
        return createVertex(label, COLOR);
    }

    public GraphCell createVertex(DependencyTarget target) {
        Color color = mapContextToColor(target);
        return createVertex(target.getName(), color);
    }

    /**
     * Map context to color.
     *
     * @param target the target
     * @return mapped color
     */
    protected Color mapContextToColor(DependencyTarget target) {
        return ComponentTypes.mapColor(target);
    }

    public void enhanceVertex(GraphCell vertex, DependencyTarget target) {
        Colorable colorable = Colorable.class.cast(vertex);
        if (colorable.getColor() == COLOR)
            colorable.setColor(mapContextToColor(target));
    }

    public Edge createEdge(Object label, GraphCell target, GraphCell source, ConnectionSet cs) {
        GrapherEdge edge = new GrapherEdge(label);
        ColorableGraphCell from = (ColorableGraphCell) target;
        ColorableGraphCell to = (ColorableGraphCell) source;
        cs.connect(edge, from.getChildAt(0), to.getChildAt(0));
        edge.setEdge(from.getVertex(), to.getVertex());

        int arrow = GraphConstants.ARROW_CLASSIC;
        GraphConstants.setLineEnd(edge.getAttributes(), arrow);
        GraphConstants.setEndFill(edge.getAttributes(), true);
        GraphConstants.setRouting(edge.getAttributes(), getRouting());

        return edge;
    }

    protected Edge.Routing getRouting() {
        return routing;
    }

    public void setRouting(Edge.Routing routing) {
        this.routing = routing;
    }
}