package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import java.util.Iterator;
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
    private ScriptedRepositories repositories;

    @Test
    public void testProvided() throws Exception {
        assertThat(client, notNullValue());
    }

    @Test
    public void testGetComments() throws Exception {
        PullRequestService service = new PullRequestService(client);
        IssueService issuer = new IssueService(client);
        Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext()) {
            ScriptedRepository scriptedRepository = iterator.next();
            RepositoryId repo = scriptedRepository.getId();
            for (PullRequest pr : service.getPullRequests(repo, "open"))
                for (Comment comment : issuer.getComments(repo, pr.getNumber()))
                    if (comment.getBody()
                            .equalsIgnoreCase("deploy this please"))
                        System.out.println("FOI!");
        }
    }
}
