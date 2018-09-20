package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.ResourceFetcher;
import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.*;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import io.github.azagniotov.matcher.AntPathMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier.PULL_REQUEST;
import static java.util.stream.Collectors.toMap;

@Slf4j
public class BestPracticeNotifierHandler implements PullRequestEventHandler {

    private final Map<String, String> configuredPatternToContentMapping;

    private final List<Notifier> notifiers;

    private RemoteGitHub remoteGitHub;

    private ResourceFetcher resourceFetcher;

    public BestPracticeNotifierHandler(Map configuredPatternToContentMapping,
            List<Notifier> notifiers, RemoteGitHub remoteGitHub, ResourceFetcher resourceFetcher) {

        this.configuredPatternToContentMapping = configuredPatternToContentMapping;
        this.notifiers = notifiers;
        this.remoteGitHub = remoteGitHub;
        this.resourceFetcher = resourceFetcher;
    }

    @Override
    public void handle(PullRequestEvent event) {

        List<PullRequestFile> filesInPr = remoteGitHub.fetchPullRequestFiles(event.getRepository().getFullName(), event.getPrNumber());

        Map<PullRequestFile, Map<String, String>> matchingPatternsByPullRequestFile = findConfiguredPatternsThatMatch(filesInPr);

        //TODO  refactor below nested loops
        if (!matchingPatternsByPullRequestFile.isEmpty()) {

            List<PullRequestComment> existingPrComments = remoteGitHub
                    .fetchPullRequestComments(event.getRepository().getFullName(), event.getPrNumber());

            Map<PullRequestFile, Map<String, String>> matchingPatternsByPullRequestFileOnWhichWeHaventCommentedYet = findConfiguredPatternsOnWhichWehaventCommentedYet(
                    matchingPatternsByPullRequestFile, existingPrComments);

            if (!matchingPatternsByPullRequestFileOnWhichWeHaventCommentedYet.isEmpty()) {

                StringBuilder sb = new StringBuilder("Reminder of best practices for files that have matched : \n");

                for (Map.Entry matchedPrFile : matchingPatternsByPullRequestFileOnWhichWeHaventCommentedYet.entrySet()) {

                    PullRequestFile prFile = (PullRequestFile) matchedPrFile.getKey();
                    Map<String, String> matchedBestPractices = (Map) matchedPrFile.getValue();

                    sb.append("- ").append(prFile.getFilename()).append(" : \n");

                    for (Map.Entry resourceToGetByPattern : matchedBestPractices.entrySet()) {

                        Optional<String> bestPracticeContent = resourceFetcher.fetch((String) resourceToGetByPattern.getValue());

                        if (bestPracticeContent.isPresent()) {
                            sb.append("\t -").append(bestPracticeContent.get()).append("\n");
                        } else {
                            log.warn("best practice located at {} doesn't seem to exist..", resourceToGetByPattern.getValue());
                        }
                    }
                }

                PullRequest pr = remoteGitHub.fetchPullRequestDetails(event.getRepository().getFullName(), event.getPrNumber());

                Map<String, Object> additionalInfosForNotification = new HashMap();
                additionalInfosForNotification.put(PULL_REQUEST, pr);

                notifiers.stream().forEach(n -> n.notify(new User(), new Message(sb.toString()), additionalInfosForNotification));

            }
        }

    }

    private Map<PullRequestFile, Map<String, String>> findConfiguredPatternsOnWhichWehaventCommentedYet(
            Map<PullRequestFile, Map<String, String>> patternsByPullRequestFileToFilter, List<PullRequestComment> existingPrComments) {

        return patternsByPullRequestFileToFilter.entrySet()
                .stream()
                .filter(prFile -> hasntReceivedAnyCommentYet(prFile.getKey().getFilename(), existingPrComments))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private boolean hasntReceivedAnyCommentYet(String fileName, List<PullRequestComment> existingPrComments) {

        return existingPrComments.stream().map(prComment -> prComment.getComment())
                .noneMatch(comment -> comment.contains(fileName));

    }

    private Map findConfiguredPatternsThatMatch(List<PullRequestFile> filesInPr) {

        Map<PullRequestFile, Map<String, String>> matchingPatternsByPullRequestFile = new HashMap<>();

        AntPathMatcher pathMatcher = new AntPathMatcher.Builder().withIgnoreCase().build();

        for (PullRequestFile file : filesInPr) {

            Map<String, String> matchingPatterns = configuredPatternToContentMapping.entrySet().stream()
                    .filter(entry -> pathMatcher.isMatch(entry.getKey(), file.getFilename()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (!matchingPatterns.isEmpty()) {
                matchingPatternsByPullRequestFile.put(file, matchingPatterns);
            }

        }

        return matchingPatternsByPullRequestFile;

    }
}
