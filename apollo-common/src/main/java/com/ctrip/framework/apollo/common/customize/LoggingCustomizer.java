package com.ctrip.framework.apollo.common.customize;

import com.google.common.base.Strings;

import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.foundation.Foundation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;

/**
 * clogging config.only used in ctrip
 * @author Jason Song(song_s@ctrip.com)
 */
public abstract class LoggingCustomizer implements InitializingBean {
  private static final Logger logger = LoggerFactory.getLogger(LoggingCustomizer.class);
  private static final String cLoggingAppenderClass =
      "com.ctrip.framework.clogging.agent.appender.CLoggingAppender";
  private static boolean cLoggingAppenderPresent =
      ClassUtils.isPresent(cLoggingAppenderClass, LoggingCustomizer.class.getClassLoader());

  @Override
  public void afterPropertiesSet() {
    if (!cLoggingAppenderPresent) {
      return;
    }

    try {
      tryConfigCLogging();
    } catch (Throwable ex) {
      logger.error("Config CLogging failed", ex);
      Tracer.logError(ex);
    }

  }

  private void tryConfigCLogging() throws Exception {
    String appId = Foundation.app().getAppId();
    if (Strings.isNullOrEmpty(appId)) {
      logger.warn("App id is null or empty!");
      return;
    }


    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
    Class clazz = Class.forName(cLoggingAppenderClass);
    Appender cLoggingAppender = (Appender) clazz.newInstance();

    ReflectionUtils.findMethod(clazz, "setAppId", String.class).invoke(cLoggingAppender, appId);
    ReflectionUtils.findMethod(clazz, "setServerIp", String.class)
        .invoke(cLoggingAppender, cloggingUrl());
    ReflectionUtils.findMethod(clazz, "setServerPort", int.class)
        .invoke(cLoggingAppender, Integer.parseInt(cloggingPort()));

    cLoggingAppender.setName("CentralLogging");
    cLoggingAppender.setContext(loggerContext);
    cLoggingAppender.start();

    ch.qos.logback.classic.Logger logger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("root");
    logger.addAppender(cLoggingAppender);

  }

  /**
   * clogging server url
   * @return
   */
  protected abstract String cloggingUrl();

  /**
   * clogging server port
   * @return
   */
  protected abstract String cloggingPort();


}
