package com.carlosbecker;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
import com.google.common.collect.Maps;

@Log4j
public class MainIntegrator {
    private final PullRequestService prService;
    private final IssueService issueService;
    private final ScriptedRepositories repositories;
    private final ProcessExecutor executor;

    @Inject
    public MainIntegrator(PullRequestService prService, IssueService issueService, ScriptedRepositories repositories,
            ProcessExecutor executor) {
        super();
        this.prService = prService;
        this.issueService = issueService;
        this.repositories = repositories;
        this.executor = executor;
    }

    public void work() throws IOException {
        final Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext())
            work(iterator.next());
    }

    private void work(ScriptedRepository scriptedRepository) throws IOException {
        final List<PullRequest> prlist = prService.getPullRequests(scriptedRepository.getId(), "open");
        log.info(format("Repository %s has %d open PRs...", scriptedRepository.getId().toString(), prlist.size()));
        for (final PullRequest pr : prlist)
            process(scriptedRepository, pr);
    }

    private void process(ScriptedRepository scriptedRepository, PullRequest pr) throws IOException {
        final List<Comment> comments = issueService.getComments(scriptedRepository.getId(), pr.getNumber());
        final Map<String, Long> reducedComments = comments.stream()
                .map(comment -> comment.getBody())
                .collect(groupingBy(identity(), counting()));
        final Map<List<String>, Long> meh = Maps.newHashMap();
        for (final Entry<String, Long> entry : reducedComments.entrySet()) {
            final String key = entry.getKey();
            if (scriptedRepository.isAsking(key)) {
                final List<String> params = scriptedRepository.getParams(key);
                meh.put(params, meh.getOrDefault(params, 0L) + 1);
            } else if (scriptedRepository.isReply(key)) {
                final List<String> params = scriptedRepository.getReplyParams(key);
                meh.put(params, meh.getOrDefault(params, 0L) - 1);
            }

        }
        meh.entrySet().forEach(request -> processEntry(scriptedRepository, pr, request));
    }

    private void processEntry(ScriptedRepository scriptedRepository, PullRequest pr, Entry<List<String>, Long> entry) {
        if (entry.getValue() > 0L)
            try {
                work(scriptedRepository, pr, entry.getKey());
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
    }

    private void work(ScriptedRepository repository, PullRequest pr, List<String> params) throws IOException {
        log.info("Running " + repository.getScript() + "...");
        issueService.createComment(repository.getOwner(), repository.getName(), pr.getNumber(),
                repository.getReplyMessage());
        final PullRequestMarker head = pr.getHead();
        final Repository headRepo = head.getRepo();
        final List<String> parsedParams = asList(headRepo.getOwner().getLogin(), headRepo.getName(), head.getRef(), ""
                + pr.getNumber());
        parsedParams.addAll(params);
        executor.execute(repository.getScript(), parsedParams);
    }
}
