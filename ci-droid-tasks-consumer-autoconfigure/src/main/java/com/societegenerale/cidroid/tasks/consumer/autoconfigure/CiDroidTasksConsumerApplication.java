package com.societegenerale.cidroid.tasks.consumer.autoconfigure;

import com.societegenerale.cidroid.tasks.consumer.infrastructure.IncomingGitHubEvent;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.CiDroidBehavior;
import com.societegenerale.cidroid.tasks.consumer.infrastructure.config.InfraConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({CiDroidTasksConsumerAutoConfiguration.class})
@EnableAutoConfiguration
@SuppressWarnings("squid:S1118") //can't add a private constructor, otherwise app won't start
public class CiDroidTasksConsumerApplication {

    @SuppressWarnings("squid:S2095")
    public static void main(String[] args) {
        SpringApplication.run(CiDroidTasksConsumerApplication.class, args);
    }
}
