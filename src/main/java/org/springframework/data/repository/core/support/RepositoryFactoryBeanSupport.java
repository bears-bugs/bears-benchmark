/*
 * Copyright 2008-2016 the original author or authors.
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
package org.springframework.data.repository.core.support;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.DefaultEvaluationContextProvider;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.util.Assert;

/**
 * Adapter for Springs {@link FactoryBean} interface to allow easy setup of repository factories via Spring
 * configuration.
 * 
 * @param <T> the type of the repository
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
public abstract class RepositoryFactoryBeanSupport<T extends Repository<S, ID>, S, ID extends Serializable>
		implements InitializingBean, RepositoryFactoryInformation<S, ID>, FactoryBean<T>, BeanClassLoaderAware,
		BeanFactoryAware, ApplicationEventPublisherAware {

	private final Class<? extends T> repositoryInterface;

	private RepositoryFactorySupport factory;
	private Key queryLookupStrategyKey;
	private Class<?> repositoryBaseClass;
	private Object customImplementation;
	private NamedQueries namedQueries;
	private MappingContext<?, ?> mappingContext;
	private ClassLoader classLoader;
	private BeanFactory beanFactory;
	private boolean lazyInit = false;
	private EvaluationContextProvider evaluationContextProvider = DefaultEvaluationContextProvider.INSTANCE;
	private ApplicationEventPublisher publisher;

	private T repository;

	private RepositoryMetadata repositoryMetadata;

	/**
	 * Creates a new {@link RepositoryFactoryBeanSupport} for the given repository interface.
	 * 
	 * @param repositoryInterface must not be {@literal null}.
	 */
	protected RepositoryFactoryBeanSupport(Class<? extends T> repositoryInterface) {

		Assert.notNull(repositoryInterface, "Repository interface must not be null!");
		this.repositoryInterface = repositoryInterface;
	}

	/**
	 * Configures the repository base class to be used.
	 * 
	 * @param repositoryBaseClass the repositoryBaseClass to set, can be {@literal null}.
	 * @since 1.11
	 */
	public void setRepositoryBaseClass(Class<?> repositoryBaseClass) {
		this.repositoryBaseClass = repositoryBaseClass;
	}

	/**
	 * Set the {@link QueryLookupStrategy.Key} to be used.
	 * 
	 * @param queryLookupStrategyKey
	 */
	public void setQueryLookupStrategyKey(Key queryLookupStrategyKey) {
		this.queryLookupStrategyKey = queryLookupStrategyKey;
	}

	/**
	 * Setter to inject a custom repository implementation.
	 * 
	 * @param customImplementation
	 */
	public void setCustomImplementation(Object customImplementation) {
		this.customImplementation = customImplementation;
	}

	/**
	 * Setter to inject a {@link NamedQueries} instance.
	 * 
	 * @param namedQueries the namedQueries to set
	 */
	public void setNamedQueries(NamedQueries namedQueries) {
		this.namedQueries = namedQueries;
	}

	/**
	 * Configures the {@link MappingContext} to be used to lookup {@link PersistentEntity} instances for
	 * {@link #getPersistentEntity()}.
	 * 
	 * @param mappingContext
	 */
	protected void setMappingContext(MappingContext<?, ?> mappingContext) {
		this.mappingContext = mappingContext;
	}

	/**
	 * Sets the {@link EvaluationContextProvider} to be used to evaluate SpEL expressions in manually defined queries.
	 * 
	 * @param evaluationContextProvider can be {@literal null}, defaults to
	 *          {@link DefaultEvaluationContextProvider#INSTANCE}.
	 */
	public void setEvaluationContextProvider(EvaluationContextProvider evaluationContextProvider) {
		this.evaluationContextProvider = evaluationContextProvider == null ? DefaultEvaluationContextProvider.INSTANCE
				: evaluationContextProvider;
	}

	/**
	 * Configures whether to initialize the repository proxy lazily. This defaults to {@literal false}.
	 * 
	 * @param lazyInit whether to initialize the repository proxy lazily. This defaults to {@literal false}.
	 */
	public void setLazyInit(boolean lazy) {
		this.lazyInit = lazy;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanClassLoaderAware#setBeanClassLoader(java.lang.ClassLoader)
	 */
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
		this.publisher = publisher;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getEntityInformation()
	 */
	@SuppressWarnings("unchecked")
	public EntityInformation<S, ID> getEntityInformation() {

		return (EntityInformation<S, ID>) factory.getEntityInformation(repositoryMetadata.getDomainType());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getRepositoryInformation()
	 */
	public RepositoryInformation getRepositoryInformation() {

		return this.factory.getRepositoryInformation(repositoryMetadata,
				customImplementation == null ? null : customImplementation.getClass());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getPersistentEntity()
	 */
	public PersistentEntity<?, ?> getPersistentEntity() {

		if (mappingContext == null) {
			return null;
		}

		return mappingContext.getPersistentEntity(repositoryMetadata.getDomainType());
	}

	/* (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryInformation#getQueryMethods()
	 */
	public List<QueryMethod> getQueryMethods() {
		return factory.getQueryMethods();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public T getObject() {
		return initAndReturn();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<? extends T> getObjectType() {
		return repositoryInterface;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() {

		this.factory = createRepositoryFactory();
		this.factory.setQueryLookupStrategyKey(queryLookupStrategyKey);
		this.factory.setNamedQueries(namedQueries);
		this.factory.setEvaluationContextProvider(evaluationContextProvider);
		this.factory.setRepositoryBaseClass(repositoryBaseClass);
		this.factory.setBeanClassLoader(classLoader);
		this.factory.setBeanFactory(beanFactory);

		if (publisher != null) {
			this.factory.addRepositoryProxyPostProcessor(new EventPublishingRepositoryProxyPostProcessor(publisher));
		}

		this.repositoryMetadata = this.factory.getRepositoryMetadata(repositoryInterface);

		if (!lazyInit) {
			initAndReturn();
		}
	}

	/**
	 * Returns the previously initialized repository proxy or creates and returns the proxy if previously uninitialized.
	 * 
	 * @return
	 */
	private T initAndReturn() {

		Assert.notNull(repositoryInterface, "Repository interface must not be null on initialization!");

		if (this.repository == null) {
			this.repository = this.factory.getRepository(repositoryInterface, customImplementation);
		}

		return this.repository;
	}

	/**
	 * Create the actual {@link RepositoryFactorySupport} instance.
	 * 
	 * @return
	 */
	protected abstract RepositoryFactorySupport createRepositoryFactory();
}
