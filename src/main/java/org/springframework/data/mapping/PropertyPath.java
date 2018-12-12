/*
 * Copyright 2011-2014 the original author or authors.
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
package org.springframework.data.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * Abstraction of a {@link PropertyPath} of a domain class.
 * 
 * @author Oliver Gierke
 */
public class PropertyPath implements Iterable<PropertyPath> {

	private static final String DELIMITERS = "_\\.";
	private static final String ALL_UPPERCASE = "[A-Z0-9._$]+";
	private static final Pattern SPLITTER = Pattern.compile("(?:[%s]?([%s]*?[^%s]+))".replaceAll("%s", DELIMITERS));

	private final TypeInformation<?> owningType;
	private final String name;
	private final TypeInformation<?> type;
	private final boolean isCollection;

	private PropertyPath next;

	/**
	 * Creates a leaf {@link PropertyPath} (no nested ones) with the given name inside the given owning type.
	 * 
	 * @param name must not be {@literal null} or empty.
	 * @param owningType must not be {@literal null}.
	 */
	PropertyPath(String name, Class<?> owningType) {
		this(name, ClassTypeInformation.from(owningType), Collections.<PropertyPath> emptyList());
	}

	/**
	 * Creates a leaf {@link PropertyPath} (no nested ones with the given name and owning type.
	 * 
	 * @param name must not be {@literal null} or empty.
	 * @param owningType must not be {@literal null}.
	 * @param base the {@link PropertyPath} previously found.
	 */
	PropertyPath(String name, TypeInformation<?> owningType, List<PropertyPath> base) {

		Assert.hasText(name, "Name must not be null or empty!");
		Assert.notNull(owningType, "Owning type must not be null!");
		Assert.notNull(base, "Perviously found properties must not be null!");

		String propertyName = name.matches(ALL_UPPERCASE) ? name : StringUtils.uncapitalize(name);
		TypeInformation<?> propertyType = owningType.getProperty(propertyName);

		if (propertyType == null) {
			throw new PropertyReferenceException(propertyName, owningType, base);
		}

		this.owningType = owningType;
		this.isCollection = propertyType.isCollectionLike();
		this.type = propertyType.getActualType();
		this.name = propertyName;
	}

	/**
	 * Returns the owning type of the {@link PropertyPath}.
	 * 
	 * @return the owningType will never be {@literal null}.
	 */
	public TypeInformation<?> getOwningType() {
		return owningType;
	}

	/**
	 * Returns the name of the {@link PropertyPath}.
	 * 
	 * @return the name will never be {@literal null}.
	 */
	public String getSegment() {
		return name;
	}

	/**
	 * Returns the leaf property of the {@link PropertyPath}.
	 * 
	 * @return will never be {@literal null}.
	 */
	public PropertyPath getLeafProperty() {

		PropertyPath result = this;

		while (result.hasNext()) {
			result = result.next();
		}

		return result;
	}

	/**
	 * Returns the type of the property will return the plain resolved type for simple properties, the component type for
	 * any {@link Iterable} or the value type of a {@link java.util.Map} if the property is one.
	 * 
	 * @return
	 */
	public Class<?> getType() {
		return this.type.getType();
	}

	/**
	 * Returns the next nested {@link PropertyPath}.
	 * 
	 * @return the next nested {@link PropertyPath} or {@literal null} if no nested {@link PropertyPath} available.
	 * @see #hasNext()
	 */
	public PropertyPath next() {
		return next;
	}

	/**
	 * Returns whether there is a nested {@link PropertyPath}. If this returns {@literal true} you can expect
	 * {@link #next()} to return a non- {@literal null} value.
	 * 
	 * @return
	 */
	public boolean hasNext() {
		return next != null;
	}

	/**
	 * Returns the {@link PropertyPath} in dot notation.
	 * 
	 * @return
	 */
	public String toDotPath() {

		if (hasNext()) {
			return getSegment() + "." + next().toDotPath();
		}

		return getSegment();
	}

	/**
	 * Returns whether the {@link PropertyPath} is actually a collection.
	 * 
	 * @return
	 */
	public boolean isCollection() {
		return isCollection;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj == null || !getClass().equals(obj.getClass())) {
			return false;
		}

		PropertyPath that = (PropertyPath) obj;

		return this.name.equals(that.name) && this.type.equals(that.type)
				&& ObjectUtils.nullSafeEquals(this.next, that.next);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		int result = 17;

