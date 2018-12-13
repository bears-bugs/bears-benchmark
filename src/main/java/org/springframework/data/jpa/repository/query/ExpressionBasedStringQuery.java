/*
 * Copyright 2013-2014 the original author or authors.
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

import java.util.regex.Pattern;

import org.springframework.data.repository.core.EntityMetadata;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

/**
 * Extension of {@link StringQuery} that evaluates the given query string as a SpEL template-expression.
 * <p>
 * Currently the following template variables are available:
 * <ol>
 * <li>{@code #entityName} - the simple class name of the given entity</li>
 * <ol>
 * 
 * @author Thomas Darimont
 * @author Oliver Gierke
 */
class ExpressionBasedStringQuery extends StringQuery {

	private static final String EXPRESSION_PARAMETER = "?#{";
	private static final String QUOTED_EXPRESSION_PARAMETER = "?__HASH__{";

	private static final Pattern EXPRESSION_PARAMETER_QUOTING = Pattern.compile(Pattern.quote(EXPRESSION_PARAMETER));
	private static final Pattern EXPRESSION_PARAMETER_UNQUOTING = Pattern.compile(Pattern
			.quote(QUOTED_EXPRESSION_PARAMETER));

	private static final String ENTITY_NAME = "entityName";
	private static final String ENTITY_NAME_VARIABLE = "#" + ENTITY_NAME;
	private static final String ENTITY_NAME_VARIABLE_EXPRESSION = "#{" + ENTITY_NAME_VARIABLE + "}";

	/**
	 * Creates a new {@link ExpressionBasedStringQuery} for the given query and {@link EntityMetadata}.
	 * 
	 * @param query must not be {@literal null} or empty.
	 * @param metadata must not be {@literal null}.
	 * @param parser must not be {@literal null}.
	 */
	public ExpressionBasedStringQuery(String query, JpaEntityMetadata<?> metadata, SpelExpressionParser parser) {
		super(renderQueryIfExpressionOrReturnQuery(query, metadata, parser));
	}

	/**
	 * @param query, the query expression potentially containing a SpEL expression. Must not be {@literal null}.}
	 * @param metadata the {@link JpaEntityMetadata} for the given entity. Must not be {@literal null}.
	 * @param parser Must not be {@literal null}.
	 * @return
	 */
	private static String renderQueryIfExpressionOrReturnQuery(String query, JpaEntityMetadata<?> metadata,
			SpelExpressionParser parser) {

		Assert.notNull(query, "query must not be null!");
		Assert.notNull(metadata, "metadata must not be null!");
		Assert.notNull(parser, "parser must not be null!");

		if (!containsExpression(query)) {
			return query;
		}

		StandardEvaluationContext evalContext = new StandardEvaluationContext();
		evalContext.setVariable(ENTITY_NAME, metadata.getEntityName());

		query = potentiallyQuoteExpressionsParameter(query);

		Expression expr = parser.parseExpression(query, ParserContext.TEMPLATE_EXPRESSION);

		String result = expr.getValue(evalContext, String.class);

		if (result == null) {
			return query;
		}

		return potentiallyUnquoteParameterExpressions(result);
	}

	private static String potentiallyUnquoteParameterExpressions(String result) {
		return EXPRESSION_PARAMETER_UNQUOTING.matcher(result).replaceAll(EXPRESSION_PARAMETER);
	}

	private static String potentiallyQuoteExpressionsParameter(String query) {
		return EXPRESSION_PARAMETER_QUOTING.matcher(query).replaceAll(QUOTED_EXPRESSION_PARAMETER);
	}

	private static boolean containsExpression(String query) {
		return query.contains(ENTITY_NAME_VARIABLE_EXPRESSION);
	}
}
