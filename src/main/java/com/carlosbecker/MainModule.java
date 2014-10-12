package com.carlosbecker;

import com.google.inject.AbstractModule;

public class MainModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new GithubModule());
        install(new ProcessModule());
    }

}
