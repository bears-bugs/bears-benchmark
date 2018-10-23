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

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;

import java.util.concurrent.atomic.AtomicLong;

public final class RandomUtil {
    private RandomUtil() {
    }

    static final ThreadLocal<Randomm> threadLocalRandom = new ThreadLocal<Randomm>() {
        @Override
        protected Randomm initialValue() {
            //Generating thread-specific seed
            long seed = seedCounter.addAndGet(353L);
            seed = HashFunctions.JenkinWang64shift(seed);

            //Creating random generator
            return new Randomm(new Well19937c(seed));
        }
    };
    //Used to generate individual seeds for each thread-local random generator
    private static final AtomicLong seedCounter = new AtomicLong(641L);

    public static void setGlobalInitialSeed(final long seed) {
        seedCounter.set(seed);
    }

    public static long reseedThreadLocal() {
        return reseedThreadLocal(getThreadLocalRandom().nextLong());
    }

    public static long reseedThreadLocal(long seed) {
        Well19937c random = getThreadLocalRandom();
        random.setSeed(seed);
        return seed;
    }

    public static Well19937c getThreadLocalRandom() {
        return threadLocalRandom.get().generator;
    }

    public static RandomDataGenerator getThreadLocalRandomData() {
        return threadLocalRandom.get().rdi;
    }

    private static final class Randomm {
        final Well19937c generator;
        final RandomDataGenerator rdi;

        private Randomm(Well19937c generator) {
            this.generator = generator;
            this.rdi = new RandomDataGenerator(generator);
        }
    }
}
