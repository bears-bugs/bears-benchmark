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
package org.springframework.data.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Special {@link TypeDiscoverer} handling {@link GenericArrayType}s.
 * 
 * @author Oliver Gierke
 */
class GenericArrayTypeInformation<S> extends ParentTypeAwareTypeInformation<S> {

	private final GenericArrayType type;

	/**
	 * Creates a new {@link GenericArrayTypeInformation} for the given {@link GenericArrayTypeInformation} and
	 * {@link TypeDiscoverer}.
	 * 
	 * @param type must not be {@literal null}.
	 * @param parent must not be {@literal null}.
	 * @param typeVariableMap must not be {@literal null}.
	 */
	protected GenericArrayTypeInformation(GenericArrayType type, TypeDiscoverer<?> parent,
			Map<TypeVariable<?>, Type> typeVariableMap) {

		super(type, parent, typeVariableMap);
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.util.TypeDiscoverer#getType()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Class<S> getType() {
		return (Class<S>) Array.newInstance(resolveType(type.getGenericComponentType()), 0).getClass();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.util.TypeDiscoverer#doGetComponentType()
	 */
	@Override
	protected TypeInformation<?> doGetComponentType() {

		Type componentType = type.getGenericComponentType();
		return createInfo(componentType);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return type.toString();
	}
}
