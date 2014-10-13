package com.carlosbecker;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
import com.google.common.base.Function;

public class MainIntegratorTest {
    private MainIntegrator integrator;
    @Mock
    private ProcessExecutor executor;
    @Mock
    private ScriptedRepositories repositories;
    @Mock
    private IssueService issueService;
    @Mock
    private PullRequestService prService;

    @Before
    public void init() {
        initMocks(this);
        integrator = new MainIntegrator(prService, issueService, repositories, executor);
    }

    @Test
    public void testNoRepositories() throws Exception {
        when(repositories.iterator()).thenReturn(new ArrayList<ScriptedRepository>().iterator());
        integrator.work();
        verifyZeroInteractions(prService);
    }

    @Test
    public void testPRWithNoComments() throws Exception {
        mockPullRequest();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1))).thenReturn(newArrayList());
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testPRWithEmptyComment() throws Exception {
        mockPullRequest();
        mockComments("");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWithNonMatchingComment() throws Exception {
        mockPullRequest();
        mockComments("This will not match. Don't do it.");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testMatchingComments() throws Exception {
        mockPullRequest();
        mockComments("do it", "do IT");
        integrator.work();
        verify(executor).execute("echo", "user", "repo", "feature/my-branch", "1");
    }

    @Test
    public void testWhenOneWasAlreadyExecuted() throws Exception {
        mockPullRequest();
        mockComments("do it", "Ok, working on 'do it'...");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    private void mockComments(String... messages) throws IOException {
        List<Comment> comments = from(asList(messages))
                .transform(new Function<String, Comment>() {
                    @Override
                    public Comment apply(String input) {
                        Comment comment = new Comment();
                        comment.setBody(input);
                        return comment;
                    }
                })
                .toList();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
                .thenReturn(comments);
    }

    private void mockPullRequest() throws IOException {
        when(repositories.iterator())
                .thenReturn(asList(new ScriptedRepository("user", "repo", "do it", "echo")).iterator());
        when(prService.getPullRequests(any(IRepositoryIdProvider.class), eq("open")))
                .thenReturn(asList(mockPR()));
    }

    private PullRequest mockPR() {
        PullRequest pr = new PullRequest();
        PullRequestMarker head = new PullRequestMarker();
        User owner = new User();
        owner.setLogin("user");
        Repository repo = new Repository();
        repo.setName("repo");
        repo.setOwner(owner);
        head.setRepo(repo);
        pr.setHead(head);
        head.setRef("feature/my-branch");
        pr.setNumber(1);
        return pr;
    }
}
