package com.carlosbecker.model;

import static java.lang.String.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.egit.github.core.RepositoryId;

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

    public boolean isAsking(String body) {
        return compile(regex, CASE_INSENSITIVE)
                .matcher(body)
                .matches();
    }

    public String getReplyMessage() {
        return format("Ok, working on '%s'...", regex);
    }

    public boolean isReply(String body) {
        return getReplyMessage().equalsIgnoreCase(body);
    }
}
