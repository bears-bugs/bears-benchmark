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

import cc.redberry.pipe.util.CountLimitingOutputPort;
import cc.redberry.pipe.util.CountingOutputPort;

import java.io.PrintStream;
import java.text.DecimalFormat;

public class SmartProgressReporter implements Runnable {
    private static final DecimalFormat percentFormat = new DecimalFormat("##.#'%'");
    private final PrintStream stream;
    private final CanReportProgressAndStage reporter;
    private double progressPeriod = 0.10, timePeriod = 120_000;
    private boolean detectStageChange = true;

    public SmartProgressReporter(CanReportProgressAndStage reporter, PrintStream stream) {
        this.stream = stream;
        this.reporter = reporter;
    }

    public SmartProgressReporter(CanReportProgressAndStage reporter) {
        this(reporter, System.out);
    }

    public SmartProgressReporter(final String prefix, final CanReportProgress reporter) {
        this(prefix, reporter, System.out);
    }

    public SmartProgressReporter(final String prefix, final CanReportProgress reporter, PrintStream stream) {
        this.stream = stream;
        this.reporter = new CanReportProgressAndStage() {
            @Override
            public String getStage() {
                return prefix;
            }

            @Override
            public double getProgress() {
                return reporter.getProgress();
            }

            @Override
            public boolean isFinished() {
                return reporter.isFinished();
            }
        };
    }

    public double getProgressPeriod() {
        return progressPeriod;
    }

    public void setProgressPeriod(double progressPeriod) {
        this.progressPeriod = progressPeriod;
    }

    public double getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(double timePeriod) {
        this.timePeriod = timePeriod;
    }

    public boolean isDetectStageChange() {
        return detectStageChange;
    }

    public void setDetectStageChange(boolean detectStageChange) {
        this.detectStageChange = detectStageChange;
    }

    @Override
    public void run() {
        long currentStamp, lastStamp = System.currentTimeMillis(), deltaTime, et;
        double currentProgress, lastProgress = Double.NaN, deltaValue;
        String currentStage, lastStage = null, etStr;
        boolean trigger;
        try {
            while (!reporter.isFinished()) {
                synchronized (reporter) {
                    currentProgress = reporter.getProgress();
                    currentStage = reporter.getStage();
                }
                currentStamp = System.currentTimeMillis();

                deltaValue = currentProgress - lastProgress;
                deltaTime = currentStamp - lastStamp;

                trigger = false;

                if (detectStageChange && !currentStage.equals(lastStage))
                    trigger = true;

                if (Double.isNaN(currentProgress) ^ Double.isNaN(lastProgress))
                    trigger = true;

                if (deltaValue >= progressPeriod
                        || deltaTime >= timePeriod)
                    trigger = true;

                if (deltaValue < 0.0) {
                    deltaValue = Double.NaN;
                    trigger = true;
                }

                long hours, minutes, seconds;

                if (trigger) {

                    if (Double.isNaN(deltaValue) || deltaTime == 0 || deltaValue == 0.0)
                        etStr = "";
                    else {
                        et = (long) ((1.0 - currentProgress) * deltaTime / deltaValue);

                        et /= 1000;
                        hours = et / 3600;
                        et -= hours * 3600;
                        minutes = (et) / 60;
                        et -= minutes * 60;
                        seconds = et;

                        etStr = "  ETA: " + timeString(hours) + ":" + timeString(minutes) + ":" + timeString(seconds);
                    }

                    if (currentStage == null)
                        currentStage = "null";

                    String sProgress;
                    if (Double.isNaN(currentProgress))
                        sProgress = "progress unknown";
                    else
                        sProgress = percentFormat.format(currentProgress * 100.0);

                    stream.println(currentStage + ": " + sProgress + etStr);

                    lastProgress = currentProgress;
                    lastStamp = currentStamp;
                    lastStage = currentStage;
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
        }
    }

    private static String timeString(long time) {
        String timeStr = Long.toString(time);
        return timeStr.length() < 2 ? ("0" + timeStr) : timeStr;
    }

    public static void startProgressReport(SmartProgressReporter reporter) {
        Thread thread = new Thread(reporter);
        thread.setDaemon(true);
        thread.start();
    }

    public static void startProgressReport(CanReportProgressAndStage reporter, PrintStream stream) {
        startProgressReport(new SmartProgressReporter(reporter, stream));
    }

    public static void startProgressReport(CanReportProgressAndStage reporter) {
        startProgressReport(new SmartProgressReporter(reporter));
    }

    public static void startProgressReport(final String prefix, final CanReportProgress reporter) {
        startProgressReport(new SmartProgressReporter(prefix, reporter));
    }

    public static void startProgressReport(final String prefix, final CanReportProgress reporter, PrintStream stream) {
        startProgressReport(new SmartProgressReporter(prefix, reporter, stream));
    }

    public static CanReportProgress extractProgress(final CountingOutputPort<?> countingOutputPort, final long size) {
        return new CanReportProgress() {
            @Override
            public double getProgress() {
                return 1.0 * countingOutputPort.getCount() / size;
            }

            @Override
            public boolean isFinished() {
                return countingOutputPort.getCount() >= size;
            }
        };
    }

    public static CanReportProgress extractProgress(final CountLimitingOutputPort<?> countLimitingOutputPort) {
        return new CanReportProgress() {
            @Override
            public double getProgress() {
                long limit = countLimitingOutputPort.getLimit();
                long done = limit - countLimitingOutputPort.getElementsLeft();
                return 1.0 * done / limit;
            }

            @Override
            public boolean isFinished() {
                return countLimitingOutputPort.getElementsLeft() == 0;
            }
        };
    }
}