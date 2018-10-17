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
package com.alibaba.dubbo.rpc.cluster.support;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.LogUtil;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcInvocation;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.cluster.Directory;
import com.alibaba.dubbo.rpc.cluster.filter.DemoService;

import junit.framework.Assert;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * FailfastClusterInvokerTest
 *
 */
@SuppressWarnings("unchecked")
public class FailSafeClusterInvokerTest {
    List<Invoker<DemoService>> invokers = new ArrayList<Invoker<DemoService>>();
    URL url = URL.valueOf("test://test:11/test");
    Invoker<DemoService> invoker = EasyMock.createMock(Invoker.class);
    RpcInvocation invocation = new RpcInvocation();
    Directory<DemoService> dic;
    Result result = new RpcResult();

    /**
     * @throws java.lang.Exception
     */

    @Before
    public void setUp() throws Exception {

        dic = EasyMock.createMock(Directory.class);

        EasyMock.expect(dic.getUrl()).andReturn(url).anyTimes();
        EasyMock.expect(dic.list(invocation)).andReturn(invokers).anyTimes();
        EasyMock.expect(dic.getInterface()).andReturn(DemoService.class).anyTimes();
        invocation.setMethodName("method1");
        EasyMock.replay(dic);

        invokers.add(invoker);
    }

    @After
    public void tearDown() {
        EasyMock.verify(invoker, dic);

    }

    private void resetInvokerToException() {
        EasyMock.reset(invoker);
        EasyMock.expect(invoker.invoke(invocation)).andThrow(new RuntimeException()).anyTimes();
        EasyMock.expect(invoker.getUrl()).andReturn(url).anyTimes();
        EasyMock.expect(invoker.getInterface()).andReturn(DemoService.class).anyTimes();
        EasyMock.replay(invoker);
    }

    private void resetInvokerToNoException() {
        EasyMock.reset(invoker);
        EasyMock.expect(invoker.invoke(invocation)).andReturn(result).anyTimes();
        EasyMock.expect(invoker.getUrl()).andReturn(url).anyTimes();
        EasyMock.expect(invoker.getInterface()).andReturn(DemoService.class).anyTimes();
        EasyMock.replay(invoker);
    }

    //TODO assert error log
    @Test
    public void testInvokeExceptoin() {
        resetInvokerToException();
        FailsafeClusterInvoker<DemoService> invoker = new FailsafeClusterInvoker<DemoService>(dic);
        invoker.invoke(invocation);
        Assert.assertNull(RpcContext.getContext().getInvoker());
    }

    @Test()
    public void testInvokeNoExceptoin() {

        resetInvokerToNoException();

        FailsafeClusterInvoker<DemoService> invoker = new FailsafeClusterInvoker<DemoService>(dic);
        Result ret = invoker.invoke(invocation);
        Assert.assertSame(result, ret);
    }

    @Test()
    public void testNoInvoke() {
        dic = EasyMock.createMock(Directory.class);

        EasyMock.expect(dic.getUrl()).andReturn(url).anyTimes();
        EasyMock.expect(dic.list(invocation)).andReturn(null).anyTimes();
        EasyMock.expect(dic.getInterface()).andReturn(DemoService.class).anyTimes();

        invocation.setMethodName("method1");
        EasyMock.replay(dic);

        resetInvokerToNoException();

        FailsafeClusterInvoker<DemoService> invoker = new FailsafeClusterInvoker<DemoService>(dic);
        LogUtil.start();
        invoker.invoke(invocation);
        assertTrue(LogUtil.findMessage("No provider") > 0);
        LogUtil.stop();
    }

}