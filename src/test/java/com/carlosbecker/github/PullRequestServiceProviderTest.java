package com.carlosbecker.github;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.eclipse.egit.github.core.service.PullRequestService;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.carlosbecker.guice.GuiceModules;
import com.carlosbecker.guice.GuiceTestRunner;

@RunWith(GuiceTestRunner.class)
@GuiceModules(GithubModule.class)
public class PullRequestServiceProviderTest {

    @Inject
    private PullRequestService service;

    @Test
    public void testProvided() throws Exception {
        assertThat(service, notNullValue());
        assertThat(service.getClient(), notNullValue());
    }
}
