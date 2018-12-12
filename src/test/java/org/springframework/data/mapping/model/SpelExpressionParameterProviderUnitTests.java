/*
 * Copyright 2012 the original author or authors.
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
package org.springframework.data.mapping.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.mapping.PreferredConstructor.Parameter;
import org.springframework.data.mapping.model.AbstractPersistentPropertyUnitTests.SamplePersistentProperty;

/**
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class SpelExpressionParameterProviderUnitTests {

	@Mock
	SpELExpressionEvaluator evaluator;
	@Mock
	ParameterValueProvider<SamplePersistentProperty> delegate;
	@Mock
	ConversionService conversionService;

	SpELExpressionParameterValueProvider<SamplePersistentProperty> provider;

	Parameter<Object, SamplePersistentProperty> parameter;

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		provider = new SpELExpressionParameterValueProvider<SamplePersistentProperty>(evaluator, conversionService,
				delegate);

		parameter = mock(Parameter.class);
		when(parameter.hasSpelExpression()).thenReturn(true);
		when(parameter.getRawType()).thenReturn(Object.class);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void delegatesIfParameterDoesNotHaveASpELExpression() {

		Parameter<Object, SamplePersistentProperty> parameter = mock(Parameter.class);
		when(parameter.hasSpelExpression()).thenReturn(false);

		provider.getParameterValue(parameter);
		verify(delegate, times(1)).getParameterValue(parameter);
		verify(evaluator, times(0)).evaluate("expression");
	}

	@Test
	public void evaluatesSpELExpression() {

		when(parameter.getSpelExpression()).thenReturn("expression");

		provider.getParameterValue(parameter);
		verify(delegate, times(0)).getParameterValue(parameter);
		verify(evaluator, times(1)).evaluate("expression");
	}

	@Test
	public void handsSpELValueToConversionService() {

		when(evaluator.evaluate(Mockito.any(String.class))).thenReturn("value");

		provider.getParameterValue(parameter);
		verify(delegate, times(0)).getParameterValue(parameter);
		verify(conversionService, times(1)).convert("value", Object.class);
	}

	@Test
	public void doesNotConvertNullValue() {

		when(evaluator.evaluate(Mockito.any(String.class))).thenReturn(null);

		provider.getParameterValue(parameter);
		verify(delegate, times(0)).getParameterValue(parameter);
		verify(conversionService, times(0)).convert("value", Object.class);
	}

	@Test
	public void returnsMassagedObjectOnOverride() {

		provider = new SpELExpressionParameterValueProvider<SamplePersistentProperty>(evaluator, conversionService,
				delegate) {
			@Override
			@SuppressWarnings("unchecked")
			protected <T> T potentiallyConvertSpelValue(Object object, Parameter<T, SamplePersistentProperty> parameter) {
				return (T) "FOO";
			}
		};

		when(evaluator.evaluate(Mockito.anyString())).thenReturn("value");

		Object result = provider.getParameterValue(parameter);
		assertThat(result, is((Object) "FOO"));
		verify(delegate, times(0)).getParameterValue(parameter);
	}
}
