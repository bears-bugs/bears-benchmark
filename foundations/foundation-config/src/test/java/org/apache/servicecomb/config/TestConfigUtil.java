/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.config;

import static com.seanyinx.github.unit.scaffolding.Randomness.uniquify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.servicecomb.config.archaius.sources.ConfigModel;
import org.apache.servicecomb.config.archaius.sources.MicroserviceConfigLoader;
import org.apache.servicecomb.config.spi.ConfigCenterConfigurationSource;
import org.apache.servicecomb.foundation.common.utils.SPIServiceUtils;
import org.apache.servicecomb.foundation.test.scaffolding.config.ArchaiusUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.netflix.config.ConcurrentCompositeConfiguration;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicWatchedConfiguration;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;

public class TestConfigUtil {

  private static final String systemPropertyName = "servicecomb.cse.servicecomb.system.setting";

  private static final String systemExpected = uniquify("ran");

  private static final String environmentPropertyName = "servicecomb.cse.servicecomb.environment.setting";

  private static final String environmentExpected = uniquify("ran");

  private final MapBasedConfigurationSource configurationSource = new MapBasedConfigurationSource();

  @BeforeClass
  public static void beforeTest() {
    ArchaiusUtils.resetConfig();

    System.setProperty(systemPropertyName, systemExpected);
    try {
      setEnv(environmentPropertyName, environmentExpected);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }

    ConfigUtil.installDynamicConfig();
  }

  @AfterClass
  public static void tearDown() throws Exception {
    ArchaiusUtils.resetConfig();
  }

  @Test
  public void testCreateConfigFromConfigCenterNoUrl(@Mocked Configuration localConfiguration) {
    AbstractConfiguration configFromConfigCenter = ConfigUtil.createConfigFromConfigCenter(localConfiguration);
    Assert.assertNull(configFromConfigCenter);
  }

  @Test
  public void testCreateDynamicConfigNoConfigCenterSPI() {
    new Expectations(SPIServiceUtils.class) {
      {
        SPIServiceUtils.getTargetService(ConfigCenterConfigurationSource.class);
        result = null;
      }
    };

    AbstractConfiguration dynamicConfig = ConfigUtil.createDynamicConfig();
    MicroserviceConfigLoader loader = ConfigUtil.getMicroserviceConfigLoader(dynamicConfig);
    List<ConfigModel> list = loader.getConfigModels();
    Assert.assertEquals(loader, ConfigUtil.getMicroserviceConfigLoader(dynamicConfig));
    Assert.assertEquals(1, list.size());
    Assert.assertNotEquals(DynamicWatchedConfiguration.class,
        ((ConcurrentCompositeConfiguration) dynamicConfig).getConfiguration(0).getClass());
  }

  @Test
  public void testCreateDynamicConfigHasConfigCenter(
      @Mocked ConfigCenterConfigurationSource configCenterConfigurationSource) {
    AbstractConfiguration dynamicConfig = ConfigUtil.createDynamicConfig();
    Assert.assertEquals(DynamicWatchedConfiguration.class,
        ((ConcurrentCompositeConfiguration) dynamicConfig).getConfiguration(0).getClass());
  }

  @Test
  public void testGetPropertyInvalidConfig() {
    Assert.assertNull(ConfigUtil.getProperty(null, "any"));
    Assert.assertNull(ConfigUtil.getProperty(new Object(), "any"));
  }

