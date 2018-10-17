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

package com.github.smallcreep.cucumber.seeds.suit;

import com.github.smallcreep.cucumber.seeds.Context;
import com.github.smallcreep.cucumber.seeds.Scenario;
import com.github.smallcreep.cucumber.seeds.Suit;
import com.github.smallcreep.cucumber.seeds.context.CxSimple;
import com.github.smallcreep.cucumber.seeds.scenario.ScSimple;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Simple Suit implementation.
 * @since 0.1.1
 */
public final class StSmart implements Suit {

    /**
     * Current suit.
     */
    private static final AtomicReference<Suit> CURRENT = new AtomicReference<>(
        new StSmart()
    );

    /**
     * Suit context.
     */
    private final Context ctx;

    /**
     * Running scenarios.
     */
    private final Map<Long, Scenario> scenarios;

    /**
     * Ctor.
     */
    private StSmart() {
        this(
            new CxSimple()
        );
    }

    /**
     * Ctor.
     * @param context Current Suit ctx
     */
    public StSmart(final Context context) {
        this.ctx = context;
        this.scenarios = new HashMap<>();
    }

    @Override
    public Context context() {
        return this.ctx;
    }

    @Override
    public Scenario scenario() {
        if (this.scenarios.get(Thread.currentThread().getId()) == null) {
            this.start();
        }
        return this.scenarios.get(Thread.currentThread().getId());
    }

    @Override
    public void finish() {
        this.scenarios.remove(Thread.currentThread().getId());
    }

    @Override
    public void start() {
        this.scenarios.put(
            Thread.currentThread().getId(),
            new ScSimple()
        );
    }

    /**
     * Get current suit instance.
     * @return Current suit instance
     * @todo #10:45m/DEV Need remove using public static method.
     *  But I don't know how fixed it. This mistake in cucumber.
     *  If not resolved this problem need remove this comment
     *  and add doc why we use it.
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static Suit instance() {
        return StSmart.CURRENT.get();
    }

    /**
     * Update current suit.
     * It's bad idea. But I don't know how do it another way.
     * @param suit New suit
     */
    @SuppressWarnings("PMD.ProhibitPublicStaticMethods")
    public static void update(final Suit suit) {
        StSmart.CURRENT.set(suit);
    }
}
