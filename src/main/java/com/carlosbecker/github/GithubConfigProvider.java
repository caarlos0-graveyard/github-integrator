package com.carlosbecker.github;

import org.aeonbits.owner.ConfigFactory;

import com.google.inject.Provider;

public class GithubConfigProvider implements Provider<GithubConfig> {
	@Override
	public GithubConfig get() {
		return ConfigFactory.create(GithubConfig.class);
	}
}
