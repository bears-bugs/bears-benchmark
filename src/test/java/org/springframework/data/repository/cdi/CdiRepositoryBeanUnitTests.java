/*
 * Copyright 2011-2017 the original author or authors.
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
package org.springframework.data.repository.cdi;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.repository.Repository;

/**
 * Unit tests for {@link CdiRepositoryBean}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class CdiRepositoryBeanUnitTests {

	static final String PASSIVATION_ID = "javax.enterprise.inject.Default:org.springframework.data.repository.cdi.CdiRepositoryBeanUnitTests$SampleRepository";

	static final Set<Annotation> NO_ANNOTATIONS = Collections.emptySet();
	static final Set<Annotation> SINGLE_ANNOTATION = Collections
			.singleton((Annotation) new CdiRepositoryExtensionSupport.DefaultAnnotationLiteral());

	@Mock
	BeanManager beanManager;

	@Test(expected = IllegalArgumentException.class)
	public void voidRejectsNullQualifiers() {
		new DummyCdiRepositoryBean<SampleRepository>(null, SampleRepository.class, beanManager);
	}

	@Test(expected = IllegalArgumentException.class)
	public void voidRejectsNullRepositoryType() {
		new DummyCdiRepositoryBean<SampleRepository>(NO_ANNOTATIONS, null, beanManager);
	}

	@Test(expected = IllegalArgumentException.class)
	public void voidRejectsNullBeanManager() {
		new DummyCdiRepositoryBean<SampleRepository>(NO_ANNOTATIONS, SampleRepository.class, null);
	}

	@Test
	public void returnsBasicMetadata() {

		DummyCdiRepositoryBean<SampleRepository> bean = new DummyCdiRepositoryBean<SampleRepository>(NO_ANNOTATIONS,
				SampleRepository.class, beanManager);

		assertThat(bean.getBeanClass(), is(typeCompatibleWith(SampleRepository.class)));
		assertThat(bean.getName(), is(SampleRepository.class.getName()));
		assertThat(bean.isNullable(), is(false));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void returnsAllImplementedTypes() {

		DummyCdiRepositoryBean<SampleRepository> bean = new DummyCdiRepositoryBean<SampleRepository>(NO_ANNOTATIONS,
				SampleRepository.class, beanManager);

		Set<Type> types = bean.getTypes();
		assertThat(types.size(), is(2));
		assertThat(types.containsAll(Arrays.asList(SampleRepository.class, Repository.class)), is(true));
	}

	@Test
	public void detectsStereotypes() {

		DummyCdiRepositoryBean<StereotypedSampleRepository> bean = new DummyCdiRepositoryBean<StereotypedSampleRepository>(
				NO_ANNOTATIONS, StereotypedSampleRepository.class, beanManager);

		Set<Class<? extends Annotation>> stereotypes = bean.getStereotypes();
		assertThat(stereotypes.size(), is(1));
		assertThat(stereotypes, hasItem(StereotypeAnnotation.class));
	}

	@Test // DATACMNS-299
	@SuppressWarnings("rawtypes")
	public void scopeDefaultsToApplicationScoped() {

		Bean<SampleRepository> bean = new DummyCdiRepositoryBean<SampleRepository>(NO_ANNOTATIONS, SampleRepository.class,
				beanManager);
		assertThat(bean.getScope(), equalTo((Class) ApplicationScoped.class));
	}

	@Test // DATACMNS-322
	public void createsPassivationId() {

		CdiRepositoryBean<SampleRepository> bean = new DummyCdiRepositoryBean<SampleRepository>(SINGLE_ANNOTATION,
				SampleRepository.class, beanManager);
		assertThat(bean.getId(), is(PASSIVATION_ID));
	}

	static class DummyCdiRepositoryBean<T> extends CdiRepositoryBean<T> {

		public DummyCdiRepositoryBean(Set<Annotation> qualifiers, Class<T> repositoryType, BeanManager beanManager) {
			super(qualifiers, repositoryType, beanManager);
		}

		@Override
		protected T create(CreationalContext<T> creationalContext, Class<T> repositoryType) {
			return null;
		}
	}

	static interface SampleRepository extends Repository<Object, Serializable> {

	}

	@StereotypeAnnotation
	static interface StereotypedSampleRepository {

	}
}
