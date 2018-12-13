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
package com.alibaba.dubbo.examples.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * GenericConsumer
 */
public class RedisConsumer {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        String config = RedisConsumer.class.getPackage().getName().replace('.', '/') + "/redis-consumer.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
        Map<String, Object> cache = (Map<String, Object>) context.getBean("cache");
        cache.remove("hello");
        Object value = cache.get("hello");
        System.out.println(value);
        if (value != null) {
            throw new IllegalStateException(value + " != null");
        }
        cache.put("hello", "world");
        value = cache.get("hello");
        System.out.println(value);
        if (!"world".equals(value)) {
            throw new IllegalStateException(value + " != world");
        }
    }

}
