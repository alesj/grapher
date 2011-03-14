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

import org.jgraph.JGraph;

import javax.servlet.http.HttpServletRequest;

/**
 * Graph creator.
 * <p/>
 * It knows how to create JGraph cells.
 *
 * @param <T> exact dependency name
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public interface GraphCreator<T> {
    /**
     * Initial value.
     *
     * @param value the string value
     */
    void initialValue(String value);

    /**
     * Create graph.
     *
     * @param request the request
     * @param dtype   the dependency type
     * @param filter  the dependency item filter
     * @return new jgraph instance
     */
    JGraph createGraph(HttpServletRequest request, DependencyType<T> dtype, DependencyFilter<T> filter);
}
