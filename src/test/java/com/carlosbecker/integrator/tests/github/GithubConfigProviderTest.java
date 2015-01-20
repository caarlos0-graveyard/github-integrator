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
package com.carlosbecker.integrator.tests.github;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.carlosbecker.integrator.ConfigModule;
import com.carlosbecker.integrator.integration.IntegratorConfig;
import com.carlosbecker.integrator.tests.ConfigLoader;
import javax.inject.Inject;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Github config provider tests.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@GuiceModules(ConfigModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubConfigProviderTest {
    /**
     * Rule.
     * @checkstyle VisibilityModifierCheck (5 lines)
     */
    @ClassRule
    public static ConfigLoader configLoader = new ConfigLoader();

    /**
     * Config.
     */
    @Inject
    private transient IntegratorConfig config;

    /**
     * Basic test.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testConfigProvided() throws Exception {
        Assert.assertThat(this.config, CoreMatchers.notNullValue());
    }

    /**
     * Test oauth.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testOauth() throws Exception {
        Assert.assertThat(this.config.oauth(), CoreMatchers.notNullValue());
    }

    /**
     * Test executions.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testExecutions() throws Exception {
        Assert.assertThat(
            this.config.executions(),
            CoreMatchers.notNullValue()
        );
    }

    /**
     * Test period.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testPeriod() throws Exception {
        Assert.assertThat(this.config.period(), CoreMatchers.notNullValue());
    }
}
