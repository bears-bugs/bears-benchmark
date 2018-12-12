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
package org.springframework.data.repository.config;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupportUnitTests.DummyRegistrar;
import org.springframework.data.repository.core.support.DummyRepositoryFactoryBean;

@Retention(RetentionPolicy.RUNTIME)
@Import(DummyRegistrar.class)
@Inherited
public @interface EnableRepositories {

	String[] value() default {};

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};

	Filter[] includeFilters() default {};

	Filter[] excludeFilters() default {};

	Class<?> repositoryFactoryBeanClass() default DummyRepositoryFactoryBean.class;

	Class<?> repositoryBaseClass() default PagingAndSortingRepository.class;

	String namedQueriesLocation() default "";

	String repositoryImplementationPostfix() default "Impl";

	boolean considerNestedRepositories() default false;
}
