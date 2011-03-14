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
package org.jboss.grapher.graph;

import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Graph settings.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class GraphSettings {
    /**
     * Create layout.
     *
     * @param cells      the cells
     * @param attributes the attributes
     */
    @SuppressWarnings("unchecked")
    public static void layout(Object[] cells, Map attributes) {
        int count = cells.length;
        int square = (int) Math.sqrt(count) + 1; //never zero
        int nodesperrow = count / square;

        int row = 0;
        int col = 0;
        for (int p = 0; p < count; p++) {
            col++;
            Object obj = cells[p];
            if (obj instanceof Colorable) {
                Colorable cell = (Colorable) obj;
                attributes.put(cell, createBounds(cell.getLabel(), col * 70, row * 70, cell.getColor()));
            }
            if (col >= nodesperrow) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Create bounds.
     *
     * @param label the label
     * @param x     the x
     * @param y     the y
     * @param c     the color
     * @return bounds map
     */
    private static Map createBounds(String label, int x, int y, Color c) {
        Map map = new HashMap();
        int length = label.length();
        int width = Math.min(200, 12 * length);
        GraphConstants.setBounds(map, new Rectangle(x, y, width, 30));
        GraphConstants.setBorder(map, BorderFactory.createRaisedBevelBorder());
        GraphConstants.setBackground(map, c.brighter());
        GraphConstants.setForeground(map, Color.white);
        GraphConstants.setFont(map, GraphConstants.DEFAULTFONT.deriveFont(Font.BOLD, 12));
        GraphConstants.setOpaque(map, true);
        return map;
    }
}
