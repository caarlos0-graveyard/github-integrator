package com.carlosbecker.github;

import com.google.inject.Provider;

import org.aeonbits.owner.ConfigFactory;

public class IntegratorConfigProvider implements Provider<IntegratorConfig> {
    @Override
    public IntegratorConfig get() {
        return ConfigFactory.create(IntegratorConfig.class);
    }
}
