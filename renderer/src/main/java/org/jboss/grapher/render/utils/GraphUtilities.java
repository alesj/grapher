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
package org.jboss.grapher.render.utils;

import org.jgraph.JGraph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Graph utils.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public final class GraphUtilities {
    /**
     * Conver graph to image.
     *
     * @param graph the graph
     * @param bg    the bg color
     * @param inset the inset
     * @return new image
     */
    public static BufferedImage toImage(JGraph graph, Color bg, int inset) {
        Object[] cells = graph.getRoots();
        Rectangle2D bounds = graph.getCellBounds(cells);
        if (bounds != null) {
            graph.toScreen(bounds);
//         ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
//         ColorModel cm = new ComponentColorModel(cs, false, true, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
            BufferedImage img = new BufferedImage((int) bounds.getWidth() + 2 * inset, (int) bounds.getHeight() + 2 * inset,
                    (bg != null) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = img.createGraphics();
            if (bg != null) {
                graphics.setColor(bg);
                graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
            } else {
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
                graphics.fillRect(0, 0, img.getWidth(), img.getHeight());
                graphics.setComposite(AlphaComposite.SrcOver);
            }
            graphics.translate((int) (-bounds.getX() + inset), (int) (-bounds.getY() + inset));
            boolean tmp = graph.isDoubleBuffered();
            RepaintManager currentManager = RepaintManager.currentManager(graph);
            currentManager.setDoubleBufferingEnabled(false);
            graph.paint(graphics);
            currentManager.setDoubleBufferingEnabled(tmp);
            return img;
        }
        return null;
    }
}