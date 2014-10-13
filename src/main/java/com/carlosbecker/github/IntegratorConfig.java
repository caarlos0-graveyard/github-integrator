package com.carlosbecker.github;

import org.aeonbits.owner.Config;

public interface IntegratorConfig extends Config {
    @Key("github.oauth")
    String oauth();

    String executions();

    @DefaultValue("30")
    int period();
}
