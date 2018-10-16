/*
 * Copyright 2013-2017 the original author or authors.
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
package org.springframework.data.web;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Sort.Direction.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.web.SortDefault.SortDefaults;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * Unit tests for {@link SortHandlerMethodArgumentResolver}.
 * 
 * @since 1.6
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Nick Williams
 */
public class SortHandlerMethodArgumentResolverUnitTests extends SortDefaultUnitTests {

	static MethodParameter PARAMETER;

	@BeforeClass
	public static void setUp() throws Exception {
		PARAMETER = new MethodParameter(Controller.class.getMethod("supportedMethod", Sort.class), 0);
	}

	@Test // DATACMNS-351
	public void fallbackToGivenDefaultSort() throws Exception {

		MethodParameter parameter = TestUtils.getParameterOfMethod(getControllerClass(), "unsupportedMethod", String.class);
		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();
		Sort fallbackSort = new Sort(Direction.ASC, "ID");
		resolver.setFallbackSort(fallbackSort);

		Sort sort = resolver.resolveArgument(parameter, null, new ServletWebRequest(new MockHttpServletRequest()), null);
		assertThat(sort, is(fallbackSort));
	}

	@Test // DATACMNS-351
	public void fallbackToDefaultDefaultSort() throws Exception {

		MethodParameter parameter = TestUtils.getParameterOfMethod(getControllerClass(), "unsupportedMethod", String.class);
		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();

		Sort sort = resolver.resolveArgument(parameter, null, new ServletWebRequest(new MockHttpServletRequest()), null);
		assertThat(sort, is(nullValue()));
	}

	@Test
	public void discoversSimpleSortFromRequest() {

		MethodParameter parameter = getParameterOfMethod("simpleDefault");
		Sort reference = new Sort("bar", "foo");
		NativeWebRequest request = getRequestWithSort(reference);

		assertSupportedAndResolvedTo(request, parameter, reference);
	}

	@Test
	public void discoversComplexSortFromRequest() {

		MethodParameter parameter = getParameterOfMethod("simpleDefault");
		Sort reference = new Sort("bar", "foo").and(new Sort("fizz", "buzz"));

		assertSupportedAndResolvedTo(getRequestWithSort(reference), parameter, reference);
	}

	@Test
	public void discoversQualifiedSortFromRequest() {

		MethodParameter parameter = getParameterOfMethod("qualifiedSort");
		Sort reference = new Sort("bar", "foo");

		assertSupportedAndResolvedTo(getRequestWithSort(reference, "qual"), parameter, reference);
	}

	@Test
	public void returnsNullForSortParameterSetToNothing() throws Exception {

		MethodParameter parameter = getParameterOfMethod("supportedMethod");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", (String) null);

		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();
		Sort result = resolver.resolveArgument(parameter, null, new ServletWebRequest(request), null);
		assertThat(result, is(nullValue()));
	}

	@Test // DATACMNS-366
	public void requestForMultipleSortPropertiesIsUnmarshalledCorrectly() throws Exception {

		MethodParameter parameter = getParameterOfMethod("supportedMethod");

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", SORT_3);

		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();
		Sort result = resolver.resolveArgument(parameter, null, new ServletWebRequest(request), null);
		assertThat(result, is(new Sort(Direction.ASC, "firstname", "lastname")));
	}

	@Test // DATACMNS-408
	public void parsesEmptySortToNull() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", "");

		assertThat(resolveSort(request, PARAMETER), is(nullValue()));
	}

	@Test // DATACMNS-408
	public void sortParamIsInvalidProperty() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", ",DESC");

		assertThat(resolveSort(request, PARAMETER), is(nullValue()));
	}

	@Test // DATACMNS-408
	public void sortParamIsInvalidPropertyWhenMultiProperty() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", "property1,,DESC");

		assertThat(resolveSort(request, PARAMETER), is(new Sort(DESC, "property1")));
	}

	@Test // DATACMNS-408
	public void sortParamIsEmptyWhenMultiParams() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", "property,DESC");
		request.addParameter("sort", "");

		assertThat(resolveSort(request, PARAMETER), is(new Sort(DESC, "property")));
	}

	@Test // DATACMNS-379
	public void parsesCommaParameterForSort() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", ",");

		assertThat(resolveSort(request, PARAMETER), is(nullValue()));
	}

	@Test // DATACMNS-753, DATACMNS-408
	public void doesNotReturnNullWhenAnnotatedWithSortDefault() throws Exception {

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("sort", "");

		assertThat(resolveSort(request, getParameterOfMethod("simpleDefault")), is(new Sort("firstname", "lastname")));
		assertThat(resolveSort(request, getParameterOfMethod("containeredDefault")), is(new Sort("foo", "bar")));
	}

	private static Sort resolveSort(HttpServletRequest request, MethodParameter parameter) throws Exception {

		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();
		return resolver.resolveArgument(parameter, null, new ServletWebRequest(request), null);
	}

	private static void assertSupportedAndResolvedTo(NativeWebRequest request, MethodParameter parameter, Sort sort) {

		SortHandlerMethodArgumentResolver resolver = new SortHandlerMethodArgumentResolver();
		assertThat(resolver.supportsParameter(parameter), is(true));

		try {
			assertThat(resolver.resolveArgument(parameter, null, request, null), is(sort));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static NativeWebRequest getRequestWithSort(Sort sort) {
		return getRequestWithSort(sort, null);
	}

	private static NativeWebRequest getRequestWithSort(Sort sort, String qualifier) {

		MockHttpServletRequest request = new MockHttpServletRequest();

		if (sort == null) {
			return new ServletWebRequest(request);
		}

		for (Order order : sort) {

			String prefix = StringUtils.hasText(qualifier) ? qualifier + "_" : "";
			request.addParameter(prefix + "sort", String.format("%s,%s", order.getProperty(), order.getDirection().name()));
		}

		return new ServletWebRequest(request);
	}

	@Override
	protected Class<?> getControllerClass() {
		return Controller.class;
	}

	interface Controller {

		void supportedMethod(Sort sort);

		void unsupportedMethod(String string);

		void qualifiedSort(@Qualifier("qual") Sort sort);

		void simpleDefault(@SortDefault({ "firstname", "lastname" }) Sort sort);

		void simpleDefaultWithDirection(
				@SortDefault(sort = { "firstname", "lastname" }, direction = Direction.DESC) Sort sort);

		void containeredDefault(@SortDefaults(@SortDefault({ "foo", "bar" })) Sort sort);

		void invalid(@SortDefaults(@SortDefault({ "foo", "bar" })) @SortDefault({ "bar", "foo" }) Sort sort);
	}
}
