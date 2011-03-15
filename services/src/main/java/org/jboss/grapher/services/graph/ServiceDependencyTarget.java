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
import org.jboss.grapher.graph.DependencyTarget;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Service deployment target.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ServiceDependencyTarget implements DependencyTarget<ServiceName> {
    private ServiceController controller;
    private DependencyInfo<ServiceName> info;

    public ServiceDependencyTarget(ServiceController controller) {
        if (controller == null)
            throw new IllegalArgumentException("Null controller");
        this.controller = controller;
        this.info = new ServiceDependencyInfo(controller.getServiceContainer());
    }

    public ServiceName getName() {
        return controller.getName();
    }

    public Set<ServiceName> getAliases() {
        ServiceName[] aliases = controller.getAliases();
        if (aliases == null || aliases.length == 0)
            return Collections.emptySet();

        return new HashSet<ServiceName>(Arrays.asList(aliases));
    }

    public DependencyInfo<ServiceName> getDependencyInfo() {
        return info;
    }
}
