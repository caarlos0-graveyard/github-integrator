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
package com.carlosbecker.integrator.model;

import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Represents a list of scripted repositories.
 *
 * @author Carlos Alexandro Becker (caarlos0@gmail.com)
 * @version $Id$
 */
@RequiredArgsConstructor
public class ScriptedRepositories {
    /**
     * List of scripted repositories.
     */
    private final List<ScriptedRepository> repositories;

    /**
     * Iterates over scripted repositories.
     * @return Scripted Repositories Iterator.
     */
    public Iterator<ScriptedRepository> iterator() {
        return this.repositories.iterator();
    }

    /**
     * Checks wether the repository list is empty.
     * @return True if empty, false otherwise.
     */
    public final boolean isEmpty() {
        return this.repositories.isEmpty();
    }
}
