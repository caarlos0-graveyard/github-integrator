package com.carlosbecker.github;

import org.aeonbits.owner.Config;

public interface GithubConfig extends Config {
	@Key("github.oauth")
	String oauth();
}
