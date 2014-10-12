package com.carlosbecker.model;

import java.util.regex.Pattern;
import lombok.Data;
import lombok.experimental.Builder;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.RepositoryId;

@Data
@Log4j
@Builder
public class ScriptedRepository {
    private final String owner;
    private final String name;
    private final String regex;
    private final String script;

    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }

    public boolean should(String body) {
        log.info(String.format("Matching '%s' agains '%s'...", body, regex));
        return Pattern.matches(regex, body);
    }
}
