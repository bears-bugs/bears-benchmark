package edu.harvard.h2ms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * H2MS Rest Application
 * @version 1.0
 */
@SpringBootApplication
public class H2MSRestAppInitializer {

	private static final Logger log = LoggerFactory.getLogger(H2MSRestAppInitializer.class);
	
	
	public static void main(String[] args) {

		SpringApplication.run(H2MSRestAppInitializer.class, args);
	}
}
