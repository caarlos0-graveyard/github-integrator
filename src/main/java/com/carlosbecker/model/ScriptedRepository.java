package com.carlosbecker.model;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        return pattern()
                .matcher(body)
                .matches();
    }

    private Pattern pattern() {
        return compile(String.format("^%s([^a-zA-Z0-9\\s]*)?$", regex), CASE_INSENSITIVE);
    }

    public String getReplyMessage() {
        return format("Ok, working on '%s'...", regex);
    }

    public boolean isReply(String body) {
        return getReplyMessage().equalsIgnoreCase(body);
    }

    public List<String> getParams(String body) {
        final List<String> params = newArrayList();
        if (isNullOrEmpty(body) || !isAsking(body))
            return params;
        final Matcher matcher = pattern().matcher(body);
        matcher.matches();
        for (int i = 1; i <= matcher.groupCount(); i++)
            if (!matcher.group(i).replaceAll(" ", "").isEmpty())
                params.add(matcher.group(i));
        return params;
    }
}
