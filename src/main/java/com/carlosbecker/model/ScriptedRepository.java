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
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.eclipse.egit.github.core.RepositoryId;

/**
 * A repository that is scripted.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@RequiredArgsConstructor
public class ScriptedRepository {
    private static final String PLEASE = "(,? please)?";
    private static final String REQUEST_REGEX = "^%s%s([^a-zA-Z0-9\\s]*)?$";
    public static final String REPLY_START = "Ok, working on '";
    public static final String REPLY_END = "'...";
    /**
     * The repository owner
     */
    @Getter
    private final String owner;
    /**
     * The repository name
     */
    @Getter
    private final String name;
    /**
     * The regex to process
     */
    private final String regex;
    /**
     * The script to execute
     */
    @Getter
    private final String script;

    /**
     * Builds an repository id for this scripted repository
     * @return A RepositoryId representing this repository
     */
    public RepositoryId getId() {
        return new RepositoryId(owner, name);
    }

    /**
     * Checks wether the given comment is requesting something from this
     * Scripted Repository
     * @param body Comment body
     * @return True if this repository should process this comment, false
     *         otherwise
     */
    public boolean isAsk(final String body) {
        return pattern()
            .matcher(body)
            .matches();
    }

    /**
     * Builds a pattern for the repository's regex.
     * @return A Pattern
     */
    private Pattern pattern() {
        return Pattern.compile(
            String.format(REQUEST_REGEX, regex, PLEASE),
            Pattern.CASE_INSENSITIVE
            );
    }

    /**
     * Builds a reply message for the given params
     * @param params A param list
     * @return A reply
     */
    public String getReplyMessage(final List<String> params) {
        String message = regex;
        for (final String param : params) {
            message = regex.replaceFirst("\\(.*\\)", param);
        }
        return String.format("%s%s%s", REPLY_START, message, REPLY_END);
    }

    /**
     * Checks wether the given comment is replying something from this
     * Scripted Repository
     * @param body Comment body
     * @return True if this repository processed this comment, false otherwise
     */
    public boolean isReply(final String body) {
        return isAsk(extractOriginalAsk(body));
    }

    /**
     * Get the request message for the given reply.
     * @param body Reply body
     * @return The possible request body
     */
    private String extractOriginalAsk(final String body) {
        return body
            .replaceAll(String.format("^%s", REPLY_START), "")
            .replaceAll(String.format("%s$", REPLY_END), "");
    }

    /**
     * Get the reply parameters
     * @param body Reply body
     * @return List of parameters
     */
    public List<String> getReplyParams(final String body) {
        return getParams(extractOriginalAsk(body));
    }

    /**
     * Extracts the parameters of a request body
     * @param body Request body
     * @return List of parameters
     */
    public List<String> getParams(final String body) {
        if (Strings.isNullOrEmpty(body) || !isAsk(body))
            return Lists.newArrayList();
        return buildParamList(body);
    }

    private List<String> buildParamList(final String body) {
        final List<String> params = Lists.newArrayList();
        final Matcher matcher = pattern().matcher(body);
        matcher.matches();
        for (int groupIdx = 1; groupIdx <= matcher.groupCount(); groupIdx++) {
            if (isValidParam(matcher.group(groupIdx))) {
                params.add(matcher.group(groupIdx));
            }
        }
        return params;
    }

    /**
     * Checks wether the given param is valid.
     * @param param Param to check
     * @return True if valid, false otherwise.
     */
    private boolean isValidParam(final String param) {
        return !Strings.isNullOrEmpty(param) && !param.matches(PLEASE);
    }
}
