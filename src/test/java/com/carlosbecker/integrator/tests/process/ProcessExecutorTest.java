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
package com.carlosbecker.integrator.tests.process;

import com.carlosbecker.integrator.process.ProcessExecutor;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the process executor class.
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
public class ProcessExecutorTest {
    /**
     * Executor
     */
    private transient ProcessExecutor executor;

    /**
     * Tear up.
     */
    @Before
    public void init() {
        this.executor = new ProcessExecutor();
    }

    /**
     * Run something.
     * @throws Exception If something goes wrong.
     */
    @Test
    public void testItWorks() throws Exception {
        this.executor.execute(
            "./src/test/resources/sample.sh",
            Arrays.asList("user", "repo", "whatever")
            );
    }
}
