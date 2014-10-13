package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import javax.inject.Inject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.ConfigModule;
import com.carlosbecker.TestPropertiesLoader;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@GuiceModules(ConfigModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubConfigProviderTest {
    @BeforeClass
    public static void before() throws Exception {
        TestPropertiesLoader.load();
    }

    @Inject
    private IntegratorConfig config;

    @Test
    public void testConfigProvided() throws Exception {
        assertThat(config, notNullValue());
    }

    @Test
    public void testOauth() throws Exception {
        assertThat(config.oauth(), notNullValue());
    }

    @Test
    public void testExecutions() throws Exception {
        assertThat(config.executions(), notNullValue());
    }

    @Test
    public void testPeriod() throws Exception {
        assertThat(config.period(), notNullValue());
    }
}
