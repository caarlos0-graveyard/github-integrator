package com.carlosbecker.github;

import org.aeonbits.owner.ConfigFactory;
import com.google.inject.Provider;

public class IntegratorConfigProvider implements Provider<IntegratorConfig> {
    @Override
    public IntegratorConfig get() {
        return ConfigFactory.create(IntegratorConfig.class, System.getProperties());
    }
}
