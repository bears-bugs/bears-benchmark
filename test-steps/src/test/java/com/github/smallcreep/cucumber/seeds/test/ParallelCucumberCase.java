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
package com.github.smallcreep.cucumber.seeds.test;

import com.github.mkolisnyk.cucumber.runner.BeforeSuite;
import com.github.mkolisnyk.cucumber.runner.ExtendedCucumberOptions;
import com.github.mkolisnyk.cucumber.runner.ExtendedParallelCucumber;
import com.github.smallcreep.cucumber.seeds.hooks.HkProperties;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Parallel runner for cucumber test.
 * @since 0.1.1
 */
@RunWith(ExtendedParallelCucumber.class)
@ExtendedCucumberOptions(
    threadsCount = 2,
    jsonReport = "target/cucumber/cucumber.json",
    detailedReport = true,
    detailedAggregatedReport = true,
    overviewReport = true,
    jsonUsageReport = "target/cucumber-usage.json",
    outputFolder = "target"
)
@CucumberOptions(
    plugin = {
        "html:target/cucumber/cucumber-html-report",
        "json:target/cucumber/cucumber.json",
        "pretty:target/cucumber/cucumber-pretty.txt",
        "junit:target/cucumber/cucumber-results.xml"
    },
    glue = {
        "com/github/smallcreep"
    },
    features = {
        "src/test/resources/com/github/smallcreep"
    },
    tags = {
        "~@inprogress"
    }
)
public final class ParallelCucumberCase {

    /**
     * Ctor.
     */
    private ParallelCucumberCase() {
    }

    /**
     * Add properties to Suit context.
     */
    @SuppressWarnings(
        {
            "PMD.JUnit4TestShouldUseBeforeAnnotation",
            "PMD.ProhibitPublicStaticMethods"
        }
    )
    @BeforeSuite
    public static void setUp() {
        HkProperties.setUp();
    }
}
