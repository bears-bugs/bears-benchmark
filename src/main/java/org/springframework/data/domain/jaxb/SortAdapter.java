/*
 * Copyright 2012-2014 the original author or authors.
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
package org.springframework.data.domain.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb.SortDto;

/**
 * {@link XmlAdapter} to convert {@link Sort} instances into {@link SortDto} instances and vice versa.
 * 
 * @author Oliver Gierke
 */
public class SortAdapter extends XmlAdapter<SortDto, Sort> {

	public static final SortAdapter INSTANCE = new SortAdapter();

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public SortDto marshal(Sort source) {

		if (source == null) {
			return null;
		}

		SortDto dto = new SortDto();
		dto.orders = SpringDataJaxb.marshal(source, OrderAdapter.INSTANCE);

		return dto;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Sort unmarshal(SortDto source) {
		return source == null ? null : new Sort(SpringDataJaxb.unmarshal(source.orders, OrderAdapter.INSTANCE));
	}
}
