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

package org.jboss.grapher.modules.graph;

import org.jboss.grapher.graph.AbstractGraphCreator;
import org.jboss.grapher.graph.DependencyFilter;
import org.jboss.grapher.graph.DependencyTarget;
import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.map.GraphCreatorExt;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoadException;
import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.GraphCell;
import org.kohsuke.MetaInfServices;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * All modules.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@MetaInfServices(GraphCreatorExt.class)
public class AllGrapherCreator extends AbstractGraphCreator<ModuleIdentifier> implements GraphCreatorExt<ModuleIdentifier> {

    @Override
    public JGraph createGraph(HttpServletRequest request, DependencyType<ModuleIdentifier> dtype, DependencyFilter<ModuleIdentifier> filter) {
        // TODO
        return super.createGraph(request, dtype, filter);
    }

    @Override
    protected void createCells(HttpServletRequest request, DependencyType<ModuleIdentifier> dtype, Map<Object, GraphCell> cells, Set<Object> objects, Map attributes, ConnectionSet cs, DependencyFilter<ModuleIdentifier> filter) {
    }

    @Override
    protected DependencyTarget<ModuleIdentifier> getDependencyTarget(HttpServletRequest request, ModuleIdentifier mi) {
        try {
            Module module = Module.getModuleFromCallerModuleLoader(mi);
            return new ModuleDependencyTarget(module);
        } catch (ModuleLoadException e) {
            throw new RuntimeException(e);
        }
    }

    public String extensionName() {
        return "all-modules";
    }

    public void initialValue(String value) {
    }
}
