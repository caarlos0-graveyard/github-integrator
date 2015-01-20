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
package com.carlosbecker.integrator.integration;

import javax.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

/**
 * The App Runner.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@Log4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class AppRunner {
    /**
     * One second, in ms.
     */
    private static final int SECOND = 1000;
    /**
     * Config.
     */
    private final transient IntegratorConfig config;
    /**
     * Integrator.
     */
    private final transient MainIntegrator integrator;

    /**
     * Keeps running if config.loop() is true, otherwise runs once.
     * @throws Exception If something goes wrong.
     */
    public final void run() throws Exception {
        do {
            this.runOnce();
            this.sleep();
        } while (this.config.loop());
    }

    /**
     * Sleep if config.loop() is true. Otherwise does nothing.
     * @throws InterruptedException If fails at sleep.
     */
    private void sleep() throws InterruptedException {
        if (this.config.loop()) {
            log.info("Waiting...");
            Thread.sleep(this.config.period() * SECOND);
        }
    }

    /**
     * Work on integrator one time.
     */
    private void runOnce() {
        log.info("Running...");
        this.integrator.work();
    }
}
