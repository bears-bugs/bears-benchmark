package com.societegenerale.cidroid.tasks.consumer.infrastructure;

import com.societegenerale.cidroid.tasks.consumer.services.ResourceFetcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

public class RestTemplateResourceFetcher implements ResourceFetcher {

    private final RestTemplate restTemplate;

    public RestTemplateResourceFetcher() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Optional<String> fetch(String remoteResource) {

        ResponseEntity<String> response = restTemplate.getForEntity(remoteResource, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return Optional.ofNullable(response.getBody());
        } else {
            return Optional.empty();
        }

    }
}
