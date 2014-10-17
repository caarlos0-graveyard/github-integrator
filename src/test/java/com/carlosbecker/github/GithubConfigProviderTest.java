package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.carlosbecker.ConfigModule;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.carlosbecker.integration.IntegratorConfig;
import com.carlosbecker.integration.TestPropertiesLoader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

@GuiceModules(ConfigModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubConfigProviderTest {
    @BeforeClass
    public static void init() throws Exception {
        TestPropertiesLoader.init();
    }

    @AfterClass
    public static void shutdown() {
        TestPropertiesLoader.shutdown();
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
