/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.validation.support.jvalidation;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.validation.Validation;
import com.alibaba.dubbo.validation.Validator;
import org.junit.Test;

import javax.validation.ValidationException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class JValidationTest {
    @Test(expected = ValidationException.class)
    public void testReturnTypeWithInvalidValidationProvider() throws Exception {
        Validation jValidation = new JValidation();
        URL url = URL.valueOf("test://test:11/com.alibaba.dubbo.validation.support.jvalidation.JValidation?" +
                "jvalidation=com.alibaba.dubbo.validation.Validation");
        jValidation.getValidator(url);
    }

    @Test
    public void testReturnTypeWithDefaultValidatorProvider() throws Exception {
        Validation jValidation = new JValidation();
        URL url = URL.valueOf("test://test:11/com.alibaba.dubbo.validation.support.jvalidation.JValidation");
        Validator validator = jValidation.getValidator(url);
        assertThat(validator instanceof JValidator, is(true));
    }
}