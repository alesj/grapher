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

import org.jboss.grapher.graph.AbstractDependencyTarget;
import org.jboss.grapher.graph.DependencyInfo;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;

import java.util.Collections;
import java.util.Set;

/**
 * Module dependency target
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleDependencyTarget extends AbstractDependencyTarget<ModuleIdentifier> {

    private Module module;
    private DependencyInfo<ModuleIdentifier> info;

    public ModuleDependencyTarget(Module module) {
        this.module = module;
        this.info = new ModuleDependencyInfo(module);
    }

    public ModuleIdentifier getName() {
        return module.getIdentifier();
    }

    public Set<ModuleIdentifier> getAliases() {
        return Collections.emptySet();
    }

    public DependencyInfo<ModuleIdentifier> getDependencyInfo() {
        return info;
    }
}