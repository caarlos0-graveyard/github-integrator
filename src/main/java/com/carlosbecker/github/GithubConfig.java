package com.carlosbecker.github;

import java.util.List;
import org.aeonbits.owner.Config;

public interface GithubConfig extends Config {
    @Key("github.oauth")
    String oauth();

    @Key("github.repos")
    List<String> repos();
}
