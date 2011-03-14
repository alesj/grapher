/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.grapher.render;

import org.jboss.grapher.graph.GVertex;
import org.jgraph.JGraph;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Render html.
 * // TODO test
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class HtmlRenderer extends AbstractRenderer {
    private static final Comparator<Object> COMPARATOR = new CellComparator();

    public void render(JGraph graph, OutputStream out, int inset) throws IOException {
        Object[] roots = graph.getRoots();
        if (roots != null) {
            Set<Object> links = new TreeSet<Object>(COMPARATOR);
            links.addAll(Arrays.asList(roots));

            write(out, "<html>");
            write(out, "<ul>");
            for (Object cell : links) {
                if (cell instanceof GVertex) {
                    write(out, "<li><a href=\"?bean=" + cell + "\">" + cell + "</a></li>\n");
                }
            }
            write(out, "</ul>");
            write(out, "</html>");
            out.flush();
        }
    }

    // simplify write
    private void write(OutputStream out, String text) throws IOException {
        out.write(text.getBytes());
    }

    private static class CellComparator implements Comparator<Object> {
        public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
        }
    }
}