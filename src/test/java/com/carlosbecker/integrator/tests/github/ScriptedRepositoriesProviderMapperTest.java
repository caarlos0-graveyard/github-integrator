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

import com.carlosbecker.integrator.integration.IntegratorConfig;
import com.carlosbecker.integrator.model.ScriptedRepositories;
import com.carlosbecker.integrator.model.ScriptedRepositoriesProvider;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Scripted Repositories Provider tests.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class ScriptedRepositoriesProviderMapperTest {
    /**
     * Provider.
     */
    private transient ScriptedRepositoriesProvider provider;
    /**
     * Mock.
     */
    @Mock
    private transient IntegratorConfig config;

    /**
     * Tear up.
     */
    @Before
    public final void init() {
        MockitoAnnotations.initMocks(this);
        this.provider = new ScriptedRepositoriesProvider(this.config);
    }

    /**
     * Test with null input.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void nullInput() throws Exception {
        Mockito.when(this.config.executions()).thenReturn(null);
        final ScriptedRepositories repositories = this.provider.get();
        Assert.assertThat(repositories, CoreMatchers.notNullValue());
        Assert.assertThat(repositories.isEmpty(), CoreMatchers.equalTo(true));
    }

    /**
     * Test with empty input.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testEmptyInput() throws Exception {
        Mockito.when(this.config.executions()).thenReturn(" ");
        final ScriptedRepositories repositories = this.provider.get();
        Assert.assertThat(repositories, CoreMatchers.notNullValue());
        Assert.assertThat(repositories.isEmpty(), CoreMatchers.equalTo(true));
    }

    /**
     * Test with valid input.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testValidInput() throws Exception {
        Mockito.when(this.config.executions())
            .thenReturn("./src/test/resources/test.executions.json");
        final ScriptedRepositories repositories = this.provider.get();
        Assert.assertThat(repositories, CoreMatchers.notNullValue());
        Assert.assertThat(repositories.isEmpty(), CoreMatchers.equalTo(false));
    }
}
