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
package org.springframework.data.web.querydsl;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QUser;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.querydsl.User;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.QuerydslBindingsFactory;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.StringPath;

/**
 * Unit tests for {@link QuerydslPredicateArgumentResolver}.
 * 
 * @author Christoph Strobl
 * @author Oliver Gierke
 */
public class QuerydslPredicateArgumentResolverUnitTests {

	static final TypeInformation<?> USER_TYPE = ClassTypeInformation.from(User.class);

	public @Rule ExpectedException exception = ExpectedException.none();

	QuerydslPredicateArgumentResolver resolver;
	MockHttpServletRequest request;

	@Before
	public void setUp() {

		resolver = new QuerydslPredicateArgumentResolver(new QuerydslBindingsFactory(SimpleEntityPathResolver.INSTANCE),
				null);
		request = new MockHttpServletRequest();
	}

	@Test // DATACMNS-669
	public void supportsParameterReturnsTrueWhenMethodParameterIsPredicateAndAnnotatedCorrectly() {
		assertThat(resolver.supportsParameter(getMethodParameterFor("simpleFind", Predicate.class)), is(true));
	}

	@Test // DATACMNS-669
	public void supportsParameterReturnsTrueWhenMethodParameterIsPredicateButNotAnnotatedAsSuch() {
		assertThat(resolver.supportsParameter(getMethodParameterFor("predicateWithoutAnnotation", Predicate.class)),
				is(true));
	}

	@Test(expected = IllegalArgumentException.class) // DATACMNS-669
	public void supportsParameterShouldThrowExceptionWhenMethodParameterIsNoPredicateButAnnotatedAsSuch() {
		resolver.supportsParameter(getMethodParameterFor("nonPredicateWithAnnotation", String.class));
	}

	@Test // DATACMNS-669
	public void supportsParameterReturnsFalseWhenMethodParameterIsNoPredicate() {
		assertThat(resolver.supportsParameter(getMethodParameterFor("nonPredicateWithoutAnnotation", String.class)),
				is(false));
	}

