package edu.harvard.h2ms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import edu.harvard.h2ms.domain.core.Answer;
import edu.harvard.h2ms.domain.core.EventTemplate;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.domain.core.User;

/**
 * REST-related configuration items.
 */
@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {

	/**
	 * Exposes the ID's for certain entities.  This goes against the HATEOAS concept
	 * built-in to Spring Data REST, but maybe it makes some things easier for the
	 * front-end folks.
	 * 
	 */
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Question.class);
		config.exposeIdsFor(Answer.class);
		config.exposeIdsFor(EventTemplate.class);
		config.exposeIdsFor(User.class);
	}

	/**
	 * Configuration to allow cross-origin requests to the API. Required for the
	 * frontend app to work in a browser. Based on the tutorial at
	 * http://chariotsolutions.com/blog/post/angular-2-spring-boot-jwt-cors_part1/.
	 */
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}
}