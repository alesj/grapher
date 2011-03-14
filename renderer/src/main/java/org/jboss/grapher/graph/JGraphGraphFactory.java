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

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;

import java.util.Hashtable;
import java.util.Map;

/**
 * A helper class that creates graphs. Currently supports tree graphs and a
 * random graph where all edges are connected at least once
 *
 * @author Gaudenz Alder
 * @author David Benson
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class JGraphGraphFactory {
    /**
     * Variant of the insert method that allows to pass a default connection set
     * and parent map and nested map.
     *
     * @param model  the graph model
     * @param cells  the graph cells
     * @param nested the graph attributes
     * @param cs     the connection set
     * @param pm     the parent map
     */
    @SuppressWarnings("unchecked")
    public static void insert(GraphModel model, Object[] cells, Map nested, ConnectionSet cs, ParentMap pm) {
        if (cells != null) {
            if (nested == null)
                nested = new Hashtable();
            if (cs == null)
                cs = new ConnectionSet();
            if (pm == null)
                pm = new ParentMap();

            for (Object cell : cells) {
                // Using the children of the vertex we construct the parent map.
                int childCount = model.getChildCount(cell);
                for (int j = 0; j < childCount; j++) {
                    Object child = model.getChild(cell, j);
                    pm.addEntry(child, cell);

                    // And add their attributes to the nested map
                    AttributeMap attrs = model.getAttributes(child);
                    if (attrs != null)
                        nested.put(child, attrs);
                }

                // A nested map with the vertex as key
                // and its attributes as the value
                // is required for the model.
                Map attrsTmp = (Map) nested.get(cell);
                Map attrs = model.getAttributes(cell);
                if (attrsTmp != null)
                    attrs.putAll(attrsTmp);
                nested.put(cell, attrs);

                // Check if we have parameters for a connection set.
                Object sourcePort = model.getSource(cell);
                if (sourcePort != null)
                    cs.connect(cell, sourcePort, true);

                Object targetPort = model.getTarget(cell);
                if (targetPort != null)
                    cs.connect(cell, targetPort, false);
            }
            // Create an array with the parent and its children.
            cells = DefaultGraphModel.getDescendants(model, cells).toArray();

            // Finally call the insert method on the parent class.
            model.insert(cells, nested, cs, pm, null);
        }
    }
}
