/*
 * Copyright 2014-2018 the original author or authors.
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.AutowireCandidateResolver;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

/**
 * Delegate for configuration integration to reuse the general way of detecting repositories. Customization is done by
 * providing a configuration format specific {@link RepositoryConfigurationSource} (currently either XML or annotations
 * are supported). The actual registration can then be triggered for different {@link RepositoryConfigurationExtension}
 * s.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 */
@Slf4j
public class RepositoryConfigurationDelegate {

	private static final String REPOSITORY_REGISTRATION = "Spring Data {} - Registering repository: {} - Interface: {} - Factory: {}";
	private static final String MULTIPLE_MODULES = "Multiple Spring Data modules found, entering strict repository configuration mode!";
	private static final String NON_DEFAULT_AUTOWIRE_CANDIDATE_RESOLVER = "Non-default AutowireCandidateResolver ({}) detected. Skipping the registration of LazyRepositoryInjectionPointResolver. Lazy repository injection will not be working!";

	static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";

	private final RepositoryConfigurationSource configurationSource;
	private final ResourceLoader resourceLoader;
	private final Environment environment;
	private final boolean isXml;
	private final boolean inMultiStoreMode;

	/**
	 * Creates a new {@link RepositoryConfigurationDelegate} for the given {@link RepositoryConfigurationSource} and
	 * {@link ResourceLoader} and {@link Environment}.
	 *
	 * @param configurationSource must not be {@literal null}.
	 * @param resourceLoader must not be {@literal null}.
	 * @param environment must not be {@literal null}.
	 */
	public RepositoryConfigurationDelegate(RepositoryConfigurationSource configurationSource,
			ResourceLoader resourceLoader, Environment environment) {

		this.isXml = configurationSource instanceof XmlRepositoryConfigurationSource;
		boolean isAnnotation = configurationSource instanceof AnnotationRepositoryConfigurationSource;

		Assert.isTrue(isXml || isAnnotation,
				"Configuration source must either be an Xml- or an AnnotationBasedConfigurationSource!");
		Assert.notNull(resourceLoader, "ResourceLoader must not be null!");

		this.configurationSource = configurationSource;
		this.resourceLoader = resourceLoader;
		this.environment = defaultEnvironment(environment, resourceLoader);
		this.inMultiStoreMode = multipleStoresDetected();
	}

	/**
	 * Defaults the environment in case the given one is null. Used as fallback, in case the legacy constructor was
	 * invoked.
	 *
	 * @param environment can be {@literal null}.
	 * @param resourceLoader can be {@literal null}.
	 * @return
	 */
	private static Environment defaultEnvironment(@Nullable Environment environment,
			@Nullable ResourceLoader resourceLoader) {

		if (environment != null) {
			return environment;
		}

		return resourceLoader instanceof EnvironmentCapable ? ((EnvironmentCapable) resourceLoader).getEnvironment()
				: new StandardEnvironment();
	}

