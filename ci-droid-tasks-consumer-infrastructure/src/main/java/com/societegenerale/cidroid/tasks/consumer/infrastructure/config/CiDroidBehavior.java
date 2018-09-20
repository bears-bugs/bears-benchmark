package com.societegenerale.cidroid.tasks.consumer.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "ciDroidBehavior")
public class CiDroidBehavior {

    private Map<String, String> patternToResourceMapping;


}