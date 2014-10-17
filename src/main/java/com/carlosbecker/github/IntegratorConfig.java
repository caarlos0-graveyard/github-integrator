package com.carlosbecker.github;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;

@Sources("file:${INTEGRATOR_CONFIG}")
public interface IntegratorConfig extends Config {
    @Key("github.oauth")
    String oauth();

    String executions();

    @DefaultValue("60")
    int period();

    @DefaultValue("true")
    boolean loop();
}
