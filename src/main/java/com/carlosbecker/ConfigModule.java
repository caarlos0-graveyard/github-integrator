package com.carlosbecker;

import com.carlosbecker.github.IntegratorConfig;
import com.carlosbecker.github.IntegratorConfigProvider;
import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IntegratorConfig.class).toProvider(IntegratorConfigProvider.class);
    }
}
