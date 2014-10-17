package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.carlosbecker.integration.TestPropertiesLoader;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@RunWith(GuiceTestRunner.class)
@GuiceModules(GithubModule.class)
public class RepositoriesProviderTest {
    @BeforeClass
    public static void init() throws Exception {
        TestPropertiesLoader.init();
    }

    @AfterClass
    public static void shutdown() {
        TestPropertiesLoader.shutdown();
    }

    @Inject
    private ScriptedRepositories repositories;

    @Test
    public void testProvided() throws Exception {
        assertThat(repositories, notNullValue());
    }

    @Test
    public void testCorrectParsing() throws Exception {
        final ScriptedRepository repository = repositories.iterator().next();
        assertThat(repository.getId().getOwner(), equalTo("caarlos0"));
        assertThat(repository.getId().getName(), equalTo("github-integrator"));
    }
}
