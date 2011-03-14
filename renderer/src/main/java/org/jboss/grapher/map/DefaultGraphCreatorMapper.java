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

import org.jboss.grapher.graph.GraphCreator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Default graph creator mapper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultGraphCreatorMapper implements GraphComponentMapper<GraphCreator> {

    private Map<String, GraphCreator> creators = new HashMap<String, GraphCreator>();

    public DefaultGraphCreatorMapper() {
        ServiceLoader<GraphCreatorExt> services = ServiceLoader.load(GraphCreatorExt.class, getClass().getClassLoader());
        for (GraphCreatorExt ext : services)
            creators.put(ext.extensionName(), ext);
    }

    public void init(Map<String, String> config) {
    }

    public GraphCreator mapComponent(HttpServletRequest request) {
        String ext = request.getParameter("extension");
        String value = request.getParameter("initialValue");
        return mapComponent(ext, value);
    }

    public GraphCreator mapComponent(String key, String value) {
        GraphCreator creator = creators.get(key);
        if (creator != null) {
            creator.initialValue(value);
            return creator;
        }
        return null;
    }
}
