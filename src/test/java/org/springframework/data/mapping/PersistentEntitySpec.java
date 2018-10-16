/*
 * Copyright (c) 2011 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mapping;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Some test methods that define expected behaviour for {@link PersistentEntity} interface. Implementation test classes
 * can simply extend that class to get the specs tested against an instance of their implementation.
 * 
 * @author Oliver Gierke
 */
public abstract class PersistentEntitySpec {

	public static void assertInvariants(PersistentEntity<?, ?> entity) {
		assertThat(entity.getName(), is(notNullValue()));
	}
}
