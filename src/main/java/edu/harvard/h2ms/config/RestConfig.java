package edu.harvard.h2ms.config;

import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/** REST-related configuration items. */
@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {

  /**
   * Exposes the ID's for all entities. This goes against the HATEOAS concept built-in to Spring
   * Data REST, it makes some things easier for the front-end folks.
   *
   * <p>Credit for this solution: https://jira.spring.io/browse/DATAREST-161
   */
  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    final ClassPathScanningCandidateComponentProvider provider =
        new ClassPathScanningCandidateComponentProvider(false);

    provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*")));
    final Set<BeanDefinition> beans = provider.findCandidateComponents("edu.harvard.h2ms.domain");

    for (BeanDefinition bean : beans) {
      Class<?> idExposedClasses = null;

      try {
        idExposedClasses = Class.forName(bean.getBeanClassName());
        config.exposeIdsFor(Class.forName(idExposedClasses.getName()));
      } catch (ClassNotFoundException e) {
        // Can't throw ClassNotFoundException due to the method signature. Need to cast it
        throw new RuntimeException("Failed to expose `id` field due to", e);
      }
    }
  }

  /**
   * Configuration to allow cross-origin requests to the API. Required for the frontend app to work
   * in a browser. Based on the tutorial at
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
