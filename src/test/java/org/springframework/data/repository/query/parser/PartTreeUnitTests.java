/*
 * Copyright 2008-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.springframework.data.repository.query.parser;

import static java.util.Arrays.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.data.repository.query.parser.Part.Type.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.repository.query.parser.Part.IgnoreCaseType;
import org.springframework.data.repository.query.parser.Part.Type;
import org.springframework.data.repository.query.parser.PartTree.OrPart;

/**
 * Unit tests for {@link PartTree}.
 * 
 * @author Oliver Gierke
 * @author Phillip Webb
 * @author Thomas Darimont
 * @author Martin Baumgartner
 * @author Christoph Strobl
 */
public class PartTreeUnitTests {

	private String[] PREFIXES = { "find", "read", "get", "query", "stream", "count", "delete", "remove" };

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullSource() throws Exception {
		new PartTree(null, getClass());
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNullDomainClass() throws Exception {
		new PartTree("test", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsMultipleOrderBy() throws Exception {
		partTree("firstnameOrderByLastnameOrderByFirstname");
	}

	@Test
	public void parsesSimplePropertyCorrectly() throws Exception {
		PartTree partTree = partTree("firstname");
		assertPart(partTree, parts("firstname"));
	}

	@Test
	public void parsesAndPropertiesCorrectly() throws Exception {
		PartTree partTree = partTree("firstnameAndLastname");
		assertPart(partTree, parts("firstname", "lastname"));
		assertThat(partTree.getSort(), is(nullValue()));
	}

	@Test
	public void parsesOrPropertiesCorrectly() throws Exception {
		PartTree partTree = partTree("firstnameOrLastname");
		assertPart(partTree, parts("firstname"), parts("lastname"));
		assertThat(partTree.getSort(), is(nullValue()));
	}

	@Test
	public void parsesCombinedAndAndOrPropertiesCorrectly() throws Exception {
		PartTree tree = partTree("firstnameAndLastnameOrLastname");
		assertPart(tree, parts("firstname", "lastname"), parts("lastname"));
	}

	@Test
	public void hasSortIfOrderByIsGiven() throws Exception {
		PartTree partTree = partTree("firstnameOrderByLastnameDesc");
		assertThat(partTree.getSort(), is(new Sort(Direction.DESC, "lastname")));
	}

	@Test
	public void hasSortIfOrderByIsGivenWithAllIgnoreCase() throws Exception {
		hasSortIfOrderByIsGivenWithAllIgnoreCase("firstnameOrderByLastnameDescAllIgnoreCase");
		hasSortIfOrderByIsGivenWithAllIgnoreCase("firstnameOrderByLastnameDescAllIgnoringCase");
		hasSortIfOrderByIsGivenWithAllIgnoreCase("firstnameAllIgnoreCaseOrderByLastnameDesc");
	}

	private void hasSortIfOrderByIsGivenWithAllIgnoreCase(String source) throws Exception {
		PartTree partTree = partTree(source);
		assertThat(partTree.getSort(), is(new Sort(Direction.DESC, "lastname")));
	}

	@Test
	public void detectsDistinctCorrectly() throws Exception {
		for (String prefix : PREFIXES) {
			detectsDistinctCorrectly(prefix + "DistinctByLastname", true);
			detectsDistinctCorrectly(prefix + "UsersDistinctByLastname", true);
			detectsDistinctCorrectly(prefix + "DistinctUsersByLastname", true);
			detectsDistinctCorrectly(prefix + "UsersByLastname", false);
			detectsDistinctCorrectly(prefix + "ByLastname", false);
			// Check it's non-greedy (would strip everything until Order*By*
			// otherwise)
			PartTree tree = detectsDistinctCorrectly(prefix + "ByLastnameOrderByFirstnameDesc", false);
			assertThat(tree.getSort(), is(new Sort(Direction.DESC, "firstname")));
		}
	}

	private PartTree detectsDistinctCorrectly(String source, boolean expected) {
		PartTree tree = partTree(source);
		assertThat("Unexpected distinct value for '" + source + "'", tree.isDistinct(), is(expected));
		return tree;
	}

	@Test
	public void parsesWithinCorrectly() {
		PartTree tree = partTree("findByLocationWithin");
		for (Part part : tree.getParts()) {
			assertThat(part.getType(), is(Type.WITHIN));
			assertThat(part.getProperty(), is(newProperty("location")));
		}
	}

	@Test
	public void parsesNearCorrectly() {
		assertType(asList("locationNear"), NEAR, "location");
	}

	@Test
	public void supportToStringWithoutSortOrder() throws Exception {
		assertType(asList("firstname"), SIMPLE_PROPERTY, "firstname");
	}

	@Test
	public void supportToStringWithSortOrder() throws Exception {
		PartTree tree = partTree("firstnameOrderByLastnameDesc");
		assertThat(tree.toString(), is(equalTo("firstname SIMPLE_PROPERTY (1): [Is, Equals] Order By lastname: DESC")));
	}

	@Test
	public void detectsIgnoreAllCase() throws Exception {
		detectsIgnoreAllCase("firstnameOrderByLastnameDescAllIgnoreCase", IgnoreCaseType.WHEN_POSSIBLE);
		detectsIgnoreAllCase("firstnameOrderByLastnameDescAllIgnoringCase", IgnoreCaseType.WHEN_POSSIBLE);
		detectsIgnoreAllCase("firstnameAllIgnoreCaseOrderByLastnameDesc", IgnoreCaseType.WHEN_POSSIBLE);
		detectsIgnoreAllCase("getByFirstnameAllIgnoreCase", IgnoreCaseType.WHEN_POSSIBLE);
		detectsIgnoreAllCase("getByFirstname", IgnoreCaseType.NEVER);
		detectsIgnoreAllCase("firstnameOrderByLastnameDesc", IgnoreCaseType.NEVER);
	}

	private void detectsIgnoreAllCase(String source, IgnoreCaseType expected) throws Exception {
		PartTree tree = partTree(source);
		for (Part part : tree.getParts()) {
			assertThat(part.shouldIgnoreCase(), is(expected));
		}
	}

	@Test
	public void detectsSpecificIgnoreCase() throws Exception {
		PartTree tree = partTree("findByFirstnameIgnoreCaseAndLastname");
		assertPart(tree, parts("firstname", "lastname"));
		Iterator<Part> parts = tree.getParts().iterator();
		assertThat(parts.next().shouldIgnoreCase(), is(IgnoreCaseType.ALWAYS));
		assertThat(parts.next().shouldIgnoreCase(), is(IgnoreCaseType.NEVER));
	}

	@Test
	public void detectsSpecificIgnoringCase() throws Exception {
		PartTree tree = partTree("findByFirstnameIgnoringCaseAndLastname");
		assertPart(tree, parts("firstname", "lastname"));
		Iterator<Part> parts = tree.getParts().iterator();
		assertThat(parts.next().shouldIgnoreCase(), is(IgnoreCaseType.ALWAYS));
		assertThat(parts.next().shouldIgnoreCase(), is(IgnoreCaseType.NEVER));
	}

	/**
	 * @see DATACMNS-78
	 */
	@Test
	public void parsesLessThanEqualCorrectly() {
		assertType(Arrays.asList("lastnameLessThanEqual", "lastnameIsLessThanEqual"), LESS_THAN_EQUAL, "lastname");
	}

	/**
	 * @see DATACMNS-78
	 */
	@Test
	public void parsesGreaterThanEqualCorrectly() {
		assertType(Arrays.asList("lastnameGreaterThanEqual", "lastnameIsGreaterThanEqual"), GREATER_THAN_EQUAL, "lastname");
	}

	@Test
	public void returnsAllParts() {

		PartTree tree = partTree("findByLastnameAndFirstname");
		assertPart(tree, parts("lastname", "firstname"));
	}

	@Test
	public void returnsAllPartsOfType() {

		PartTree tree = partTree("findByLastnameAndFirstnameGreaterThan");

		Collection<Part> parts = toCollection(tree.getParts(Type.SIMPLE_PROPERTY));
		assertThat(parts, hasItem(part("lastname")));
		assertThat(parts, is(hasSize(1)));

		parts = toCollection(tree.getParts(Type.GREATER_THAN));
		assertThat(parts, hasItem(new Part("FirstnameGreaterThan", User.class)));
		assertThat(parts, is(hasSize(1)));
	}

	/**
	 * @see DATACMNS-94
	 */
	@Test
	public void parsesExistsKeywordCorrectly() {
		assertType(asList("lastnameExists"), EXISTS, "lastname", 0, false);
	}

	/**
	 * @see DATACMNS-94
	 */
	@Test
	public void parsesRegexKeywordCorrectly() {
		assertType(asList("lastnameRegex", "lastnameMatchesRegex", "lastnameMatches"), REGEX, "lastname");
	}

	/**
	 * @see DATACMNS-107
	 */
	@Test
	public void parsesTrueKeywordCorrectly() {
		assertType(asList("activeTrue", "activeIsTrue"), TRUE, "active", 0, false);
	}

	/**
	 * @see DATACMNS-107
	 */
	@Test
	public void parsesFalseKeywordCorrectly() {
		assertType(asList("activeFalse", "activeIsFalse"), FALSE, "active", 0, false);
	}

	/**
	 * @see DATACMNS-111
	 */
	@Test
	public void parsesStartingWithKeywordCorrectly() {
		assertType(asList("firstnameStartsWith", "firstnameStartingWith", "firstnameIsStartingWith"), STARTING_WITH,
				"firstname");
	}

	/**
	 * @see DATACMNS-111
	 */
	@Test
	public void parsesEndingWithKeywordCorrectly() {
		assertType(asList("firstnameEndsWith", "firstnameEndingWith", "firstnameIsEndingWith"), ENDING_WITH, "firstname");
	}

	/**
	 * @see DATACMNS-111
	 */
	@Test
	public void parsesContainingKeywordCorrectly() {
		assertType(asList("firstnameIsContaining", "firstnameContains", "firstnameContaining"), CONTAINING, "firstname");
	}

	/**
	 * @see DATACMNS-141
	 */
	@Test
	public void parsesAfterKeywordCorrectly() {
		assertType(asList("birthdayAfter", "birthdayIsAfter"), Type.AFTER, "birthday");
	}

	/**
	 * @see DATACMNS-141
	 */
	@Test
	public void parsesBeforeKeywordCorrectly() {
		assertType(Arrays.asList("birthdayBefore", "birthdayIsBefore"), Type.BEFORE, "birthday");
	}

	/**
	 * @see DATACMNS-433
	 */
	@Test
	public void parsesLikeKeywordCorrectly() {
		assertType(asList("activeLike", "activeIsLike"), LIKE, "active");
	}

	/**
	 * @see DATACMNS-433
	 */
	@Test
	public void parsesNotLikeKeywordCorrectly() {
		assertType(asList("activeNotLike", "activeIsNotLike"), NOT_LIKE, "active");
	}

	/**
	 * @see DATACMNS-182
	 */
	@Test
	public void parsesContainingCorrectly() {

		PartTree tree = new PartTree("findAllByLegalNameContainingOrCommonNameContainingAllIgnoringCase",
				Organization.class);
		assertPart(tree, new Part[] { new Part("legalNameContaining", Organization.class) }, new Part[] { new Part(
				"commonNameContaining", Organization.class) });
	}

	/**
	 * @see DATACMNS-221
	 */
	@Test
	public void parsesSpecialCharactersCorrectly() {
		PartTree tree = new PartTree("findByØreAndÅrOrderByÅrAsc", DomainObjectWithSpecialChars.class);
		assertPart(tree, new Part[] { new Part("øre", DomainObjectWithSpecialChars.class),
				new Part("år", DomainObjectWithSpecialChars.class) });
		assertTrue(tree.getSort().getOrderFor("år").isAscending());
	}

	/**
	 * @see DATACMNS-363
	 */
	@Test
	public void parsesSpecialCharactersOnlyCorrectly_Korean() {

		PartTree tree = new PartTree("findBy이름And생일OrderBy생일Asc", DomainObjectWithSpecialChars.class);

		assertPart(tree, new Part[] { new Part("이름", DomainObjectWithSpecialChars.class),
				new Part("생일", DomainObjectWithSpecialChars.class) });
		assertTrue(tree.getSort().getOrderFor("생일").isAscending());
	}

	/**
	 * @see DATACMNS-363
	 */
	@Test
	public void parsesSpecialUnicodeCharactersMixedWithRegularCharactersCorrectly_Korean() {

		PartTree tree = new PartTree("findBy이름AndOrderIdOrderBy생일Asc", DomainObjectWithSpecialChars.class);

		assertPart(tree, new Part[] { new Part("이름", DomainObjectWithSpecialChars.class),
				new Part("order.id", DomainObjectWithSpecialChars.class) });
		assertTrue(tree.getSort().getOrderFor("생일").isAscending());
	}

	/**
	 * @see DATACMNS-363
	 */
	@Test
	public void parsesNestedSpecialUnicodeCharactersMixedWithRegularCharactersCorrectly_Korean() {

		PartTree tree = new PartTree( //
				"findBy" + "이름" //
						+ "And" + "OrderId" //
						+ "And" + "Nested_이름" // we use _ here to mark the beginning of a new property reference "이름"
						+ "Or" + "NestedOrderId" //
						+ "OrderBy" + "생일" + "Asc", DomainObjectWithSpecialChars.class);

		Iterator<OrPart> parts = tree.iterator();
		assertPartsIn(parts.next(), new Part[] { //
				new Part("이름", DomainObjectWithSpecialChars.class), //
						new Part("order.id", DomainObjectWithSpecialChars.class), //
						new Part("nested.이름", DomainObjectWithSpecialChars.class) //
				});
		assertPartsIn(parts.next(), new Part[] { //
				new Part("nested.order.id", DomainObjectWithSpecialChars.class) //
				});

		assertTrue(tree.getSort().getOrderFor("생일").isAscending());
	}

	/**
	 * @see DATACMNS-363
	 */
	@Test
	public void parsesNestedSpecialUnicodeCharactersMixedWithRegularCharactersCorrectly_KoreanNumbersSymbols() {

		PartTree tree = new PartTree( //
				"findBy" + "이름" //
						+ "And" + "OrderId" //
						+ "And" + "Anders" //
						+ "And" + "Property1" //
						+ "And" + "Øre" //
						+ "And" + "År" //
						+ "Or" + "NestedOrderId" //
						+ "And" + "Nested_property1" // we use _ here to mark the beginning of a new property reference "이름"
						+ "And" + "Property1" //
						+ "OrderBy" + "생일" + "Asc", DomainObjectWithSpecialChars.class);

		Iterator<OrPart> parts = tree.iterator();
		assertPartsIn(parts.next(), new Part[] { //
				new Part("이름", DomainObjectWithSpecialChars.class), //
						new Part("order.id", DomainObjectWithSpecialChars.class), //
						new Part("anders", DomainObjectWithSpecialChars.class), //
						new Part("property1", DomainObjectWithSpecialChars.class), //
						new Part("øre", DomainObjectWithSpecialChars.class), //
						new Part("år", DomainObjectWithSpecialChars.class) //
				});
		assertPartsIn(parts.next(), new Part[] { //
				new Part("nested.order.id", DomainObjectWithSpecialChars.class), //
						new Part("nested.property1", DomainObjectWithSpecialChars.class), //
						new Part("property1", DomainObjectWithSpecialChars.class) //
				});

		assertTrue(tree.getSort().getOrderFor("생일").isAscending());
	}

	/**
	 * @see DATACMNS-303
	 */
	@Test
	public void identifiesSimpleCountByCorrectly() {

		PartTree tree = new PartTree("countByLastname", User.class);
		assertThat(tree.isCountProjection(), is(true));
	}

	/**
	 * @see DATACMNS-399
	 */
	@Test
	public void queryPrefixShouldBeSupportedInRepositoryQueryMethods() {

		PartTree tree = new PartTree("queryByFirstnameAndLastname", User.class);
		Iterable<Part> parts = tree.getParts();

		assertThat(parts, hasItem(part("firstname")));
		assertThat(parts, hasItem(part("lastname")));
	}

	/**
	 * @see DATACMNS-303
	 */
	@Test
	public void identifiesExtendedCountByCorrectly() {

		PartTree tree = new PartTree("countUserByLastname", User.class);
		assertThat(tree.isCountProjection(), is(true));
	}

	/**
	 * @see DATACMNS-303
	 */
	@Test
	public void identifiesCountAndDistinctByCorrectly() {

		PartTree tree = new PartTree("countDistinctUserByLastname", User.class);
		assertThat(tree.isCountProjection(), is(true));
		assertThat(tree.isDistinct(), is(true));
	}

	/**
	 * @see DATAJPA-324
	 */
	@Test
	public void resolvesPropertyPathFromGettersOnInterfaces() {
		assertThat(new PartTree("findByCategoryId", Product.class), is(notNullValue()));
	}

	/**
	 * @see DATACMNS-368
	 */
	@Test
	public void detectPropertyWithOrKeywordPart() {
		assertThat(new PartTree("findByOrder", Product.class), is(notNullValue()));
	}

	/**
	 * @see DATACMNS-368
	 */
	@Test
	public void detectPropertyWithAndKeywordPart() {
		assertThat(new PartTree("findByAnders", Product.class), is(notNullValue()));
	}

	/**
	 * @see DATACMNS-368
	 */
	@Test
	public void detectPropertyPathWithOrKeywordPart() {
		assertThat(new PartTree("findByOrderId", Product.class), is(notNullValue()));
	}

	/**
	 * @see DATACMNS-387
	 */
	@Test
	public void buildsPartTreeFromEmptyPredicateCorrectly() {

		PartTree tree = new PartTree("findAllByOrderByLastnameAsc", User.class);

		assertThat(tree.getParts(), is(emptyIterable()));
		assertThat(tree.getSort(), is(new Sort(Direction.ASC, "lastname")));
	}

	/**
	 * @see DATACMNS-448
	 */
	@Test
	public void identifiesSimpleDeleteByCorrectly() {

		PartTree tree = new PartTree("deleteByLastname", User.class);
		assertThat(tree.isDelete(), is(true));
	}

	/**
	 * @see DATACMNS-448
	 */
	@Test
	public void identifiesExtendedDeleteByCorrectly() {

		PartTree tree = new PartTree("deleteUserByLastname", User.class);
		assertThat(tree.isDelete(), is(true));
	}

	/**
	 * @see DATACMNS-448
	 */
	@Test
	public void identifiesSimpleRemoveByCorrectly() {

		PartTree tree = new PartTree("removeByLastname", User.class);
		assertThat(tree.isDelete(), is(true));
	}

	/**
	 * @see DATACMNS-448
	 */
	@Test
	public void identifiesExtendedRemoveByCorrectly() {

		PartTree tree = new PartTree("removeUserByLastname", User.class);
		assertThat(tree.isDelete(), is(true));
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void disablesFindFirstKImplicitIfNotPresent() {
		assertLimiting("findByLastname", User.class, false, null);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindFirstImplicit() {
		assertLimiting("findFirstByLastname", User.class, true, 1);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindFirst1Explicit() {
		assertLimiting("findFirstByLastname", User.class, true, 1);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindFirstKExplicit() {
		assertLimiting("findFirst10ByLastname", User.class, true, 10);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindFirstKUsersExplicit() {
		assertLimiting("findFirst10UsersByLastname", User.class, true, 10);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindFirstKDistinctUsersExplicit() {
		assertLimiting("findFirst10DistinctUsersByLastname", User.class, true, 10, true);
		assertLimiting("findDistinctFirst10UsersByLastname", User.class, true, 10, true);
		assertLimiting("findFirst10UsersDistinctByLastname", User.class, true, 10, true);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindTopImplicit() {
		assertLimiting("findTopByLastname", User.class, true, 1);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindTop1Explicit() {
		assertLimiting("findTop1ByLastname", User.class, true, 1);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindTopKExplicit() {
		assertLimiting("findTop10ByLastname", User.class, true, 10);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindTopKUsersExplicit() {
		assertLimiting("findTop10UsersByLastname", User.class, true, 10);
	}

	/**
	 * @see DATACMNS-516
	 */
	@Test
	public void identifiesFindTopKDistinctUsersExplicit() {
		assertLimiting("findTop10DistinctUsersByLastname", User.class, true, 10, true);
		assertLimiting("findDistinctTop10UsersByLastname", User.class, true, 10, true);
		assertLimiting("findTop10UsersDistinctByLastname", User.class, true, 10, true);
	}

	@Test
	public void shouldNotSupportLimitingCountQueries() {
		assertLimiting("countFirst10DistinctUsersByLastname", User.class, false, null, true);
		assertLimiting("countTop10DistinctUsersByLastname", User.class, false, null, true);
	}

	/**
	 * @see DATACMNS-581
	 */
	@Test
	public void parsesIsNotContainingCorrectly() throws Exception {
		assertType(asList("firstnameIsNotContaining", "firstnameNotContaining", "firstnameNotContains"), NOT_CONTAINING,
				"firstname");
	}

	/**
	 * @see DATACMNS-581
	 */
	@Test
	public void buildsPartTreeForNotContainingCorrectly() throws Exception {

		PartTree tree = new PartTree("findAllByLegalNameNotContaining", Organization.class);
		assertPart(tree, new Part[] { new Part("legalNameNotContaining", Organization.class) });
	}

	/**
	 * @see DATACMNS-750
	 */
	@Test
	public void doesNotFailOnPropertiesContainingAKeyword() {

		PartTree partTree = new PartTree("findBySomeInfoIn", Category.class);

		Iterable<Part> parts = partTree.getParts();

		assertThat(parts, is(Matchers.<Part> iterableWithSize(1)));

		Part part = parts.iterator().next();

		assertThat(part.getType(), is(Type.IN));
		assertThat(part.getProperty(), is(PropertyPath.from("someInfo", Category.class)));
	}

	private static void assertLimiting(String methodName, Class<?> entityType, boolean limiting, Integer maxResults) {
		assertLimiting(methodName, entityType, limiting, maxResults, false);
	}

	private static void assertLimiting(String methodName, Class<?> entityType, boolean limiting, Integer maxResults,
			boolean distinct) {

		PartTree tree = new PartTree(methodName, entityType);

		assertThat(tree.isLimiting(), is(limiting));
		assertThat(tree.getMaxResults(), is(maxResults));
		assertThat(tree.isDistinct(), is(distinct));
	}

	private static void assertType(Iterable<String> sources, Type type, String property) {
		assertType(sources, type, property, 1, true);
	}

	private static void assertType(Iterable<String> sources, Type type, String property, int numberOfArguments,
			boolean parameterRequired) {

		for (String source : sources) {
			Part part = part(source);
			assertThat(part.getType(), is(type));
			assertThat(part.getProperty(), is(newProperty(property)));
			assertThat(part.getNumberOfArguments(), is(numberOfArguments));
			assertThat(part.getParameterRequired(), is(parameterRequired));
		}
	}

	private static PartTree partTree(String source) {
		return new PartTree(source, User.class);
	}

	private static Part part(String part) {
		return new Part(part, User.class);
	}

	private static Part[] parts(String... part) {
		Part[] parts = new Part[part.length];
		for (int i = 0; i < parts.length; i++) {
			parts[i] = part(part[i]);
		}
		return parts;
	}

	private static PropertyPath newProperty(String name) {
		return PropertyPath.from(name, User.class);
	}

	private void assertPart(PartTree tree, Part[]... parts) {

		Iterator<OrPart> orParts = tree.iterator();
		for (Part[] part : parts) {
			assertThat(orParts.hasNext(), is(true));
			assertPartsIn(orParts.next(), part);
		}
		assertThat("Too many or parts!", orParts.hasNext(), is(false));
	}

	private void assertPartsIn(OrPart orPart, Part[] part) {
		Iterator<Part> partIterator = orPart.iterator();
		for (int k = 0; k < part.length; k++) {
			assertThat(String.format("Expected %d parts but have %d", part.length, k), partIterator.hasNext(), is(true));
			Part next = partIterator.next();
			assertThat(String.format("Expected %s but got %s!", part[k], next), part[k], is(next));
		}
		assertThat("Too many parts!", partIterator.hasNext(), is(false));
	}

	private static <T> Collection<T> toCollection(Iterable<T> iterable) {

		List<T> result = new ArrayList<T>();
		for (T element : iterable) {
			result.add(element);
		}
		return result;
	}

	class User {
		String firstname;
		String lastname;
		double[] location;
		boolean active;
		Date birthday;
	}

	class Organization {

		String commonName;
		String legalName;
	}

	class DomainObjectWithSpecialChars {
		String øre;
		String år;

		String 생일; // Birthday
		String 이름; // Name

		int property1;

		Order order;

		Anders anders;

		DomainObjectWithSpecialChars nested;
	}

	interface Product {

		Order getOrder(); // contains Or keyword

		Anders getAnders(); // constains And keyword

		Category getCategory();
	}

	interface Category {

		Long getId();

		String getSomeInfo();
	}

	interface Order {

		Long getId();
	}

	interface Anders {

		Long getId();
	}
}
