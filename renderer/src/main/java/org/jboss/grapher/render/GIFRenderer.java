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

import org.jboss.grapher.render.utils.GIFOutputStream;
import org.jboss.grapher.render.utils.GraphUtilities;
import org.jgraph.JGraph;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Render gif image.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class GIFRenderer extends AbstractRenderer {
    public void render(JGraph graph, OutputStream writer, int inset) throws IOException {
        BufferedImage img = GraphUtilities.toImage(graph, null, inset);
        writer.write(convertToGif(img));
        writer.flush();
    }

    /**
     * Convert Image to GIF-encoded data, reducing the number of colors if needed
     *
     * @param image the image
     * @return image bytes
     * @throws IOException for any io error
     */
    protected static byte[] convertToGif(Image image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GIFOutputStream.writeGIF(out, image);
        return out.toByteArray();
    }
}