	@Test // DATACMNS-669
	public void resolveArgumentShouldCreateSingleStringParameterPredicateCorrectly() throws Exception {

		request.addParameter("firstname", "rand");

		Predicate predicate = (BooleanExpression) resolver.resolveArgument(
				getMethodParameterFor("simpleFind", Predicate.class), null, new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.firstname.eq("rand")));
	}

	@Test // DATACMNS-669
	public void resolveArgumentShouldCreateMultipleParametersPredicateCorrectly() throws Exception {

		request.addParameter("firstname", "rand");
		request.addParameter("lastname", "al'thor");

		Predicate predicate = resolver.resolveArgument(getMethodParameterFor("simpleFind", Predicate.class), null,
				new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.firstname.eq("rand").and(QUser.user.lastname.eq("al'thor"))));
	}

	@Test // DATACMNS-669
	public void resolveArgumentShouldCreateNestedObjectPredicateCorrectly() throws Exception {

		request.addParameter("address.city", "two rivers");

		Predicate predicate = resolver.resolveArgument(getMethodParameterFor("simpleFind", Predicate.class), null,
				new ServletWebRequest(request), null);

		BooleanExpression eq = QUser.user.address.city.eq("two rivers");

		assertThat(predicate, is((Predicate) eq));
	}

	@Test // DATACMNS-669
	public void resolveArgumentShouldResolveTypePropertyFromPageCorrectly() throws Exception {

		request.addParameter("address.city", "tar valon");

		Predicate predicate = resolver.resolveArgument(getMethodParameterFor("pagedFind", Predicate.class, Pageable.class),
				null, new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.address.city.eq("tar valon")));
	}

	@Test // DATACMNS-669
	public void resolveArgumentShouldHonorCustomSpecification() throws Exception {

		request.addParameter("firstname", "egwene");
		request.addParameter("lastname", "al'vere");

		Predicate predicate = resolver.resolveArgument(getMethodParameterFor("specificFind", Predicate.class), null,
				new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.firstname.eq("egwene".toUpperCase())
				.and(QUser.user.lastname.toLowerCase().eq("al'vere"))));
	}

	@Test // DATACMNS-669
	public void shouldCreatePredicateForNonStringPropertyCorrectly() throws Exception {

		request.addParameter("inceptionYear", "978");

		Predicate predicate = (BooleanExpression) resolver.resolveArgument(
				getMethodParameterFor("specificFind", Predicate.class), null, new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.inceptionYear.eq(978L)));
	}

	@Test // DATACMNS-669
	public void shouldCreatePredicateForNonStringListPropertyCorrectly() throws Exception {

		request.addParameter("inceptionYear", new String[] { "978", "998" });

		Predicate predicate = (BooleanExpression) resolver.resolveArgument(
				getMethodParameterFor("specificFind", Predicate.class), null, new ServletWebRequest(request), null);

		assertThat(predicate, is((Predicate) QUser.user.inceptionYear.in(978L, 998L)));
	}

	@Test // DATACMNS-669
	public void shouldExcludePropertiesCorrectly() throws Exception {

		request.addParameter("address.street", "downhill");
		request.addParameter("inceptionYear", "973");

		Object predicate = resolver.resolveArgument(getMethodParameterFor("specificFind", Predicate.class), null,
				new ServletWebRequest(request), null);

		assertThat(predicate.toString(), is(QUser.user.inceptionYear.eq(973L).toString()));
	}

	@Test // DATACMNS-669
	@SuppressWarnings("rawtypes")
	public void extractTypeInformationShouldUseTypeExtractedFromMethodReturnTypeIfPredicateNotAnnotated() {

		TypeInformation<?> type = ReflectionTestUtils.invokeMethod(resolver, "extractTypeInfo",
				getMethodParameterFor("predicateWithoutAnnotation", Predicate.class));

		assertThat(type, is((TypeInformation) ClassTypeInformation.from(User.class)));
	}

	@Test // DATACMNS-669
	@SuppressWarnings("rawtypes")
	public void detectsDomainTypesCorrectly() {

		TypeInformation USER_TYPE = ClassTypeInformation.from(User.class);
		TypeInformation MODELA_AND_VIEW_TYPE = ClassTypeInformation.from(ModelAndView.class);

		assertThat(extractTypeInfo(getMethodParameterFor("forEntity")), is(USER_TYPE));
		assertThat(extractTypeInfo(getMethodParameterFor("forResourceOfUser")), is(USER_TYPE));
		assertThat(extractTypeInfo(getMethodParameterFor("forModelAndView")), is(MODELA_AND_VIEW_TYPE));
	}

	private static MethodParameter getMethodParameterFor(String methodName, Class<?>... args) throws RuntimeException {

		try {
			return new MethodParameter(Sample.class.getMethod(methodName, args), args.length == 0 ? -1 : 0);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	static class SpecificBinding implements QuerydslBinderCustomizer<QUser> {

		public void customize(QuerydslBindings bindings, QUser user) {

			bindings.bind(user.firstname).first(new SingleValueBinding<StringPath, String>() {

				@Override
				public Predicate bind(StringPath path, String value) {
					return path.eq(value.toUpperCase());
				}
			});

			bindings.bind(user.lastname).first(new SingleValueBinding<StringPath, String>() {

				@Override
				public Predicate bind(StringPath path, String value) {
					return path.toLowerCase().eq(value);
				}
			});

			bindings.excluding(user.address);
		}
	}

	static interface Sample {

		User predicateWithoutAnnotation(Predicate predicate);

		User nonPredicateWithAnnotation(@QuerydslPredicate String predicate);

		User nonPredicateWithoutAnnotation(String predicate);

		User simpleFind(@QuerydslPredicate Predicate predicate);

		Page<User> pagedFind(@QuerydslPredicate Predicate predicate, Pageable page);

		User specificFind(@QuerydslPredicate(bindings = SpecificBinding.class) Predicate predicate);

		HttpEntity<User> forEntity();

		ModelAndView forModelAndView();

		ResponseEntity<Resource<User>> forResourceOfUser();
	}

	public static class SampleRepo implements QuerydslBinderCustomizer<QUser> {

		@Override
		public void customize(QuerydslBindings bindings, QUser user) {

			bindings.bind(QUser.user.firstname).first(new SingleValueBinding<StringPath, String>() {

				@Override
				public Predicate bind(StringPath path, String value) {
					return path.contains(value);
				}
			});
		}
	}
}
