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
package com.carlosbecker;

import com.carlosbecker.github.GithubModule;
import com.carlosbecker.integration.AppRunner;
import com.google.inject.Guice;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * Main App class.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@Log4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class App {
    /**
     * Main
     * @param args Params
     * @throws Exception If something goes wrong.
     */
    public static void main(String[] args) throws Exception {
        new App().run();
    }

    /**
     * Main
     * @throws Exception If something goes wrong.
     */
    private void run() throws Exception {
        log.info("Starting up...");
        Guice.createInjector(
            new ConfigModule(),
            new GithubModule()
        ).getInstance(AppRunner.class).run();
    }

}
