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

import org.jboss.logging.Logger;
import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphModel;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Abstract MC components renderer.
 *
 * @param <T> exact dependency name type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractGraphCreator<T> implements GraphCreator<T> {
    protected Logger log = Logger.getLogger(getClass());

    /**
     * The graph component factory
     */
    private static GraphComponentFactory factory = new DefaultGraphComponentFactory();

    public JGraph createGraph(HttpServletRequest request, DependencyType<T> dtype, DependencyFilter<T> filter) {
        Map attributes = new HashMap();
        ConnectionSet cs = new ConnectionSet();
        Object[] cells = createCells(request, dtype, attributes, cs, filter);
        return createGraph(cells, attributes, cs);
    }

    /**
     * Create cells.
     *
     * @param request       the request
     * @param dtype         dependency type
     * @param attributes    the attributes
     * @param cs            the connection set
     * @param filter        the component types filter
     * @return graph cells
     */
    protected Object[] createCells(HttpServletRequest request, DependencyType<T> dtype, Map attributes, ConnectionSet cs, DependencyFilter<T> filter) {
        Map<Object, GraphCell> cells = new HashMap<Object, GraphCell>();
        Set<Object> objects = new HashSet<Object>();
        createCells(request, dtype, cells, objects, attributes, cs, filter);

        // change to array
        Object[] graph = new Object[objects.size()];
        int i = 0;
        for (Object object : objects)
            graph[i++] = object;

        return graph;
    }

    /**
     * Do create cells.
     *
     * @param request    the request
     * @param dtype         dependency type
     * @param cells         the cells
     * @param objects       the graph's objects
     * @param attributes    the graph attributes
     * @param cs            the connection set
     * @param filter        the component types filter
     */
    protected abstract void createCells(HttpServletRequest request, DependencyType<T> dtype, Map<Object, GraphCell> cells, Set<Object> objects, Map attributes, ConnectionSet cs, DependencyFilter<T> filter);

    /**
     * Do we recurse into context.
     *
     * @param target the target to examine
     * @return true if we recurse, false otherwise
     */
    protected boolean doRecurse(DependencyTarget target) {
        return false; // by default we don't recurse
    }

    /**
     * Handle single context.
     *
     * @param request    the request
     * @param dtype         dependency type
     * @param cells         the cells
     * @param objects       the graph's objects
     * @param attributes    the graph attributes
     * @param cs            the connection set
     * @param target        the target to render
     * @param filter        the component types filter
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void handleContext(HttpServletRequest request, DependencyType<T> dtype, Map<Object, GraphCell> cells, Set<Object> objects, Map attributes, ConnectionSet cs, DependencyTarget<T> target, DependencyFilter<T> filter) {
        if (isComponentExcluded(target, filter)) {
            if (log.isTraceEnabled())
                log.trace("Component is excluded [" + filter + "]: " + target);

            return;
        }

        DependencyInfo<T> info = target.getDependencyInfo();
        if (info == null)
            return;

        GraphCell owner = getCell(cells, objects, target);

        Set<DependencyItem<T>> items = dtype.getDependencies(info);
        if (items != null && items.isEmpty() == false) {
            for (DependencyItem<T> item : items) {
                T dependencyId = item.getDependency();
                if (dependencyId != null) {
                    GraphCell dependency = cells.get(dependencyId);
                    DependencyTarget<T> dependant = null;
                    if (dependency == null) {
                        dependant = getDependencyTarget(request, dependencyId);
                        if (dependant != null) {
                            if (isComponentExcluded(dependant, filter)) {
                                if (log.isTraceEnabled())
                                    log.trace("Component is excluded [" + filter + "]: " + dependant);

                                return;
                            }

                            dependency = getCell(cells, objects, dependant);
                        } else {
                            dependency = createCell(dependencyId);
                            cells.put(dependencyId, dependency);
                            objects.add(dependency);
                        }
                    }

                    String state = item.getWhenRequired();
                    Object[] order = dtype.getEdgeOrder(owner, dependency);
                    Edge edge = factory.createEdge(state != null ? state : "UNKNOWN", (GraphCell) order[0], (GraphCell) order[1], cs);
                    objects.add(edge);

                    if (dependant != null && doRecurse(dependant))
                        handleContext(request, dtype, cells, objects, attributes, cs, dependant, filter);
                }
            }
        }
    }

    /**
     * Get real dependency target.
     *
     * @param request the request
     * @param target the dependency name
     * @return the real dependency target
     */
    protected abstract DependencyTarget<T> getDependencyTarget(HttpServletRequest request, T target);

    /**
     * Is the target excluded.
     *
     * @param target       the target
     * @param filter       the excluded types filter
     * @return true if excluded, false otherwise
     */
    protected boolean isComponentExcluded(DependencyTarget<T> target, DependencyFilter<T> filter) {
        return target == null || (filter != null && filter.accepts(target));
    }

    /**
     * Create graph.
     *
     * @param cells      the graph cells
     * @param attributes the graph attributes
     * @param cs         the connection set
     * @return new jgraph instance
     */
    protected JGraph createGraph(Object[] cells, Map attributes, ConnectionSet cs) {
        info(cells);

        GraphModel model = new ModifiedGraphModel();
        //create simple layout
        GraphSettings.layout(cells, attributes);
        // insert cells
        model.insert(cells, attributes, cs, null, null);
        JGraph graph = new JGraph(model);
        applySwingHack(graph);
        return graph;
    }

    /**
     * Get info.
     *
     * @param cells the cells
     */
    protected void info(Object[] cells) {
        int v = 0;
        int e = 0;
        for (Object cell : cells) {
            if (cell instanceof GEdge)
                e++;
            else if (cell instanceof GVertex)
                v++;
        }
        log.info("Vertices: " + v + ", Edges: " + e);

        if (log.isTraceEnabled()) {
            log.trace("Graph objects: " + Arrays.asList(cells));
        }
    }

    /**
     * Apply swing hack.
     *
     * @param graph the graph
     */
    protected void applySwingHack(JGraph graph) {
        JPanel panel = new JPanel();
        panel.setDoubleBuffered(false);// always turn double buffering off when  exporting
        panel.add(graph);
        panel.setVisible(true);
        panel.setEnabled(true);
        panel.addNotify();// workaround to pack() on a JFrame
        panel.validate();
    }

    /**
     * Create the cell.
     *
     * @param label the cell's label
     * @return new cell instance
     */
    protected GraphCell createCell(Object label) {
        return factory.createVertex(label);
    }

    /**
     * Get the cell for the param target.
     *
     * @param cells   the current cells
     * @param objects all of graph's objects
     * @param target the target
     * @return context's cell
     */
    protected GraphCell getCell(Map<Object, GraphCell> cells, Set<Object> objects, DependencyTarget<T> target) {
        Object name = target.getName();
        GraphCell cell = cells.get(name);
        if (cell == null) {
            cell = createCell(name);
            cells.put(name, cell);
            objects.add(cell);
            // fill in aliases as well
            Set<T> aliases = target.getAliases();
            if (aliases != null && aliases.isEmpty() == false) {
                for (Object alias : aliases) {
                    if (cells.containsKey(alias) == false) {
                        cells.put(alias, cell);
                    }
                }
            }
        }

        enhanceCell(cell, target);

        return cell;
    }

    /**
     * Enhance cell rendering.
     *
     * @param cell    the cell to enhance
     * @param target the target to get info from
     */
    protected void enhanceCell(GraphCell cell, DependencyTarget<T> target) {
        factory.enhanceVertex(cell, target);
    }

    private class ModifiedGraphModel extends DefaultGraphModel {
        @SuppressWarnings({"unchecked", "deprecation"})
        protected Map handleAttributes(Map attributes) {
            if (attributes != null) {
                Hashtable undo = new Hashtable();
                for (Object o : attributes.entrySet()) {
                    Map.Entry entry = (Map.Entry) o;
                    Object cell = entry.getKey();
                    Object value = entry.getValue();
                    if (value instanceof Map) {
                        Map deltaNew = (Map) entry.getValue();
                        //System.out.println("deltaNew="+deltaNew);
                        //System.out.println("stateOld="+getAttributes(cell));
                        if (cell instanceof GraphCell) {
                            Map deltaOld = ((GraphCell) cell).changeAttributes(deltaNew);
                            //System.out.println("stateNew=" + getAttributes(cell));
                            //System.out.println("deltaOld=" + deltaOld);
                            undo.put(cell, deltaOld);
                        } else {
                            AttributeMap attr = getAttributes(cell);
                            if (attr != null) {
                                Map deltaOld = attr.applyMap(deltaNew);
                                //System.out.println("stateNew="+getAttributes(cell));
                                //System.out.println("deltaOld="+deltaOld);
                                undo.put(cell, deltaOld);
                            }
                        }
                    }
                }
                return undo;
            }
            return null;
        }
    }
}