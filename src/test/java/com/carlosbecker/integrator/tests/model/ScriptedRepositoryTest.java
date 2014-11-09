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
package com.carlosbecker.integrator.tests.model;

import com.carlosbecker.integrator.model.ScriptedRepository;
import com.google.common.collect.Lists;
import java.util.Arrays;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Scripted Repository Test.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 * @checkstyle MultipleStringLiteralsCheck (200 lines)
 */
public class ScriptedRepositoryTest {

    /**
     * Basic Regex test.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testBasicRegex() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository(
            "",
            "",
            "test this",
            ""
        );
        Assert.assertThat(
            subject.isAsk("test this please"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this please..."),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this please!"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this please;"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this pLEase"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("dont test this please"),
            CoreMatchers.equalTo(false)
        );
    }

    /**
     * Basic Regex with optional group.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testRegexWithOptional() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository(
            "",
            "",
            "test this( for me)?",
            ""
        );
        Assert.assertThat(
            subject.isAsk("test this, please"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this for me..."),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this, please!"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this for me, please!"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test this!!"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("test thIs"),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("dont test this, please"),
            CoreMatchers.equalTo(false)
        );
    }

    /**
     * Basic get Regex group's values.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testGetGroups() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository(
            "",
            "",
            "deploy this to (production|staging)",
            ""
        );
        Assert.assertThat(
            subject.isAsk("deploy this, please"),
            CoreMatchers.equalTo(false)
        );
        Assert.assertThat(
            subject.isAsk("deploy this..."),
            CoreMatchers.equalTo(false)
        );
        Assert.assertThat(
            subject.isAsk("deploy this, please!"),
            CoreMatchers.equalTo(false)
        );
        Assert.assertThat(
            subject.isAsk("deploy this!!"),
            CoreMatchers.equalTo(false)
        );
        Assert.assertThat(
            subject.isAsk("deploy thIs"),
            CoreMatchers.equalTo(false)
        );
        Assert.assertThat(
            subject.isAsk("deploy this to production please..."),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.isAsk("deploy this to staging, please..."),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.getParams("deploy this to production, please"),
            CoreMatchers.equalTo(Arrays.asList("production"))
        );
    }

    /**
     * Basic get parameters from regex with no regex body.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testGetParamsWithNoBody() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository(
            "",
            "",
            "",
            ""
        );
        Assert.assertThat(
            subject.getParams(null),
            CoreMatchers.equalTo(Lists.newArrayList())
        );
        Assert.assertThat(
            subject.getParams(""),
            CoreMatchers.equalTo(Lists.newArrayList())
        );
        Assert.assertThat(
            subject.getParams("  "),
            CoreMatchers.equalTo(Lists.newArrayList())
        );
    }

    /**
     * Test if the getReply works as expected.
     * @throws Exception If something goes wrong.
     */
    @Test
    public final void testReply() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository(
            "",
            "",
            "deploy this to (production|staging)",
            ""
        );
        Assert.assertThat(
            subject.isReply(
                "Ok, working on 'deploy this to production, please'..."
            ),
            CoreMatchers.equalTo(true)
        );
        Assert.assertThat(
            subject.getReplyParams(
                "Ok, working on 'deploy this to production, please'..."
            ),
            CoreMatchers.equalTo(Arrays.asList("production"))
        );
    }
}
