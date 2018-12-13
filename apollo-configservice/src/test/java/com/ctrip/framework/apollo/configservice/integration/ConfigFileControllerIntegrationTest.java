package com.ctrip.framework.apollo.configservice.integration;

import com.ctrip.framework.apollo.configservice.service.AppNamespaceServiceWithCache;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import com.ctrip.framework.apollo.biz.entity.Namespace;
import com.ctrip.framework.apollo.core.ConfigConsts;
import com.netflix.servo.util.Strings;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class ConfigFileControllerIntegrationTest extends AbstractBaseIntegrationTest {
  private String someAppId;
  private String somePublicAppId;
  private String someCluster;
  private String someNamespace;
  private String somePublicNamespace;
  private String someDC;
  private String someDefaultCluster;
  private String grayClientIp;
  private String nonGrayClientIp;
  private Gson gson = new Gson();
  private ExecutorService executorService;
  private Type mapResponseType = new TypeToken<Map<String, String>>(){}.getType();

  @Autowired
  private AppNamespaceServiceWithCache appNamespaceServiceWithCache;

  @Before
  public void setUp() throws Exception {
    ReflectionTestUtils.invokeMethod(appNamespaceServiceWithCache, "reset");
    someDefaultCluster = ConfigConsts.CLUSTER_NAME_DEFAULT;
    someAppId = "someAppId";
    somePublicAppId = "somePublicAppId";
    someCluster = "someCluster";
    someNamespace = "someNamespace";
    somePublicNamespace = "somePublicNamespace";
    someDC = "someDC";
    grayClientIp = "1.1.1.1";
    nonGrayClientIp = "2.2.2.2";
    executorService = Executors.newSingleThreadExecutor();
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryConfigAsProperties() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace);

    String result = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(result.contains("k2=v2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-gray-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryConfigAsPropertiesWithGrayRelease() throws Exception {
    AtomicBoolean stop = new AtomicBoolean();
    periodicSendMessage(executorService, assembleKey(someAppId, ConfigConsts.CLUSTER_NAME_DEFAULT, ConfigConsts.NAMESPACE_APPLICATION),
        stop);

    TimeUnit.MILLISECONDS.sleep(500);

    stop.set(true);

    ResponseEntity<String> response =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}?ip={clientIp}", String.class,
                getHostUrl(), someAppId, someDefaultCluster, ConfigConsts.NAMESPACE_APPLICATION, grayClientIp);

    ResponseEntity<String> anotherResponse =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}?ip={clientIp}", String.class,
                getHostUrl(), someAppId, someDefaultCluster, ConfigConsts.NAMESPACE_APPLICATION, nonGrayClientIp);

    String result = response.getBody();
    String anotherResult = anotherResponse.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(result.contains("k1=v1-gray"));

    assertEquals(HttpStatus.OK, anotherResponse.getStatusCode());
    assertFalse(anotherResult.contains("k1=v1-gray"));
    assertTrue(anotherResult.contains("k1=v1"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-release-public-dc-override.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryPublicConfigAsProperties() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}?dataCenter={dateCenter}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace, someDC);

    String result = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(result.contains("k1=override-someDC-v1"));
    assertTrue(result.contains("k2=someDC-v2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryConfigAsJson() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace);

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("v2", configs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryConfigAsJsonWithIncorrectCase() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace.toUpperCase());

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("v2", configs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-release-public-dc-override.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryPublicConfigAsJson() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?dataCenter={dateCenter}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace, someDC);

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("override-someDC-v1", configs.get("k1"));
    assertEquals("someDC-v2", configs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-release-public-dc-override.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryPublicConfigAsJsonWithIncorrectCase() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?dataCenter={dateCenter}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace.toUpperCase(), someDC);

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("override-someDC-v1", configs.get("k1"));
    assertEquals("someDC-v2", configs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-release-public-default-override.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-gray-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryPublicConfigAsJsonWithGrayRelease() throws Exception {
    AtomicBoolean stop = new AtomicBoolean();
    periodicSendMessage(executorService, assembleKey(somePublicAppId, ConfigConsts.CLUSTER_NAME_DEFAULT, somePublicNamespace),
        stop);

    TimeUnit.MILLISECONDS.sleep(500);

    stop.set(true);

    ResponseEntity<String> response =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?ip={clientIp}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace, grayClientIp);

    ResponseEntity<String> anotherResponse =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?ip={clientIp}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace, nonGrayClientIp);

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);
    Map<String, String> anotherConfigs = gson.fromJson(anotherResponse.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.OK, anotherResponse.getStatusCode());

    assertEquals("override-v1", configs.get("k1"));
    assertEquals("gray-v2", configs.get("k2"));

    assertEquals("override-v1", anotherConfigs.get("k1"));
    assertEquals("default-v2", anotherConfigs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-release-public-default-override.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/test-gray-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testQueryPublicConfigAsJsonWithGrayReleaseAndIncorrectCase() throws Exception {
    AtomicBoolean stop = new AtomicBoolean();
    periodicSendMessage(executorService, assembleKey(somePublicAppId, ConfigConsts.CLUSTER_NAME_DEFAULT, somePublicNamespace),
        stop);

    TimeUnit.MILLISECONDS.sleep(500);

    stop.set(true);

    ResponseEntity<String> response =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?ip={clientIp}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace.toUpperCase(), grayClientIp);

    ResponseEntity<String> anotherResponse =
        restTemplate
            .getForEntity(
                "http://{baseurl}/configfiles/json/{appId}/{clusterName}/{namespace}?ip={clientIp}",
                String.class,
                getHostUrl(), someAppId, someDefaultCluster, somePublicNamespace.toUpperCase(), nonGrayClientIp);

    Map<String, String> configs = gson.fromJson(response.getBody(), mapResponseType);
    Map<String, String> anotherConfigs = gson.fromJson(anotherResponse.getBody(), mapResponseType);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(HttpStatus.OK, anotherResponse.getStatusCode());

    assertEquals("override-v1", configs.get("k1"));
    assertEquals("gray-v2", configs.get("k2"));

    assertEquals("override-v1", anotherConfigs.get("k1"));
    assertEquals("default-v2", anotherConfigs.get("k2"));
  }

  @Test
  @Sql(scripts = "/integration-test/test-release.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "/integration-test/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  public void testConfigChanged() throws Exception {
    ResponseEntity<String> response =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace);

    String result = response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(result.contains("k2=v2"));

    String someReleaseName = "someReleaseName";
    String someReleaseComment = "someReleaseComment";
    Namespace namespace = new Namespace();
    namespace.setAppId(someAppId);
    namespace.setClusterName(someCluster);
    namespace.setNamespaceName(someNamespace);
    String someOwner = "someOwner";

    Map<String, String> newConfigurations = ImmutableMap.of("k1", "v1-changed", "k2", "v2-changed");

    buildRelease(someReleaseName, someReleaseComment, namespace, newConfigurations, someOwner);

    ResponseEntity<String> anotherResponse =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace);

    assertEquals(response.getBody(), anotherResponse.getBody());

    List<String> keys = Lists.newArrayList(someAppId, someCluster, someNamespace);
    String message = Strings.join(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR, keys.iterator());
    sendReleaseMessage(message);

    TimeUnit.MILLISECONDS.sleep(500);

    ResponseEntity<String> newResponse =
        restTemplate
            .getForEntity("http://{baseurl}/configfiles/{appId}/{clusterName}/{namespace}", String.class,
                getHostUrl(), someAppId, someCluster, someNamespace);

    result = newResponse.getBody();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(result.contains("k1=v1-changed"));
    assertTrue(result.contains("k2=v2-changed"));
  }

  private String assembleKey(String appId, String cluster, String namespace) {
    return Joiner.on(ConfigConsts.CLUSTER_NAMESPACE_SEPARATOR).join(appId, cluster, namespace);
  }
}
