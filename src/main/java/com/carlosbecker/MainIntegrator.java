package com.carlosbecker;

import static java.lang.String.format;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import com.carlosbecker.model.ScriptedRepositories;
import com.carlosbecker.model.ScriptedRepository;
import com.carlosbecker.process.ProcessExecutor;

@Log4j
public class MainIntegrator {
    @Inject
    private PullRequestService prService;
    @Inject
    private IssueService issueService;
    @Inject
    private ScriptedRepositories repositories;
    @Inject
    private ProcessExecutor executor;

    public void work() throws IOException {
        Iterator<ScriptedRepository> iterator = repositories.iterator();
        while (iterator.hasNext())
            work(iterator.next());
    }

    private void work(ScriptedRepository scriptedRepository) throws IOException {
        // TODO: it may process the same thing more than one time!!
        List<PullRequest> prlist = prService.getPullRequests(scriptedRepository.getId(), "open");
        log.info(format("Repository %s has %d open PRs...", scriptedRepository.getId().toString(), prlist.size()));
        for (PullRequest pr : prlist)
            for (Comment comment : issueService.getComments(scriptedRepository.getId(), pr.getNumber()))
                if (scriptedRepository.should(comment.getBody()))
                    work(scriptedRepository, pr);
    }

    private void work(ScriptedRepository repository, PullRequest pr) throws IOException {
        log.info("Running " + repository.getScript() + "...");
        issueService.createComment(repository.getOwner(), repository.getName(), pr.getNumber(),
                format("Ok, working on '%s'...", repository.getRegex()));
        Repository baseRepo = pr.getBase().getRepo();
        executor.execute(repository.getScript(), baseRepo.getOwner().getLogin(), baseRepo.getName(),
                "" + pr.getNumber());
    }
}
