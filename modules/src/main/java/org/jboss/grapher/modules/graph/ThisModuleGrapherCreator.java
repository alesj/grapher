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

import org.jboss.grapher.map.GraphCreatorExt;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.kohsuke.MetaInfServices;

/**
 * This module.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
@MetaInfServices(GraphCreatorExt.class)
public class ThisModuleGrapherCreator extends TreeModuleGrapherCreator {

    @Override
    protected ModuleIdentifier toName(String value) {
        Module callerModule = Module.getCallerModule();
        log.info("Current module: " + callerModule);
        return callerModule.getIdentifier();
    }

    public String extensionName() {
        return "this-module";
    }
}
