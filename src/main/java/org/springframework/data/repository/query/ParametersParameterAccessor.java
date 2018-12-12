/*
 * Copyright 2008-2015 the original author or authors.
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
package org.springframework.data.repository.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.util.QueryExecutionConverters;
import org.springframework.util.Assert;

/**
 * {@link ParameterAccessor} implementation using a {@link Parameters} instance to find special parameters.
 * 
 * @author Oliver Gierke
 */
public class ParametersParameterAccessor implements ParameterAccessor {

	private final Parameters<?, ?> parameters;
	private final List<Object> values;

	/**
	 * Creates a new {@link ParametersParameterAccessor}.
	 * 
	 * @param parameters must not be {@literal null}.
	 * @param values must not be {@literal null}.
	 */
	public ParametersParameterAccessor(Parameters<?, ?> parameters, Object[] values) {

		Assert.notNull(parameters, "Parameters must not be null!");
		Assert.notNull(values, "Values must not be null!");

		Assert.isTrue(parameters.getNumberOfParameters() == values.length, "Invalid number of parameters given!");

		this.parameters = parameters;

		List<Object> unwrapped = new ArrayList<Object>(values.length);

		for (Object element : values.clone()) {
			unwrapped.add(QueryExecutionConverters.unwrap(element));
		}

		this.values = unwrapped;
	}

	/**
	 * Returns the {@link Parameters} instance backing the accessor.
	 * 
	 * @return the parameters will never be {@literal null}.
	 */
	public Parameters<?, ?> getParameters() {
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.ParameterAccessor#getPageable()
	 */
	public Pageable getPageable() {

		if (!parameters.hasPageableParameter()) {
			return null;
		}

		return (Pageable) values.get(parameters.getPageableIndex());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.ParameterAccessor#getSort()
	 */
	public Sort getSort() {

		if (parameters.hasSortParameter()) {
			return (Sort) values.get(parameters.getSortIndex());
		}

		if (parameters.hasPageableParameter() && getPageable() != null) {
			return getPageable().getSort();
		}

		return null;
	}

	/**
	 * Returns the dynamic projection type if available, {@literal null} otherwise.
	 * 
	 * @return
	 */
	public Class<?> getDynamicProjection() {
		return parameters.hasDynamicProjection() ? (Class<?>) values.get(parameters.getDynamicProjectionIndex()) : null;
	}

	/**
	 * Returns the value with the given index.
	 * 
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getValue(int index) {
		return (T) values.get(index);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.ParameterAccessor#getBindableValue(int)
	 */
	public Object getBindableValue(int index) {
		return values.get(parameters.getBindableParameter(index).getIndex());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.ParameterAccessor#hasBindableNullValue()
	 */
	public boolean hasBindableNullValue() {

		for (Parameter parameter : parameters.getBindableParameters()) {
			if (values.get(parameter.getIndex()) == null) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.query.ParameterAccessor#iterator()
	 */
	public BindableParameterIterator iterator() {
		return new BindableParameterIterator(this);
	}

	/**
	 * Iterator class to allow traversing all bindable parameters inside the accessor.
	 * 
	 * @author Oliver Gierke
	 */
	private static class BindableParameterIterator implements Iterator<Object> {

		private final int bindableParameterCount;
		private final ParameterAccessor accessor;

		private int currentIndex = 0;

		/**
		 * Creates a new {@link BindableParameterIterator}.
		 * 
		 * @param accessor must not be {@literal null}.
		 */
		public BindableParameterIterator(ParametersParameterAccessor accessor) {

			Assert.notNull(accessor, "ParametersParameterAccessor must not be null!");

			this.accessor = accessor;
			this.bindableParameterCount = accessor.getParameters().getBindableParameters().getNumberOfParameters();
		}

		/**
		 * Returns the next bindable parameter.
		 * 
		 * @return
		 */
		public Object next() {
			return accessor.getBindableValue(currentIndex++);
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		public boolean hasNext() {
			return bindableParameterCount > currentIndex;
		}

		/*
		 * (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
