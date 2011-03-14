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
package org.jboss.grapher;

import org.jboss.grapher.graph.DependencyFilter;
import org.jboss.grapher.graph.DependencyType;
import org.jboss.grapher.graph.GraphCreator;
import org.jboss.grapher.layout.GraphLayout;
import org.jboss.grapher.map.GraphComponentMapper;
import org.jboss.grapher.render.Renderer;
import org.jboss.logging.Logger;
import org.jgraph.JGraph;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

/**
 * Grapher servlet.
 * <p/>
 * It knows how to render all of MC components,
 * or you can specify just a single bean via parameter.
 * e.g. grapher/?bean=TransactionManager
 * <p/>
 * You can also change the type of rendering.
 * Currently we're supporting svg, jpg and gif.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class GrapherServlet extends HttpServlet {
    private Logger log = Logger.getLogger(getClass());

    private static final String AWT_HEADLESS_FLAG = "java.awt.headless";
    private String previous;

    private boolean initialized;

    private GraphComponentMapper<GraphCreator> creatorMapper;
    private GraphComponentMapper<DependencyType> dependencyTypeMapper;
    private GraphComponentMapper<DependencyFilter> dependencyFilterMapper;
    private GraphComponentMapper<GraphLayout> layoutMapper;
    private GraphComponentMapper<Renderer> rendererMapper;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // apply headless graphics
        previous = System.setProperty(AWT_HEADLESS_FLAG, "true");
        if ("false".equals(previous)) {
            log.warn("Headless AWT was explicitly disabled, potential graph rendering conflict.");
        }
    }

    /**
     * Initialize mappers.
     */
    private synchronized void initalizeMappers() {
        if (initialized == false) {
            initialized = true;

            creatorMapper = createMapper("CreatorMapper", GrapherConstants.CREATOR_MAPPER, GraphCreator.class);
            dependencyTypeMapper = createMapper("DependencyMapper", GrapherConstants.DEPENDENCY_TYPE_MAPPER, DependencyType.class);
            dependencyFilterMapper = createMapper("DependencyFilter", GrapherConstants.DEPENDENCY_FILTER_MAPPER, DependencyFilter.class);
            layoutMapper = createMapper("LayoutMapper", GrapherConstants.LAYOUT_MAPPER, GraphLayout.class);

            rendererMapper = createMapper("RendererMapper", GrapherConstants.RENDERER_MAPPER, Renderer.class);
            rendererMapper.init(Collections.singletonMap("format", getServletConfig().getInitParameter("format")));
        }
    }

    @Override
    public void destroy() {
        if (previous == null)
            System.clearProperty(AWT_HEADLESS_FLAG);
        else
            System.setProperty(AWT_HEADLESS_FLAG, previous);
    }

    /**
     * Do render MC graph.
     *
     * @param request  the http request
     * @param response the http response
     * @throws ServletException the servlet exception
     * @throws IOException      the io exception
     */
    protected void doRenderGraph(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        initalizeMappers();

        boolean trace = log.isTraceEnabled();

        GraphCreator creator = creatorMapper.mapComponent(request);
        if (creator == null)
            throw new ServletException("No such graph creator: " + request);

        if (trace)
            log.trace("Invoking creator: " + creator);

        DependencyType dtype = dependencyTypeMapper.mapComponent(request);

        if (trace)
            log.trace("Using dependency type: " + dtype);

        DependencyFilter filter = dependencyFilterMapper.mapComponent(request);

        if (trace)
            log.trace("Using dependency filter: " + filter);

        JGraph graph = creator.createGraph(request, dtype, filter);

        // apply layout
        GraphLayout currentLayout = layoutMapper.mapComponent(request);

        if (trace)
            log.trace("Using layout: " + currentLayout);

        currentLayout.applyLayout(graph);

        Renderer currentRenderer = rendererMapper.mapComponent(request);
        if (trace)
            log.trace("Using renderer: " + currentRenderer);

        OutputStream writer = response.getOutputStream();
        try {
            currentRenderer.render(graph, dtype, writer, 10);
        } finally {
            writer.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRenderGraph(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doRenderGraph(request, response);
    }

    /**
     * Create new mapper.
     *
     * @param reference       the class name reference
     * @param defaultInstance the default instance
     * @param expectedClass   the expected class
     * @param <T>             the expected type
     * @return new instance or default for any error
     */
    @SuppressWarnings({"unchecked"})
    protected <T> GraphComponentMapper<T> createMapper(String reference, GraphComponentMapper<T> defaultInstance, Class<T> expectedClass) {
        if (reference == null)
            return defaultInstance;

        Object instance = getServletContext().getAttribute(reference);
        if (instance != null)
            return GraphComponentMapper.class.cast(instance);

        String className = getServletConfig().getInitParameter(reference);
        if (className == null)
            return defaultInstance;

        try {
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            Object object = clazz.newInstance();
            return GraphComponentMapper.class.cast(object);
        } catch (Exception e) {
            log.warn("Exception instantiating new " + expectedClass.getName() + " instance: " + e);
        }
        return defaultInstance;
    }
}
