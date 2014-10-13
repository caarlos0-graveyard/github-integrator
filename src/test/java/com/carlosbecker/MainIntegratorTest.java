package com.carlosbecker;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import java.io.IOException;
import java.util.ArrayList;
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
    public void testPRWithNullComment() throws Exception {
        mockPullRequest();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
                .thenReturn(asList(new Comment()));
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testPRWithEmptyComment() throws Exception {
        mockPullRequest();
        Comment emptyComment = new Comment();
        emptyComment.setBody("");
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
                .thenReturn(asList(emptyComment));
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWithNonMatchingComment() throws Exception {
        mockPullRequest();
        Comment comment = new Comment();
        comment.setBody("This will not match. Don't do it.");
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
                .thenReturn(asList(comment));
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testMatchingComments() throws Exception {
        mockPullRequest();
        Comment comment1 = new Comment();
        comment1.setBody("do it");
        Comment comment2 = new Comment();
        comment2.setBody("do IT");
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
                .thenReturn(asList(comment1, comment2));
        integrator.work();
        verify(executor, times(2)).execute("echo", "user", "repo", "1");
    }

    private void mockPullRequest() throws IOException {
        when(repositories.iterator())
                .thenReturn(asList(new ScriptedRepository("user", "repo", "do it", "echo")).iterator());
        PullRequest pr = new PullRequest();
        PullRequestMarker base = new PullRequestMarker();
        User owner = new User();
        owner.setLogin("user");
        Repository repo = new Repository();
        repo.setName("repo");
        repo.setOwner(owner);
        base.setRepo(repo);
        pr.setBase(base);
        pr.setNumber(1);
        when(prService.getPullRequests(any(IRepositoryIdProvider.class), eq("open")))
                .thenReturn(asList(pr));
    }
}
