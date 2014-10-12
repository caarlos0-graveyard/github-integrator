package com.carlosbecker;

import static com.google.common.base.Strings.isNullOrEmpty;
import java.io.File;
import java.io.FileInputStream;
import javax.inject.Inject;
import lombok.extern.log4j.Log4j;
import com.carlosbecker.github.IntegratorConfig;
import com.google.inject.Guice;
import com.google.inject.Injector;

@Log4j
public class App {
    @Inject
    private IntegratorConfig config;
    @Inject
    private MainIntegrator integrator;

    private void run() throws Exception {
        do {
            log.info("Running...");
            integrator.work();
            Thread.sleep(config.period() * 1000);
        } while (true);
    }

    public static void main(String[] args) throws Exception {
        log.info("Starting up...");
        if (!isNullOrEmpty(args[0]))
            System.getProperties().load(new FileInputStream(new File(args[0])));
        Injector injector = Guice.createInjector(new MainModule());
        App app = injector.getInstance(App.class);
        app.run();
    }

}
