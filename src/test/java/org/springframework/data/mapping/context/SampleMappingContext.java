package org.springframework.data.mapping.context;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;

import org.springframework.data.mapping.model.BasicPersistentEntity;
import org.springframework.data.mapping.model.SimpleTypeHolder;
import org.springframework.data.util.TypeInformation;

public class SampleMappingContext extends
		AbstractMappingContext<BasicPersistentEntity<Object, SamplePersistentProperty>, SamplePersistentProperty> {

	@Override
	@SuppressWarnings("unchecked")
	protected <S> BasicPersistentEntity<Object, SamplePersistentProperty> createPersistentEntity(
			TypeInformation<S> typeInformation) {
		return new BasicPersistentEntity<Object, SamplePersistentProperty>((TypeInformation<Object>) typeInformation);
	}

	@Override
	protected SamplePersistentProperty createPersistentProperty(final Field field, final PropertyDescriptor descriptor,
			final BasicPersistentEntity<Object, SamplePersistentProperty> owner, final SimpleTypeHolder simpleTypeHolder) {

		return new SamplePersistentProperty(field, descriptor, owner, simpleTypeHolder);
	}
}
