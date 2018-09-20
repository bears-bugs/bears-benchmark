package com.societegenerale.cidroid.tasks.consumer.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalProperties {

    private static String gitHubApiUrl = null;

    public static String getGitHubApiUrl() {
        return gitHubApiUrl;
    }

    @Value("${gitHub.api.url}")
    public void setGithubInstanceUrl(String gitHubApiUrlFromProperties) {

        gitHubApiUrl = gitHubApiUrlFromProperties;

        log.info("Initiating GitHub API URL : {}", gitHubApiUrl);
    }

}
