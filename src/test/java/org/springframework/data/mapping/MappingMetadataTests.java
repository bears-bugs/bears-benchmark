/*
 * Copyright 2011-2013 the original author or authors.
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

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mapping.context.SampleMappingContext;
import org.springframework.data.mapping.context.SamplePersistentProperty;

/**
 * Integration tests for Mapping metadata.
 * 
 * @author Jon Brisbin
 * @author Oliver Gierke
 */
public class MappingMetadataTests {

	SampleMappingContext ctx;

	@Before
	public void setup() {
		ctx = new SampleMappingContext();
	}

	@Test
	public void testPojoWithId() {

		ctx.setInitialEntitySet(Collections.singleton(PersonWithId.class));
		ctx.initialize();

		PersistentEntity<?, SamplePersistentProperty> person = ctx.getPersistentEntity(PersonWithId.class);
		assertNotNull(person.getIdProperty());
		assertEquals(String.class, person.getIdProperty().getType());
	}

	@Test
	public void testAssociations() {

		ctx.setInitialEntitySet(Collections.singleton(PersonWithChildren.class));
		ctx.initialize();

		PersistentEntity<?, SamplePersistentProperty> person = ctx.getPersistentEntity(PersonWithChildren.class);
		person.doWithAssociations(new AssociationHandler<SamplePersistentProperty>() {
			public void doWithAssociation(Association<SamplePersistentProperty> association) {
				assertEquals(Child.class, association.getInverse().getComponentType());
			}
		});
	}
}
