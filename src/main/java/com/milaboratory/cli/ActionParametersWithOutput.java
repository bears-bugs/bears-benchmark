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
import com.beust.jcommander.ParameterException;

import java.io.File;
import java.util.List;

public abstract class ActionParametersWithOutput extends ActionParameters {
    @Parameter(names = {"-f", "--force"}, description = "Force overwrite of output file(s).")
    public boolean force = false;

    protected abstract List<String> getOutputFiles();

    public boolean isForceOverwrite() {
        return force;
    }

    @Override
    public void validate() {
        if (help)
            return;
        for (String fileName : getOutputFiles()) {
            if (fileName.equals("."))
                continue;
            File file = new File(fileName);
            if (file.exists())
                handleExistenceOfOutputFile(fileName);
        }
    }

    /**
     * Specifies behaviour in the case with output exists (default is to throw exception)
     */
    public void handleExistenceOfOutputFile(String outFileName) {
        if (!isForceOverwrite())
            throw new ParameterException("File " + outFileName + " already exists. Use -f option to overwrite it.");
    }
}
