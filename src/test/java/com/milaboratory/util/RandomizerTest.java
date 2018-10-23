/*
 * Copyright 2017 MiLaboratory.com
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

import cc.redberry.pipe.CUtils;
import cc.redberry.pipe.OutputPort;
import cc.redberry.pipe.OutputPortCloseable;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * Created by dbolotin on 04/04/2017.
 */
public class RandomizerTest {
    @Test
    public void test1() throws Exception {
        for (int nElements : new int[]{100_132, 10_000}) {
            testWithInts(nElements, nElements / 100);
            testWithInts(nElements, nElements / 10);
            testWithInts(nElements, nElements * 1);
            testWithInts(nElements, nElements * 2);
            testWithInts(nElements, nElements * 10);
        }
    }

    private static void testWithInts(int nElements, int chunkSize) throws Exception {
        File tmpFile = TempFileManager.getTempFile();

        ArrayList<Integer> source = new ArrayList<>();
        for (int i = 0; i < nElements; i++) {
            int e = RandomUtil.getThreadLocalRandom().nextInt();
            source.add(e);
        }

        source = new ArrayList<>(new HashSet<>(source));

        ObjectSerializer<Integer> intSerializer = new ObjectSerializer<Integer>() {
            @Override
            public void write(Collection<Integer> data, OutputStream stream) {
                try (DataOutputStream out = new DataOutputStream(stream)) {
                    for (Integer datum : data) {
                        out.writeInt(datum);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public OutputPort<Integer> read(InputStream stream) {
                final DataInputStream in = new DataInputStream(stream);
                return new OutputPort<Integer>() {
                    @Override
                    public Integer take() {
                        try {
                            return in.readInt();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };

        OutputPortCloseable<Integer> sorted = Randomizer.randomize(CUtils.asOutputPort(source),
                RandomUtil.getThreadLocalRandomData(), chunkSize, intSerializer, tmpFile);

        HashSet<Integer> set = new HashSet<>(source);
        for (Integer integer : CUtils.it(sorted))
            Assert.assertTrue(set.remove(integer));

        Assert.assertTrue(set.isEmpty());
    }
}