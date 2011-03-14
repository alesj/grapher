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
package org.jboss.grapher.map;

import org.jboss.grapher.GrapherConstants;
import org.jboss.grapher.analysis.SubGraphRenderer;
import org.jboss.grapher.render.GIFRenderer;
import org.jboss.grapher.render.HtmlRenderer;
import org.jboss.grapher.render.JPGRenderer;
import org.jboss.grapher.render.PNGRenderer;
import org.jboss.grapher.render.Renderer;
import org.jboss.grapher.render.SVGRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Default graph creator mapper.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class DefaultRendererMapper extends AbstractGraphComponentMapper<Renderer> {
    /**
     * The default renderer
     */
    private Renderer defaultRenderer;
    private Map<String, Renderer> renderers;

    public DefaultRendererMapper() {
        renderers = new HashMap<String, Renderer>();

        // image renderers
        renderers.put("html", new HtmlRenderer());
        renderers.put("svg", new SVGRenderer());
        renderers.put("jpg", new JPGRenderer());
        renderers.put("png", new PNGRenderer());
        renderers.put("gif", new GIFRenderer());

        // analysis renderers
        renderers.put("sub-graph", new SubGraphRenderer());
    }

    @Override
    public void init(Map<String, String> config) {
        String format = (config != null) ? config.get("format") : null;
        if (format != null)
            defaultRenderer = renderers.get(format);

        if (defaultRenderer == null)
            defaultRenderer = renderers.get(GrapherConstants.FORMAT);
    }

    protected String getKey() {
        return "renderer";
    }

    public Renderer mapComponent(String key, String value) {
        Renderer renderer = renderers.get(value);
        return (renderer != null) ? renderer : defaultRenderer;
    }

    @Override
    protected Renderer getDefault() {
        return defaultRenderer;
    }
}