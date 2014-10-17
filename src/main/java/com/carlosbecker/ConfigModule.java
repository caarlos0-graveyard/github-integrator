package com.carlosbecker;

import com.carlosbecker.integration.IntegratorConfig;
import com.carlosbecker.integration.IntegratorConfigProvider;
import com.google.inject.AbstractModule;

public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IntegratorConfig.class).toProvider(IntegratorConfigProvider.class);
    }
}
