package com.carlosbecker.model;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.eclipse.egit.github.core.RepositoryId;

@Log4j
@Getter
@RequiredArgsConstructor
public class ScriptedRepository {
    private final String owner;
    private final String name;
    private final String regex;
    private final String script;

    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }

    public boolean should(String body) {
        log.info(String.format("Matching '%s' against '%s'...", body, regex));
        if (body == null)
            return false;
        return compile(regex, CASE_INSENSITIVE)
                .matcher(body)
                .matches();
    }
}
