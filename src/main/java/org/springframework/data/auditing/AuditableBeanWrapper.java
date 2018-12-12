/*
 * Copyright 2012-2015 the original author or authors.
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
package org.springframework.data.auditing;

import java.util.Calendar;

/**
 * Interface to abstract the ways setting the auditing information can be implemented.
 * 
 * @author Oliver Gierke
 * @since 1.5
 */
public interface AuditableBeanWrapper {

	/**
	 * Set the creator of the object.
	 * 
	 * @param value
	 */
	void setCreatedBy(Object value);

	/**
	 * Set the date the object was created.
	 * 
	 * @param value
	 */
	void setCreatedDate(Calendar value);

	/**
	 * Set the last modifier of the object.
	 * 
	 * @param value
	 */
	void setLastModifiedBy(Object value);

	/**
	 * Returns the date of the last modification date of the backing bean.
	 * 
	 * @return the date of the last modification, can be {@literal null}.
	 * @since 1.10
	 */
	Calendar getLastModifiedDate();

	/**
	 * Set the last modification date.
	 * 
	 * @param value
	 */
	void setLastModifiedDate(Calendar value);
}
