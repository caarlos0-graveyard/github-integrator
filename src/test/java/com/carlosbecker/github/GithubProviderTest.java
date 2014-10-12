package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.GithubModule;
import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@GuiceModules(GithubModule.class)
@RunWith(GuiceTestRunner.class)
public class GithubProviderTest {

	@Inject
	private GitHubClient client;

	@Test
	public void testProvided() throws Exception {
		assertThat(client, notNullValue());
	}

	@Test
	public void testGetComments() throws Exception {
		RepositoryId repo = new RepositoryId("", "");
		PullRequestService service = new PullRequestService(client);
		IssueService issuer = new IssueService(client);
		List<PullRequest> prs = service.getPullRequests(repo, "open");
		for (PullRequest pr : prs)
			if (pr.isMergeable())
				for (Comment comment : issuer.getComments(repo, pr.getNumber()))
						if (comment.getBodyText().equalsIgnoreCase("deploy this please"))
							System.out.println("FOI!");
	}
}
