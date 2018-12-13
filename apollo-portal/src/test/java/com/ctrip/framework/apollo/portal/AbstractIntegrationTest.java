package com.ctrip.framework.apollo.portal;


import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PortalApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationTest {

  protected RestTemplate restTemplate = (new TestRestTemplate()).getRestTemplate();
 
  @PostConstruct
  private void postConstruct() {
    System.setProperty("spring.profiles.active", "test");
    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
  }

  @Value("${local.server.port}")
  int port;

}
