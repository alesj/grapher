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

import org.jboss.grapher.graph.DependencyFilter;
import org.jboss.grapher.graph.DependencyTarget;
import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.graph.SingleGraphCreator;
import org.jboss.grapher.map.GraphCreatorExt;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.jgraph.JGraph;
import org.kohsuke.MetaInfServices;

import javax.servlet.http.HttpServletRequest;

/**
 * Single service.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@MetaInfServices(GraphCreatorExt.class)
public class SingleServiceGraphCreator extends SingleGraphCreator<ServiceName> implements GraphCreatorExt<ServiceName> {

    private ServiceRegistry registry;

    @Override
    public JGraph createGraph(HttpServletRequest request, DependencyType<ServiceName> dtype, DependencyFilter<ServiceName> serviceNameDependencyFilter) {
        registry = null; // TODO
        return super.createGraph(request, dtype, serviceNameDependencyFilter);
    }

    @Override
    protected DependencyTarget<ServiceName> getDependencyTarget(HttpServletRequest request, ServiceName target) {
        return new ServiceDependencyTarget(registry.getRequiredService(target));
    }

    public String extensionName() {
        return "single-service";
    }

    @Override
    protected ServiceName toName(String value) {
        return ServiceName.parse(value);
    }
}
