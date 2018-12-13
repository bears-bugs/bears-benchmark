/*
 * Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.tamaya.core.internal.converters;

import org.apache.tamaya.TypeLiteral;
import org.apache.tamaya.spi.ConversionContext;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests conversion of the {@link ClassConverter}.
 */
public class ClassConverterTest {

    ConversionContext context = new ConversionContext.Builder(TypeLiteral.of(Class.class))
            .build();

    @Test
    public void testConvert_Class() throws Exception {
        ClassConverter converter = new ClassConverter();
        assertThat(BigDecimal.class).isEqualTo(converter.convert("java.math.BigDecimal", context));
    }

    @Test
    public void testConvert_Class_WithSpaces() throws Exception {
        ClassConverter converter = new ClassConverter();
         assertThat(BigDecimal.class).isEqualTo(converter.convert("  java.math.BigDecimal\t", context));
    }

    @Test
    public void testConvert_Class_WithSpacesBefore() throws Exception {
        ClassConverter converter = new ClassConverter();
        assertThat(BigDecimal.class).isEqualTo(converter.convert("  java.math.BigDecimal", context));
    }

    @Test
    public void testConvert_Class_WithSpacesAfter() throws Exception {
        ClassConverter converter = new ClassConverter();
        assertThat(BigDecimal.class).isEqualTo(converter.convert("java.math.BigDecimal  ", context));
    }

    @Test
    public void testConvert_NotPresent() throws Exception {
        ClassConverter converter = new ClassConverter();
        assertThat(converter.convert("", context)).isNull();
        assertThat(converter.convert(null, context)).isNull();
    }

    @Test
    public void callToConvertAddsMoreSupportedFormatsToTheContext() throws Exception {
        ConversionContext context = new ConversionContext.Builder(TypeLiteral.of(Class.class)).build();

        ClassConverter converter = new ClassConverter();
        converter.convert("", context);

        assertThat(context.getSupportedFormats().contains("<fullyQualifiedClassName> (ClassConverter)")).isTrue();
    }

    @Test
    public void testHashCode() {
        ClassConverter instance = new ClassConverter();
        assertThat(instance.hashCode()).isEqualTo(ClassConverter.class.hashCode());
    }

    @Test
    public void testEquality() {
        ClassConverter converter = new ClassConverter();

        assertThat(converter).isEqualTo(new ClassConverter());
        assertThat(converter).isNotEqualTo(new CurrencyConverter());
        assertThat(converter).isNotEqualTo(null);
    }
}
