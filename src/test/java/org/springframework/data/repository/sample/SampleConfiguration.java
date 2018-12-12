package org.springframework.data.repository.sample;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.DummyRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.support.Repositories;

@Configuration
public class SampleConfiguration {

	@Autowired ApplicationContext context;

	@Bean
	public Repositories repositories() {
		return new Repositories(context);
	}

	@Bean
	public RepositoryFactoryBeanSupport<Repository<User, Long>, User, Long> userRepositoryFactory() {

		return new DummyRepositoryFactoryBean<>(UserRepository.class);
	}

	@Bean
	public RepositoryFactoryBeanSupport<Repository<Product, Long>, Product, Long> productRepositoryFactory(
			ProductRepository productRepository) {

		DummyRepositoryFactoryBean<Repository<Product, Long>, Product, Long> factory = new DummyRepositoryFactoryBean<>(
				ProductRepository.class);
		factory.setCustomImplementation(productRepository);

		return factory;
	}

	@Bean
	public ProductRepository productRepository() {
		return mock(ProductRepository.class);
	}
}
