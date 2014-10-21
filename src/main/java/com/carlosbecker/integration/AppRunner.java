package com.carlosbecker.integration;

import javax.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class AppRunner {
    private final IntegratorConfig config;
    private final MainIntegrator integrator;

    public void run() throws Exception {
        do {
            runOnce();
        } while (config.loop());
    }

    private void runOnce() throws InterruptedException {
        log.info("Running...");
        integrator.work();
        log.info("Waiting...");
        Thread.sleep(config.period() * 1000);
    }
}
