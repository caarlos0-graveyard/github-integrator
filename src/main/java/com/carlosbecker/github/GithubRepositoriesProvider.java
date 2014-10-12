package com.carlosbecker.github;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import java.util.List;
import javax.inject.Inject;
import org.eclipse.egit.github.core.RepositoryId;
import com.google.common.base.Function;
import com.google.inject.Provider;

public class GithubRepositoriesProvider implements Provider<List<RepositoryId>> {
    private final GithubConfig config;

    @Inject
    public GithubRepositoriesProvider(GithubConfig config) {
        this.config = config;
    }

    @Override
    public List<RepositoryId> get() {
        if (config.repos() == null)
            return newArrayList();
        return from(config.repos()).transform(new RepositoryMapper()).toList();
    }

    class RepositoryMapper implements Function<String, RepositoryId> {
        @Override
        public RepositoryId apply(String repo) {
            repo = repo.replaceAll(" ", "");
            int slash = repo.indexOf('/');
            return new RepositoryId(repo.substring(0, slash),
                    repo.substring(slash + 1));
        }
    }
}
