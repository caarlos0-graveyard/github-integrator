package com.carlosbecker.model;

import static com.google.common.collect.Lists.newArrayList;

import com.carlosbecker.integration.IntegratorConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import lombok.AllArgsConstructor;

@AllArgsConstructor(onConstructor = @__(@Inject))
public class ScriptedRepositoriesProvider implements Provider<ScriptedRepositories> {
    private static final Type TYPE = new TypeToken<List<ScriptedRepository>>() {
    }.getType();

    private final IntegratorConfig config;

    @Override
    public ScriptedRepositories get() {
        if (config.executions() == null)
            return new ScriptedRepositories(newArrayList());
        return parse();
    }

    private ScriptedRepositories parse() {
        try {
            return new ScriptedRepositories(new Gson().fromJson(loadFile(), TYPE));
        } catch (final FileNotFoundException e) {
            return new ScriptedRepositories(newArrayList());
        }
    }

    private BufferedReader loadFile() throws FileNotFoundException {
        final File file = new File(config.executions());
        final FileInputStream inputStream = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
