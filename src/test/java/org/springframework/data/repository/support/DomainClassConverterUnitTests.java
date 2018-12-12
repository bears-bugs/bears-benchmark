/*
 * Copyright 2008-2017 the original author or authors.
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
package org.springframework.data.repository.support;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.support.DummyRepositoryFactoryBean;
import org.springframework.data.repository.support.DomainClassConverter.ToIdConverter;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Unit test for {@link DomainClassConverter}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
@RunWith(MockitoJUnitRunner.class)
public class DomainClassConverterUnitTests {

	static final User USER = new User();
	static final TypeDescriptor STRING_TYPE = TypeDescriptor.valueOf(String.class);
	static final TypeDescriptor USER_TYPE = TypeDescriptor.valueOf(User.class);
	static final TypeDescriptor SUB_USER_TYPE = TypeDescriptor.valueOf(SubUser.class);
	static final TypeDescriptor LONG_TYPE = TypeDescriptor.valueOf(Long.class);

	@SuppressWarnings("rawtypes") DomainClassConverter converter;

	@Mock DefaultConversionService service;

	@Before
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setUp() {
		converter = new DomainClassConverter(service);
	}

	@Test
	public void matchFailsIfNoDaoAvailable() throws Exception {

		GenericApplicationContext ctx = new GenericApplicationContext();
		ctx.refresh();
		converter.setApplicationContext(ctx);
		assertMatches(false);
	}

	@Test
	public void matchesIfConversionInBetweenIsPossible() throws Exception {

		converter.setApplicationContext(initContextWithRepo());

		when(service.canConvert(String.class, Long.class)).thenReturn(true);

		assertMatches(true);
	}

	@Test
	public void matchFailsIfNoIntermediateConversionIsPossible() throws Exception {

		converter.setApplicationContext(initContextWithRepo());

		when(service.canConvert(String.class, Long.class)).thenReturn(false);

		assertMatches(false);
	}

	// DATACMNS-233
	public void returnsNullForNullSource() {
		assertThat(converter.convert(null, STRING_TYPE, USER_TYPE), is(nullValue()));
	}

	// DATACMNS-233
	public void returnsNullForEmptyStringSource() {
		assertThat(converter.convert("", STRING_TYPE, USER_TYPE), is(nullValue()));
	}

	private void assertMatches(boolean matchExpected) {

		assertThat(converter.matches(STRING_TYPE, USER_TYPE), is(matchExpected));
	}

	@Test
	public void convertsStringToUserCorrectly() throws Exception {

		ApplicationContext context = initContextWithRepo();
		converter.setApplicationContext(context);

		when(service.canConvert(String.class, Long.class)).thenReturn(true);
		when(service.convert(anyString(), eq(Long.class))).thenReturn(1L);

		converter.convert("1", STRING_TYPE, USER_TYPE);

		UserRepository bean = context.getBean(UserRepository.class);
		UserRepository repo = (UserRepository) ((Advised) bean).getTargetSource().getTarget();

		verify(repo, times(1)).findOne(1L);
	}

	@Test // DATACMNS-133
	public void discoversFactoryAndRepoFromParentApplicationContext() {

		ApplicationContext parent = initContextWithRepo();
		GenericApplicationContext context = new GenericApplicationContext(parent);
		context.refresh();

		when(service.canConvert(String.class, Long.class)).thenReturn(true);

		converter.setApplicationContext(context);
		assertThat(converter.matches(STRING_TYPE, USER_TYPE), is(true));
	}

	@Test // DATACMNS-583
	public void converterDoesntMatchIfTargetTypeIsAssignableFromSource() {

		converter.setApplicationContext(initContextWithRepo());

		assertThat(converter.matches(SUB_USER_TYPE, USER_TYPE), is(false));
		assertThat((User) converter.convert(USER, USER_TYPE, USER_TYPE), is(USER));
	}

	@Test // DATACMNS-627
	public void supportsConversionFromIdType() {

		converter.setApplicationContext(initContextWithRepo());

		assertThat(converter.matches(LONG_TYPE, USER_TYPE), is(true));
	}

	@Test // DATACMNS-627
	public void supportsConversionFromEntityToIdType() {

		converter.setApplicationContext(initContextWithRepo());

		assertThat(converter.matches(USER_TYPE, LONG_TYPE), is(true));
	}

	@Test // DATACMNS-627
	public void supportsConversionFromEntityToString() {

		converter.setApplicationContext(initContextWithRepo());

		when(service.canConvert(Long.class, String.class)).thenReturn(true);
		assertThat(converter.matches(USER_TYPE, STRING_TYPE), is(true));
	}

	@Test // DATACMNS-683
	public void toIdConverterDoesNotMatchIfTargetTypeIsAssignableFromSource() throws Exception {

		converter.setApplicationContext(initContextWithRepo());

		@SuppressWarnings("rawtypes")
		DomainClassConverter.ToIdConverter toIdConverter = (ToIdConverter) ReflectionTestUtils.getField(converter,
				"toIdConverter");

		Method method = Wrapper.class.getMethod("foo", User.class);
		TypeDescriptor target = TypeDescriptor.nested(new MethodParameter(method, 0), 0);

		assertThat(toIdConverter.matches(SUB_USER_TYPE, target), is(false));
	}

	private ApplicationContext initContextWithRepo() {

		BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(DummyRepositoryFactoryBean.class);
		builder.addConstructorArgValue(UserRepository.class);

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerBeanDefinition("provider", builder.getBeanDefinition());

		GenericApplicationContext ctx = new GenericApplicationContext(factory);
		ctx.refresh();
		return ctx;
	}

	static interface Wrapper {

		void foo(@ModelAttribute User user);
	}

	private static class User {

	}

	private static class SubUser extends User {}

	private static interface UserRepository extends CrudRepository<User, Long> {

	}
}