  @Test
  public void propertiesFromFileIsDuplicatedToCse() throws Exception {
    String expected = "value";

    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty("servicecomb.cse.servicecomb.file", null)
        .get(),
        equalTo(expected));

    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty("cse.cse.servicecomb.file", null)
        .get(),
        equalTo(expected));
  }

  @Test
  public void propertiesFromSystemIsDuplicatedToCse() throws Exception {
    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty(systemPropertyName, null)
        .get(),
        equalTo(systemExpected));

    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty("servicecomb.cse.servicecomb.system.setting", null)
        .get(),
        equalTo(systemExpected));
  }

  @Test
  public void propertiesFromEnvironmentIsDuplicatedToCse() throws Exception {
    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty(environmentPropertyName, null)
        .get(),
        equalTo(environmentExpected));

    assertThat(DynamicPropertyFactory
        .getInstance()
        .getStringProperty("servicecomb.cse.servicecomb.environment.setting", null)
        .get(),
        equalTo(environmentExpected));
  }

  @Test
  public void duplicateServiceCombConfigToCseListValue() throws Exception {
    List<String> list = Arrays.asList("a", "b");

    AbstractConfiguration config = new DynamicConfiguration();
    config.addProperty("servicecomb.list", list);
    Deencapsulation.invoke(ConfigUtil.class, "duplicateServiceCombConfigToCse", config);

    Object result = config.getProperty("cse.list");
    assertThat(result, instanceOf(List.class));
    assertThat(result, equalTo(list));
  }

  @Test
  public void propertiesAddFromDynamicConfigSourceIsDuplicated() throws Exception {
    String expected = uniquify("ran");
    String someProperty = "servicecomb.cse.servicecomb.add";
    String injectProperty = "cse.cse.servicecomb.add";

    configurationSource.addProperty(someProperty, expected);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    String changed = uniquify("changed");
    configurationSource.addProperty(someProperty, changed);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(changed));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(changed));

    expected = uniquify("ran");
    someProperty = "servicecomb.servicecomb.cse.add";
    injectProperty = "cse.servicecomb.cse.add";

    configurationSource.addProperty(someProperty, expected);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    changed = uniquify("changed");
    configurationSource.addProperty(someProperty, changed);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(changed));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(changed));
  }

  @Test
  public void propertiesChangeFromDynamicConfigSourceIsDuplicated() throws Exception {
    String expected = uniquify("ran");
    String someProperty = "servicecomb.cse.servicecomb.change";
    String injectProperty = "cse.cse.servicecomb.change";
    configurationSource.addProperty(someProperty, expected);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    String changed = uniquify("changed");
    configurationSource.setProperty(someProperty, changed);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(changed));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(changed));

    expected = uniquify("ran");
    someProperty = "servicecomb.servicecomb.cse.change";
    injectProperty = "cse.servicecomb.cse.change";
    configurationSource.addProperty(someProperty, expected);
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    changed = uniquify("changed");
    configurationSource.setProperty(someProperty, changed);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(changed));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(changed));
  }

  @Test
  public void propertiesDeleteFromDynamicConfigSourceIsDuplicated() throws Exception {
    String expected = uniquify("ran");
    String someProperty = "servicecomb.cse.servicecomb.delete";
    String injectProperty = "cse.cse.servicecomb.delete";
    configurationSource.addProperty(someProperty, expected);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    configurationSource.deleteProperty(someProperty);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(null));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(null));

    expected = uniquify("ran");
    someProperty = "servicecomb.servicecomb.cse.delete";
    injectProperty = "cse.servicecomb.cse.delete";
    configurationSource.addProperty(someProperty, expected);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(expected));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(expected));

    configurationSource.deleteProperty(someProperty);

    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(someProperty, null).get(),
        equalTo(null));
    assertThat(DynamicPropertyFactory.getInstance().getStringProperty(injectProperty, null).get(),
        equalTo(null));
  }

  @Test
  public void testConvertEnvVariable() {
    String someProperty = "cse_service_registry_address";
    AbstractConfiguration config = new DynamicConfiguration();
    config.addProperty(someProperty, "testing");
    AbstractConfiguration result = ConfigUtil.convertEnvVariable(config);
    assertThat(result.getString("cse.service.registry.address"), equalTo("testing"));
    assertThat(result.getString("cse_service_registry_address"), equalTo("testing"));
  }

  @SuppressWarnings("unchecked")
  private static void setEnv(String key, String value) throws IllegalAccessException, NoSuchFieldException {
    Class<?>[] classes = Collections.class.getDeclaredClasses();
    Map<String, String> env = System.getenv();
    for (Class<?> cl : classes) {
      if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
        Field field = cl.getDeclaredField("m");
        field.setAccessible(true);
        Object obj = field.get(env);
        Map<String, String> map = (Map<String, String>) obj;
        map.put(key, value);
      }
    }
  }
}
