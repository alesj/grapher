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
package org.jboss.grapher;

import org.jboss.grapher.graph.DependencyFilter;
import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.graph.GraphCreator;
import org.jboss.grapher.layout.GraphLayout;
import org.jboss.grapher.map.DefaultDependencyFilterMapper;
import org.jboss.grapher.map.DefaultDependencyTypeMapper;
import org.jboss.grapher.map.DefaultGraphCreatorMapper;
import org.jboss.grapher.map.DefaultGraphLayoutMapper;
import org.jboss.grapher.map.DefaultRendererMapper;
import org.jboss.grapher.map.GraphComponentMapper;
import org.jboss.grapher.render.Renderer;

/**
 * Grapher constants.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class GrapherConstants {
    private GrapherConstants() {
    }

    /**
     * The default format
     */
    public static final String FORMAT = "gif";

    /**
     * The default creator mapper
     */
    public static final GraphComponentMapper<GraphCreator> CREATOR_MAPPER = new DefaultGraphCreatorMapper();

    /**
     * The default dependency type mapper
     */
    public static final GraphComponentMapper<DependencyType> DEPENDENCY_TYPE_MAPPER = new DefaultDependencyTypeMapper();

    /**
     * The default dependency filter mapper
     */
    public static final GraphComponentMapper<DependencyFilter> DEPENDENCY_FILTER_MAPPER = new DefaultDependencyFilterMapper();

    /**
     * The default layout mapper
     */
    public static final GraphComponentMapper<GraphLayout> LAYOUT_MAPPER = new DefaultGraphLayoutMapper();

    /**
     * The default renderer mapper
     */
    public static final GraphComponentMapper<Renderer> RENDERER_MAPPER = new DefaultRendererMapper();
}