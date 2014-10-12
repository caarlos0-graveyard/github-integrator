package com.carlosbecker;

import com.carlosbecker.process.ProcessExecutor;
import com.google.inject.AbstractModule;

public class ProcessModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ProcessExecutor.class);
    }
}
