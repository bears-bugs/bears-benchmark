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
package org.springframework.data.jpa.repository.query;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceProviderResolver;
import javax.persistence.spi.PersistenceProviderResolverHolder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.sample.Category;
import org.springframework.data.jpa.domain.sample.Order;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.jpa.infrastructure.HibernateTestUtils;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration tests for {@link QueryUtils}.
 * 
 * @author Oliver Gierke
 * @author Sébastien Péralta
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:infrastructure.xml")
public class QueryUtilsIntegrationTests {

	@PersistenceContext EntityManager em;

	@Test // DATAJPA-403
	public void reusesExistingJoinForExpression() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> from = query.from(User.class);

		PropertyPath managerFirstname = PropertyPath.from("manager.firstname", User.class);
		PropertyPath managerLastname = PropertyPath.from("manager.lastname", User.class);

		QueryUtils.toExpressionRecursively(from, managerLastname);
		QueryUtils.toExpressionRecursively(from, managerFirstname);

		assertThat(from.getJoins()).hasSize(1);
	}

	@Test // DATAJPA-401
	public void createsJoinForOptionalAssociation() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		QueryUtils.toExpressionRecursively(root, PropertyPath.from("manager", User.class));

		assertThat(root.getJoins()).hasSize(1);
	}

	@Test // DATAJPA-401
	public void doesNotCreateAJoinForNonOptionalAssociation() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Order> query = builder.createQuery(Order.class);
		Root<Order> root = query.from(Order.class);

		QueryUtils.toExpressionRecursively(root, PropertyPath.from("customer", Order.class));
	}

	@Test // DATAJPA-454
	public void createsJoingToTraverseCollectionPath() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		QueryUtils.toExpressionRecursively(root, PropertyPath.from("colleaguesLastname", User.class));

		assertThat(root.getJoins()).hasSize(1);
	}

	@Test // DATAJPA-476
	public void traversesPluralAttributeCorrectly() {

		PersistenceProviderResolver originalPersistenceProviderResolver = PersistenceProviderResolverHolder
				.getPersistenceProviderResolver();

		try {

			PersistenceProviderResolverHolder.setPersistenceProviderResolver(new HibernateOnlyPersistenceProviderResolver());
			EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("merchant");
			CriteriaBuilder builder = entityManagerFactory.createEntityManager().getCriteriaBuilder();
			CriteriaQuery<Merchant> query = builder.createQuery(Merchant.class);
			Root<Merchant> root = query.from(Merchant.class);

			QueryUtils.toExpressionRecursively(root, PropertyPath.from("employeesCredentialsUid", Merchant.class));

		} finally {
			PersistenceProviderResolverHolder.setPersistenceProviderResolver(originalPersistenceProviderResolver);
		}
	}

	@Test // DATAJPA-763
	@SuppressWarnings("unchecked")
	public void doesNotCreateAJoinForAlreadyFetchedAssociation() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Category> query = builder.createQuery(Category.class);

		Root<Category> root = query.from(Category.class);

		Root<Category> mock = Mockito.mock(Root.class);
		doReturn(root.getModel()).when(mock).getModel();
		doReturn(Collections.singleton(root.fetch("product", JoinType.LEFT))).when(mock).getFetches();

		QueryUtils.toExpressionRecursively(mock, PropertyPath.from("product", Category.class));

		verify(mock, times(1)).get("product");
		verify(mock, times(0)).join(Mockito.eq("product"), Mockito.any(JoinType.class));
	}

	@Test // DATAJPA-1080
	public void toOrdersCanSortByJoinColumn() {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		Join<User, User> join = root.join("manager", JoinType.LEFT);

		Sort sort = new Sort(Direction.ASC, "age");

		List<javax.persistence.criteria.Order> orders = QueryUtils.toOrders(sort, join, builder);

		assertThat(orders).hasSize(1);
	}

	@Entity
	@SuppressWarnings("unused")
	static class Merchant {

		@Id String id;
		@OneToMany Set<Employee> employees;
	}

	@Entity
	@SuppressWarnings("unused")
	static class Employee {

		@Id String id;
		@OneToMany Set<Credential> credentials;
	}

	@Entity
	@SuppressWarnings("unused")
	static class Credential {

		@Id String id;
		String uid;
	}

	/**
	 * A {@link PersistenceProviderResolver} that returns only a Hibernate {@link PersistenceProvider} and ignores others.
	 * 
	 * @author Thomas Darimont
	 * @author Oliver Gierke
	 */
	static class HibernateOnlyPersistenceProviderResolver implements PersistenceProviderResolver {

		@Override
		public List<PersistenceProvider> getPersistenceProviders() {
			return singletonList(HibernateTestUtils.getPersistenceProvider());
		}

		@Override
		public void clearCachedProviders() {}
	}
}
