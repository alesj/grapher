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

import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.graph.DependencyTypes;
import org.jboss.grapher.map.GraphComponentMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Dependency type mapper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class DefaultDependencyTypeMapper implements GraphComponentMapper<DependencyType> {
    public void init(Map<String, String> config) {
    }

    public DependencyType mapComponent(HttpServletRequest request) {
        String key = "dependency";
        return mapComponent(key, request.getParameter(key));
    }

    public DependencyType mapComponent(String key, String value) {
        if ("DependsOnMe".equalsIgnoreCase(value))
            return DependencyTypes.DependsOnMe;
        else if ("Unresolved".equalsIgnoreCase(value))
            return DependencyTypes.Unresolved;
        else
            return DependencyTypes.IDependOn;
    }
}
