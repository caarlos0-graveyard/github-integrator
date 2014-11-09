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
package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import com.carlosbecker.integration.IntegratorConfig;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepositoriesProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class ScriptedRepositoriesProviderMapperTest {
    private ScriptedRepositoriesProvider provider;

    @Mock
    private IntegratorConfig config;

    @Before
    public void init() {
        initMocks(this);
        provider = new ScriptedRepositoriesProvider(config);
    }

    @Test
    public void nullInput() throws Exception {
        when(config.executions()).thenReturn(null);
        ScriptedRepositories repositories = provider.get();
        assertThat(repositories, notNullValue());
        assertThat(repositories.isEmpty(), equalTo(true));
    }

    @Test
    public void testEmptyInput() throws Exception {
        when(config.executions()).thenReturn(" ");
        ScriptedRepositories repositories = provider.get();
        assertThat(repositories, notNullValue());
        assertThat(repositories.isEmpty(), equalTo(true));
    }

    @Test
    public void testValidInput() throws Exception {
        when(config.executions()).thenReturn(
            "./src/test/resources/test.executions.json");
        ScriptedRepositories repositories = provider.get();
        assertThat(repositories, notNullValue());
        assertThat(repositories.isEmpty(), equalTo(false));
    }
}
