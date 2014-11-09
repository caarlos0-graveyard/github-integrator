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

import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;

/**
 * The main integrator class.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@Log4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class MainIntegrator {
    private static final String PROCESS_FAIL_MSG = "Failed to process repository '%s' due to a network or github error.";
    /**
     * Deal with pull requests.
     */
    private final transient PullRequestService prService;
    /**
     * Deal with issues.
     */
    private final transient IssueService issueService;
    /**
     * Contains the configured repositories and its scripted routines.
     */
    private final transient ScriptedRepositories repositories;
    /**
     * Execute processes.
     */
    private final transient ProcessExecutor executor;
    /**
     * Deals with pendencies.
     */
    private final transient PendencyService pendencyService;

    /**
     * Loop throug repositories and work if there is something to do.
     */
    public void work() {
        final Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext()) {
            this.tryToWork(iterator.next());
        }
    }

    /**
     * Tries to process a repository.
     * @param repository Repository to be processed.
     */
    private void tryToWork(final ScriptedRepository repository) {
        try {
            this.work(repository);
        } catch (final IOException exception) {
            final RepositoryId id = repository.getId();
            final String msg = String.format(PROCESS_FAIL_MSG, id);
            log.info(msg, exception);
        }
    }

    /**
     * Iterates on all open PRs, get comments and work on them if needed.
     * @param repository Repository to work on.
     * @throws IOException If can't connect.
     */
    private void work(final ScriptedRepository repository) throws IOException {
        final List<PullRequest> prlist = prService.getPullRequests(
            repository.getId(),
            "open"
            );
        final String msg = String.format(
            "Repository %s has %d open PRs...",
            repository.getId().toString(),
            prlist.size()
            );
        log.info(msg);
        for (final PullRequest pr : prlist) {
            this.process(repository, pr);
        }
    }

    /**
     * Iterates the pullrequests comments.
     * @param repository Scripted repository
     * @param pr Pull Request
     * @throws IOException If can't connect.
     */
    private void process(final ScriptedRepository repository,
        final PullRequest pr) throws IOException {
        final List<Comment> comments = issueService.getComments(
            repository.getId(),
            pr.getNumber()
            );
        this.pendencyService
        .filter(repository, comments)
        .entrySet()
        .forEach(entry -> this.processPendency(repository, pr, entry));
    }

    /**
     * Tries to process the given pendency
     * @param repository Scripted Repository
     * @param pr Pull Request
     * @param entry Pendency to be processed.
     */
    private void processPendency(final ScriptedRepository repository,
        final PullRequest pr, final Entry<List<String>, Long> entry) {
        try {
            this.work(repository, pr, entry.getKey());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Process the given pendency
     * @param repository Scripted Repository
     * @param pr Pull Request
     * @param params Params.
     * @throws IOException If fails to connect
     */
    private void work(final ScriptedRepository repository,
        final PullRequest pr,
        final List<String> params) throws IOException {
        log.info("Running " + repository.getScript() + "...");
        this.issueService.createComment(
            repository.getOwner(),
            repository.getName(),
            pr.getNumber(),
            repository.getReplyMessage(params)
            );
        this.executor.execute(
            repository.getScript(),
            buildParamList(pr, params)
            );
    }

    /**
     * Build the param list to pass down to the script.
     * @param pr Pull Request
     * @param params Params to append on
     * @return A list of params
     */
    private List<String> buildParamList(final PullRequest pr,
        final List<String> params) {
        final PullRequestMarker head = pr.getHead();
        final Repository headRepo = head.getRepo();
        final List<String> parsedParams = Lists.newArrayList();
        parsedParams.addAll(
            Arrays.asList(
                headRepo.getOwner().getLogin(),
                headRepo.getName(),
                head.getRef(),
                "" + pr.getNumber()
                )
            );
        parsedParams.addAll(params);
        return parsedParams;
    }
}
