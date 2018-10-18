/*
 * Copyright 2017 the original author or authors.
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

import java.util.Date;
import java.util.function.Function;

import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.ParameterExpression;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * The interface encapsulates the setting of query parameters which might use a significant number of variations of
 * {@literal Query.setParameter}.
 *
 * @author Jens Schauder
 * @author Mark Paluch
 * @since 2.0
 */
interface QueryParameterSetter {

	void setParameter(Query query, Object[] values);

	/** Noop implementation */
	QueryParameterSetter NOOP = (query, values) -> {};

	/**
	 * {@link QueryParameterSetter} for named or indexed parameters that might have a {@link TemporalType} specified.
	 */
	class NamedOrIndexedQueryParameterSetter implements QueryParameterSetter {

		private final Function<Object[], Object> valueExtractor;
		private final Parameter<?> parameter;
		private final @Nullable TemporalType temporalType;

		/**
		 * @param valueExtractor must not be {@literal null}.
		 * @param parameter must not be {@literal null}.
		 * @param temporalType may be {@literal null}.
		 */
		NamedOrIndexedQueryParameterSetter(Function<Object[], Object> valueExtractor, Parameter<?> parameter,
				@Nullable TemporalType temporalType) {

			Assert.notNull(valueExtractor, "ValueExtractor must not be null!");

			this.valueExtractor = valueExtractor;
			this.parameter = parameter;
			this.temporalType = temporalType;
		}

		/*
		 * (non-Javadoc)
		 * @see org.springframework.data.jpa.repository.query.QueryParameterSetter#setParameter(javax.persistence.Query, java.lang.Object[])
		 */
		@SuppressWarnings("unchecked")
		public void setParameter(Query query, Object[] values) {

			Object value = valueExtractor.apply(values);

			if (temporalType != null) {

				// One would think we can simply use parameter to identify the parameter we want to set.
				// But that does not work with list valued parameters. At least Hibernate tries to bind them by name.
				// TODO: move to using setParameter(Parameter, value) when https://hibernate.atlassian.net/browse/HHH-11870 is
				// fixed.

				if (parameter instanceof ParameterExpression) {
					query.setParameter((Parameter<Date>) parameter, (Date) value, temporalType);
				} else if (parameter.getName() != null && QueryUtils.hasNamedParameter(query)) {
					query.setParameter(parameter.getName(), (Date) value, temporalType);
				} else {
					if (query.getParameters().size() >= parameter.getPosition() || registerExcessParameters(query)) {
						query.setParameter(parameter.getPosition(), (Date) value, temporalType);
					}
				}

			} else {

				if (parameter instanceof ParameterExpression) {
					query.setParameter((Parameter<Object>) parameter, value);
				} else if (parameter.getName() != null && QueryUtils.hasNamedParameter(query)) {
					query.setParameter(parameter.getName(), value);
				} else {
					if (query.getParameters().size() >= parameter.getPosition() || registerExcessParameters(query)) {
						query.setParameter(parameter.getPosition(), value);
					}
				}
			}
		}

		private boolean registerExcessParameters(Query query) {

			// DATAJPA-1172
			// Since EclipseLink doesn't reliably report whether a query has parameters
			// we simply try to set the parameters and ignore possible failures.
			// this is relevant for native queries with SpEL expressions, where the method parameters don't have to match the
			// parameters in the query.
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=521915

			return query.getParameters().size() == 0 && query.getClass().getName().startsWith("org.eclipse");
		}
	}
}
