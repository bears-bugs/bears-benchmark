package com.ctrip.framework.apollo.configservice.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.ctrip.framework.apollo.biz.config.BizConfig;
import com.ctrip.framework.apollo.biz.entity.ReleaseMessage;
import com.ctrip.framework.apollo.biz.message.Topics;
import com.ctrip.framework.apollo.biz.repository.ReleaseMessageRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@RunWith(MockitoJUnitRunner.class)
public class ReleaseMessageServiceWithCacheTest {

  private ReleaseMessageServiceWithCache releaseMessageServiceWithCache;

  @Mock
  private ReleaseMessageRepository releaseMessageRepository;

  @Mock
  private BizConfig bizConfig;

  private int scanInterval;

  private TimeUnit scanIntervalTimeUnit;

  @Before

  public void setUp() throws Exception {
    releaseMessageServiceWithCache = new ReleaseMessageServiceWithCache();

    ReflectionTestUtils.setField(releaseMessageServiceWithCache, "releaseMessageRepository",
        releaseMessageRepository);
    ReflectionTestUtils.setField(releaseMessageServiceWithCache, "bizConfig", bizConfig);

    scanInterval = 10;
    scanIntervalTimeUnit = TimeUnit.MILLISECONDS;
    when(bizConfig.releaseMessageCacheScanInterval()).thenReturn(scanInterval);
    when(bizConfig.releaseMessageCacheScanIntervalTimeUnit()).thenReturn(scanIntervalTimeUnit);
  }

