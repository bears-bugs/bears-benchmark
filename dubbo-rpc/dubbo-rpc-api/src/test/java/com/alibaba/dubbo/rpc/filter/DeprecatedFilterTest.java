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
package com.alibaba.dubbo.rpc.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.LogUtil;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.support.DemoService;
import com.alibaba.dubbo.rpc.support.MockInvocation;
import com.alibaba.dubbo.rpc.support.MyInvoker;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * DeprecatedFilterTest.java
 */
public class DeprecatedFilterTest {

    Filter deprecatedFilter = new DeprecatedFilter();

    @Test
    public void testDeprecatedFilter() {
        URL url = URL.valueOf("test://test:11/test?group=dubbo&version=1.1&echo." + Constants.DEPRECATED_KEY + "=true");
        LogUtil.start();
        deprecatedFilter.invoke(new MyInvoker<DemoService>(url), new MockInvocation());
        assertEquals(1,
                LogUtil.findMessage("The service method com.alibaba.dubbo.rpc.support.DemoService.echo(String) is DEPRECATED"));
        LogUtil.stop();
    }
}