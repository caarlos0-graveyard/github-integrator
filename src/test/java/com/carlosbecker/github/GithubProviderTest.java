package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.carlosbecker.TestPropertiesLoader;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@GuiceModules(GithubModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubProviderTest {
    @BeforeClass
    public static void init() throws Exception {
        TestPropertiesLoader.init();
    }

    @AfterClass
    public static void shutdown() {
        TestPropertiesLoader.shutdown();
    }

    @Inject
    private GitHubClient client;

    @Test
    public void testProvided() throws Exception {
        assertThat(client, notNullValue());
    }
}
