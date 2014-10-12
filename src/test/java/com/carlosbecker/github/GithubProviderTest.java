package com.carlosbecker.github;

import static com.google.common.collect.Collections2.transform;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.util.Collection;
import javax.inject.Inject;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.carlosbecker.GithubModule;
import com.carlosbecker.TestPropertiesLoader;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;
import com.google.common.base.Function;

@GuiceModules(GithubModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubProviderTest {
    @BeforeClass
    public static void before() throws Exception {
        TestPropertiesLoader.load();
    }

    @Inject
    private GitHubClient client;

    @Inject
    private GithubConfig config;

    @Test
    public void testProvided() throws Exception {
        assertThat(client, notNullValue());
    }

    @Test
    public void testGetComments() throws Exception {
        Collection<RepositoryId> repositories = transform(config.repos(),
                new Function<String, RepositoryId>() {
                    @Override
                    public RepositoryId apply(String repo) {
                        int slash = repo.indexOf('/');
                        return new RepositoryId(repo.substring(0, slash),
                                repo.substring(slash + 1));
                    }
                });

        PullRequestService service = new PullRequestService(client);
        IssueService issuer = new IssueService(client);
        for (RepositoryId repo : repositories)
            for (PullRequest pr : service.getPullRequests(repo, "open"))
                for (Comment comment : issuer.getComments(repo, pr.getNumber()))
                    if (comment.getBody()
                            .equalsIgnoreCase("deploy this please"))
                        System.out.println("FOI!");
    }
}
