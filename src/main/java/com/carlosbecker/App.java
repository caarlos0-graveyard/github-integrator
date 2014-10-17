package com.carlosbecker;

import static com.google.inject.Guice.createInjector;

import com.carlosbecker.github.GithubModule;
import com.carlosbecker.integration.AppRunner;

import lombok.extern.log4j.Log4j;

@Log4j
public final class App {
    public static void main(String[] args) throws Exception {
        log.info("Starting up...");
        createInjector(new ConfigModule(), new GithubModule()).getInstance(AppRunner.class).run();
    }
}
