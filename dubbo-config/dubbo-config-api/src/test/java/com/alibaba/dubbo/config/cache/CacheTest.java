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
package com.alibaba.dubbo.config.cache;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.cache.support.threadlocal.ThreadLocalCache;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.MethodConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.RpcInvocation;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheTest
 */
public class CacheTest extends TestCase {

    private void testCache(String type) throws Exception {
        ServiceConfig<CacheService> service = new ServiceConfig<CacheService>();
        service.setApplication(new ApplicationConfig("cache-provider"));
        service.setRegistry(new RegistryConfig("N/A"));
        service.setProtocol(new ProtocolConfig("dubbo", 29582));
        service.setInterface(CacheService.class.getName());
        service.setRef(new CacheServiceImpl());
        service.export();
        try {
            ReferenceConfig<CacheService> reference = new ReferenceConfig<CacheService>();
            reference.setApplication(new ApplicationConfig("cache-consumer"));
            reference.setInterface(CacheService.class);
            reference.setUrl("dubbo://127.0.0.1:29582?scope=remote&cache=true");

            MethodConfig method = new MethodConfig();
            method.setName("findCache");
            method.setCache(type);
            reference.setMethods(Arrays.asList(method));

            CacheService cacheService = reference.get();
            try {
                // verify cache, same result is returned for multiple invocations (in fact, the return value increases
                // on every invocation on the server side)
                String fix = null;
                for (int i = 0; i < 3; i++) {
                    String result = cacheService.findCache("0");
                    assertTrue(fix == null || fix.equals(result));
                    fix = result;
                    Thread.sleep(100);
                }

                if ("lru".equals(type)) {
                    // default cache.size is 1000 for LRU, should have cache expired if invoke more than 1001 times
                    for (int n = 0; n < 1001; n++) {
                        String pre = null;
                        for (int i = 0; i < 10; i++) {
                            String result = cacheService.findCache(String.valueOf(n));
                            assertTrue(pre == null || pre.equals(result));
                            pre = result;
                        }
                    }

                    // verify if the first cache item is expired in LRU cache
                    String result = cacheService.findCache("0");
                    assertFalse(fix == null || fix.equals(result));
                }
            } finally {
                reference.destroy();
            }
        } finally {
            service.unexport();
        }
    }

    @Test
    public void testCache() throws Exception {
        testCache("lru");
        testCache("threadlocal");
    }

    @Test
    public void testCacheProvider() throws Exception {
        CacheFactory cacheFactory = ExtensionLoader.getExtensionLoader(CacheFactory.class).getAdaptiveExtension();

        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("findCache.cache", "threadlocal");
        URL url = new URL("dubbo", "127.0.0.1", 29582, "com.alibaba.dubbo.config.cache.CacheService", parameters);

        Invocation invocation = new RpcInvocation("findCache", new Class[]{String.class}, new String[]{"0"}, null, null);

        Cache cache = cacheFactory.getCache(url, invocation);
        assertTrue(cache instanceof ThreadLocalCache);
    }

}
