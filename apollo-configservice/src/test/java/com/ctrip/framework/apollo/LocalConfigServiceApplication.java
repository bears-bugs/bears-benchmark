package com.ctrip.framework.apollo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LocalConfigServiceApplication {
  public static void main(String[] args) {
    new SpringApplicationBuilder(LocalConfigServiceApplication.class).run(args);
  }
}
