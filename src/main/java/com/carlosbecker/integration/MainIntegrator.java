package com.carlosbecker.integration;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.Arrays.asList;

import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;

import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class MainIntegrator {
    private final PullRequestService prService;
    private final IssueService issueService;
    private final ScriptedRepositories repositories;
    private final ProcessExecutor executor;
    private final PendencyService pendencyService;

    public void work() {
        final Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext())
            tryToWork(iterator.next());
    }

    private void tryToWork(final ScriptedRepository repository) {
        try {
            work(repository);
        } catch (final IOException e) {
            log.info(String.format("Failed to process repository '%s' due to a network or github error.",
                    repository.getId()), e);
        }
    }

    private void work(ScriptedRepository repository) throws IOException {
        final List<PullRequest> prlist = prService.getPullRequests(repository.getId(), "open");
        log.info(format("Repository %s has %d open PRs...", repository.getId().toString(), prlist.size()));
        for (final PullRequest pr : prlist)
            process(repository, pr);
    }

    private void process(ScriptedRepository repository, PullRequest pr) throws IOException {
        final List<Comment> comments = issueService.getComments(repository.getId(), pr.getNumber());
        pendencyService.filter(repository, comments).entrySet()
                .forEach(entry -> processPendency(repository, pr, entry));
    }

    private void processPendency(ScriptedRepository repository, PullRequest pr, Entry<List<String>, Long> entry) {
        try {
            work(repository, pr, entry.getKey());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void work(ScriptedRepository repository, PullRequest pr, List<String> params) throws IOException {
        log.info("Running " + repository.getScript() + "...");
        issueService.createComment(repository.getOwner(), repository.getName(), pr.getNumber(),
                repository.getReplyMessage(params));
        executor.execute(repository.getScript(), buildParamList(pr, params));
    }

    private List<String> buildParamList(PullRequest pr, List<String> params) {
        final PullRequestMarker head = pr.getHead();
        final Repository headRepo = head.getRepo();
        final List<String> parsedParams = newArrayList();
        parsedParams.addAll(asList(headRepo.getOwner().getLogin(),
                headRepo.getName(),
                head.getRef(),
                "" + pr.getNumber()));
        parsedParams.addAll(params);
        return parsedParams;
    }
}
