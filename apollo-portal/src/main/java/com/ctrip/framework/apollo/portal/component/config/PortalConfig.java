package com.ctrip.framework.apollo.portal.component.config;


import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.ctrip.framework.apollo.common.config.RefreshableConfig;
import com.ctrip.framework.apollo.common.config.RefreshablePropertySource;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.apollo.portal.entity.vo.Organization;
import com.ctrip.framework.apollo.portal.service.PortalDBPropertySource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class PortalConfig extends RefreshableConfig {

  private Gson gson = new Gson();
  private static final Type ORGANIZATION = new TypeToken<List<Organization>>() {
  }.getType();

  @Autowired
  private PortalDBPropertySource portalDBPropertySource;

  @Override
  public List<RefreshablePropertySource> getRefreshablePropertySources() {
    return Collections.singletonList(portalDBPropertySource);
  }

  /***
   * Level: important
   **/
  public List<Env> portalSupportedEnvs() {
    String[] configurations = getArrayProperty("apollo.portal.envs", new String[]{"FAT", "UAT", "PRO"});
    List<Env> envs = Lists.newLinkedList();

    for (String env : configurations) {
      envs.add(Env.fromString(env));
    }

    return envs;
  }

  public List<String> superAdmins() {
    String superAdminConfig = getValue("superAdmin", "");
    if (Strings.isNullOrEmpty(superAdminConfig)) {
      return Collections.emptyList();
    }
    return splitter.splitToList(superAdminConfig);
  }

  public Set<Env> emailSupportedEnvs() {
    String[] configurations = getArrayProperty("email.supported.envs", null);

    Set<Env> result = Sets.newHashSet();
    if (configurations == null || configurations.length == 0) {
      return result;
    }

    for (String env : configurations) {
      result.add(Env.fromString(env));
    }

    return result;
  }

  public boolean isConfigViewMemberOnly(String env) {
    String[] configViewMemberOnlyEnvs = getArrayProperty("configView.memberOnly.envs", new String[0]);

    for (String memberOnlyEnv : configViewMemberOnlyEnvs) {
      if (memberOnlyEnv.equalsIgnoreCase(env)) {
        return true;
      }
    }

    return false;
  }

  /***
   * Level: normal
   **/
  public int connectTimeout() {
    return getIntProperty("api.connectTimeout", 3000);
  }

  public int readTimeout() {
    return getIntProperty("api.readTimeout", 10000);
  }

  public List<Organization> organizations() {

    String organizations = getValue("organizations");
    return organizations == null ? Collections.emptyList() : gson.fromJson(organizations, ORGANIZATION);
  }

  public String portalAddress() {
    return getValue("apollo.portal.address");
  }

  public boolean isEmergencyPublishAllowed(Env env) {
    String targetEnv = env.name();

    String[] emergencyPublishSupportedEnvs = getArrayProperty("emergencyPublish.supported.envs", new String[0]);

    for (String supportedEnv : emergencyPublishSupportedEnvs) {
      if (Objects.equals(targetEnv, supportedEnv.toUpperCase().trim())) {
        return true;
      }
    }

    return false;
  }

  /***
   * Level: low
   **/
  public Set<Env> publishTipsSupportedEnvs() {
    String[] configurations = getArrayProperty("namespace.publish.tips.supported.envs", null);

    Set<Env> result = Sets.newHashSet();
    if (configurations == null || configurations.length == 0) {
      return result;
    }

    for (String env : configurations) {
      result.add(Env.fromString(env));
    }

    return result;
  }

  public String consumerTokenSalt() {
    return getValue("consumer.token.salt", "apollo-portal");
  }

  public String emailSender() {
    return getValue("email.sender");
  }

  public String emailTemplateFramework() {
    return getValue("email.template.framework", "");
  }

  public String emailReleaseDiffModuleTemplate() {
    return getValue("email.template.release.module.diff", "");
  }

  public String emailRollbackDiffModuleTemplate() {
    return getValue("email.template.rollback.module.diff", "");
  }

  public String emailGrayRulesModuleTemplate() {
    return getValue("email.template.release.module.rules", "");
  }

  public String wikiAddress() {
    return getValue("wiki.address", "https://github.com/ctripcorp/apollo/wiki");
  }

  public boolean canAppAdminCreatePrivateNamespace() {
    return getBooleanProperty("admin.createPrivateNamespace.switch", true);
  }

  /***
   * The following configurations are used in ctrip profile
   **/

  public int appId() {
    return getIntProperty("ctrip.appid", 0);
  }

  //send code & template id. apply from ewatch
  public String sendCode() {
    return getValue("ctrip.email.send.code");
  }

  public int templateId() {
    return getIntProperty("ctrip.email.template.id", 0);
  }

  //email retention time in email server queue.TimeUnit: hour
  public int survivalDuration() {
    return getIntProperty("ctrip.email.survival.duration", 5);
  }

  public boolean isSendEmailAsync() {
    return getBooleanProperty("email.send.async", true);
  }

  public String portalServerName() {
    return getValue("serverName");
  }

  public String casServerLoginUrl() {
    return getValue("casServerLoginUrl");
  }

  public String casServerUrlPrefix() {
    return getValue("casServerUrlPrefix");
  }

  public String credisServiceUrl() {
    return getValue("credisServiceUrl");
  }

  public String userServiceUrl() {
    return getValue("userService.url");
  }

  public String userServiceAccessToken() {
    return getValue("userService.accessToken");
  }

  public String soaServerAddress() {
    return getValue("soa.server.address");
  }

  public String cloggingUrl() {
    return getValue("clogging.server.url");
  }

  public String cloggingPort() {
    return getValue("clogging.server.port");
  }

  public String hermesServerAddress() {
    return getValue("hermes.server.address");
  }

}
