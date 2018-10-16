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

/**
 * Factory interface to create {@link IsNewStrategy} instances for a given class.
 * 
 * @author Oliver Gierke
 * @since 1.5
 */
public interface IsNewStrategyFactory {

	/**
	 * Returns the {@link IsNewStrategy} to be used for the given type.
	 * 
	 * @param type must not be {@literal null}.
	 * @return the {@link IsNewStrategy} to be used for the given type, will never be {@literal null}.
	 * @throws IllegalArgumentException in case no {@link IsNewStrategy} could be determined.
	 */
	IsNewStrategy getIsNewStrategy(Class<?> type);
}
