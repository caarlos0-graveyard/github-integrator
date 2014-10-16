package com.carlosbecker;

import static com.google.inject.Guice.createInjector;
import com.carlosbecker.github.IntegratorConfig;
import java.io.File;
import java.io.FileInputStream;
import javax.inject.Inject;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

@Log4j
public class App {
    private final IntegratorConfig config;
    private final MainIntegrator integrator;

    @Inject
    public App(IntegratorConfig config, MainIntegrator integrator) {
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

    public static void main(@NonNull String[] args) throws Exception {
        log.info("Starting up...");
        System.getProperties().load(new FileInputStream(new File(args[0])));
        createInjector(new ConfigModule(), new GithubModule()).getInstance(App.class).run();
    }

}
