package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.ConfigModule;
import com.carlosbecker.github.GithubConfig;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@GuiceModules(ConfigModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubConfigProviderTest {
	@Inject
	private GithubConfig config;

	@Test
	public void testConfigProvided() throws Exception {
		assertThat(config, notNullValue());
	}
	
	
}
