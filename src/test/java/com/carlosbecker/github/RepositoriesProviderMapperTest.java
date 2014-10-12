package com.carlosbecker.github;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import java.util.List;
import org.eclipse.egit.github.core.RepositoryId;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RepositoriesProviderMapperTest {
    @Mock
    private GithubConfig config;

    @Before
    public void init() {
        initMocks(this);
    }

    @Test
    public void testWithNull() throws Exception {
        when(config.repos()).thenReturn(null);
        List<RepositoryId> repos = new GithubRepositoriesProvider(config).get();
        assertThat(repos.isEmpty(), equalTo(true));
    }

    @Test
    public void testWithOneRepo() throws Exception {
        when(config.repos()).thenReturn(asList("caarlos0/some-repo"));
        List<RepositoryId> repos = new GithubRepositoriesProvider(config).get();
        assertThat(repos.size(), equalTo(1));
        assertThat(repos.get(0).getOwner(), equalTo("caarlos0"));
        assertThat(repos.get(0).getName(), equalTo("some-repo"));
    }

    @Test
    public void testWithSpaces() throws Exception {
        when(config.repos()).thenReturn(asList(" caarlos0/  some-repo  "));
        List<RepositoryId> repos = new GithubRepositoriesProvider(config).get();
        assertThat(repos.size(), equalTo(1));
        assertThat(repos.get(0).getOwner(), equalTo("caarlos0"));
        assertThat(repos.get(0).getName(), equalTo("some-repo"));
    }

    @Test
    public void testWithSeveralRepo() throws Exception {
        when(config.repos()).thenReturn(
                asList("caarlos0/some-repo", "caarlos0/another-repo", "caarlos0/one-more"));
        List<RepositoryId> repos = new GithubRepositoriesProvider(config).get();
        assertThat(repos.size(), equalTo(3));
    }
}
