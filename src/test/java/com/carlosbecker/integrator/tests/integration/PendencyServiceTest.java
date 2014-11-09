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
package com.carlosbecker.integrator.tests.integration;

import com.carlosbecker.integrator.integration.PendencyService;
import com.carlosbecker.integrator.model.ScriptedRepository;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.egit.github.core.Comment;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the pendency service.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class PendencyServiceTest {
    /**
     * Service.
     */
    private PendencyService service;

    /**
     * Tear up.
     */
    @Before
    public void init() {
        service = new PendencyService();
    }

    /**
     * Test empty comment list.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testNoComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "", ""),
            Lists.newArrayList()
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(0));
    }

    /**
     * Test non matching comment list.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testNonMatchingComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do this", ""),
            this.commentList("do whatever")
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(0));
    }

    /**
     * Test one comment.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testOneComment() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do this", ""),
            this.commentList("do this")
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList()),
            CoreMatchers.equalTo(1L)
            );
    }

    /**
     * Test two comment.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testTwoComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do this", ""),
            this.commentList("do this", "do this")
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList()),
            CoreMatchers.equalTo(2L)
            );
    }

    /**
     * Test two matching comments.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testTwoMatchingComments() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do it", ""),
            this.commentList("do it", "do IT")
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList()),
            CoreMatchers.equalTo(2L)
            );
    }

    /**
     * Test two matching comments with one already processed.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testTwoCommentsWithOneProcessed() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do this", ""),
            this.commentList(
                "do this",
                "do this",
                "Ok, working on 'do this'..."
                )
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList()),
            CoreMatchers.equalTo(1L)
            );
    }

    /**
     * Test two matching comments with one already processed with different
     * params.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testWhenOneWasAlreadyExecutedWithDifferentParams()
        throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do (it|that)", ""),
            commentList(
                "do it",
                "Ok, working on 'do it'...",
                "Ok, working on 'do it'...",
                "do that"
                )
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(1));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList("that")),
            CoreMatchers.equalTo(1L)
            );
    }

    /**
     * Test two matching comments with one already processed with multiple
     * replies.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testWithMultipleReplies() throws Exception {
        final Map<List<String>, Long> pendencies = service.filter(
            new ScriptedRepository("", "", "do (it|that)", ""),
            commentList(
                "do it",
                "do it",
                "Ok, working on 'do it'...",
                "Ok, working on 'do it'...",
                "do it",
                "do that"
                )
            );
        Assert.assertThat(pendencies.size(), CoreMatchers.equalTo(2));
        Assert.assertThat(
            pendencies.get(Lists.newArrayList("it")),
            CoreMatchers.equalTo(1L)
            );
        Assert.assertThat(
            pendencies.get(Lists.newArrayList("that")),
            CoreMatchers.equalTo(1L)
            );
    }

    /**
     * Builds a list of comments for the given messages.
     * @param messages String list.
     * @return List of Comment.
     * @throws IOException If something goes wrong.
     */
    private List<Comment> commentList(final String... messages)
        throws IOException {
        return Stream.of(messages)
            .map(body -> {
                final Comment comment = new Comment();
                comment.setBody(body);
                return comment;
            })
            .collect(Collectors.toList());
    }
}
