package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
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
