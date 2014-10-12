package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import javax.inject.Inject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.GithubModule;
import com.carlosbecker.TestPropertiesLoader;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;

@RunWith(GuiceTestRunner.class)
@GuiceModules(GithubModule.class)
public class RepositoriesProviderTest {
    @BeforeClass
    public static void before() throws Exception {
        TestPropertiesLoader.load();
    }

    @Inject
    private ScriptedRepositories repositories;

    @Test
    public void testProvided() throws Exception {
        assertThat(repositories, notNullValue());
    }

    @Test
    public void testCorrectParsing() throws Exception {
        ScriptedRepository repository = repositories.iterator().next();
        assertThat(repository.getId().getOwner(), equalTo("caarlos0"));
        assertThat(repository.getId().getName(), equalTo("github-integrator"));
    }
}
