package com.carlosbecker.integration;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.atomic.AtomicInteger;

public class AppRunnerTest {
    private AppRunner app;
    @Mock
    private IntegratorConfig config;
    @Mock
    private MainIntegrator integrator;

    @Before
    public void init() {
        initMocks(this);
        app = new AppRunner(config, integrator);
    }

    @Test(timeout = 500)
    public void testRunOnce() throws Exception {
        app.run();
        verify(integrator).work();
    }

    @Test(timeout = 500)
    public void testRunTwice() throws Exception {
        final AtomicInteger loop = new AtomicInteger(0);
        when(config.loop()).then(invocation -> loop.incrementAndGet() < 2);
        app.run();
        verify(integrator, times(2)).work();
    }
}
