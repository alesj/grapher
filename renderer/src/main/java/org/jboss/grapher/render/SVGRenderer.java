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

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.jgraph.JGraph;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Render SVG image.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class SVGRenderer extends AbstractRenderer {
    public void render(JGraph graph, OutputStream out, int inset) throws IOException {
        Object[] roots = graph.getRoots();
        Rectangle2D bounds = graph.toScreen(graph.getCellBounds(roots));
        if (bounds != null) {
            // Constructs the svg generator used for painting the graph to
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument(null, "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
            svgGenerator.translate(-bounds.getX() + inset, -bounds.getY() + inset);
            // Paints the graph to the svg generator with no double
            // buffering enabled to make sure we get a vector image.
            RepaintManager currentManager = RepaintManager.currentManager(graph);
            currentManager.setDoubleBufferingEnabled(false);
            graph.paint(svgGenerator);
            // Writes the graph to the specified file as an SVG stream
            Writer writer = new OutputStreamWriter(out);
            svgGenerator.stream(writer, false);
            currentManager.setDoubleBufferingEnabled(true);
        }
    }
}
