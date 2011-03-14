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

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewFactory;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

/**
 * A Graph Layout Cache that automatically attaches itself as a listener
 * of its associated graph model. The intend usage is for applications
 * that do not create a JGraph instance. Without a JGraph instance there
 * is no UI (in JGraph 5.x) and it is the UI that causes the layout cache
 * to automatically listen to the model.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DataGraphLayoutCache extends GraphLayoutCache implements GraphModelListener {
    /**
     * Constructs a graph layout cache.
     */
    public DataGraphLayoutCache() {
        this(new DefaultGraphModel(), new DefaultCellViewFactory());
    }

    /**
     * Constructs a view for the specified model that uses <code>factory</code>
     * to create its views.
     *
     * @param model   the model that constitues the data source
     * @param factory the cell view factory
     */
    public DataGraphLayoutCache(GraphModel model, CellViewFactory factory) {
        this(model, factory, false);
    }

    /**
     * Constructs a view for the specified model that uses <code>factory</code>
     * to create its views.
     *
     * @param model   the model that constitues the data source
     * @param factory the cell view factory
     * @param partial the partial flag
     */
    public DataGraphLayoutCache(GraphModel model, CellViewFactory factory, boolean partial) {
        this(model, factory, null, null, partial);
    }

    /**
     * Constructs a view for the specified model that uses <code>factory</code>
     * to create its views.
     *
     * @param model           the model that constitues the data source
     * @param factory         the cell view factory
     * @param cellViews       the cell views
     * @param hiddenCellViews the hidden cell views
     * @param partial         the partial flag
     */
    public DataGraphLayoutCache(GraphModel model, CellViewFactory factory, CellView[] cellViews, CellView[] hiddenCellViews, boolean partial) {
        super(model, factory, cellViews, hiddenCellViews, partial);
        if (this.graphModel != null) {
            graphModel.addGraphModelListener(this);
        }
    }

    public void graphChanged(GraphModelEvent e) {
        graphChanged(e.getChange());
    }
}