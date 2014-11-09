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
package com.carlosbecker.integrator.tests.integration;

import com.carlosbecker.integrator.integration.MainIntegrator;
import com.carlosbecker.integrator.integration.PendencyService;
import com.carlosbecker.integrator.model.ScriptedRepositories;
import com.carlosbecker.integrator.model.ScriptedRepository;
import com.carlosbecker.integrator.process.ProcessExecutor;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.VerificationModeFactory;

/**
 * Main integrator class tests.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class MainIntegratorTest {
    /**
     * Mock.
     */
    @Mock
    private transient ProcessExecutor executor;
    /**
     * Mock.
     */
    @Mock
    private transient ScriptedRepositories repositories;
    /**
     * Mock.
     */
    @Mock
    private transient IssueService issueService;
    /**
     * Mock.
     */
    @Mock
    private transient PullRequestService prService;
    /**
     * Mock.
     */
    @Mock
    private transient PendencyService pendencyService;
    /**
     * Integrator
     */
    private transient MainIntegrator integrator;

    /**
     * Tear up.
     */
    @Before
    public final void init() {
        MockitoAnnotations.initMocks(this);
        this.integrator = new MainIntegrator(
            prService,
            issueService,
            repositories,
            executor,
            pendencyService
            );
    }

    @Test
    public final void testNoRepositories() throws Exception {
        Mockito.when(this.repositories.iterator())
        .thenReturn(new ArrayList<ScriptedRepository>().iterator());
        this.integrator.work();
        Mockito.verifyZeroInteractions(issueService);
        Mockito.verifyZeroInteractions(prService);
    }

    @Test
    public final void testPRWithNoComments() throws Exception {
        this.mockPullRequest();
        this.mockRepository();
        Mockito.when(
            this.issueService.getComments(
                Matchers.any(IRepositoryIdProvider.class),
                Matchers.eq(1)
                )
            ).thenReturn(Lists.newArrayList());
        this.integrator.work();
        Mockito.verify(this.issueService, VerificationModeFactory.times(0))
        .createComment(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
            );
        Mockito.verifyZeroInteractions(this.executor);
    }

    @Test
    public final void testPRWithEmptyComment() throws Exception {
        this.mockPullRequest();
        this.mockRepository();
        this.mockComments("");
        this.integrator.work();
        Mockito.verify(this.issueService, VerificationModeFactory.times(0))
        .createComment(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
            );
        Mockito.verifyZeroInteractions(this.executor);
    }

    @Test
    public void testWithNonMatchingComment() throws Exception {
        this.mockPullRequest();
        this.mockRepository();
        this.mockComments("This will not match. Don't do it.");
        this.integrator.work();
        Mockito.verify(this.issueService, VerificationModeFactory.times(0))
        .createComment(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
            );
        Mockito.verifyZeroInteractions(this.executor);
    }

    @Test
    public final void testWhenOneWasAlreadyExecuted() throws Exception {
        this.mockPullRequest();
        this.mockRepository();
        this.mockComments("do it", "Ok, working on 'do it'...");
        this.integrator.work();
        Mockito.verify(this.issueService, VerificationModeFactory.times(0))
        .createComment(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString()
            );
        Mockito.verifyZeroInteractions(this.executor);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testModerateScenario() throws Exception {
        this.mockPullRequest();
        final ScriptedRepository repository = mockRepository("do (it|that)");
        this.mockComments(
            "do it",
            "do that",
            "Ok, working on 'do it'...",
            "Ok, working on 'do that'...",
            "do that again",
            "do it",
            "nice",
            "+1",
            ":+1:"
            );
        final Map<List<String>, Long> comments = Maps.newHashMap();
        comments.put(Lists.newArrayList("it"), 1L);
        Mockito.when(
            this.pendencyService.filter(
                Matchers.eq(repository),
                Matchers.any(List.class)
                )
            ).thenReturn(comments);
        this.integrator.work();
        Mockito.verify(this.issueService)
        .createComment(
            Matchers.eq("user"),
            Matchers.eq("repo"),
            Matchers.eq(1),
            Matchers.eq("Ok, working on 'do it'...")
            );
    }

    @SuppressWarnings("unchecked")
    @Test(expected = RuntimeException.class)
    public final void testCommentError() throws Exception {
        this.mockPullRequest();
        final ScriptedRepository repository = this.mockRepository();
        this.mockComments("do it");
        final Map<List<String>, Long> comments = Maps.newHashMap();
        comments.put(Lists.newArrayList(), 1L);
        Mockito.when(
            this.pendencyService.filter(
                Matchers.eq(repository),
                Matchers.any(List.class)
                )
            ).thenReturn(comments);
        Mockito.when(
            this.issueService.createComment(
                Matchers.eq("user"),
                Matchers.eq("repo"),
                Matchers.eq(1),
                Matchers.eq("Ok, working on 'do it'...")
                )
            ).thenThrow(IOException.class);
        this.integrator.work();
    }

    @Test
    @SuppressWarnings("unchecked")
    public final void testGithubParseError() throws Exception {
        final ScriptedRepository repo = this.mockRepository();
        Mockito.when(
            this.prService.getPullRequests(
                Matchers.eq(repo.getId()),
                Matchers.eq("open")
                )
            ).thenThrow(IOException.class);
        this.integrator.work();
        Mockito.verifyZeroInteractions(this.issueService);
    }

    private final void mockComments(String... messages) throws IOException {
        final List<Comment> comments = FluentIterable.from(
            Arrays.asList(messages)
            ).transform(input -> {
                final Comment comment = new Comment();
                comment.setBody(input);
                return comment;
            }).toList();
        Mockito.when(
            this.issueService.getComments(
                Matchers.any(IRepositoryIdProvider.class),
                Matchers.eq(1)
                )
            ).thenReturn(comments);
    }

    private ScriptedRepository mockRepository(String regex) {
        final ScriptedRepository repository = new ScriptedRepository(
            "user",
            "repo",
            regex,
            "echo"
            );
        Mockito.when(this.repositories.iterator())
        .thenReturn(Arrays.asList(repository).iterator());
        return repository;
    }

    private ScriptedRepository mockRepository() {
        return this.mockRepository("do it");
    }

    private void mockPullRequest() throws IOException {
        Mockito.when(
            this.prService.getPullRequests(
                Matchers.any(IRepositoryIdProvider.class),
                Matchers.eq("open")
                )
            ).thenReturn(Arrays.asList(this.mockPR()));
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
