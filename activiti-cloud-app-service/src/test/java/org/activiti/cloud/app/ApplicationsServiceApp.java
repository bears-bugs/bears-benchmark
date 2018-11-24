package org.activiti.cloud.app;

import org.activiti.cloud.app.services.ApplicationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApplicationsServiceApp implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(ApplicationsServiceApp.class,
                              args);
    }

    @Override
    public void run(String... args) throws Exception {

    }


}