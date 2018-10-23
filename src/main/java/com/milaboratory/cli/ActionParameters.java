/*
 * Copyright 2016 MiLaboratory.com
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
package com.milaboratory.cli;

import com.beust.jcommander.Parameter;

import java.util.Collections;
import java.util.List;

public abstract class ActionParameters {
    @Parameter(names = {"-h", "--help"}, help = true, description = "Displays help for this command.")
    public boolean help = false;

    public boolean help() {
        return help;
    }

    public void validate() {}

    /**
     * Force to hide some parameters. The use case if when {@code ActionParameters} class is derived from some super
     * class which already defines some parameters that should be hidden in the inherited action.
     *
     * @return a list of valid parameter names (longest name should be used)
     */
    public List<String> forceHideParameters() {
        return Collections.emptyList();
    }
}
