package com.carlosbecker;

import org.eclipse.egit.github.core.client.GitHubClient;
import com.carlosbecker.github.GitHubClientProvider;
import com.carlosbecker.github.ScriptedRepositories;
import com.carlosbecker.github.ScriptedRepositoriesProvider;
import com.google.inject.AbstractModule;

public class GithubModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ConfigModule());

        bind(GitHubClient.class).toProvider(GitHubClientProvider.class);
        bind(ScriptedRepositories.class)
                .toProvider(ScriptedRepositoriesProvider.class);
    }
}
