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
package com.alibaba.dubbo.rpc.benchmark;

import com.alibaba.dubbo.remoting.exchange.ExchangeClient;
import com.alibaba.dubbo.remoting.exchange.Exchangers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

/**
 * Abstract ExchangeClient Factory,create custom nums ExchangeClient
 */
public class ExchangeClientFactory {

    // Cache ExchangeClient
    private static ConcurrentHashMap<String, FutureTask<List<ExchangeClient>>> clients = new ConcurrentHashMap<String, FutureTask<List<ExchangeClient>>>();

    public static ExchangeClientFactory getInstance() {
        throw new UnsupportedOperationException("should be implemented by true class");
    }

    public ExchangeClient get(final String targetIP, final int targetPort, final int connectTimeout) throws Exception {
        return get(targetIP, targetPort, connectTimeout, 1);
    }

    public ExchangeClient get(final String targetIP, final int targetPort, final int connectTimeout,
                              final int clientNums) throws Exception {
        String key = targetIP + ":" + targetPort;
        if (clients.containsKey(key)) {
            if (clientNums == 1) {
                return clients.get(key).get().get(0);
            } else {
                Random random = new Random();
                return clients.get(key).get().get(random.nextInt(clientNums));
            }
        } else {
            FutureTask<List<ExchangeClient>> task = new FutureTask<List<ExchangeClient>>(
                    new Callable<List<ExchangeClient>>() {

                        @Override
                        public List<ExchangeClient> call()
                                throws Exception {
                            List<ExchangeClient> clients = new ArrayList<ExchangeClient>(
                                    clientNums);
                            for (int i = 0; i < clientNums; i++) {
                                clients.add(createClient(targetIP,
                                        targetPort,
                                        connectTimeout));
                            }
                            return clients;
                        }
                    });
            FutureTask<List<ExchangeClient>> currentTask = clients.putIfAbsent(key, task);
            if (currentTask == null) {
                task.run();
            } else {
                task = currentTask;
            }
            if (clientNums == 1) return task.get().get(0);
            else {
                Random random = new Random();
                return task.get().get(random.nextInt(clientNums));
            }
        }
    }

    public void removeClient(String key, ExchangeClient ExchangeClient) {
        try {
            // TODO: Fix It
            clients.remove(key);
            // clients.get(key).get().remove(ExchangeClient);
            // clients.get(key)
            // .get()
            // .add(createClient(ExchangeClient.getServerIP(),
            // ExchangeClient.getServerPort(), ExchangeClient.getConnectTimeout(),
            // key));
        } catch (Exception e) {
            // IGNORE
        }
    }

    protected ExchangeClient createClient(String targetIP, int targetPort, int connectTimeout) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append("exchange://");
        url.append(targetIP);
        url.append(":");
        url.append(targetPort);
        url.append("?");
        url.append("timeout=");
        url.append(connectTimeout);
        return Exchangers.connect(url.toString());
    }

}
