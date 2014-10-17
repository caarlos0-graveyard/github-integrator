package com.carlosbecker;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import com.carlosbecker.github.IntegratorConfig;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.concurrent.atomic.AtomicBoolean;

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
    }

    @Test(timeout = 500)
    public void testRunTwice() throws Exception {
        final AtomicBoolean loop = new AtomicBoolean(true);
        when(config.loop()).then(invocation -> loop.getAndSet(false));
        app.run();
    }
}
