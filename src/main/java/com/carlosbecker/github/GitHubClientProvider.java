package com.carlosbecker.github;

import javax.inject.Inject;

import org.eclipse.egit.github.core.client.GitHubClient;

import com.carlosbecker.integration.IntegratorConfig;
import com.google.inject.Provider;

public class GitHubClientProvider implements Provider<GitHubClient> {
    @Inject
    private IntegratorConfig config;

    @Override
    public GitHubClient get() {
        GitHubClient client = new GitHubClient();
        client.setOAuth2Token(config.oauth());
        return client;
    }
}
