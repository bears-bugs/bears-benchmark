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
package com.alibaba.dubbo.config.mock;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Exporter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Protocol;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

public class MockProtocol implements Protocol {

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.Protocol#getDefaultPort()
     */
    @Override
    public int getDefaultPort() {

        return 0;
    }

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.Protocol#export(com.alibaba.dubbo.rpc.Invoker)
     */
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        return null;
    }

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.Protocol#refer(java.lang.Class, com.alibaba.dubbo.common.URL)
     */
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {

        final URL u = url;

        return new Invoker<T>() {
            @Override
            public Class<T> getInterface() {
                return null;
            }

            public URL getUrl() {
                return u;
            }

            @Override
            public boolean isAvailable() {
                return true;
            }

            public Result invoke(Invocation invocation) throws RpcException {
                return null;
            }

            @Override
            public void destroy() {

            }
        };
    }

    /* (non-Javadoc)
     * @see com.alibaba.dubbo.rpc.Protocol#destroy()
     */
    @Override
    public void destroy() {

    }

}
