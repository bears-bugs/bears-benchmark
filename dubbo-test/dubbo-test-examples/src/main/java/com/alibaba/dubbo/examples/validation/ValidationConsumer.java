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
package com.alibaba.dubbo.examples.validation;

import com.alibaba.dubbo.examples.validation.api.ValidationParameter;
import com.alibaba.dubbo.examples.validation.api.ValidationService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;

/**
 * ValidationConsumer
 */
public class ValidationConsumer {

    public static void main(String[] args) throws Exception {
        String config = ValidationConsumer.class.getPackage().getName().replace('.', '/') + "/validation-consumer.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();

        ValidationService validationService = (ValidationService) context.getBean("validationService");

        // Save OK
        ValidationParameter parameter = new ValidationParameter();
        parameter.setName("liangfei");
        parameter.setEmail("liangfei@liang.fei");
        parameter.setAge(50);
        parameter.setLoginDate(new Date(System.currentTimeMillis() - 1000000));
        parameter.setExpiryDate(new Date(System.currentTimeMillis() + 1000000));
        validationService.save(parameter);
        System.out.println("Validation Save OK");

        // Save Error
        try {
            parameter = new ValidationParameter();
            validationService.save(parameter);
            System.err.println("Validation Save ERROR");
        } catch (Exception e) {
            ConstraintViolationException ve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
            System.out.println(violations);
        }

        // Delete OK
        validationService.delete(2, "abc");
        System.out.println("Validation Delete OK");

        // Delete Error
        try {
            validationService.delete(0, "abc");
            System.err.println("Validation Delete ERROR");
        } catch (Exception e) {
            ConstraintViolationException ve = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = ve.getConstraintViolations();
            System.out.println(violations);
        }
    }

}
