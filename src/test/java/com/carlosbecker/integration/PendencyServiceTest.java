package com.carlosbecker.integration;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.carlosbecker.model.ScriptedRepository;

import org.eclipse.egit.github.core.Comment;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PendencyServiceTest {
    private PendencyService service;

    @Before
    public void init() {
        service = new PendencyService();
    }

    @Test
    public void testNoComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "", ""),
                newArrayList());
        assertThat(pendencies.size(), equalTo(0));
    }

    @Test
    public void testNonMatchingComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do this", ""),
                commentList("do whatever"));
        assertThat(pendencies.size(), equalTo(0));
    }

    @Test
    public void testOneComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do this", ""),
                commentList("do this"));
        assertThat(pendencies.size(), equalTo(1));
        assertThat(pendencies.get(newArrayList()), equalTo(1L));
    }

    @Test
    public void testTwoComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do this", ""),
                commentList("do this", "do this"));
        assertThat(pendencies.size(), equalTo(1));
        assertThat(pendencies.get(newArrayList()), equalTo(2L));
    }

    @Test
    public void testTwoMatchingComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do it", ""),
                commentList("do it", "do IT"));
        assertThat(pendencies.size(), equalTo(1));
        assertThat(pendencies.get(newArrayList()), equalTo(2L));
    }

    @Test
    public void testTwoCommentsWithOneProcessed() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do this", ""),
                commentList("do this", "do this", "Ok, working on 'do this'..."));
        assertThat(pendencies.size(), equalTo(1));
        assertThat(pendencies.get(newArrayList()), equalTo(1L));
    }

    @Test
    public void testWhenOneWasAlreadyExecutedWithDifferentParams() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(new ScriptedRepository("", "", "do (it|that)", ""),
                commentList("do it", "Ok, working on 'do it'...", "Ok, working on 'do it'...", "do that"));
        assertThat(pendencies.size(), equalTo(1));
        assertThat(pendencies.get(newArrayList("that")), equalTo(1L));
    }

    @Test
    public void testWithMultipleReplies() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
                new ScriptedRepository("", "", "do (it|that)", ""),
                commentList("do it", "do it", "Ok, working on 'do it'...", "Ok, working on 'do it'...", "do it",
                        "do that"));
        assertThat(pendencies.size(), equalTo(2));
        assertThat(pendencies.get(newArrayList("it")), equalTo(1L));
        assertThat(pendencies.get(newArrayList("that")), equalTo(1L));
    }

    private List<Comment> commentList(String... messages) throws IOException {
        return Stream.of(messages)
                .map(body -> {
                    final Comment comment = new Comment();
                    comment.setBody(body);
                    return comment;
                })
                .collect(toList());
    }
}
