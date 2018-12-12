/*
 * Copyright 2016-2017 the original author or authors.
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
package org.springframework.data.repository.support;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.LongSupplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Unit tests for {@link PageableExecutionUtils}.
 * 
 * @author Mark Paluch
 * @author Oliver Gierke
 */
@RunWith(MockitoJUnitRunner.class)
public class PageableExecutionUtilsUnitTests {

	@Mock LongSupplier totalSupplierMock;

	@Test // DATAMCNS-884
	public void firstPageRequestIsLessThanOneFullPageDoesNotRequireTotal() {

		Page<Integer> page = PageableExecutionUtils.getPage(Arrays.asList(1, 2, 3), PageRequest.of(0, 10),
				totalSupplierMock);

		assertThat(page).contains(1, 2, 3);
		assertThat(page.getTotalElements()).isEqualTo(3L);
		verifyZeroInteractions(totalSupplierMock);
	}

	@Test // DATAMCNS-884
	public void noPageableRequestDoesNotRequireTotal() {

		Page<Integer> page = PageableExecutionUtils.getPage(Arrays.asList(1, 2, 3), Pageable.unpaged(), totalSupplierMock);

		assertThat(page).contains(1, 2, 3);
		assertThat(page.getTotalElements()).isEqualTo(3L);

		verifyZeroInteractions(totalSupplierMock);
	}

	@Test // DATAMCNS-884
	public void subsequentPageRequestIsLessThanOneFullPageDoesNotRequireTotal() {

		Page<Integer> page = PageableExecutionUtils.getPage(Arrays.asList(1, 2, 3), PageRequest.of(5, 10),
				totalSupplierMock);

		assertThat(page).contains(1, 2, 3);
		assertThat(page.getTotalElements()).isEqualTo(53L);

		verifyZeroInteractions(totalSupplierMock);
	}

	@Test // DATAMCNS-884
	public void firstPageRequestHitsUpperBoundRequiresTotal() {

		doReturn(4L).when(totalSupplierMock).getAsLong();

		Page<Integer> page = PageableExecutionUtils.getPage(Arrays.asList(1, 2, 3), PageRequest.of(0, 3),
				totalSupplierMock);

		assertThat(page).contains(1, 2, 3);
		assertThat(page.getTotalElements()).isEqualTo(4L);

		verify(totalSupplierMock).getAsLong();
	}

	@Test // DATAMCNS-884
	public void subsequentPageRequestHitsUpperBoundRequiresTotal() {

		doReturn(7L).when(totalSupplierMock).getAsLong();

		Page<Integer> page = PageableExecutionUtils.getPage(Arrays.asList(1, 2, 3), PageRequest.of(1, 3),
				totalSupplierMock);

		assertThat(page).contains(1, 2, 3);
		assertThat(page.getTotalElements()).isEqualTo(7L);

		verify(totalSupplierMock).getAsLong();
	}

	@Test // DATAMCNS-884
	public void subsequentPageRequestWithoutResultRequiresRequireTotal() {

		doReturn(7L).when(totalSupplierMock).getAsLong();
		Page<Integer> page = PageableExecutionUtils.getPage(Collections.<Integer> emptyList(), PageRequest.of(5, 10),
				totalSupplierMock);

		assertThat(page.getTotalElements()).isEqualTo(7L);

		verify(totalSupplierMock).getAsLong();
	}
}
