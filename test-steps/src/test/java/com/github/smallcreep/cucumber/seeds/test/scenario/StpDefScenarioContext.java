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

package com.github.smallcreep.cucumber.seeds.test.scenario;

import com.github.smallcreep.cucumber.seeds.Suit;
import com.github.smallcreep.cucumber.seeds.suit.StSmart;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;

/**
 * Steps to check start and finish Scenario.
 * @since 0.1.1
 */
public class StpDefScenarioContext {

    /**
     * Current suit.
     */
    private final Suit suit;

    /**
     * Ctor.
     */
    public StpDefScenarioContext() {
        this(StSmart.instance());
    }

    /**
     * Ctor.
     * @param suit Current suit
     */
    private StpDefScenarioContext(final Suit suit) {
        this.suit = suit;
    }

    /**
     * Steps add property to scenario context.
     * @param name Property name
     * @param value Property value
     */
    @Given("^There is property (.*)=(.*) in scenario context$")
    public void addProperty(final String name, final String value) {
        this.suit.scenario().context().add(name, value);
    }

    /**
     * Check scenario property equals expected value.
     * @param name Property name
     * @param value Expected value
     */
    @Then("^The property (.*) in scenario context has value (.*)$")
    public void checkPropertyHasCorrectValue(
        final String name,
        final String value
    ) {
        MatcherAssert.assertThat(
            this.suit.scenario().context().value(name),
            CoreMatchers.equalTo(value)
        );
    }
}
