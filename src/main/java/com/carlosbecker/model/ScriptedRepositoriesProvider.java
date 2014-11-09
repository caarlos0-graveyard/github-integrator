/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Carlos Alexandro Becker
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

/**
 * Produces the Scripted Repositories list.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ScriptedRepositoriesProvider implements
Provider<ScriptedRepositories> {
    /**
     * The Scripted Repository List type token
     */
    private static final Type TYPE = new TypeToken<List<ScriptedRepository>>() {
    }.getType();
    /**
     * Config
     */
    private final transient IntegratorConfig config;

    @Override
    public ScriptedRepositories get() {
        if (config.executions() == null)
            return new ScriptedRepositories(newArrayList());
        return parse();
    }

    /**
     * Parse the executions file to a ScriptedRepositories instance.
     * @return ScriptedRepositories instance.
     */
    private ScriptedRepositories parse() {
        try {
            final Gson gson = new Gson();
            return new ScriptedRepositories(gson.fromJson(loadFile(), TYPE));
        } catch (final FileNotFoundException e) {
            return new ScriptedRepositories(newArrayList());
        }
    }

    /**
     * Load the executions file from settings.
     * @return The BufferedReader of the file.
     * @throws FileNotFoundException If the file doesn't exists.
     */
    private BufferedReader loadFile() throws FileNotFoundException {
        final File file = new File(config.executions());
        final FileInputStream inputStream = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
