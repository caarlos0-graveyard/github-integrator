package com.carlosbecker;

import org.eclipse.egit.github.core.client.GitHubClient;

import com.google.inject.AbstractModule;

public class GithubModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new ConfigModule());
		bind(GitHubClient.class).toProvider(GitHubClientProvider.class);
	}
}
