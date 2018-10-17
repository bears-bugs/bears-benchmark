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

import com.github.smallcreep.cucumber.seeds.Scenario;
import com.github.smallcreep.cucumber.seeds.Suit;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * TestCase for {@link StEnvelope}.
 * @since 0.1.1
 */
public class StEnvelopeTest {

    /**
     * Check context return from origin.
     */
    @Test
    public void checkContextTheSameOrigin() {
        MatcherAssert.assertThat(
            new StCheckEnvelope().context(),
            CoreMatchers.equalTo(
                StSmart.instance().context()
            )
        );
    }

    /**
     * Check scenario return from origin.
     */
    @Test
    public void checkScenarioTheSameOrigin() {
        MatcherAssert.assertThat(
            new StCheckEnvelope().scenario(),
            CoreMatchers.equalTo(
                StSmart.instance().scenario()
            )
        );
    }

    /**
     * Check finish from origin was run.
     */
    @Test
    public void checkFinishWasRun() {
        final Suit suit = new StCheckEnvelope();
        suit.start();
        final Scenario first = suit.scenario();
        suit.finish();
        MatcherAssert.assertThat(
            suit.scenario(),
            CoreMatchers.not(
                CoreMatchers.equalTo(
                    first
                )
            )
        );
    }

    /**
     * Test Suit for check envelope suit.
     */
    private final class StCheckEnvelope extends StEnvelope {

        /**
         * Ctor.
         */
        StCheckEnvelope() {
            super(StSmart.instance());
        }
    }
}
