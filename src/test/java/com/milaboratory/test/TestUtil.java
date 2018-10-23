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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.milaboratory.core.sequence.Alphabet;
import com.milaboratory.core.sequence.Sequence;
import com.milaboratory.core.sequence.SequenceBuilder;
import com.milaboratory.primitivio.PrimitivI;
import com.milaboratory.primitivio.PrimitivO;
import com.milaboratory.util.GlobalObjectMappers;
import com.milaboratory.util.RandomUtil;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestUtil {
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    public static boolean lt() {
        return Objects.equals(System.getProperty("longTests"), "") ||
                Objects.equals(System.getProperty("longTests"), "true") ||
                Objects.equals(System.getProperty("longTest"), "") ||
                Objects.equals(System.getProperty("longTest"), "true");
    }

    @Test
    public void testLT() throws Exception {
        if (lt())
            System.out.println("Long tests.");
        else
            System.out.println("Short tests.");

        if (getProperties().isEmpty()) {
            System.out.println("No system env properties.");
        } else {
            System.out.println("There are some system env properties.");
        }
    }

    public static int its(int shortTest, int longTest) {
        return lt() ? longTest : shortTest;
    }

    public static long its(long shortTest, long longTest) {
        return lt() ? longTest : shortTest;
    }

    public static String env() {
        String serverEnv = System.getProperty("serverEnv");

        if (serverEnv == null)
            return null;

        File serverEnvPath = new File(serverEnv);
        if (serverEnvPath.exists() && serverEnvPath.isDirectory()) {
            String ret = serverEnvPath.getAbsolutePath();
            if (!ret.endsWith(File.separator))
                ret = ret + File.separator;
            return ret;
        } else
            throw new IllegalArgumentException(serverEnv + " not exists.");
    }

    public static final String BIG_TEST_RESOURCE_PREFIX = "/big/";

    public static String getBigTestResource(String file) {
        return getBigTestResource(file, file);
    }

    public static String getBigTestResource(String name, String file) {
        try {
            URL resource = TestUtil.class.getResource(BIG_TEST_RESOURCE_PREFIX + file);
            Assume.assumeNotNull(resource);
            Path path = Paths.get(resource.toURI()).toAbsolutePath().resolveSibling(name);
            return path.toString();
        } catch (AssumptionViolatedException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void assertPrimitivIO(Object object) {
        assertPrimitivIO(object, object.getClass());
    }

    public static void assertPrimitivIO(Object object, Class clazz) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivO o = new PrimitivO(bos);
        o.writeObject(object);
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        PrimitivI i = new PrimitivI(bis);
        Object o1 = i.readObject(clazz);
        Assert.assertEquals(object, o1);
    }

    public static void assertJson(Object object) {
        assertJson(object, TypeFactory.defaultInstance().constructType(object.getClass()), false);
    }

    public static void assertJson(Object object, boolean sout) {
        assertJson(object, TypeFactory.defaultInstance().constructType(object.getClass()), sout);
    }

    public static void assertJson(Object object, Class clazz) {
        assertJson(object, TypeFactory.defaultInstance().constructType(clazz), false);
    }

    public static void assertJson(Object object, JavaType clazz) {
        assertJson(object, clazz, false);
    }

    public static void assertJson(Object object, JavaType clazz, boolean sout) {
        try {
            String str = GlobalObjectMappers.PRETTY.writeValueAsString(object);
            if (sout)
                System.out.println(str);
            Object deser = GlobalObjectMappers.PRETTY.readValue(str, clazz);
            Assert.assertEquals(object, deser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static volatile Map<String, String> envProperties;

    public static synchronized Map<String, String> getProperties() {
        if (envProperties == null) {
            String e = env();

            if (e == null)
                envProperties = Collections.EMPTY_MAP;
            else {
                File propsFile = new File(e + "properties.json");

                if (!propsFile.exists())
                    envProperties = Collections.EMPTY_MAP;
                else {
                    try {
                        envProperties = GlobalObjectMappers.ONE_LINE
                                .readValue(propsFile, new TypeReference<HashMap<String, String>>() {
                                });
                    } catch (IOException ex) {
                        envProperties = Collections.EMPTY_MAP;
                    }
                }
            }
        }

        return envProperties;
    }

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

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet,
                                                           int minLength, int maxLength) {
        return randomSequence(alphabet, RandomUtil.getThreadLocalRandom(), minLength, maxLength);
    }

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet,
                                                           int minLength, int maxLength, boolean basicLettersOnly) {
        return randomSequence(alphabet, RandomUtil.getThreadLocalRandom(), minLength, maxLength, basicLettersOnly);
    }

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet, RandomDataGenerator r,
                                                           int minLength, int maxLength) {
        return randomSequence(alphabet, r.getRandomGenerator(), minLength, maxLength);
    }

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet, RandomDataGenerator r,
                                                           int minLength, int maxLength, boolean basicLettersOnly) {
        return randomSequence(alphabet, r.getRandomGenerator(), minLength, maxLength, basicLettersOnly);
    }

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet, RandomGenerator r,
                                                           int minLength, int maxLength) {
        return randomSequence(alphabet, r, minLength, maxLength, true);
    }

    public static <S extends Sequence<S>> S randomSequence(Alphabet<S> alphabet, RandomGenerator r,
                                                           int minLength, int maxLength, boolean basicLettersOnly) {
        int length = minLength == maxLength ?
                minLength : minLength + r.nextInt(maxLength - minLength + 1);
        SequenceBuilder<S> builder = alphabet.createBuilder();
        for (int i = 0; i < length; ++i)
            builder.append((byte) r.nextInt(basicLettersOnly ? alphabet.basicSize() : alphabet.size()));
        return builder.createAndDestroy();
    }
}
