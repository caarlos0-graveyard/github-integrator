package com.carlosbecker.model;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class ScriptedRepositoryTest {

    @Test
    public void testBasicRegex() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "test this please", "");
        assertThat(subject.isAsking("test this please"), equalTo(true));
        assertThat(subject.isAsking("test this please..."), equalTo(true));
        assertThat(subject.isAsking("test this please!"), equalTo(true));
        assertThat(subject.isAsking("test this please;"), equalTo(true));
        assertThat(subject.isAsking("test this pLEase"), equalTo(true));
        assertThat(subject.isAsking("dont test this please"), equalTo(false));
    }

    @Test
    public void testRegexWithOptional() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "test this(,? please)?", "");
        assertThat(subject.isAsking("test this, please"), equalTo(true));
        assertThat(subject.isAsking("test this..."), equalTo(true));
        assertThat(subject.isAsking("test this, please!"), equalTo(true));
        assertThat(subject.isAsking("test this!!"), equalTo(true));
        assertThat(subject.isAsking("test thIs"), equalTo(true));
        assertThat(subject.isAsking("dont test this, please"), equalTo(false));
    }

    @Test
    public void testGetGroups() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "",
                "deploy this to (production|staging)(,? please)?", "");
        assertThat(subject.isAsking("deploy this, please"), equalTo(false));
        assertThat(subject.isAsking("deploy this..."), equalTo(false));
        assertThat(subject.isAsking("deploy this, please!"), equalTo(false));
        assertThat(subject.isAsking("deploy this!!"), equalTo(false));
        assertThat(subject.isAsking("deploy thIs"), equalTo(false));
        assertThat(subject.isAsking("deploy this to production..."), equalTo(true));
        assertThat(subject.isAsking("deploy this to staging, please..."), equalTo(true));
        assertThat(subject.getParams("deploy this to production, please"), equalTo(asList("production", ", please")));
    }

    @Test
    public void testGetParamsWithNoBody() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "", "", "");
        assertThat(subject.getParams(null), equalTo(newArrayList()));
        assertThat(subject.getParams(""), equalTo(newArrayList()));
        assertThat(subject.getParams("  "), equalTo(newArrayList()));
    }

    @Test
    public void testIsReply() throws Exception {
        final ScriptedRepository subject = new ScriptedRepository("", "",
                "deploy this to (production|staging)(,? please)?", "");
        assertThat(subject.isReply("Ok, working on 'deploy this to production, please'..."), equalTo(true));
    }
}
