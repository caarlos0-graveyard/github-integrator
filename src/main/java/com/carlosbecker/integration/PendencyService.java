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
package com.carlosbecker.integration;

import com.carlosbecker.model.ScriptedRepository;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.egit.github.core.Comment;

/**
 * Verifies if there is any pendencies to process
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class PendencyService {
    /**
     * Filter all comments down to pendencies.
     * @param repository Scripted Repository
     * @param comments Comments
     * @return A map of pendencies ready to be processed
     */
    public Map<List<String>, Long> filter(ScriptedRepository repository,
        List<Comment> comments) {
        final Map<String, Long> reducedComments = comments
            .stream()
            .map(comment -> comment.getBody())
            .collect(
                Collectors.groupingBy(
                    Function.identity(),
                    Collectors.counting()
                    )
                );
        return getPendencies(repository, reducedComments);
    }

    /**
     * Remove already processed pendencies.
     * @param repository Scripted Repository
     * @param comments All comments
     * @return Filtered list of comments
     */
    private Map<List<String>, Long> getPendencies(
        ScriptedRepository repository, final Map<String, Long> comments) {
        final Map<List<String>, Long> pendencies = Maps.newHashMap();
        for (final Entry<String, Long> entry : comments.entrySet()) {
            verifyPossiblePendency(entry, pendencies, repository);
        }
        return pendencies.entrySet()
            .stream()
            .filter(entry -> entry.getValue() > 0)
            .collect(
                Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> entry.getValue()
                    )
                );
    }

    /**
     * Add the given entry to the pendency map if needed.
     * @param entry Possible pendency
     * @param pendencies Current pendency map.
     * @param repository Scripted Repository
     */
    private void verifyPossiblePendency(final Entry<String, Long> entry,
        final Map<List<String>, Long> pendencies,
        ScriptedRepository repository) {
        final String key = entry.getKey();
        if (repository.isAsk(key)) {
            final List<String> params = repository.getParams(key);
            pendencies.put(
                params,
                pendencies.getOrDefault(params, 0L) + entry.getValue()
                );
        } else if (repository.isReply(key)) {
            final List<String> params = repository.getReplyParams(key);
            pendencies.put(
                params,
                pendencies.getOrDefault(params, 0L) - entry.getValue()
                );
        }
    }
}
