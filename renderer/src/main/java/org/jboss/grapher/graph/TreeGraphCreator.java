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
import org.jgraph.graph.GraphCell;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Render single bean.
 *
 * @param <T> exact dependency name
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class TreeGraphCreator<T> extends AbstractGraphCreator<T> {
    private T root;
    private Set<T> visited;

    public TreeGraphCreator() {
        visited = new HashSet<T>();
    }

    public void initialValue(final String value) {
        root = toName(value);
        log.info("Root value: " + value);
    }

    protected abstract T toName(String value);

    @Override
    protected void createCells(HttpServletRequest request, DependencyType<T> dtype, Map<Object, GraphCell> cells, Set<Object> objects, Map attributes, ConnectionSet cs, DependencyFilter<T> filter) {
        DependencyTarget<T> context = getDependencyTarget(request, root);
        if (context == null)
            throw new IllegalArgumentException("No such target: " + root);

        handleContext(request, dtype, cells, objects, attributes, cs, context, filter);
    }

    @Override
    protected boolean doRecurse(DependencyTarget<T> target) {
        T name = target.getName();
        return visited.add(name);
    }

    @Override
    public String toString() {
        return super.toString() + "[root=" + root + "]";
    }
}