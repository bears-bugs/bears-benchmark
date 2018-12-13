package com.ctrip.framework.apollo.configservice.controller;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import com.ctrip.framework.apollo.biz.entity.ReleaseMessage;
import com.ctrip.framework.apollo.biz.message.ReleaseMessageListener;
import com.ctrip.framework.apollo.biz.message.Topics;
import com.ctrip.framework.apollo.biz.utils.EntityManagerUtil;
import com.ctrip.framework.apollo.configservice.service.ReleaseMessageServiceWithCache;
import com.ctrip.framework.apollo.configservice.util.NamespaceUtil;
import com.ctrip.framework.apollo.configservice.util.WatchKeysUtil;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.core.dto.ApolloConfigNotification;
import com.ctrip.framework.apollo.tracer.Tracer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.Set;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
@Deprecated
@RestController
@RequestMapping("/notifications")
public class NotificationController implements ReleaseMessageListener {
  private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
  private static final long TIMEOUT = 30 * 1000;//30 seconds
  private final Multimap<String, DeferredResult<ResponseEntity<ApolloConfigNotification>>>
      deferredResults = Multimaps.synchronizedSetMultimap(HashMultimap.create());
  private static final ResponseEntity<ApolloConfigNotification>
      NOT_MODIFIED_RESPONSE = new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  private static final Splitter STRING_SPLITTER =
      Splitter.on(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR).omitEmptyStrings();

  @Autowired
  private WatchKeysUtil watchKeysUtil;

  @Autowired
  private ReleaseMessageServiceWithCache releaseMessageService;

  @Autowired
  private EntityManagerUtil entityManagerUtil;

  @Autowired
  private NamespaceUtil namespaceUtil;

  /**
   * For single namespace notification, reserved for older version of apollo clients
   *
   * @param appId          the appId
   * @param cluster        the cluster
   * @param namespace      the namespace name
   * @param dataCenter     the datacenter
   * @param notificationId the notification id for the namespace
   * @param clientIp       the client side ip
   * @return a deferred result
   */
  @RequestMapping(method = RequestMethod.GET)
  public DeferredResult<ResponseEntity<ApolloConfigNotification>> pollNotification(
      @RequestParam(value = "appId") String appId,
      @RequestParam(value = "cluster") String cluster,
      @RequestParam(value = "namespace", defaultValue = ConfigConsts.NAMESPACE_APPLICATION) String namespace,
      @RequestParam(value = "dataCenter", required = false) String dataCenter,
      @RequestParam(value = "notificationId", defaultValue = "-1") long notificationId,
      @RequestParam(value = "ip", required = false) String clientIp) {
    //strip out .properties suffix
    namespace = namespaceUtil.filterNamespaceName(namespace);

    Set<String> watchedKeys = watchKeysUtil.assembleAllWatchKeys(appId, cluster, namespace, dataCenter);

    DeferredResult<ResponseEntity<ApolloConfigNotification>> deferredResult =
        new DeferredResult<>(TIMEOUT, NOT_MODIFIED_RESPONSE);

    //check whether client is out-dated
    ReleaseMessage latest = releaseMessageService.findLatestReleaseMessageForMessages(watchedKeys);

    /**
     * Manually close the entity manager.
     * Since for async request, Spring won't do so until the request is finished,
     * which is unacceptable since we are doing long polling - means the db connection would be hold
     * for a very long time
     */
    entityManagerUtil.closeEntityManager();

    if (latest != null && latest.getId() != notificationId) {
      deferredResult.setResult(new ResponseEntity<>(
          new ApolloConfigNotification(namespace, latest.getId()), HttpStatus.OK));
    } else {
      //register all keys
      for (String key : watchedKeys) {
        this.deferredResults.put(key, deferredResult);
      }

      deferredResult
          .onTimeout(() -> logWatchedKeys(watchedKeys, "Apollo.LongPoll.TimeOutKeys"));

      deferredResult.onCompletion(() -> {
        //unregister all keys
        for (String key : watchedKeys) {
          deferredResults.remove(key, deferredResult);
        }
        logWatchedKeys(watchedKeys, "Apollo.LongPoll.CompletedKeys");
      });

      logWatchedKeys(watchedKeys, "Apollo.LongPoll.RegisteredKeys");
      logger.debug("Listening {} from appId: {}, cluster: {}, namespace: {}, datacenter: {}",
          watchedKeys, appId, cluster, namespace, dataCenter);
    }

    return deferredResult;
  }

  @Override
  public void handleMessage(ReleaseMessage message, String channel) {
    logger.info("message received - channel: {}, message: {}", channel, message);

    String content = message.getMessage();
    Tracer.logEvent("Apollo.LongPoll.Messages", content);
    if (!Topics.APOLLO_RELEASE_TOPIC.equals(channel) || Strings.isNullOrEmpty(content)) {
      return;
    }
    List<String> keys = STRING_SPLITTER.splitToList(content);
    //message should be appId+cluster+namespace
    if (keys.size() != 3) {
      logger.error("message format invalid - {}", content);
      return;
    }

    ResponseEntity<ApolloConfigNotification> notification =
        new ResponseEntity<>(
            new ApolloConfigNotification(keys.get(2), message.getId()), HttpStatus.OK);

    if (!deferredResults.containsKey(content)) {
      return;
    }
    //create a new list to avoid ConcurrentModificationException
    List<DeferredResult<ResponseEntity<ApolloConfigNotification>>> results =
        Lists.newArrayList(deferredResults.get(content));
    logger.debug("Notify {} clients for key {}", results.size(), content);

    for (DeferredResult<ResponseEntity<ApolloConfigNotification>> result : results) {
      result.setResult(notification);
    }
    logger.debug("Notification completed");
  }

  private void logWatchedKeys(Set<String> watchedKeys, String eventName) {
    for (String watchedKey : watchedKeys) {
      Tracer.logEvent(eventName, watchedKey);
    }
  }
}

