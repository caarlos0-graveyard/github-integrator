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
