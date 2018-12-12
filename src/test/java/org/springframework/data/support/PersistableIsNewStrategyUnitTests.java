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
package org.springframework.data.support;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.data.domain.Persistable;

/**
 * Unit tests for {@link PersistableIsNewStrategy}.
 * 
 * @author Oliver Gierke
 */
public class PersistableIsNewStrategyUnitTests {

	IsNewStrategy strategy = PersistableIsNewStrategy.INSTANCE;

	@Test
	public void invokesPersistableIsNewForTest() {

		PersistableEntity entity = new PersistableEntity();
		assertThat(strategy.isNew(entity), is(true));

		entity.isNew = false;
		assertThat(strategy.isNew(entity), is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNonPersistableEntity() {
		strategy.isNew(new Object());
	}

	@SuppressWarnings("serial")
	static class PersistableEntity implements Persistable<Long> {

		boolean isNew = true;

		public Long getId() {
			return null;
		}

		public boolean isNew() {
			return isNew;
		}
	}
}
