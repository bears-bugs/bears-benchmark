/*
 * Copyright 2008-2010 the original author or authors.
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
package org.springframework.data.jpa.repository.config;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * Integration test for {@link JpaRepositoryConfigDefinitionParser}.
 * 
 * @author Oliver Gierke
 */
public class JpaRepositoryConfigDefinitionParserTests {

	@Test
	public void getsTransactionManagerSet() throws Exception {

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		reader.loadBeanDefinitions(new ClassPathResource("multiple-entity-manager-integration-context.xml"));

		BeanDefinition definition = factory.getBeanDefinition("auditableUserRepository");
		assertThat(definition, is(notNullValue()));

		PropertyValue transactionManager = definition.getPropertyValues().getPropertyValue("transactionManager");
		assertThat(transactionManager, is(notNullValue()));
		assertThat(transactionManager.getValue().toString(), is("transactionManager-2"));
	}
}
