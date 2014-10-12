package com.carlosbecker.github;

import org.aeonbits.owner.Config;

public interface IntegratorConfig extends Config {
    @Key("github.oauth")
    String oauth();

    @Key("executions")
    String executions();
}
