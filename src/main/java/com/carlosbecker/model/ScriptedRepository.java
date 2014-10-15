package com.carlosbecker.model;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import org.eclipse.egit.github.core.RepositoryId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScriptedRepository {
    private static final String PLEASE = "(,? please)?";
    private static final String REQUEST_REGEX = "^%s%s([^a-zA-Z0-9\\s]*)?$";
    public static final String REPLY_START = "Ok, working on '";
    public static final String REPLY_END = "'...";
    @Getter
    private final String owner;
    @Getter
    private final String name;
    private final String regex;
    @Getter
    private final String script;

    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }

    public boolean isAsk(String body) {
        return patternFor(regex)
                .matcher(body)
                .matches();
    }

    private Pattern patternFor(String exp) {
        return compile(format(REQUEST_REGEX, exp, PLEASE), CASE_INSENSITIVE);
    }

    public String getReplyMessage() {
        return format("%s%s%s", REPLY_START, regex, REPLY_END);
    }

    public boolean isReply(String body) {
        return isAsk(extractOriginalAsk(body));
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
        if (isNullOrEmpty(body) || !isAsk(body))
            return newArrayList();
        return buildParamList(body);
    }

    private List<String> buildParamList(String body) {
        final List<String> params = newArrayList();
        final Matcher matcher = patternFor(regex).matcher(body);
        matcher.matches();
        for (int i = 1; i <= matcher.groupCount(); i++)
            if (isValidParam(matcher.group(i)))
                params.add(matcher.group(i));
        return params;
    }

    private boolean isValidParam(final String param) {
        return !isNullOrEmpty(param) && !param.matches(PLEASE);
    }
}
