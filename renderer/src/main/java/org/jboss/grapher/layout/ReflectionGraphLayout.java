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
package org.jboss.grapher.layout;

import org.jboss.logging.Logger;
import org.jgraph.JGraph;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Reflection based graph layout due to license limitations.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ReflectionGraphLayout implements GraphLayout {
    private Logger log = Logger.getLogger(getClass());

    private String jgraphLayoutClass;
    private Class<?>[] signature;
    private Object[] parameters;

    private boolean alreadyFailed;

    public ReflectionGraphLayout(String jgraphLayoutClass) {
        this.jgraphLayoutClass = jgraphLayoutClass;
    }

    public ReflectionGraphLayout(String jgraphLayoutClass, Class<?>[] signature, Object[] parameters) {
        this(jgraphLayoutClass);
        this.signature = signature;
        this.parameters = parameters;
    }

    public void applyLayout(JGraph graph) {
        if (alreadyFailed || jgraphLayoutClass == null)
            return;

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            Class<?> layoutClass = loader.loadClass(jgraphLayoutClass);
            Object layout;
            if (signature == null || parameters == null) {
                layout = layoutClass.newInstance();
            } else {
                Constructor layoutCtor = layoutClass.getConstructor(signature);
                layout = layoutCtor.newInstance(parameters);
            }
            Class<?> facadeClass = loader.loadClass("com.jgraph.layout.JGraphFacade");
            Constructor facadeCtor = facadeClass.getConstructor(JGraph.class);
            Object facade = facadeCtor.newInstance(graph);
            Method runLayout = layout.getClass().getMethod("run", facadeClass);
            runLayout.invoke(layout, facade);
            Method createNestedMap = facadeClass.getMethod("createNestedMap", Boolean.TYPE, Boolean.TYPE);
            Map nested = (Map) createNestedMap.invoke(facade, true, true);
            graph.getGraphLayoutCache().edit(nested);
            log.debug("Successfully run layout " + layout + " on " + graph);
        } catch (Exception e) {
            alreadyFailed = true;
            log.info("Missing layout classes [" + jgraphLayoutClass + "]: " + e);
        }
    }
}