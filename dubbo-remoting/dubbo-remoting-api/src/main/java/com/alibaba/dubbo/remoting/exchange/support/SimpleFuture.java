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
package com.alibaba.dubbo.remoting.exchange.support;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.remoting.exchange.ResponseFuture;

/**
 * SimpleFuture
 */
public class SimpleFuture implements ResponseFuture {

    private final Object value;

    public SimpleFuture(Object value) {
        this.value = value;
    }

    public Object get() throws RemotingException {
        return value;
    }

    public Object get(int timeoutInMillis) throws RemotingException {
        return value;
    }

    public void setCallback(ResponseCallback callback) {
        callback.done(value);
    }

    public boolean isDone() {
        return true;
    }

}