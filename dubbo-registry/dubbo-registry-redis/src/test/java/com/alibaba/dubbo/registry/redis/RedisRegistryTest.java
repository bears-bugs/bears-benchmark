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
package com.alibaba.dubbo.registry.redis;

import com.alibaba.dubbo.common.URL;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * RedisRegistryTest
 *
 */
public class RedisRegistryTest {

    String service = "com.alibaba.dubbo.test.injvmServie";
    URL registryUrl = URL.valueOf("redis://239.255.255.255/");
    URL registryUrlWithPasswd = URL.valueOf("zookeeper://239.255.255.255?password=123456");
    URL serviceUrl = URL.valueOf("redis://redis/" + service
            + "?notify=false&methods=test1,test2");
    URL consumerUrl = URL.valueOf("redis://consumer/" + service + "?notify=false&methods=test1,test2");
    // RedisRegistry registry    = new RedisRegistry(registryUrl);
    // RedisRegistry registryWithPasswd  = new RedisRegistry(registryUrlWithPasswd);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        //registry.register(serviceUrl);
    }

    /**
     * Test method for {@link com.alibaba.dubbo.registry.redis.RedisRegistry#getRegistered()}.
     */
    @Test
    public void testRegister() {
        /*List<URL> registered = null;
        // clear first
        registered = registry.getRegistered(service);

        for (int i = 0; i < 2; i++) {
            registry.register(service, serviceUrl);
            registered = registry.getRegistered(service);
            assertTrue(registered.contains(serviceUrl));
        }
        // confirm only 1 regist success;
        registered = registry.getRegistered(service);
        assertEquals(1, registered.size());*/
    }

    /**
     * Test method for
     * {@link com.alibaba.dubbo.registry.redis.RedisRegistry#subscribe(URL, com.alibaba.dubbo.registry.NotifyListener)}
     * .
     */
    @Test
    public void testSubscribe() {
        /*final String subscribearg = "arg1=1&arg2=2";
        // verify lisener.
        final AtomicReference<Map<String, String>> args = new AtomicReference<Map<String, String>>();
        registry.subscribe(service, new URL("dubbo", NetUtils.getLocalHost(), 0, StringUtils.parseQueryString(subscribearg)), new NotifyListener() {

            public void notify(List<URL> urls) {
                // FIXME assertEquals(RedisRegistry.this.service, service);
                args.set(urls.get(0).getParameters());
            }
        });
        assertEquals(serviceUrl.toParameterString(), StringUtils.toQueryString(args.get()));
        Map<String, String> arg = registry.getSubscribed(service);
        assertEquals(subscribearg, StringUtils.toQueryString(arg));*/

    }

    @Test
    public void testRedisPasswd() {
        // Assert.assertNotNull(registryWithPasswd);
    }

}