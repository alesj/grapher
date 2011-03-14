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

package org.jboss.grapher.analysis;

import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.render.AbstractRenderer;
import org.jgraph.JGraph;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Abstract HTML renderer.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractHtmlRenderer extends AbstractRenderer {
    public void render(JGraph graph, OutputStream writer, int inset) throws IOException {
        render(graph, null, writer, inset);
    }

    @Override
    public void render(JGraph graph, DependencyType dtype, OutputStream writer, int inset) throws IOException {
        writer.write("<html>".getBytes());
        writer.write("<head>".getBytes());
        writer.write("<body>".getBytes());
        doAnalyse(graph, dtype, writer);
        writer.write("</body>".getBytes());
        writer.write("</html>".getBytes());
    }

    /**
     * Do analyse graph.
     *
     * @param graph  the graph
     * @param dtype  the dependency type
     * @param writer the writer
     * @throws IOException for any IO error
     */
    protected abstract void doAnalyse(JGraph graph, DependencyType dtype, OutputStream writer) throws IOException;
}
