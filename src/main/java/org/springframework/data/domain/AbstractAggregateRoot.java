/*
 * Copyright 2016 the original author or authors.
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
package org.springframework.data.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

/**
 * Convenience base class for aggregate roots that exposes a {@link #registerEvent(Object)} to capture domain events and
 * expose them via {@link #getDomainEvents()}. The implementation is using the general event publication mechanism
 * implied by {@link DomainEvents} and {@link AfterDomainEventPublication}. If in doubt or need to customize anything
 * here, rather build your own base class and use the annotations directly.
 * 
 * @author Oliver Gierke
 * @since 1.13
 */
public class AbstractAggregateRoot {

	/**
	 * All domain events currently captured by the aggregate.
	 */
	@Getter(onMethod = @__(@DomainEvents)) //
	private transient final List<Object> domainEvents = new ArrayList<Object>();

	/**
	 * Registers the given event object for publication on a call to a Spring Data repository's save method.
	 * 
	 * @param event must not be {@literal null}.
	 * @return
	 */
	protected <T> T registerEvent(T event) {

		Assert.notNull(event, "Domain event must not be null!");

		this.domainEvents.add(event);
		return event;
	}

	/**
	 * Clears all domain events currently held. Usually invoked by the infrastructure in place in Spring Data
	 * repositories.
	 */
	@AfterDomainEventPublication
	public void clearDomainEvents() {
		this.domainEvents.clear();
	}
}
