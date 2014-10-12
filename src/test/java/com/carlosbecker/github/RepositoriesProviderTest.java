package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.egit.github.core.RepositoryId;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.GithubModule;
import com.carlosbecker.TestPropertiesLoader;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@RunWith(GuiceTestRunner.class)
@GuiceModules(GithubModule.class)
public class RepositoriesProviderTest {
    @BeforeClass
    public static void before() throws Exception {
        TestPropertiesLoader.load();
    }

    @Inject
    @Repositories
    private List<RepositoryId> repositories;

    @Test
    public void testProvided() throws Exception {
        assertThat(repositories, notNullValue());
    }
}
