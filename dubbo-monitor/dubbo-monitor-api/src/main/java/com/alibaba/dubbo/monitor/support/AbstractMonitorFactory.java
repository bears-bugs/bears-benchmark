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
package com.alibaba.dubbo.monitor.support;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.concurrent.ListenableFuture;
import com.alibaba.dubbo.common.concurrent.ListenableFutureTask;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.monitor.Monitor;
import com.alibaba.dubbo.monitor.MonitorFactory;
import com.alibaba.dubbo.monitor.MonitorService;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AbstractMonitorFactory. (SPI, Singleton, ThreadSafe)
 */
public abstract class AbstractMonitorFactory implements MonitorFactory {
    private static final Logger logger = LoggerFactory.getLogger(AbstractMonitorFactory.class);

    // lock for getting monitor center
    private static final ReentrantLock LOCK = new ReentrantLock();

    // monitor centers Map<RegistryAddress, Registry>
    private static final Map<String, Monitor> MONITORS = new ConcurrentHashMap<String, Monitor>();

    private static final Map<String, ListenableFuture<Monitor>> FUTURES = new ConcurrentHashMap<String, ListenableFuture<Monitor>>();

    private static final ExecutorService executor = new ThreadPoolExecutor(0, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new NamedThreadFactory("DubboMonitorCreator", true));

    public static Collection<Monitor> getMonitors() {
        return Collections.unmodifiableCollection(MONITORS.values());
    }

    @Override
    public Monitor getMonitor(URL url) {
        url = url.setPath(MonitorService.class.getName()).addParameter(Constants.INTERFACE_KEY, MonitorService.class.getName());
        String key = url.toServiceStringWithoutResolving();
        Monitor monitor = MONITORS.get(key);
        Future<Monitor> future = FUTURES.get(key);
        if (monitor != null || future != null) {
            return monitor;
        }

        LOCK.lock();
        try {
            monitor = MONITORS.get(key);
            future = FUTURES.get(key);
            if (monitor != null || future != null) {
                return monitor;
            }

            final URL monitorUrl = url;
            final ListenableFutureTask<Monitor> listenableFutureTask = ListenableFutureTask.create(new MonitorCreator(monitorUrl));
            listenableFutureTask.addListener(new MonitorListener(key));
            executor.execute(listenableFutureTask);
            FUTURES.put(key, listenableFutureTask);

            return null;
        } finally {
            // unlock
            LOCK.unlock();
        }
    }

    protected abstract Monitor createMonitor(URL url);

    class MonitorCreator implements Callable<Monitor> {

        private URL url;

        public MonitorCreator(URL url) {
            this.url = url;
        }

        @Override
        public Monitor call() throws Exception {
            Monitor monitor = AbstractMonitorFactory.this.createMonitor(url);
            return monitor;
        }
    }

    class MonitorListener implements Runnable {

        private String key;

        public MonitorListener(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            try {
                ListenableFuture<Monitor> listenableFuture = AbstractMonitorFactory.FUTURES.get(key);
                AbstractMonitorFactory.MONITORS.put(key, listenableFuture.get());
                AbstractMonitorFactory.FUTURES.remove(key);
            } catch (InterruptedException e) {
                logger.warn("Thread was interrupted unexpectedly, monitor will never be got.");
                AbstractMonitorFactory.FUTURES.remove(key);
            } catch (ExecutionException e) {
                logger.warn("Create monitor failed, monitor data will not be collected until you fix this problem. ", e);
            }
        }
    }

}
