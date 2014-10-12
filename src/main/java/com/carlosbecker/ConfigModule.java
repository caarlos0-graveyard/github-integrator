package com.carlosbecker;

import com.carlosbecker.github.GithubConfig;
import com.carlosbecker.github.GithubConfigProvider;
import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(GithubConfig.class).toProvider(GithubConfigProvider.class);
	}
}
