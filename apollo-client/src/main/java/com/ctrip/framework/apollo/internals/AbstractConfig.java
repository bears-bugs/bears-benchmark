package com.ctrip.framework.apollo.internals;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.build.ApolloInjector;
import com.ctrip.framework.apollo.core.utils.ApolloThreadFactory;
import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.exceptions.ApolloConfigException;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ctrip.framework.apollo.tracer.spi.Transaction;
import com.ctrip.framework.apollo.util.ConfigUtil;
import com.ctrip.framework.apollo.util.function.Functions;
import com.ctrip.framework.apollo.util.parser.Parsers;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public abstract class AbstractConfig implements Config {
  private static final Logger logger = LoggerFactory.getLogger(AbstractConfig.class);

  private static final ExecutorService m_executorService;

  private final List<ConfigChangeListener> m_listeners = Lists.newCopyOnWriteArrayList();
  private final Map<ConfigChangeListener, Set<String>> m_interestedKeys = Maps.newConcurrentMap();
  private final ConfigUtil m_configUtil;
  private volatile Cache<String, Integer> m_integerCache;
  private volatile Cache<String, Long> m_longCache;
  private volatile Cache<String, Short> m_shortCache;
  private volatile Cache<String, Float> m_floatCache;
  private volatile Cache<String, Double> m_doubleCache;
  private volatile Cache<String, Byte> m_byteCache;
  private volatile Cache<String, Boolean> m_booleanCache;
  private volatile Cache<String, Date> m_dateCache;
  private volatile Cache<String, Long> m_durationCache;
  private final Map<String, Cache<String, String[]>> m_arrayCache;
  private final List<Cache> allCaches;
  private final AtomicLong m_configVersion; //indicate config version

  static {
    m_executorService = Executors.newCachedThreadPool(ApolloThreadFactory
        .create("Config", true));
  }

  public AbstractConfig() {
    m_configUtil = ApolloInjector.getInstance(ConfigUtil.class);
    m_configVersion = new AtomicLong();
    m_arrayCache = Maps.newConcurrentMap();
    allCaches = Lists.newArrayList();
  }

  @Override
  public void addChangeListener(ConfigChangeListener listener) {
    addChangeListener(listener, null);
  }

  @Override
  public void addChangeListener(ConfigChangeListener listener, Set<String> interestedKeys) {
    if (!m_listeners.contains(listener)) {
      m_listeners.add(listener);
      if (interestedKeys != null && !interestedKeys.isEmpty()) {
        m_interestedKeys.put(listener, Sets.newHashSet(interestedKeys));
      }
    }
  }

  @Override
  public boolean removeChangeListener(ConfigChangeListener listener) {
    m_interestedKeys.remove(listener);
    return m_listeners.remove(listener);
  }

  @Override
  public Integer getIntProperty(String key, Integer defaultValue) {
    try {
      if (m_integerCache == null) {
        synchronized (this) {
          if (m_integerCache == null) {
            m_integerCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_INT_FUNCTION, m_integerCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getIntProperty for %s failed, return default value %d", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Long getLongProperty(String key, Long defaultValue) {
    try {
      if (m_longCache == null) {
        synchronized (this) {
          if (m_longCache == null) {
            m_longCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_LONG_FUNCTION, m_longCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getLongProperty for %s failed, return default value %d", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Short getShortProperty(String key, Short defaultValue) {
    try {
      if (m_shortCache == null) {
        synchronized (this) {
          if (m_shortCache == null) {
            m_shortCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_SHORT_FUNCTION, m_shortCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getShortProperty for %s failed, return default value %d", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Float getFloatProperty(String key, Float defaultValue) {
    try {
      if (m_floatCache == null) {
        synchronized (this) {
          if (m_floatCache == null) {
            m_floatCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_FLOAT_FUNCTION, m_floatCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getFloatProperty for %s failed, return default value %f", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Double getDoubleProperty(String key, Double defaultValue) {
    try {
      if (m_doubleCache == null) {
        synchronized (this) {
          if (m_doubleCache == null) {
            m_doubleCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_DOUBLE_FUNCTION, m_doubleCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getDoubleProperty for %s failed, return default value %f", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Byte getByteProperty(String key, Byte defaultValue) {
    try {
      if (m_byteCache == null) {
        synchronized (this) {
          if (m_byteCache == null) {
            m_byteCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_BYTE_FUNCTION, m_byteCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getByteProperty for %s failed, return default value %d", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public Boolean getBooleanProperty(String key, Boolean defaultValue) {
    try {
      if (m_booleanCache == null) {
        synchronized (this) {
          if (m_booleanCache == null) {
            m_booleanCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_BOOLEAN_FUNCTION, m_booleanCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getBooleanProperty for %s failed, return default value %b", key,
              defaultValue), ex));
    }
    return defaultValue;
  }

  @Override
  public String[] getArrayProperty(String key, final String delimiter, String[] defaultValue) {
    try {
      if (!m_arrayCache.containsKey(delimiter)) {
        synchronized (this) {
          if (!m_arrayCache.containsKey(delimiter)) {
            m_arrayCache.put(delimiter, this.<String[]>newCache());
          }
        }
      }

      Cache<String, String[]> cache = m_arrayCache.get(delimiter);
      String[] result = cache.getIfPresent(key);

      if (result != null) {
        return result;
      }

      return getValueAndStoreToCache(key, new Function<String, String[]>() {
        @Override
        public String[] apply(String input) {
          return input.split(delimiter);
        }
      }, cache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getArrayProperty for %s failed, return default value", key), ex));
    }
    return defaultValue;
  }

  @Override
  public <T extends Enum<T>> T getEnumProperty(String key, Class<T> enumType, T defaultValue) {
    try {
      String value = getProperty(key, null);

      if (value != null) {
        return Enum.valueOf(enumType, value);
      }
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getEnumProperty for %s failed, return default value %s", key,
              defaultValue), ex));
    }

    return defaultValue;
  }

  @Override
  public Date getDateProperty(String key, Date defaultValue) {
    try {
      if (m_dateCache == null) {
        synchronized (this) {
          if (m_dateCache == null) {
            m_dateCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_DATE_FUNCTION, m_dateCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getDateProperty for %s failed, return default value %s", key,
              defaultValue), ex));
    }

    return defaultValue;
  }

  @Override
  public Date getDateProperty(String key, String format, Date defaultValue) {
    try {
      String value = getProperty(key, null);

      if (value != null) {
        return Parsers.forDate().parse(value, format);
      }
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getDateProperty for %s failed, return default value %s", key,
              defaultValue), ex));
    }

    return defaultValue;
  }

  @Override
  public Date getDateProperty(String key, String format, Locale locale, Date defaultValue) {
    try {
      String value = getProperty(key, null);

      if (value != null) {
        return Parsers.forDate().parse(value, format, locale);
      }
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getDateProperty for %s failed, return default value %s", key,
              defaultValue), ex));
    }

    return defaultValue;
  }

  @Override
  public long getDurationProperty(String key, long defaultValue) {
    try {
      if (m_durationCache == null) {
        synchronized (this) {
          if (m_durationCache == null) {
            m_durationCache = newCache();
          }
        }
      }

      return getValueFromCache(key, Functions.TO_DURATION_FUNCTION, m_durationCache, defaultValue);
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
          String.format("getDurationProperty for %s failed, return default value %d", key,
              defaultValue), ex));
    }

    return defaultValue;
  }

  @Override
  public <T> T getProperty(String key, Function<String, T> function, T defaultValue) {
    try {
      String value = getProperty(key, null);

      if (value != null) {
        return function.apply(value);
      }
    } catch (Throwable ex) {
      Tracer.logError(new ApolloConfigException(
              String.format("getProperty for %s failed, return default value %s", key,
                      defaultValue), ex));
    }

    return defaultValue;
  }

  private <T> T getValueFromCache(String key, Function<String, T> parser, Cache<String, T> cache, T defaultValue) {
    T result = cache.getIfPresent(key);

    if (result != null) {
      return result;
    }

    return getValueAndStoreToCache(key, parser, cache, defaultValue);
  }

  private <T> T getValueAndStoreToCache(String key, Function<String, T> parser, Cache<String, T> cache, T defaultValue) {
    long currentConfigVersion = m_configVersion.get();
    String value = getProperty(key, null);

    if (value != null) {
      T result = parser.apply(value);

      if (result != null) {
        synchronized (this) {
          if (m_configVersion.get() == currentConfigVersion) {
            cache.put(key, result);
          }
        }
        return result;
      }
    }

    return defaultValue;
  }

  private <T> Cache<String, T> newCache() {
    Cache<String, T> cache = CacheBuilder.newBuilder()
        .maximumSize(m_configUtil.getMaxConfigCacheSize())
        .expireAfterAccess(m_configUtil.getConfigCacheExpireTime(), m_configUtil.getConfigCacheExpireTimeUnit())
        .build();
    allCaches.add(cache);
    return cache;
  }

  /**
   * Clear config cache
   */
  protected void clearConfigCache() {
    synchronized (this) {
      for (Cache c : allCaches) {
        if (c != null) {
          c.invalidateAll();
        }
      }
      m_configVersion.incrementAndGet();
    }
  }

  protected void fireConfigChange(final ConfigChangeEvent changeEvent) {
    for (final ConfigChangeListener listener : m_listeners) {
      // check whether the listener is interested in this change event
      if (!isConfigChangeListenerInterested(listener, changeEvent)) {
        continue;
      }
      m_executorService.submit(new Runnable() {
        @Override
        public void run() {
          String listenerName = listener.getClass().getName();
          Transaction transaction = Tracer.newTransaction("Apollo.ConfigChangeListener", listenerName);
          try {
            listener.onChange(changeEvent);
            transaction.setStatus(Transaction.SUCCESS);
          } catch (Throwable ex) {
            transaction.setStatus(ex);
            Tracer.logError(ex);
            logger.error("Failed to invoke config change listener {}", listenerName, ex);
          } finally {
            transaction.complete();
          }
        }
      });
    }
  }

  private boolean isConfigChangeListenerInterested(ConfigChangeListener configChangeListener, ConfigChangeEvent configChangeEvent) {
    Set<String> interestedKeys = m_interestedKeys.get(configChangeListener);

    if (interestedKeys == null || interestedKeys.isEmpty()) {
      return true; // no interested keys means interested in all keys
    }

    for (String interestedKey : interestedKeys) {
      if (configChangeEvent.isChanged(interestedKey)) {
        return true;
      }
    }

    return false;
  }

  List<ConfigChange> calcPropertyChanges(String namespace, Properties previous,
                                         Properties current) {
    if (previous == null) {
      previous = new Properties();
    }

    if (current == null) {
      current = new Properties();
    }

    Set<String> previousKeys = previous.stringPropertyNames();
    Set<String> currentKeys = current.stringPropertyNames();

    Set<String> commonKeys = Sets.intersection(previousKeys, currentKeys);
    Set<String> newKeys = Sets.difference(currentKeys, commonKeys);
    Set<String> removedKeys = Sets.difference(previousKeys, commonKeys);

    List<ConfigChange> changes = Lists.newArrayList();

    for (String newKey : newKeys) {
      changes.add(new ConfigChange(namespace, newKey, null, current.getProperty(newKey),
          PropertyChangeType.ADDED));
    }

    for (String removedKey : removedKeys) {
      changes.add(new ConfigChange(namespace, removedKey, previous.getProperty(removedKey), null,
          PropertyChangeType.DELETED));
    }

    for (String commonKey : commonKeys) {
      String previousValue = previous.getProperty(commonKey);
      String currentValue = current.getProperty(commonKey);
      if (Objects.equal(previousValue, currentValue)) {
        continue;
      }
      changes.add(new ConfigChange(namespace, commonKey, previousValue, currentValue,
          PropertyChangeType.MODIFIED));
    }

    return changes;
  }
}
