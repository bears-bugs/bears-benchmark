/*
 * Copyright 2015 MiLaboratory.com
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
package com.milaboratory.util;

public final class ProgressAndStage implements CanReportProgressAndStage {
    volatile String stage;
    volatile double progress;
    volatile boolean finished;

    public ProgressAndStage(String stage) {
        this.stage = stage;
    }

    @Override
    public String getStage() {
        return stage;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
