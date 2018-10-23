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
package com.milaboratory.test;

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import cc.redberry.pipe.VoidProcessor;
import com.milaboratory.core.io.sequence.SingleRead;
import com.milaboratory.core.io.sequence.fastq.SingleFastqReader;
import com.milaboratory.util.SmartProgressReporter;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;

public class Read {
    public static void main(String[] args) throws IOException, InterruptedException {
        long numberOfReads = 0;
        long time = System.nanoTime();
        final AtomicLong count = new AtomicLong(), g = new AtomicLong();
        try (SingleFastqReader reader = new SingleFastqReader(args[0], args[1].equals("true"))) {
            SmartProgressReporter.startProgressReport("Reading: ", reader);

            OutputPort<SingleRead> input = reader;

            if (args[2].equals("true"))
                input = CUtils.buffered(input, 1024);

            CUtils.processAllInParallel(input, new VoidProcessor<SingleRead>() {
                @Override
                public void process(SingleRead singleRead) {
                    count.incrementAndGet();
                    g.addAndGet(singleRead.getData().hashCode());
                    g.addAndGet(singleRead.getDescription().hashCode());
                }
            }, Integer.parseInt(args[3], 10));
        }
        numberOfReads = count.get();
        System.out.println("Number of reads: " + numberOfReads);
        System.out.println("Time: " + time(System.nanoTime() - time));
        System.out.println("Time per read: " + time((System.nanoTime() - time) / numberOfReads));
    }

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static String time(long t) {
        double v = t;
        if ((t /= 1000) == 0)
            return "" + DECIMAL_FORMAT.format(v) + "ns";

        v /= 1000;
        if ((t /= 1000) == 0)
            return "" + DECIMAL_FORMAT.format(v) + "us";

        v /= 1000;
        if ((t /= 1000) == 0)
            return "" + DECIMAL_FORMAT.format(v) + "ms";

        v /= 1000;
        if ((t /= 60) == 0)
            return "" + DECIMAL_FORMAT.format(v) + "s";

        v /= 60;
        return "" + DECIMAL_FORMAT.format(v) + "m";
    }
}
