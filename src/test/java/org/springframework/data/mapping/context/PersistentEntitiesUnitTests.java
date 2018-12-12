/*
 * Copyright 2014-2017 the original author or authors.
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
package org.springframework.data.mapping.context;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.util.ClassTypeInformation;

/**
 * Unit tests for {@link PersistentEntities}.
 * 
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class PersistentEntitiesUnitTests {

	@Mock SampleMappingContext first;
	@Mock SampleMappingContext second;

	@Test(expected = IllegalArgumentException.class) // DATACMNS-458
	public void rejectsNullMappingContexts() {
		new PersistentEntities(null);
	}

	@Test // DATACMNS-458
	public void returnsPersistentEntitiesFromMappingContexts() {

		when(first.hasPersistentEntityFor(Sample.class)).thenReturn(false);
		when(second.hasPersistentEntityFor(Sample.class)).thenReturn(true);

		new PersistentEntities(Arrays.asList(first, second)).getPersistentEntity(Sample.class);

		verify(first, times(1)).hasPersistentEntityFor(Sample.class);
		verify(first, times(0)).getRequiredPersistentEntity(Sample.class);

		verify(second, times(1)).hasPersistentEntityFor(Sample.class);
		verify(second, times(1)).getRequiredPersistentEntity(Sample.class);
	}

	@Test // DATACMNS-458
	public void indicatesManagedType() {

		SampleMappingContext context = new SampleMappingContext();
		context.setInitialEntitySet(Collections.singleton(Sample.class));
		context.initialize();

		PersistentEntities entities = new PersistentEntities(Collections.singletonList(context));

		assertThat(entities.getPersistentEntity(Sample.class)).isPresent();
		assertThat(entities.getPersistentEntity(Object.class)).isNotPresent();
		assertThat(entities.getManagedTypes()).contains(ClassTypeInformation.from(Sample.class));

		assertThat(entities.getPersistentEntity(Sample.class)).hasValueSatisfying(it -> assertThat(entities).contains(it));

	}

	static class Sample {

	}
}
