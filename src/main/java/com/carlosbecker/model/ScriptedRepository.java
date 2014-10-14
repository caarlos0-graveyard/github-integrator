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
    public static final String REPLY_START = "Ok, working on '";
    public static final String REPLY_END = "'...";
    private final String owner;
    private final String name;
    private final String regex;
    private final String script;

    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }

    public boolean isAsking(String body) {
        return patternFor(regex)
                .matcher(body)
                .matches();
    }

    private Pattern patternFor(String exp) {
        return compile(String.format("^%s([^a-zA-Z0-9\\s]*)?$", exp), CASE_INSENSITIVE);
    }

    public String getReplyMessage() {
        return format("%s%s%s", REPLY_START, regex, REPLY_END);
    }

    public boolean isReply(String body) {
        return isAsking(extractOriginalAsk(body));
    }

    private String extractOriginalAsk(String body) {
        return body
                .replaceAll(format("^%s", REPLY_START), "")
                .replaceAll(format("%s$", REPLY_END), "");
    }

    public List<String> getReplyParams(String body) {
        return getParams(extractOriginalAsk(body));
    }

    public List<String> getParams(String body) {
        if (isNullOrEmpty(body) || !isAsking(body))
            return newArrayList();
        return buildParamList(body);
    }

    private List<String> buildParamList(String body) {
        final List<String> params = newArrayList();
        final Matcher matcher = patternFor(regex).matcher(body);
        matcher.matches();
        for (int i = 1; i <= matcher.groupCount(); i++)
            if (!matcher.group(i).replaceAll(" ", "").isEmpty())
                params.add(matcher.group(i));
        return params;
    }
}
