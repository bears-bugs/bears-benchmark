/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.projection;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for {@link MapAccessingMethodInterceptor}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class MapAccessingMethodInterceptorUnitTests {

	@Mock MethodInvocation invocation;

	@Test(expected = IllegalArgumentException.class) // DATACMNS-630
	public void rejectsNullMap() {
		new MapAccessingMethodInterceptor(null);
	}

	@Test // DATACMNS-630
	public void forwardsObjectMethodsToBackingMap() throws Throwable {

		Map<String, Object> map = Collections.emptyMap();

		when(invocation.proceed()).thenReturn(map.toString());
		when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

		MapAccessingMethodInterceptor interceptor = new MapAccessingMethodInterceptor(map);
		Object result = interceptor.invoke(invocation);

		assertThat(result, is((Object) map.toString()));
	}

	@Test // DATACMNS-630
	public void setterInvocationStoresValueInMap() throws Throwable {

		Map<String, Object> map = new HashMap<String, Object>();

		when(invocation.getMethod()).thenReturn(Sample.class.getMethod("setName", String.class));
		when(invocation.getArguments()).thenReturn(new Object[] { "Foo" });

		Object result = new MapAccessingMethodInterceptor(map).invoke(invocation);

		assertThat(result, is(nullValue()));
		assertThat(map.get("name"), is((Object) "Foo"));
	}

	@Test // DATACMNS-630
	public void getterInvocationReturnsValueFromMap() throws Throwable {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "Foo");

		when(invocation.getMethod()).thenReturn(Sample.class.getMethod("getName"));

		Object result = new MapAccessingMethodInterceptor(map).invoke(invocation);

		assertThat(result, is((Object) "Foo"));
	}

	@Test // DATACMNS-630
	public void getterReturnsNullIfMapDoesNotContainValue() throws Throwable {

		Map<String, Object> map = new HashMap<String, Object>();

		when(invocation.getMethod()).thenReturn(Sample.class.getMethod("getName"));

		assertThat(new MapAccessingMethodInterceptor(map).invoke(invocation), is(nullValue()));
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-630
	public void rejectsNonAccessorInvocation() throws Throwable {

		when(invocation.getMethod()).thenReturn(Sample.class.getMethod("someMethod"));
		new MapAccessingMethodInterceptor(Collections.<String, Object> emptyMap()).invoke(invocation);
	}

	interface Sample {

		String getName();

		void setName(String name);

		void someMethod();
	}
}
