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

package org.jboss.grapher.services.graph;

import org.jboss.grapher.graph.DependencyInfo;
import org.jboss.grapher.graph.DependencyItem;
import org.jboss.msc.service.ServiceContainer;
import org.jboss.msc.service.ServiceName;

import java.util.HashSet;
import java.util.Set;

/**
 * Service deployment info.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ServiceDependencyInfo implements DependencyInfo<ServiceName> {
    private ServiceContainer container;

    public ServiceDependencyInfo(ServiceContainer container) {
        if (container == null)
            throw new IllegalArgumentException("Null container");
        this.container = container;
    }

    public Set<DependencyItem<ServiceName>> getIDependOn() {
        Set<DependencyItem<ServiceName>> dependencies = new HashSet<DependencyItem<ServiceName>>();
        for (ServiceName sn : container.getDependencies())
            dependencies.add(new ServiceDependencyItem(sn, null));
        return dependencies;
    }

    public Set<DependencyItem<ServiceName>> getDependsOnMe() {
        return null; // TODO
    }

    public Set<DependencyItem<ServiceName>> getUnresolved() {
        return null; // TODO
    }
}
