/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.grapher.mc.graph;

import org.jboss.grapher.graph.DependencyInfo;
import org.jboss.grapher.graph.DependencyItem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * MC context dependency target.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ContextDependencyInfo implements DependencyInfo {

    private org.jboss.dependency.spi.DependencyInfo info;

    public ContextDependencyInfo(org.jboss.dependency.spi.DependencyInfo info) {
        if (info == null)
            throw new IllegalArgumentException("Null info");
        this.info = info;
    }

    protected Set<DependencyItem> toItems(Set<org.jboss.dependency.spi.DependencyItem> items) {
        if (items == null || items.isEmpty())
            return Collections.emptySet();

        Set<DependencyItem> dependencies = new HashSet<DependencyItem>();
        for (org.jboss.dependency.spi.DependencyItem di : items)
            dependencies.add(new ContextDependencyItem(di));
        return dependencies;
    }

    public Set<DependencyItem> getIDependOn() {
        return toItems(info.getIDependOn(null));
    }

    public Set<DependencyItem> getDependsOnMe() {
        return toItems(info.getDependsOnMe(null));
    }

    public Set<DependencyItem> getUnresolved() {
        return toItems(info.getUnresolvedDependencies(null));
    }
}
