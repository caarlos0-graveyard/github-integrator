/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Carlos Alexandro Becker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

    public String getReplyMessage(List<String> params) {
        String message = regex;
        for (final String param : params)
            message = regex.replaceFirst("\\(.*\\)", param);
        return format("%s%s%s", REPLY_START, message, REPLY_END);
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
