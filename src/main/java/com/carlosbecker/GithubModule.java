package com.carlosbecker;

import java.util.List;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import com.carlosbecker.github.GitHubClientProvider;
import com.carlosbecker.github.GithubRepositoriesProvider;
import com.carlosbecker.github.Repositories;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

public class GithubModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new ConfigModule());

        bind(GitHubClient.class).toProvider(GitHubClientProvider.class);
        bind(new TypeLiteral<List<RepositoryId>>() {}).annotatedWith(
                Repositories.class)
                .toProvider(GithubRepositoriesProvider.class);
    }
}
