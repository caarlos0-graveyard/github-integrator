package com.carlosbecker.github;

import static org.mockito.MockitoAnnotations.initMocks;
import org.junit.Before;
import org.mockito.Mock;

public class RepositoriesProviderMapperTest {
    @Mock
    private IntegratorConfig config;

    @Before
    public void init() {
        initMocks(this);
    }

    // @Test
    // public void testWithNull() throws Exception {
    // when(config.executions()).thenReturn(null);
    // ScriptedRepositories repos = new ScriptedRepositoriesProvider(config)
    // .get();
    // assertThat(repos.isEmpty(), equalTo(true));
    // }
    //
    // @Test
    // public void testWithOneRepo() throws Exception {
    // when(config.executions()).thenReturn(asList("caarlos0/some-repo"));
    // List<RepositoryId> repos = new
    // ScriptedRepositoriesProvider(config).get();
    // assertThat(repos.size(), equalTo(1));
    // assertThat(repos.get(0).getOwner(), equalTo("caarlos0"));
    // assertThat(repos.get(0).getName(), equalTo("some-repo"));
    // }
    //
    // @Test
    // public void testWithSpaces() throws Exception {
    // when(config.executions()).thenReturn(asList(" caarlos0/  some-repo  "));
    // List<RepositoryId> repos = new
    // ScriptedRepositoriesProvider(config).get();
    // assertThat(repos.size(), equalTo(1));
    // assertThat(repos.get(0).getOwner(), equalTo("caarlos0"));
    // assertThat(repos.get(0).getName(), equalTo("some-repo"));
    // }
    //
    // @Test
    // public void testWithSeveralRepo() throws Exception {
    // when(config.executions()).thenReturn(
    // asList("caarlos0/some-repo", "caarlos0/another-repo",
    // "caarlos0/one-more"));
    // List<RepositoryId> repos = new
    // ScriptedRepositoriesProvider(config).get();
    // assertThat(repos.size(), equalTo(3));
    // }
}
