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

import com.carlosbecker.integrator.integration.AppRunner;
import com.carlosbecker.integrator.integration.IntegratorConfig;
import com.carlosbecker.integrator.integration.MainIntegrator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

/**
 * App Runner tests.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class AppRunnerTest {
    /**
     * Mock.
     */
    @Mock
    private transient IntegratorConfig config;
    /**
     * Mock.
     */
    @Mock
    private transient MainIntegrator integrator;
    /**
     * Runner.
     */
    private transient AppRunner app;

    /**
     * Tear up.
     */
    @Before
    public final void init() {
        MockitoAnnotations.initMocks(this);
        this.app = new AppRunner(this.config, this.integrator);
    }

    @Test(timeout = 500)
    public final void testRunOnce() throws Exception {
        this.app.run();
        Mockito.verify(this.integrator).work();
    }

    @Test(timeout = 500)
    public final void testRunTwice() throws Exception {
        final AtomicInteger loop = new AtomicInteger(0);
        Mockito.when(this.config.loop())
        .then(invocation -> loop.incrementAndGet() < 3);
        this.app.run();
        Mockito.verify(this.integrator, VerificationModeFactory.times(2))
        .work();
    }
}