		result += 31 * name.hashCode();
		result += 31 * type.hashCode();
		result += 31 * (next == null ? 0 : next.hashCode());

		return result;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<PropertyPath> iterator() {
		return new Iterator<PropertyPath>() {

			private PropertyPath current = PropertyPath.this;

			public boolean hasNext() {
				return current != null;
			}

			public PropertyPath next() {
				PropertyPath result = current;
				this.current = current.next();
				return result;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * Extracts the {@link PropertyPath} chain from the given source {@link String} and type.
	 * 
	 * @param source
	 * @param type
	 * @return
	 */
	public static PropertyPath from(String source, Class<?> type) {
		return from(source, ClassTypeInformation.from(type));
	}

	/**
	 * Extracts the {@link PropertyPath} chain from the given source {@link String} and {@link TypeInformation}.
	 * 
	 * @param source must not be {@literal null}.
	 * @param type
	 * @return
	 */
	public static PropertyPath from(String source, TypeInformation<?> type) {

		Assert.hasText(source, "Source must not be null or empty!");
		Assert.notNull(type, "TypeInformation must not be null or empty!");

		List<String> iteratorSource = new ArrayList<String>();
		Matcher matcher = SPLITTER.matcher("_" + source);

		while (matcher.find()) {
			iteratorSource.add(matcher.group(1));
		}

		Iterator<String> parts = iteratorSource.iterator();

		PropertyPath result = null;
		Stack<PropertyPath> current = new Stack<PropertyPath>();

		while (parts.hasNext()) {
			if (result == null) {
				result = create(parts.next(), type, current);
				current.push(result);
			} else {
				current.push(create(parts.next(), current));
			}
		}

		return result;
	}

	/**
	 * Creates a new {@link PropertyPath} as subordinary of the given {@link PropertyPath}.
	 * 
	 * @param source
	 * @param base
	 * @return
	 */
	private static PropertyPath create(String source, Stack<PropertyPath> base) {

		PropertyPath previous = base.peek();

		PropertyPath propertyPath = create(source, previous.type, base);
		previous.next = propertyPath;
		return propertyPath;
	}

	/**
	 * Factory method to create a new {@link PropertyPath} for the given {@link String} and owning type. It will inspect
	 * the given source for camel-case parts and traverse the {@link String} along its parts starting with the entire one
	 * and chewing off parts from the right side then. Whenever a valid property for the given class is found, the tail
	 * will be traversed for subordinary properties of the just found one and so on.
	 * 
	 * @param source
	 * @param type
	 * @return
	 */
	private static PropertyPath create(String source, TypeInformation<?> type, List<PropertyPath> base) {
		return create(source, type, "", base);
	}

	/**
	 * Tries to look up a chain of {@link PropertyPath}s by trying the givne source first. If that fails it will split the
	 * source apart at camel case borders (starting from the right side) and try to look up a {@link PropertyPath} from
	 * the calculated head and recombined new tail and additional tail.
	 * 
	 * @param source
	 * @param type
	 * @param addTail
	 * @return
	 */
	private static PropertyPath create(String source, TypeInformation<?> type, String addTail, List<PropertyPath> base) {

		PropertyReferenceException exception = null;
		PropertyPath current = null;

		try {

			current = new PropertyPath(source, type, base);

			if (!base.isEmpty()) {
				base.get(base.size() - 1).next = current;
			}

			List<PropertyPath> newBase = new ArrayList<PropertyPath>(base);
			newBase.add(current);

			if (StringUtils.hasText(addTail)) {
				current.next = create(addTail, current.type, newBase);
			}

			return current;

		} catch (PropertyReferenceException e) {

			if (current != null) {
				throw e;
			}

			exception = e;
		}

		Pattern pattern = Pattern.compile("\\p{Lu}+\\p{Ll}*$");
		Matcher matcher = pattern.matcher(source);

		if (matcher.find() && matcher.start() != 0) {

			int position = matcher.start();
			String head = source.substring(0, position);
			String tail = source.substring(position);

			try {
				return create(head, type, tail + addTail, base);
			} catch (PropertyReferenceException e) {
				throw e.hasDeeperResolutionDepthThan(exception) ? e : exception;
			}
		}

		throw exception;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("%s.%s", owningType.getType().getSimpleName(), toDotPath());
	}
}
