package com.carlosbecker.github;

import javax.inject.Inject;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import com.google.inject.Provider;

public class IssueServiceProvider implements Provider<IssueService> {
    @Inject
    private GitHubClient client;

    @Override
    public IssueService get() {
        return new IssueService(client);
    }
}
