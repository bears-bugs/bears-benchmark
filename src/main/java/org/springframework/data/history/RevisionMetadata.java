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
package org.springframework.data.history;

import org.joda.time.DateTime;

/**
 * Metadata about a revision.
 * 
 * @author Philipp Huegelmeyer
 * @author Oliver Gierke
 */
public interface RevisionMetadata<N extends Number & Comparable<N>> {

	/**
	 * Returns the revision number of the revision.
	 * 
	 * @return
	 */
	N getRevisionNumber();

	/**
	 * Returns the date of the revision.
	 * 
	 * @return
	 */
	DateTime getRevisionDate();

	/**
	 * Returns the underlying revision metadata which might provider more detailed implementation specific information.
	 * 
	 * @return
	 */
	<T> T getDelegate();
}
