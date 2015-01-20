/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Carlos Alexandro Becker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.carlosbecker.integrator.tests;

import lombok.RequiredArgsConstructor;
import org.junit.rules.ExternalResource;

/**
 * ClassRule that loads up the INTEGRATOR_CONFIG file for tests.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@RequiredArgsConstructor
public class ConfigLoader extends ExternalResource {
    /**
     * Integrator config key.
     */
    private static final String KEY = "INTEGRATOR_CONFIG";
    /**
     * Path to integrator config file.
     */
    private final transient String config;

    /**
     * Constructor with the default test.properties.
     */
    public ConfigLoader() {
        this("./src/test/resources/test.properties");
    }

    @Override
    protected final void before() {
        System.setProperty(KEY, this.config);
    }

    @Override
    protected final void after() {
        System.clearProperty(KEY);
    }
}
