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

package com.github.smallcreep.cucumber.seeds.hooks;

import com.github.smallcreep.cucumber.seeds.Suit;
import com.github.smallcreep.cucumber.seeds.suit.StSmart;
import cucumber.api.java.After;
import cucumber.api.java.Before;

/**
 * Default hooks for every scenario.
 * Start and finish new scenario.
 * @since 0.1.1
 */
public final class HkScenario {

    /**
     * Current suit.
     */
    private Suit suit;

    /**
     * Ctor.
     */
    public HkScenario() {
        this(StSmart.instance());
    }

    /**
     * Ctor.
     * @param suit Current suit
     */
    private HkScenario(final Suit suit) {
        this.suit = suit;
    }

    /**
     * Start new scenario.
     */
    @Before(order = 0)
    public void start() {
        this.suit.start();
    }

    /**
     * Finish current scenario.
     */
    @After(order = 0)
    public void finish() {
        this.suit.finish();
    }
}
