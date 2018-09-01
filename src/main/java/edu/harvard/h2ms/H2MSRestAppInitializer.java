package edu.harvard.h2ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import edu.harvard.h2ms.validator.EventValidator;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * H2MS Rest Application
 * @version 1.0
 */
@SpringBootApplication
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class H2MSRestAppInitializer {

	private static final Logger log = LoggerFactory.getLogger(H2MSRestAppInitializer.class);

	public static void main(String[] args) {
		SpringApplication.run(H2MSRestAppInitializer.class, args);
	}
}
