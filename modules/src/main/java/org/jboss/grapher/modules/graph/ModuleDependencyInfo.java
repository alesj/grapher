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

import org.jboss.grapher.graph.DependencyInfo;
import org.jboss.grapher.graph.DependencyItem;
import org.jboss.grapher.graph.DependencyTarget;
import org.jboss.grapher.graph.DependencyType;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;

import java.util.Collections;
import java.util.Set;

/**
 * Module dependency info
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleDependencyInfo implements DependencyInfo<ModuleIdentifier> {

    private Module module;

    public ModuleDependencyInfo(Module module) {
        this.module = module;
    }

    public Set<DependencyItem<ModuleIdentifier>> getDependencies(DependencyType type) {
        return Collections.emptySet(); // TODO
    }
}