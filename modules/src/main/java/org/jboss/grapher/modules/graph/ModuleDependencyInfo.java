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
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Module dependency info
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class ModuleDependencyInfo implements DependencyInfo<ModuleIdentifier> {

    private static Logger log = Logger.getLogger(ModuleDependencyInfo.class.getName());
    private static Method getDependencies;
    private static Method getModuleIdentifier;
    private Module module;

    static {
        try {
            getDependencies = Module.class.getDeclaredMethod("getDependencies");
            getDependencies.setAccessible(true);

            Class<?> clazz = ModuleDependencyInfo.class.getClassLoader().loadClass("org.jboss.modules.ModuleDependency");
            getModuleIdentifier = clazz.getDeclaredMethod("getIdentifier");
            getModuleIdentifier.setAccessible(true);
        } catch (Throwable t) {
            log.severe("Error using reflection: " + t);
            throw new RuntimeException(t);
        }
    }

    public ModuleDependencyInfo(Module module) {
        this.module = module;
    }

    public Set<DependencyItem<ModuleIdentifier>> getIDependOn() {
        try {
            Object[] dependecies = (Object[]) getDependencies.invoke(module);
            if (dependecies == null || dependecies.length == 0)
                return Collections.emptySet();

            Set<DependencyItem<ModuleIdentifier>> modules = new HashSet<DependencyItem<ModuleIdentifier>>(dependecies.length);
            for (Object dependency : dependecies) {
                Class<?> clazz = dependency.getClass();
                if (clazz.getName().contains("ModuleDependency")) {
                    ModuleIdentifier mi = (ModuleIdentifier) getModuleIdentifier.invoke(dependency);
                    modules.add(new ModuleDependencyItem(mi));
                }
            }
            return modules;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public Set<DependencyItem<ModuleIdentifier>> getDependsOnMe() {
        return null; // TODO
    }

    public Set<DependencyItem<ModuleIdentifier>> getUnresolved() {
        return null; // TODO
    }
}