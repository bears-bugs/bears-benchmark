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

package org.apache.servicecomb.foundation.common.utils;

import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

/**
 * SPI Service utils
 *
 *
 */
public final class SPIServiceUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(SPIServiceUtils.class);

  private SPIServiceUtils() {

  }

  /**
   * get target service.if target services are array,only random access to a service.
   */
  public static <T> T getTargetService(Class<T> serviceType) {
    ServiceLoader<T> loader = ServiceLoader.load(serviceType);
    for (T service : loader) {
      LOGGER.info("get the SPI service success, the extend service is: {}", service.getClass());
      return service;
    }
    LOGGER.info("Can not get the SPI service, the interface type is: {}", serviceType.toString());
    return null;
  }

  public static <T> List<T> getAllService(Class<T> serviceType) {
    List<T> list = new ArrayList<>();
    ServiceLoader.load(serviceType).forEach(list::add);

    return list;
  }

  public static <T> List<T> getSortedService(Class<T> serviceType) {
    List<Entry<Integer, T>> serviceEntries = new ArrayList<>();
    ServiceLoader<T> serviceLoader = ServiceLoader.load(serviceType);
    serviceLoader.forEach(service -> {
      int serviceOrder = 0;
      Method getOrder = ReflectionUtils.findMethod(service.getClass(), "getOrder");
      if (getOrder != null) {
        serviceOrder = (int) ReflectionUtils.invokeMethod(getOrder, service);
      }

      Entry<Integer, T> entry = new SimpleEntry<>(serviceOrder, service);
      serviceEntries.add(entry);
    });

    return serviceEntries.stream()
        .sorted(Comparator.comparingInt(Entry::getKey))
        .map(Entry::getValue)
        .collect(Collectors.toList());
  }

  public static <T> T getPriorityHighestService(Class<T> serviceType) {
    List<T> services = getSortedService(serviceType);
    if (services.isEmpty()) {
      return null;
    }

    return services.get(0);
  }
}
