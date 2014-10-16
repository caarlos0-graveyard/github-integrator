package com.carlosbecker;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;
import com.google.common.collect.Maps;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.PullRequestMarker;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j;

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

    public void work() {
        final Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext()) {
            final ScriptedRepository repository = iterator.next();
            try {
                work(repository);
            } catch (final IOException e) {
                log.info(String.format("Failed to process repository '%s' due to a network or github error.",
                        repository.getId()), e);
            }
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
        final Map<String, Long> reducedComments = comments.stream()
                .map(comment -> comment.getBody())
                .collect(groupingBy(identity(), counting()));
        getPendencies(repository, reducedComments)
        .entrySet()
        .forEach(request -> processPendency(repository, pr, request));
    }

    private Map<List<String>, Long> getPendencies(ScriptedRepository repository, final Map<String, Long> comments) {
        final Map<List<String>, Long> pendencies = Maps.newHashMap();
        for (final Entry<String, Long> entry : comments.entrySet())
            verifyPossiblePendency(entry.getKey(), pendencies, repository);
        return pendencies.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    private void verifyPossiblePendency(final String key, final Map<List<String>, Long> pendencies,
            ScriptedRepository repository) {
        if (repository.isAsk(key)) {
            final List<String> params = repository.getParams(key);
            pendencies.put(params, pendencies.getOrDefault(params, 0L) + 1);
        } else if (repository.isReply(key)) {
            final List<String> params = repository.getReplyParams(key);
            pendencies.put(params, pendencies.getOrDefault(params, 0L) - 1);
        }
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
                repository.getReplyMessage());
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
