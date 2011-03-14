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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * The abstract type mapper.
 *
 * @param <T> component type
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractGraphComponentMapper<T> implements GraphComponentMapper<T> {
    public void init(Map<String, String> config) {
    }

    /**
     * Get key parameter name.
     *
     * @return the key parameter name
     */
    protected abstract String getKey();

    /**
     * Should we skip null value.
     *
     * @return true if we skip null, false otherwise
     */
    protected boolean skipNull() {
        return true;
    }

    public T mapComponent(HttpServletRequest request) {
        String key = getKey();
        String value = request.getParameter(key);
        return (value == null && skipNull()) ? getDefault() : mapComponent(key, value);
    }

    /**
     * Get default impl.
     *
     * @return the default impl
     */
    protected T getDefault() {
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public T mapComponent(String key, String value) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            Class<?> clazz = cl.loadClass(value);
            return (T) clazz.newInstance();
        } catch (Throwable t) {
            throw new RuntimeException();
        }
    }
}