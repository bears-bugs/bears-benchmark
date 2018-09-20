package com.societegenerale.cidroid.tasks.consumer.services.actionHandlers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.societegenerale.cidroid.tasks.consumer.services.RemoteGitHub;
import com.societegenerale.cidroid.tasks.consumer.services.ResourceFetcher;
import com.societegenerale.cidroid.tasks.consumer.services.model.Message;
import com.societegenerale.cidroid.tasks.consumer.services.model.github.*;
import com.societegenerale.cidroid.tasks.consumer.services.notifiers.Notifier;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class BestPracticeNotifierHandlerTest {

    public static final String MATCHING_FILENAME = "commons/src/main/resources/db/changelog/changes/004-create-risk-table.yml";

    private final String REPO_FULL_NAME = "someOrg/someRepo";

    ResourceFetcher mockResourceFetcher = mock(ResourceFetcher.class);

    Notifier mockNotifier = mock(Notifier.class);

    RemoteGitHub mockRemoteGitHub = mock(RemoteGitHub.class);

    Map patternToContentMapping = new HashMap<>();

    BestPracticeNotifierHandler handler = new BestPracticeNotifierHandler(patternToContentMapping, Arrays.asList(mockNotifier), mockRemoteGitHub,
            mockResourceFetcher);

    ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

    Repository repository = new Repository();

    PullRequestFile matchingPullRequestFile = new PullRequestFile();

    PullRequestEvent pullRequestEvent;

    @Before
    public void setup() {

        matchingPullRequestFile.setFilename(MATCHING_FILENAME);

        when(mockResourceFetcher.fetch("http://someUrl/liquibaseBestPractices.md")).thenReturn(Optional.of("careful with Liquibase changes !"));
        when(mockRemoteGitHub.fetchPullRequestFiles(REPO_FULL_NAME, 123)).thenReturn(Arrays.asList(matchingPullRequestFile));

        patternToContentMapping.put("**/db/changelog/**/*.yml", "http://someUrl/liquibaseBestPractices.md");

        repository.setFullName(REPO_FULL_NAME);
        pullRequestEvent = new PullRequestEvent("created", 123, repository);

    }

    @Test
    public void shouldNotifyWithConfiguredContentIfMatching() {

        handler.handle(pullRequestEvent);

        verify(mockNotifier, times(1)).notify(any(User.class), messageCaptor.capture(), any(Map.class));
        assertThat(messageCaptor.getValue().getContent()).contains("careful with Liquibase changes !");

    }

    @Test
    public void shouldNotNotifyWhenNoMatch() {

        matchingPullRequestFile.setFilename("commons/src/main/resources/db/chaelog/changes/004-create-risk-table.yml");

        handler.handle(pullRequestEvent);

        verify(mockNotifier, never()).notify(any(User.class), messageCaptor.capture(), any(Map.class));
    }

    @Test
    public void shouldNotifyOnlyOnceWhenSeveralMatches() {

        PullRequestFile anotherMatchingPullRequestFile = new PullRequestFile();
        anotherMatchingPullRequestFile.setFilename("myModule/src/main/java/org/myPackage/CoucouDto.java");

        when(mockRemoteGitHub.fetchPullRequestFiles(REPO_FULL_NAME, 123))
                .thenReturn(Arrays.asList(matchingPullRequestFile, anotherMatchingPullRequestFile));
        when(mockResourceFetcher.fetch("http://someUrl/noDtoBestPractice.md")).thenReturn(Optional.of("Don't name java object with DTO suffix"));

        patternToContentMapping.put("**/*Dto.java", "http://someUrl/noDtoBestPractice.md");

        handler.handle(pullRequestEvent);

        verify(mockNotifier, times(1)).notify(any(User.class), messageCaptor.capture(), any(Map.class));
        assertThat(messageCaptor.getValue().getContent()).contains("careful with Liquibase changes !");
        assertThat(messageCaptor.getValue().getContent()).contains("Don't name java object with DTO suffix");

    }

    @Test
    public void shouldLogMonitoringEventWhenCantFetchResource() {

        Appender appender = mock(Appender.class);

        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        when(appender.getName()).thenReturn("MOCK");
        when(appender.isStarted()).thenReturn(true);
        logger.addAppender(appender);

        String urlWithNoResource = "http://someUrl/liquibaseBestPractices.md";

        when(mockResourceFetcher.fetch(urlWithNoResource)).thenReturn(Optional.empty());

        handler.handle(pullRequestEvent);

        verify(mockNotifier, times(1)).notify(any(User.class), any(Message.class), any(Map.class));

        ArgumentCaptor<ILoggingEvent> logEventCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);

        verify(appender, atLeastOnce()).doAppend(logEventCaptor.capture());

        assertThat(logEventCaptor.getAllValues().stream()
                .filter(loggingEvent -> loggingEvent.getLevel() == Level.WARN &&
                        loggingEvent.getFormattedMessage().contains("best practice located at " + urlWithNoResource + " doesn't seem to exist"))
                .count()).isEqualTo(1);

    }

    @Test
    public void shouldNotPostNotificationIfAlreadyThereFromPreviousEvent() {

        PullRequestComment existingPrComment = new PullRequestComment("some comments about " + MATCHING_FILENAME,
                new User("someLogin", "firstName.lastName@domain.com"));

        List<PullRequestComment> existingPRcomments = Arrays.asList(existingPrComment);
        when(mockRemoteGitHub.fetchPullRequestComments(REPO_FULL_NAME, 123)).thenReturn(existingPRcomments);

        handler.handle(pullRequestEvent);

        verify(mockNotifier, never()).notify(any(User.class), messageCaptor.capture(), any(Map.class));
    }

    @Test
    public void shouldPostOnlyForFilesOnwhichWeHaventCommentedYet() {

        String secondMatchingFileNameOnWhichTheresNoCommentYet = "commons/src/main/resources/db/changelog/changes/005-someOtherFile.yml";

        PullRequestFile secondMatchingPullRequestFile = new PullRequestFile();
        secondMatchingPullRequestFile.setFilename(secondMatchingFileNameOnWhichTheresNoCommentYet);

        when(mockRemoteGitHub.fetchPullRequestFiles(REPO_FULL_NAME, 123))
                .thenReturn(Arrays.asList(matchingPullRequestFile, secondMatchingPullRequestFile));

        PullRequestComment existingPrComment = new PullRequestComment("some comments about " + MATCHING_FILENAME,
                new User("someLogin", "firstName.lastName@domain.com"));

        List<PullRequestComment> existingPRcomments = Arrays.asList(existingPrComment);
        when(mockRemoteGitHub.fetchPullRequestComments(REPO_FULL_NAME, 123)).thenReturn(existingPRcomments);

        handler.handle(pullRequestEvent);

        verify(mockNotifier, times(1)).notify(any(User.class), messageCaptor.capture(), any(Map.class));

        String commentPublished = messageCaptor.getValue().getContent();

        assertThat(commentPublished).contains(secondMatchingFileNameOnWhichTheresNoCommentYet);
        assertThat(commentPublished).doesNotContain(MATCHING_FILENAME);

    }

}