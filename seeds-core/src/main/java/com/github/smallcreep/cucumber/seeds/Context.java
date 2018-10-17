/*
 * MIT License
 *
 * Copyright (c) 2018 Ilia Rogozhin (@smallcreep) <ilia.rogozhin@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.smallcreep.cucumber.seeds;

import javax.management.openmbean.KeyAlreadyExistsException;

/**
 * Context scenario or suit.
 * @since 0.1.1
 */
public interface Context {

    /**
     * Get value by key from the current scenario context.
     * @param key Key
     * @return Value
     * @throws NullPointerException if the specified key is null
     */
    Object value(String key);

    /**
     * Add value to current scenario context.
     * @param key Key
     * @param value Value
     * @throws KeyAlreadyExistsException if the specified already exist.
     */
    void add(String key, Object value);

    /**
     * Check the value with this key contains in this context.
     * @param key Key value
     * @return True if value with this key contains
     */
    boolean contains(String key);

}
