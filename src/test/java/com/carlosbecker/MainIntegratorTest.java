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
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        mockRepository();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1))).thenReturn(newArrayList());
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testPRWithEmptyComment() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWithNonMatchingComment() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("This will not match. Don't do it.");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testMatchingComments() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("do it", "do IT");
        integrator.work();
        verify(executor).execute("echo", asList("user", "repo", "feature/my-branch", "1"));
    }

    @Test
    public void testWhenOneWasAlreadyExecuted() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("do it", "Ok, working on 'do it'...");
        integrator.work();
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWhenOneWasAlreadyExecutedWithDifferentParams() throws Exception {
        mockPullRequest();
        mockRepository("do (it|that)");
        mockComments("do it", "Ok, working on 'do it'...", "Ok, working on 'do it'...", "do that");
        integrator.work();
        verify(executor).execute("echo", asList("user", "repo", "feature/my-branch", "1", "that"));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void testCommentError() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("do it");
        when(issueService.createComment(eq("user"), eq("repo"), eq(1), eq("Ok, working on 'do it'..."))).thenThrow(
                IOException.class);
        integrator.work();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGithubParseError() throws Exception {
        final ScriptedRepository repo = mockRepository();
        when(prService.getPullRequests(eq(repo.getId()), eq("open"))).thenThrow(IOException.class);
        integrator.work();
        verifyZeroInteractions(issueService);
    }

    private void mockComments(String... messages) throws IOException {
        final List<Comment> comments = from(asList(messages))
                .transform(input -> {
                    final Comment comment = new Comment();
                    comment.setBody(input);
                    return comment;
                }).toList();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1))).thenReturn(comments);
    }

    private ScriptedRepository mockRepository(String regex) {
        final ScriptedRepository repository = new ScriptedRepository("user", "repo", regex, "echo");
        when(repositories.iterator()).thenReturn(
                asList(repository).iterator());
        return repository;
    }

    private ScriptedRepository mockRepository() {
        return mockRepository("do it");
    }

    private void mockPullRequest() throws IOException {
        when(prService.getPullRequests(any(IRepositoryIdProvider.class), eq("open"))).thenReturn(asList(mockPR()));
    }

    private PullRequest mockPR() {
        final PullRequest pr = new PullRequest();
        final PullRequestMarker head = new PullRequestMarker();
        final User owner = new User();
        owner.setLogin("user");
        final Repository repo = new Repository();
        repo.setName("repo");
        repo.setOwner(owner);
        head.setRepo(repo);
        pr.setHead(head);
        head.setRef("feature/my-branch");
        pr.setNumber(1);
        return pr;
    }
}
