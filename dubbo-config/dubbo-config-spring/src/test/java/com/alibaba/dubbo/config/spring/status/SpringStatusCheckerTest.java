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
package com.alibaba.dubbo.config.spring.status;

import com.alibaba.dubbo.common.status.Status;
import com.alibaba.dubbo.config.spring.ServiceBean;

import com.alibaba.dubbo.config.spring.extension.SpringExtensionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.Lifecycle;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class SpringStatusCheckerTest {
    private SpringStatusChecker springStatusChecker;

    @Mock
    private ApplicationContext applicationContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.springStatusChecker = new SpringStatusChecker();
        new ServiceBean<Object>().setApplicationContext(applicationContext);
    }

    @After
    public void tearDown() throws Exception {
        SpringExtensionFactory.clearContexts();
        Mockito.reset(applicationContext);
    }

    @Test
    public void testWithoutApplicationContext() {
        Status status = springStatusChecker.check();

        assertThat(status.getLevel(), is(Status.Level.UNKNOWN));
    }

    @Test
    public void testWithLifeCycleRunning() {
        SpringExtensionFactory.clearContexts();
        ApplicationLifeCycle applicationLifeCycle = mock(ApplicationLifeCycle.class);
        new ServiceBean<Object>().setApplicationContext(applicationLifeCycle);
        given(applicationLifeCycle.getConfigLocations()).willReturn(new String[]{"test1", "test2"});
        given(applicationLifeCycle.isRunning()).willReturn(true);

        Status status = springStatusChecker.check();

        assertThat(status.getLevel(), is(Status.Level.OK));
        assertThat(status.getMessage(), is("test1,test2"));
    }

    @Test
    public void testWithoutLifeCycleRunning() {
        SpringExtensionFactory.clearContexts();
        ApplicationLifeCycle applicationLifeCycle = mock(ApplicationLifeCycle.class);
        new ServiceBean<Object>().setApplicationContext(applicationLifeCycle);
        given(applicationLifeCycle.isRunning()).willReturn(false);

        Status status = springStatusChecker.check();

        assertThat(status.getLevel(), is(Status.Level.ERROR));
    }

    interface ApplicationLifeCycle extends Lifecycle, ApplicationContext {
        String[] getConfigLocations();
    }
}
