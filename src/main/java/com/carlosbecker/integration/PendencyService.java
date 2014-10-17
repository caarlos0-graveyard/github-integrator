package com.carlosbecker.integration;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.carlosbecker.model.ScriptedRepository;
import com.google.common.collect.Maps;

import org.eclipse.egit.github.core.Comment;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PendencyService {
    public Map<List<String>, Long> filter(ScriptedRepository repository, List<Comment> comments) {
        final Map<String, Long> reducedComments = comments.stream()
                .map(comment -> comment.getBody())
                .collect(groupingBy(identity(), counting()));
        return getPendencies(repository, reducedComments);
    }

    private Map<List<String>, Long> getPendencies(ScriptedRepository repository, final Map<String, Long> comments) {
        final Map<List<String>, Long> pendencies = Maps.newHashMap();
        for (final Entry<String, Long> entry : comments.entrySet())
            verifyPossiblePendency(entry, pendencies, repository);
        return pendencies.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    private void verifyPossiblePendency(final Entry<String, Long> entry, final Map<List<String>, Long> pendencies,
            ScriptedRepository repository) {
        final String key = entry.getKey();
        if (repository.isAsk(key)) {
            final List<String> params = repository.getParams(key);
            pendencies.put(params, pendencies.getOrDefault(params, 0L) + entry.getValue());
        } else if (repository.isReply(key)) {
            final List<String> params = repository.getReplyParams(key);
            pendencies.put(params, pendencies.getOrDefault(params, 0L) - entry.getValue());
        }
    }
}
