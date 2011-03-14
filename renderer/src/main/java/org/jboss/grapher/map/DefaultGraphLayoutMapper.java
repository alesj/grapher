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
package org.jboss.grapher.map;

import org.jboss.grapher.layout.DefaultGraphLayout;
import org.jboss.grapher.layout.GraphLayout;
import org.jboss.grapher.layout.NoopGraphLayout;
import org.jboss.grapher.layout.ReflectionGraphLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Map simple name to full layout class name.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class DefaultGraphLayoutMapper extends AbstractGraphComponentMapper<GraphLayout> {
    /**
     * The default layout
     */
    public static final GraphLayout LAYOUT = new DefaultGraphLayout();

    /**
     * The noop layout
     */
    public static final GraphLayout NOOP = new NoopGraphLayout();

    private static final Map<String, String> map = new HashMap<String, String>();

    static {
        // a ctor hack
        map.put("simple0", "com.jgraph.layout.graph.JGraphSimpleLayout");
        map.put("simple1", "com.jgraph.layout.graph.JGraphSimpleLayout");
        map.put("simple2", "com.jgraph.layout.graph.JGraphSimpleLayout");

        map.put("tree", "com.jgraph.layout.tree.JGraphTreeLayout");
        map.put("spring", "com.jgraph.layout.graph.JGraphSpringLayout");
        map.put("hierarchical", "com.jgraph.layout.hierarchical.JGrapHierarchicalhLayout");
        map.put("compound", "com.jgraph.layout.JGraphCompoundLayout");
        map.put("compact", "com.jgraph.layout.tree.JGraphCompactTreeLayout");
        map.put("moen", "com.jgraph.layout.tree.JGraphMoenLayout");
        map.put("radial", "com.jgraph.layout.graph.JGraphRadialTreeLayout");
        map.put("annealing", "com.jgraph.layout.graph.JGraphAnnealingLayout");
        map.put("fr", "com.jgraph.layout.graph.JGraphFRLayout");
        map.put("organic", "com.jgraph.layout.organic.JGraphOrganicLayout");
        map.put("self", "com.jgraph.layout.organic.JGraphSelfOrganizingOrganicLayout");
        map.put("isom", "com.jgraph.layout.graph.JGraphISOMLayout");
        map.put("org", "com.jgraph.layout.tree.OrganizationalChart");
        map.put("grid", "com.jgraph.layout.simpl.SimpleGridLayout");
    }

    protected String getKey() {
        return "layout";
    }

    @Override
    protected boolean skipNull() {
        return false;
    }

    public GraphLayout mapComponent(String key, String value) {
        return getLayout(value);
    }

    /**
     * Get layout.
     *
     * @param key the layout key
     * @return the layout
     */
    static GraphLayout getLayout(String key) {
        if (key == null)
            return LAYOUT;

        if ("noop".equalsIgnoreCase(key))
            return NOOP;

        String className = map.get(key);
        if (className == null)
            return LAYOUT;

        // treat simple differently
        if (key.startsWith("simple")) {
            int arg = Integer.parseInt(key.substring("simple".length()));
            return new ReflectionGraphLayout(className, new Class<?>[]{int.class}, new Object[]{arg});
        } else {
            return new ReflectionGraphLayout(className);
        }
    }
}