	/**
	 * Registers the found repositories in the given {@link BeanDefinitionRegistry}.
	 *
	 * @param registry
	 * @param extension
	 * @return {@link BeanComponentDefinition}s for all repository bean definitions found.
	 */
	public List<BeanComponentDefinition> registerRepositoriesIn(BeanDefinitionRegistry registry,
			RepositoryConfigurationExtension extension) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Bootstrapping Spring Data repositories in {} mode.", configurationSource.getBootstrapMode().name());
		}

		extension.registerBeansForRoot(registry, configurationSource);

		RepositoryBeanDefinitionBuilder builder = new RepositoryBeanDefinitionBuilder(registry, extension,
				configurationSource, resourceLoader, environment);
		List<BeanComponentDefinition> definitions = new ArrayList<>();

		StopWatch watch = new StopWatch();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Scanning for repositories in packages {}.",
					configurationSource.getBasePackages().stream().collect(Collectors.joining(", ")));
		}

		watch.start();

		Collection<RepositoryConfiguration<RepositoryConfigurationSource>> configurations = extension
				.getRepositoryConfigurations(configurationSource, resourceLoader, inMultiStoreMode);

		Map<String, RepositoryConfiguration<?>> configurationsByRepositoryName = new HashMap<>(configurations.size());

		for (RepositoryConfiguration<? extends RepositoryConfigurationSource> configuration : configurations) {

			configurationsByRepositoryName.put(configuration.getRepositoryInterface(), configuration);

			BeanDefinitionBuilder definitionBuilder = builder.build(configuration);

			extension.postProcess(definitionBuilder, configurationSource);

			if (isXml) {
				extension.postProcess(definitionBuilder, (XmlRepositoryConfigurationSource) configurationSource);
			} else {
				extension.postProcess(definitionBuilder, (AnnotationRepositoryConfigurationSource) configurationSource);
			}

			AbstractBeanDefinition beanDefinition = definitionBuilder.getBeanDefinition();
			String beanName = configurationSource.generateBeanName(beanDefinition);

			if (LOG.isTraceEnabled()) {
				LOG.trace(REPOSITORY_REGISTRATION, extension.getModuleName(), beanName, configuration.getRepositoryInterface(),
						configuration.getRepositoryFactoryBeanClassName());
			}

			beanDefinition.setAttribute(FACTORY_BEAN_OBJECT_TYPE, configuration.getRepositoryInterface());

			registry.registerBeanDefinition(beanName, beanDefinition);
			definitions.add(new BeanComponentDefinition(beanDefinition, beanName));
		}

		potentiallyLazifyRepositories(configurationsByRepositoryName, registry, configurationSource.getBootstrapMode());

		watch.stop();

		if (LOG.isInfoEnabled()) {
			LOG.info("Finished Spring Data repository scanning in {}ms. Found {} repository interfaces.", //
					watch.getLastTaskTimeMillis(), configurations.size());
		}

		return definitions;
	}

	/**
	 * Registers a {@link LazyRepositoryInjectionPointResolver} over the default
	 * {@link ContextAnnotationAutowireCandidateResolver} to make injection points of lazy repositories lazy, too. Will
	 * augment the {@link LazyRepositoryInjectionPointResolver}'s configuration if there already is one configured.
	 * 
	 * @param configurations must not be {@literal null}.
	 * @param registry must not be {@literal null}.
	 */
	private static void potentiallyLazifyRepositories(Map<String, RepositoryConfiguration<?>> configurations,
			BeanDefinitionRegistry registry, BootstrapMode mode) {

		if (!DefaultListableBeanFactory.class.isInstance(registry) || mode.equals(BootstrapMode.DEFAULT)) {
			return;
		}

		DefaultListableBeanFactory beanFactory = DefaultListableBeanFactory.class.cast(registry);

		AutowireCandidateResolver resolver = beanFactory.getAutowireCandidateResolver();

		if (!Arrays.asList(ContextAnnotationAutowireCandidateResolver.class, LazyRepositoryInjectionPointResolver.class)
				.contains(resolver.getClass())) {

			LOG.warn(NON_DEFAULT_AUTOWIRE_CANDIDATE_RESOLVER, resolver.getClass().getName());

			return;
		}

		AutowireCandidateResolver newResolver = LazyRepositoryInjectionPointResolver.class.isInstance(resolver) //
				? LazyRepositoryInjectionPointResolver.class.cast(resolver).withAdditionalConfigurations(configurations) //
				: new LazyRepositoryInjectionPointResolver(configurations);

		beanFactory.setAutowireCandidateResolver(newResolver);

		if (mode.equals(BootstrapMode.DEFERRED)) {

			LOG.debug("Registering deferred repository initialization listener.");

			beanFactory.registerSingleton(DeferredRepositoryInitializationListener.class.getName(),
					new DeferredRepositoryInitializationListener(beanFactory));
		}
	}

	/**
	 * Scans {@code repository.support} packages for implementations of {@link RepositoryFactorySupport}. Finding more
	 * than a single type is considered a multi-store configuration scenario which will trigger stricter repository
	 * scanning.
	 *
	 * @return
	 */
	private boolean multipleStoresDetected() {

		boolean multipleModulesFound = SpringFactoriesLoader
				.loadFactoryNames(RepositoryFactorySupport.class, resourceLoader.getClassLoader()).size() > 1;

		if (multipleModulesFound) {
			LOG.info(MULTIPLE_MODULES);
		}

		return multipleModulesFound;
	}

	/**
	 * Customer {@link ContextAnnotationAutowireCandidateResolver} that also considers all injection points for lazy
	 * repositories lazy.
	 *
	 * @author Oliver Gierke
	 * @since 2.1
	 */
	@Slf4j
	@RequiredArgsConstructor
	static class LazyRepositoryInjectionPointResolver extends ContextAnnotationAutowireCandidateResolver {

		private final Map<String, RepositoryConfiguration<?>> configurations;

		/**
		 * Returns a new {@link LazyRepositoryInjectionPointResolver} that will have its configurations augmented with the
		 * given ones.
		 * 
		 * @param configurations must not be {@literal null}.
		 * @return
		 */
		LazyRepositoryInjectionPointResolver withAdditionalConfigurations(
				Map<String, RepositoryConfiguration<?>> configurations) {

			Map<String, RepositoryConfiguration<?>> map = new HashMap<>(this.configurations);
			map.putAll(configurations);

			return new LazyRepositoryInjectionPointResolver(map);
		}

		/* 
		 * (non-Javadoc)
		 * @see org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver#isLazy(org.springframework.beans.factory.config.DependencyDescriptor)
		 */
		@Override
		protected boolean isLazy(DependencyDescriptor descriptor) {

			Class<?> type = descriptor.getDependencyType();

			RepositoryConfiguration<?> configuration = configurations.get(type.getName());

			if (configuration == null) {
				return super.isLazy(descriptor);
			}

			boolean lazyInit = configuration.isLazyInit();

			if (lazyInit) {
				LOG.debug("Creating lazy injection proxy for {}…", configuration.getRepositoryInterface());
			}

			return lazyInit;
		}
	}
}
