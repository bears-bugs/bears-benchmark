/*
 * Copyright 2016-2017 the original author or authors.
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
package org.springframework.data.jpa.convert;

import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.Example.*;

import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.util.ObjectUtils;

/**
 * Unit tests for {@link QueryByExamplePredicateBuilder}.
 *
 * @author Christoph Strobl
 * @author Mark Paluch
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.Silent.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class QueryByExamplePredicateBuilderUnitTests {

	@Mock CriteriaBuilder cb;
	@Mock Root root;
	@Mock EntityType<Person> personEntityType;
	@Mock Expression expressionMock;
	@Mock Predicate truePredicate, dummyPredicate, andPredicate, orPredicate;
	@Mock Path dummyPath;

	Set<SingularAttribute<? super Person, ?>> personEntityAttribtues;

	SingularAttribute<? super Person, Long> personIdAttribute;
	SingularAttribute<? super Person, String> personFirstnameAttribute;
	SingularAttribute<? super Person, Long> personAgeAttribute;
	SingularAttribute<? super Person, Person> personFatherAttribute;
	SingularAttribute<? super Person, Skill> personSkillAttribute;
	SingularAttribute<? super Person, Address> personAddressAttribute;

	public @Rule ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() {

		personIdAttribute = new SingluarAttributeStub<Person, Long>("id", PersistentAttributeType.BASIC, Long.class);
		personFirstnameAttribute = new SingluarAttributeStub<Person, String>("firstname", PersistentAttributeType.BASIC,
				String.class);
		personAgeAttribute = new SingluarAttributeStub<Person, Long>("age", PersistentAttributeType.BASIC, Long.class);
		personFatherAttribute = new SingluarAttributeStub<Person, Person>("father", PersistentAttributeType.MANY_TO_ONE,
				Person.class, personEntityType);
		personSkillAttribute = new SingluarAttributeStub<Person, Skill>("skill", PersistentAttributeType.MANY_TO_ONE,
				Skill.class);
		personAddressAttribute = new SingluarAttributeStub<Person, Address>("address", PersistentAttributeType.EMBEDDED,
				Address.class);

		personEntityAttribtues = new LinkedHashSet<SingularAttribute<? super Person, ?>>();
		personEntityAttribtues.add(personIdAttribute);
		personEntityAttribtues.add(personFirstnameAttribute);
		personEntityAttribtues.add(personAgeAttribute);
		personEntityAttribtues.add(personFatherAttribute);
		personEntityAttribtues.add(personAddressAttribute);
		personEntityAttribtues.add(personSkillAttribute);

		doReturn(dummyPath).when(root).get(any(SingularAttribute.class));

		doReturn(personEntityType).when(root).getModel();
		doReturn(personEntityAttribtues).when(personEntityType).getSingularAttributes();

		doReturn(dummyPredicate).when(cb).equal(any(Expression.class), any(String.class));
		doReturn(dummyPredicate).when(cb).equal(any(Expression.class), any(Long.class));
		doReturn(dummyPredicate).when(cb).like(any(Expression.class), any(String.class));

		doReturn(expressionMock).when(cb).literal(any(Boolean.class));
		doReturn(truePredicate).when(cb).isTrue(eq(expressionMock));
		doReturn(andPredicate).when(cb).and(ArgumentMatchers.any());
		doReturn(orPredicate).when(cb).or(ArgumentMatchers.any());
	}

	@Test(expected = IllegalArgumentException.class) // DATAJPA-218
	public void getPredicateShouldThrowExceptionOnNullRoot() {
		QueryByExamplePredicateBuilder.getPredicate(null, cb, of(new Person()));
	}

	@Test(expected = IllegalArgumentException.class) // DATAJPA-218
	public void getPredicateShouldThrowExceptionOnNullCriteriaBuilder() {
		QueryByExamplePredicateBuilder.getPredicate(root, null, of(new Person()));
	}

	@Test(expected = IllegalArgumentException.class) // DATAJPA-218
	public void getPredicateShouldThrowExceptionOnNullExample() {
		QueryByExamplePredicateBuilder.getPredicate(root, null, null);
	}

	@Test // DATAJPA-218
	public void emptyCriteriaListShouldResultTruePredicate() {
		assertThat(QueryByExamplePredicateBuilder.getPredicate(root, cb, of(new Person())), equalTo(truePredicate));
	}

	@Test // DATAJPA-218
	public void singleElementCriteriaShouldJustReturnIt() {

		Person p = new Person();
		p.firstname = "foo";

		assertThat(QueryByExamplePredicateBuilder.getPredicate(root, cb, of(p)), equalTo(dummyPredicate));
		verify(cb, times(1)).equal(any(Expression.class), eq("foo"));
	}

	@Test // DATAJPA-937
	public void unresolvableNestedAssociatedPathShouldFail() {

		Person p = new Person();
		Person father = new Person();
		father.father = new Person();
		p.father = father;

		exception.expectCause(IsInstanceOf.<Throwable> instanceOf(IllegalArgumentException.class));
		exception.expectMessage("Unexpected path type");

		QueryByExamplePredicateBuilder.getPredicate(root, cb, of(p));
	}

	@Test // DATAJPA-218
	public void multiPredicateCriteriaShouldReturnCombinedOnes() {

		Person p = new Person();
		p.firstname = "foo";
		p.age = 2L;

		assertThat(QueryByExamplePredicateBuilder.getPredicate(root, cb, of(p)), equalTo(andPredicate));

		verify(cb, times(1)).equal(any(Expression.class), eq("foo"));
		verify(cb, times(1)).equal(any(Expression.class), eq(2L));
	}

	@Test // DATAJPA-879
	public void orConcatenatesPredicatesIfMatcherSpecifies() {

		Person person = new Person();
		person.firstname = "foo";
		person.age = 2L;

		Example<Person> example = of(person, ExampleMatcher.matchingAny());

		assertThat(QueryByExamplePredicateBuilder.getPredicate(root, cb, example), equalTo(orPredicate));

		verify(cb, times(1)).or(ArgumentMatchers.any());
	}

	static class Person {

		@Id Long id;
		String firstname;
		Long age;

		Person father;
		Address address;
		Skill skill;
	}

	static class Address {

		String city;
		String country;
	}

	static class Skill {

		@Id Long id;
		String name;
	}

	static class SingluarAttributeStub<X, T> implements SingularAttribute<X, T> {

		private String name;
		private PersistentAttributeType attributeType;
		private Class<T> javaType;
		private Type<T> type;

		public SingluarAttributeStub(String name,
				javax.persistence.metamodel.Attribute.PersistentAttributeType attributeType, Class<T> javaType) {
			this(name, attributeType, javaType, null);
		}

		public SingluarAttributeStub(String name,
				javax.persistence.metamodel.Attribute.PersistentAttributeType attributeType, Class<T> javaType, Type<T> type) {
			this.name = name;
			this.attributeType = attributeType;
			this.javaType = javaType;
			this.type = type;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public javax.persistence.metamodel.Attribute.PersistentAttributeType getPersistentAttributeType() {
			return attributeType;
		}

		@Override
		public ManagedType<X> getDeclaringType() {
			return null;
		}

		@Override
		public Class<T> getJavaType() {
			return javaType;
		}

		@Override
		public Member getJavaMember() {
			return null;
		}

		@Override
		public boolean isAssociation() {
			return !attributeType.equals(PersistentAttributeType.BASIC)
					&& !attributeType.equals(PersistentAttributeType.EMBEDDED);
		}

		@Override
		public boolean isCollection() {
			return false;
		}

		@Override
		public javax.persistence.metamodel.Bindable.BindableType getBindableType() {
			return BindableType.SINGULAR_ATTRIBUTE;
		}

		@Override
		public Class<T> getBindableJavaType() {
			return javaType;
		}

		@Override
		public boolean isId() {
			return ObjectUtils.nullSafeEquals(name, "id");
		}

		@Override
		public boolean isVersion() {
			return false;
		}

		@Override
		public boolean isOptional() {
			return false;
		}

		@Override
		public Type<T> getType() {
			return type;
		}

	}
}
