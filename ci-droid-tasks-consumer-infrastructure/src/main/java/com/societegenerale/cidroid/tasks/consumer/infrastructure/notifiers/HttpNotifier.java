package com.societegenerale.cidroid.tasks.consumer.infrastructure.notifiers;

import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.User;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j

public class HttpNotifier implements Notifier {

    private String targetUrl;

    private RestTemplate restTemplate ;

    public HttpNotifier(@Value("${notifiers.http.targetUrl}") String targetUrl) {
        this.targetUrl = targetUrl;
        restTemplate = new RestTemplate();
    }

    @Override
    public void notify(User user, Message message, Map<String,Object> additionalInfos) {

        val response = restTemplate.postForEntity(targetUrl, new NotificationBody(user.getEmail(),message.getContent()), String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("couldn't push notification for {} with content {}", user.getEmail(), message.getContent());
        }

    }


    @Override
    public String getNotificationMode() {
        return "HTTP";
    }

    @Data
    private class NotificationBody{

        private final String userEmail;

        private final String message;

        private final LocalDateTime timestamp=LocalDateTime.now();

    }
}
