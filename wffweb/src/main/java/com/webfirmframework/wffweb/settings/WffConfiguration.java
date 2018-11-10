/*
 * Copyright 2014-2018 Web Firm Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webfirmframework.wffweb.settings;

/**
 * @author WFF
 * @since 1.0.0
 * @version 1.0.0
 *
 */
public class WffConfiguration {

    private static boolean debugMode;
    private static boolean directionWarningOn;

    /**
     * @return the debugMode
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isDebugMode() {
        return debugMode;
    }

    /**
     * @param debugMode
     *            the debugMode to set
     * @author WFF
     * @since 1.0.0
     */
    public static void setDebugMode(final boolean debugMode) {
        WffConfiguration.debugMode = debugMode;
    }

    /**
     * @return the directionWarningOn
     * @author WFF
     * @since 1.0.0
     */
    public static boolean isDirectionWarningOn() {
        return directionWarningOn;
    }

    /**
     * gives warning message on inappropriate usage of code if it is set to
     * true. NB:- its implementation is not finished yet so it may give unwanted
     * warning.
     *
     * @param directionWarningOn
     *            the directionWarningOn to set
     * @author WFF
     * @since 1.0.0
     */
    public static void setDirectionWarningOn(final boolean directionWarningOn) {
        WffConfiguration.directionWarningOn = directionWarningOn;
    }

}
