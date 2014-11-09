/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Carlos Alexandro Becker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.carlosbecker.integration;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import org.mockito.Mockito;

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
    @Mock
    private PendencyService pendencyService;

    @Before
    public void init() {
        initMocks(this);
        integrator = new MainIntegrator(prService, issueService, repositories,
            executor, pendencyService);
    }

    @Test
    public void testNoRepositories() throws Exception {
        when(repositories.iterator()).thenReturn(
            new ArrayList<ScriptedRepository>().iterator());
        integrator.work();
        verifyZeroInteractions(issueService);
        verifyZeroInteractions(prService);
    }

    @Test
    public void testPRWithNoComments() throws Exception {
        mockPullRequest();
        mockRepository();
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
            .thenReturn(newArrayList());
        integrator.work();
        verify(issueService, times(0)).createComment(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString());
        verifyZeroInteractions(executor);
    }

    @Test
    public void testPRWithEmptyComment() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("");
        integrator.work();
        verify(issueService, times(0)).createComment(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString());
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWithNonMatchingComment() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("This will not match. Don't do it.");
        integrator.work();
        verify(issueService, times(0)).createComment(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString());
        verifyZeroInteractions(executor);
    }

    @Test
    public void testWhenOneWasAlreadyExecuted() throws Exception {
        mockPullRequest();
        mockRepository();
        mockComments("do it", "Ok, working on 'do it'...");
        integrator.work();
        verify(issueService, times(0)).createComment(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyString(),
            Mockito.anyString());
        verifyZeroInteractions(executor);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testModerateScenario() throws Exception {
        mockPullRequest();
        final ScriptedRepository repository = mockRepository("do (it|that)");
        mockComments("do it", "do that", "Ok, working on 'do it'...",
            "Ok, working on 'do that'...", "do that again",
            "do it", "nice", "+1", ":+1:");
        final Map<List<String>, Long> comments = Maps.newHashMap();
        comments.put(newArrayList("it"), 1L);
        when(pendencyService.filter(eq(repository), any(List.class)))
            .thenReturn(comments);

        integrator.work();

        verify(issueService).createComment(eq("user"), eq("repo"), eq(1),
            eq("Ok, working on 'do it'..."));
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public void testCommentError() throws Exception {
        mockPullRequest();
        final ScriptedRepository repository = mockRepository();
        mockComments("do it");
        final Map<List<String>, Long> comments = Maps.newHashMap();
        comments.put(newArrayList(), 1L);
        when(pendencyService.filter(eq(repository), any(List.class)))
            .thenReturn(comments);
        when(
            issueService.createComment(eq("user"), eq("repo"), eq(1),
                eq("Ok, working on 'do it'..."))).thenThrow(
            IOException.class);
        integrator.work();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGithubParseError() throws Exception {
        final ScriptedRepository repo = mockRepository();
        when(prService.getPullRequests(eq(repo.getId()), eq("open")))
            .thenThrow(IOException.class);
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
        when(issueService.getComments(any(IRepositoryIdProvider.class), eq(1)))
            .thenReturn(comments);
    }

    private ScriptedRepository mockRepository(String regex) {
        final ScriptedRepository repository = new ScriptedRepository("user",
            "repo", regex, "echo");
        when(repositories.iterator()).thenReturn(
            asList(repository).iterator());
        return repository;
    }

    private ScriptedRepository mockRepository() {
        return mockRepository("do it");
    }

    private void mockPullRequest() throws IOException {
        when(
            prService.getPullRequests(any(IRepositoryIdProvider.class),
                eq("open"))).thenReturn(asList(mockPR()));
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
