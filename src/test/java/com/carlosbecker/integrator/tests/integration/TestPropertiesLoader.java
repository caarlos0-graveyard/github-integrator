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
package com.carlosbecker.integrator.tests.integration;

import lombok.RequiredArgsConstructor;
import org.junit.rules.ExternalResource;

/**
 * Rule that loads up the INTEGRATOR_CONFIG file for tests.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@RequiredArgsConstructor
public class TestPropertiesLoader extends ExternalResource {
    /**
     * Path to integrator config file
     */
    private final transient String config;

    /**
     * Constructor with the default test.properties
     */
    public TestPropertiesLoader() {
        this("./src/test/resources/test.properties");
    }

    @Override
    protected void before() throws Throwable {
        System.out.println("OI");
        System.setProperty("INTEGRATOR_CONFIG", config);
    }

    @Override
    protected void after() {
        System.clearProperty("INTEGRATOR_CONFIG");
    }
}
