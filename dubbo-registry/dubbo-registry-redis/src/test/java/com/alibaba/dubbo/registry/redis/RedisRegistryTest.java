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
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.Registry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.embedded.RedisServer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.alibaba.dubbo.common.Constants.BACKUP_KEY;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class RedisRegistryTest {

    private String service = "com.alibaba.dubbo.test.injvmServie";
    private URL serviceUrl = URL.valueOf("redis://redis/" + service + "?notify=false&methods=test1,test2");
    private RedisServer redisServer;
    private RedisRegistry redisRegistry;
    private URL registryUrl;

    @Before
    public void setUp() throws Exception {
        int redisPort = NetUtils.getAvailablePort();
        this.redisServer = new RedisServer(redisPort);
        this.redisServer.start();
        this.registryUrl = URL.valueOf("redis://localhost:" + redisPort);

        redisRegistry = (RedisRegistry) new RedisRegistryFactory().createRegistry(registryUrl);
    }

    @After
    public void tearDown() throws Exception {
        this.redisServer.stop();
    }

    @Test
    public void testRegister() {
        Set<URL> registered = null;

        for (int i = 0; i < 2; i++) {
            redisRegistry.register(serviceUrl);
            registered = redisRegistry.getRegistered();
            assertThat(registered.contains(serviceUrl), is(true));
        }

        registered = redisRegistry.getRegistered();
        assertThat(registered.size(), is(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAnyHost() {
        URL errorUrl = URL.valueOf("multicast://0.0.0.0/");
        new RedisRegistryFactory().createRegistry(errorUrl);
    }


    @Test
    public void testSubscribeAndUnsubscribe() {
        NotifyListener listener = new NotifyListener() {
            @Override
            public void notify(List<URL> urls) {

            }
        };
        redisRegistry.subscribe(serviceUrl, listener);

        Map<URL, Set<NotifyListener>> subscribed = redisRegistry.getSubscribed();
        assertThat(subscribed.size(), is(1));
        assertThat(subscribed.get(serviceUrl).size(), is(1));

        redisRegistry.unsubscribe(serviceUrl, listener);
        subscribed = redisRegistry.getSubscribed();
        assertThat(subscribed.get(serviceUrl).size(), is(0));
    }

    @Test
    public void testAvailable() {
        redisRegistry.register(serviceUrl);
        assertThat(redisRegistry.isAvailable(), is(true));

        redisRegistry.destroy();
        assertThat(redisRegistry.isAvailable(), is(false));
    }

    @Test
    public void testAvailableWithBackup() {
        URL url = URL.valueOf("redis://redisOne:8880").addParameter(BACKUP_KEY, "redisTwo:8881");
        Registry registry = new RedisRegistryFactory().createRegistry(url);

        assertThat(registry.isAvailable(), is(false));

        url = URL.valueOf(this.registryUrl.toFullString()).addParameter(BACKUP_KEY, "redisTwo:8881");
        registry = new RedisRegistryFactory().createRegistry(url);

        assertThat(registry.isAvailable(), is(true));
    }
}