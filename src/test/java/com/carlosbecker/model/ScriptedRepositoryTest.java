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

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class ScriptedRepositoryTest {

    @Test
    public void testBasicRegex() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "test this", "");
        assertThat(subject.isAsk("test this please"), equalTo(true));
        assertThat(subject.isAsk("test this please..."), equalTo(true));
        assertThat(subject.isAsk("test this please!"), equalTo(true));
        assertThat(subject.isAsk("test this please;"), equalTo(true));
        assertThat(subject.isAsk("test this pLEase"), equalTo(true));
        assertThat(subject.isAsk("dont test this please"), equalTo(false));
    }

    @Test
    public void testRegexWithOptional() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "test this( for me)?", "");
        assertThat(subject.isAsk("test this, please"), equalTo(true));
        assertThat(subject.isAsk("test this for me..."), equalTo(true));
        assertThat(subject.isAsk("test this, please!"), equalTo(true));
        assertThat(subject.isAsk("test this for me, please!"), equalTo(true));
        assertThat(subject.isAsk("test this!!"), equalTo(true));
        assertThat(subject.isAsk("test thIs"), equalTo(true));
        assertThat(subject.isAsk("dont test this, please"), equalTo(false));
    }

    @Test
    public void testGetGroups() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "deploy this to (production|staging)", "");
        assertThat(subject.isAsk("deploy this, please"), equalTo(false));
        assertThat(subject.isAsk("deploy this..."), equalTo(false));
        assertThat(subject.isAsk("deploy this, please!"), equalTo(false));
        assertThat(subject.isAsk("deploy this!!"), equalTo(false));
        assertThat(subject.isAsk("deploy thIs"), equalTo(false));
        assertThat(subject.isAsk("deploy this to production please..."), equalTo(true));
        assertThat(subject.isAsk("deploy this to staging, please..."), equalTo(true));
        assertThat(subject.getParams("deploy this to production, please"), equalTo(asList("production")));
    }

    @Test
    public void testGetParamsWithNoBody() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "", "");
        assertThat(subject.getParams(null), equalTo(newArrayList()));
        assertThat(subject.getParams(""), equalTo(newArrayList()));
        assertThat(subject.getParams("  "), equalTo(newArrayList()));
    }

    @Test
    public void testReply() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "deploy this to (production|staging)", "");
        assertThat(subject.isReply("Ok, working on 'deploy this to production, please'..."), equalTo(true));
        assertThat(subject.getReplyParams("Ok, working on 'deploy this to production, please'..."),
                equalTo(asList("production")));
    }
}
