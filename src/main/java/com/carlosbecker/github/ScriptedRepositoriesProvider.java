package com.carlosbecker.github;

import static com.google.common.collect.FluentIterable.from;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.RepositoryId;
import com.google.common.base.Function;
import com.google.inject.Provider;

@Log4j
public class ScriptedRepositoriesProvider implements
        Provider<ScriptedRepositories> {
    private final IntegratorConfig config;

    @Inject
    public ScriptedRepositoriesProvider(IntegratorConfig config) {
        this.config = config;
    }

    @Override
    public ScriptedRepositories get() {
        if (config.executions() == null)
            return new ScriptedRepositories(null);
        return parse();
    }

    private ScriptedRepositories parse() {
        Properties executions = new Properties();
        try {
            executions.load(new FileInputStream(new File(config.executions())));
        } catch (IOException e) {
            log.error("Failed to load executions script", e);
            return new ScriptedRepositories(null);
        }
        List<ScriptedRepository> repositories = from(executions.entrySet())
                .transform(new ScriptedRepositoryMapper()).toList();
        return new ScriptedRepositories(repositories);
    }

    class ScriptedRepositoryMapper implements
            Function<Entry<Object, Object>, ScriptedRepository> {
        @Override
        public ScriptedRepository apply(Entry<Object, Object> entry) {
            return ScriptedRepository.builder()
                    .id(parse(entry.getKey().toString()))
                    .script(entry.getValue().toString()).build();
        }
    }

    private RepositoryId parse(String repo) {
        repo = repo.replaceAll(" ", "");
        int slash = repo.indexOf('/');
        return new RepositoryId(repo.substring(0, slash),
                repo.substring(slash + 1));
    }
}
