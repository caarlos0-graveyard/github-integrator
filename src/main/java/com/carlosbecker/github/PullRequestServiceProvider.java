package com.carlosbecker.github;

import javax.inject.Inject;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.PullRequestService;
import com.google.inject.Provider;

public class PullRequestServiceProvider implements Provider<PullRequestService> {
    @Inject
    private GitHubClient client;

    @Override
    public PullRequestService get() {
        return new PullRequestService(client);
    }
}