  @Test
  public void testWhenNoReleaseMessages() throws Exception {
    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0L)).thenReturn
        (Collections.emptyList());

    releaseMessageServiceWithCache.afterPropertiesSet();

    String someMessage = "someMessage";
    String anotherMessage = "anotherMessage";
    Set<String> messages = Sets.newHashSet(someMessage, anotherMessage);

    assertNull(releaseMessageServiceWithCache.findLatestReleaseMessageForMessages(messages));
    assertTrue(releaseMessageServiceWithCache.findLatestReleaseMessagesGroupByMessages(messages)
        .isEmpty());
  }

  @Test
  public void testWhenHasReleaseMsgAndHasRepeatMsg() throws Exception {
    String someMsgContent = "msg1";
    ReleaseMessage someMsg = assembleReleaseMsg(1, someMsgContent);
    String anotherMsgContent = "msg2";
    ReleaseMessage anotherMsg = assembleReleaseMsg(2, anotherMsgContent);
    ReleaseMessage anotherRepeatMsg = assembleReleaseMsg(3, anotherMsgContent);

    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0L))
        .thenReturn(Arrays.asList(someMsg, anotherMsg, anotherRepeatMsg));

    releaseMessageServiceWithCache.afterPropertiesSet();

    verify(bizConfig).releaseMessageCacheScanInterval();

    ReleaseMessage latestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMsgContent, anotherMsgContent));

    assertNotNull(latestReleaseMsg);
    assertEquals(3, latestReleaseMsg.getId());
    assertEquals(anotherMsgContent, latestReleaseMsg.getMessage());

    List<ReleaseMessage> latestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMsgContent, anotherMsgContent));

    assertEquals(2, latestReleaseMsgGroupByMsgContent.size());
    assertEquals(1, latestReleaseMsgGroupByMsgContent.get(1).getId());
    assertEquals(someMsgContent, latestReleaseMsgGroupByMsgContent.get(1).getMessage());
    assertEquals(3, latestReleaseMsgGroupByMsgContent.get(0).getId());
    assertEquals(anotherMsgContent, latestReleaseMsgGroupByMsgContent.get(0).getMessage());

  }

  @Test
  public void testWhenReleaseMsgSizeBiggerThan500() throws Exception {
    String someMsgContent = "msg1";
    List<ReleaseMessage> firstBatchReleaseMsg = new ArrayList<>(500);
    for (int i = 0; i < 500; i++) {
      firstBatchReleaseMsg.add(assembleReleaseMsg(i + 1, someMsgContent));
    }

    String antherMsgContent = "msg2";
    ReleaseMessage antherMsg = assembleReleaseMsg(501, antherMsgContent);

    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0L))
        .thenReturn(firstBatchReleaseMsg);
    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(500L))
        .thenReturn(Collections.singletonList(antherMsg));

    releaseMessageServiceWithCache.afterPropertiesSet();

    verify(releaseMessageRepository, times(1)).findFirst500ByIdGreaterThanOrderByIdAsc(500L);

    ReleaseMessage latestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMsgContent, antherMsgContent));

    assertNotNull(latestReleaseMsg);
    assertEquals(501, latestReleaseMsg.getId());
    assertEquals(antherMsgContent, latestReleaseMsg.getMessage());

    List<ReleaseMessage> latestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMsgContent, antherMsgContent));

    assertEquals(2, latestReleaseMsgGroupByMsgContent.size());
    assertEquals(500, latestReleaseMsgGroupByMsgContent.get(1).getId());
    assertEquals(501, latestReleaseMsgGroupByMsgContent.get(0).getId());
  }

  @Test
  public void testNewReleaseMessagesBeforeHandleMessage() throws Exception {
    String someMessageContent = "someMessage";
    long someMessageId = 1;
    ReleaseMessage someMessage = assembleReleaseMsg(someMessageId, someMessageContent);

    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0L)).thenReturn(Lists.newArrayList
        (someMessage));

    releaseMessageServiceWithCache.afterPropertiesSet();

    ReleaseMessage latestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMessageContent));

    List<ReleaseMessage> latestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMessageContent));

    assertEquals(someMessageId, latestReleaseMsg.getId());
    assertEquals(someMessageContent, latestReleaseMsg.getMessage());
    assertEquals(latestReleaseMsg, latestReleaseMsgGroupByMsgContent.get(0));

    long newMessageId = 2;
    ReleaseMessage newMessage = assembleReleaseMsg(newMessageId, someMessageContent);

    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(someMessageId)).thenReturn(Lists
        .newArrayList(newMessage));

    scanIntervalTimeUnit.sleep(scanInterval * 10);

    ReleaseMessage newLatestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMessageContent));

    List<ReleaseMessage> newLatestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMessageContent));

    assertEquals(newMessageId, newLatestReleaseMsg.getId());
    assertEquals(someMessageContent, newLatestReleaseMsg.getMessage());
    assertEquals(newLatestReleaseMsg, newLatestReleaseMsgGroupByMsgContent.get(0));
  }

  @Test
  public void testNewReleasesWithHandleMessage() throws Exception {
    String someMessageContent = "someMessage";
    long someMessageId = 1;
    ReleaseMessage someMessage = assembleReleaseMsg(someMessageId, someMessageContent);

    when(releaseMessageRepository.findFirst500ByIdGreaterThanOrderByIdAsc(0L)).thenReturn(Lists.newArrayList
        (someMessage));

    releaseMessageServiceWithCache.afterPropertiesSet();

    ReleaseMessage latestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMessageContent));

    List<ReleaseMessage> latestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMessageContent));

    assertEquals(someMessageId, latestReleaseMsg.getId());
    assertEquals(someMessageContent, latestReleaseMsg.getMessage());
    assertEquals(latestReleaseMsg, latestReleaseMsgGroupByMsgContent.get(0));

    long newMessageId = 2;
    ReleaseMessage newMessage = assembleReleaseMsg(newMessageId, someMessageContent);

    releaseMessageServiceWithCache.handleMessage(newMessage, Topics.APOLLO_RELEASE_TOPIC);

    ReleaseMessage newLatestReleaseMsg =
        releaseMessageServiceWithCache
            .findLatestReleaseMessageForMessages(Sets.newHashSet(someMessageContent));

    List<ReleaseMessage> newLatestReleaseMsgGroupByMsgContent =
        releaseMessageServiceWithCache
            .findLatestReleaseMessagesGroupByMessages(Sets.newHashSet(someMessageContent));

    assertEquals(newMessageId, newLatestReleaseMsg.getId());
    assertEquals(someMessageContent, newLatestReleaseMsg.getMessage());
    assertEquals(newLatestReleaseMsg, newLatestReleaseMsgGroupByMsgContent.get(0));
  }

  private ReleaseMessage assembleReleaseMsg(long id, String msgContent) {

    ReleaseMessage msg = new ReleaseMessage(msgContent);
    msg.setId(id);

    return msg;
  }
}
