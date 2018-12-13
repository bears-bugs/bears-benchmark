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
package com.alibaba.dubbo.config.spring.context.annotation;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ModuleConfig;
import com.alibaba.dubbo.config.MonitorConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

/**
 * {@link EnableDubboConfig} Test
 *
 * @since 2.5.8
 */
public class EnableDubboConfigTest {

    @Test
    public void testSingle() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TestConfig.class);
        context.refresh();

        // application
        ApplicationConfig applicationConfig = context.getBean("applicationBean", ApplicationConfig.class);
        Assert.assertEquals("dubbo-demo-application", applicationConfig.getName());

        // module
        ModuleConfig moduleConfig = context.getBean("moduleBean", ModuleConfig.class);
        Assert.assertEquals("dubbo-demo-module", moduleConfig.getName());

        // registry
        RegistryConfig registryConfig = context.getBean(RegistryConfig.class);
        Assert.assertEquals("zookeeper://192.168.99.100:32770", registryConfig.getAddress());

        // protocol
        ProtocolConfig protocolConfig = context.getBean(ProtocolConfig.class);
        Assert.assertEquals("dubbo", protocolConfig.getName());
        Assert.assertEquals(Integer.valueOf(20880), protocolConfig.getPort());

        // monitor
        MonitorConfig monitorConfig = context.getBean(MonitorConfig.class);
        Assert.assertEquals("zookeeper://127.0.0.1:32770", monitorConfig.getAddress());

        // provider
        ProviderConfig providerConfig = context.getBean(ProviderConfig.class);
        Assert.assertEquals("127.0.0.1", providerConfig.getHost());


        // consumer
        ConsumerConfig consumerConfig = context.getBean(ConsumerConfig.class);
        Assert.assertEquals("netty", consumerConfig.getClient());

    }

    @Test
    public void testMultiple() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(TestMultipleConfig.class);
        context.refresh();

        // application
        ApplicationConfig applicationConfig = context.getBean("applicationBean", ApplicationConfig.class);
        Assert.assertEquals("dubbo-demo-application", applicationConfig.getName());

        ApplicationConfig applicationBean2 = context.getBean("applicationBean2", ApplicationConfig.class);
        Assert.assertEquals("dubbo-demo-application2", applicationBean2.getName());

        ApplicationConfig applicationBean3 = context.getBean("applicationBean3", ApplicationConfig.class);
        Assert.assertEquals("dubbo-demo-application3", applicationBean3.getName());

    }

    @EnableDubboConfig(multiple = true)
    @PropertySource("META-INF/config.properties")
    private static class TestMultipleConfig {

    }

    @EnableDubboConfig
    @PropertySource("META-INF/config.properties")
    private static class TestConfig {

    }
}
