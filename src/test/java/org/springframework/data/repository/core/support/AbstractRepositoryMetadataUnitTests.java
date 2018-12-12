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
package org.springframework.data.repository.core.support;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.RepositoryMetadata;

/**
 * Unit tests for {@link AbstractRepositoryMetadata}.
 * 
 * @author Oliver Gierke
 * @author Thomas Darimont
 * @author Fabian Buch
 */
public class AbstractRepositoryMetadataUnitTests {

	@Test // DATACMNS-98
	public void discoversSimpleReturnTypeCorrectly() throws Exception {

		RepositoryMetadata metadata = new DummyRepositoryMetadata(UserRepository.class);
		Method method = UserRepository.class.getMethod("findSingle");
		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(User.class)));
	}

	@Test // DATACMNS-98
	public void resolvesTypeParameterReturnType() throws Exception {
		RepositoryMetadata metadata = new DummyRepositoryMetadata(ConcreteRepository.class);
		Method method = ConcreteRepository.class.getMethod("intermediateMethod");
		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(User.class)));
	}

	@Test // DATACMNS-98
	public void determinesReturnTypeFromPageable() throws Exception {

		RepositoryMetadata metadata = new DummyRepositoryMetadata(ExtendingRepository.class);
		Method method = ExtendingRepository.class.getMethod("findByFirstname", Pageable.class, String.class);
		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(User.class)));
	}

	@Test // DATACMNS-453
	public void nonPageableRepository() {

		RepositoryMetadata metadata = new DummyRepositoryMetadata(UserRepository.class);
		assertThat(metadata.isPagingRepository(), is(false));
	}

	@Test // DATACMNS-453
	public void pageableRepository() {

		RepositoryMetadata metadata = new DummyRepositoryMetadata(PagedRepository.class);
		assertThat(metadata.isPagingRepository(), is(true));
	}

	@Test // DATACMNS-98
	public void determinesReturnTypeFromGenericType() throws Exception {
		RepositoryMetadata metadata = new DummyRepositoryMetadata(ExtendingRepository.class);
		Method method = ExtendingRepository.class.getMethod("someMethod");
		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(GenericType.class)));
	}

	@Test // DATACMNS-98
	public void handlesGenericTypeInReturnedCollectionCorrectly() throws SecurityException, NoSuchMethodException {

		RepositoryMetadata metadata = new DummyRepositoryMetadata(ExtendingRepository.class);
		Method method = ExtendingRepository.class.getMethod("anotherMethod");
		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(Map.class)));
	}

	@Test // DATACMNS-471
	public void detectsArrayReturnTypeCorrectly() throws Exception {

		RepositoryMetadata metadata = new DefaultRepositoryMetadata(PagedRepository.class);
		Method method = PagedRepository.class.getMethod("returnsArray");

		assertThat(metadata.getReturnedDomainClass(method), is(typeCompatibleWith(User.class)));
	}

	interface UserRepository extends Repository<User, Long> {

		User findSingle();
	}

	interface IntermediateRepository<T> extends Repository<T, Long> {

		List<T> intermediateMethod();
	}

	interface ConcreteRepository extends IntermediateRepository<User> {

	}

	interface ExtendingRepository extends Serializable, UserRepository {

		Page<User> findByFirstname(Pageable pageable, String firstname);

		GenericType<User> someMethod();

		List<Map<String, Object>> anotherMethod();
	}

	interface PagedRepository extends PagingAndSortingRepository<User, Long> {

		User[] returnsArray();
	}

	class GenericType<T> {

	}

	class DummyRepositoryMetadata extends AbstractRepositoryMetadata {

		public DummyRepositoryMetadata(Class<?> repositoryInterface) {
			super(repositoryInterface);
		}

		public Class<? extends Serializable> getIdType() {
			return null;
		}

		public Class<?> getDomainType() {
			return null;
		}
	}

}
