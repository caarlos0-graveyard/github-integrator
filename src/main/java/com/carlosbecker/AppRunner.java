package com.carlosbecker;

import com.carlosbecker.github.IntegratorConfig;

import javax.inject.Inject;

import lombok.extern.log4j.Log4j;

@Log4j
public class AppRunner {
    private final IntegratorConfig config;
    private final MainIntegrator integrator;

    @Inject
    public AppRunner(IntegratorConfig config, MainIntegrator integrator) {
        this.config = config;
        this.integrator = integrator;
    }

    public void run() throws Exception {
        do {
            runOnce();
        } while (config.loop());
    }

    private void runOnce() throws InterruptedException {
        log.info("Running...");
        integrator.work();
        Thread.sleep(config.period() * 1000);
    }
}
