package com.ctrip.framework.apollo.core.utils;

import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Properties;

public class ResourceUtils {

  private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);
  private static final String[] DEFAULT_FILE_SEARCH_LOCATIONS = new String[]{"./config/", "./"};

  @SuppressWarnings("unchecked")
  public static Properties readConfigFile(String configPath, Properties defaults) {
    Properties props = new Properties();
    if (defaults != null) {
      props.putAll(defaults);
    }

    InputStream in = loadConfigFileFromDefaultSearchLocations(configPath);

    try {
      if (in != null) {
        props.load(in);
      }
    } catch (IOException ex) {
      logger.warn("Reading config failed: {}", ex.getMessage());
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException ex) {
          logger.warn("Close config failed: {}", ex.getMessage());
        }
      }
    }

    if (logger.isDebugEnabled()) {
      StringBuilder sb = new StringBuilder();
      for (String propertyName : props.stringPropertyNames()) {
        sb.append(propertyName).append('=').append(props.getProperty(propertyName)).append('\n');

      }
      if (sb.length() > 0) {
        logger.debug("Reading properties: \n" + sb.toString());
      } else {
        logger.warn("No available properties: {}", configPath);
      }
    }
    return props;
  }

  private static InputStream loadConfigFileFromDefaultSearchLocations(String configPath) {
    try {
      // load from default search locations
      for (String searchLocation : DEFAULT_FILE_SEARCH_LOCATIONS) {
        File candidate = Paths.get(searchLocation, configPath).toFile();
        if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
          logger.debug("Reading config from resource {}", candidate.getAbsolutePath());
          return new FileInputStream(candidate);
        }
      }

      // load from classpath
      URL url = ClassLoaderUtil.getLoader().getResource(configPath);

      if (url != null) {
        InputStream in = getResourceAsStream(url);

        if (in != null) {
          logger.debug("Reading config from resource {}", url.getPath());
          return in;
        }
      }

      // load outside resource under current user path
      File candidate = new File(System.getProperty("user.dir"), configPath);
      if (candidate.exists() && candidate.isFile() && candidate.canRead()) {
        logger.debug("Reading config from resource {}", candidate.getAbsolutePath());
        return new FileInputStream(candidate);
      }
    } catch (FileNotFoundException e) {
      //ignore
    }
    return null;
  }

  private static InputStream getResourceAsStream(URL url) {
    try {
      return url != null ? url.openStream() : null;
    } catch (IOException e) {
      return null;
    }
  }
}
