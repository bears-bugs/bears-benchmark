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
package org.apache.dubbo.rpc.cluster.configurator;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.cluster.Configurator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AbstractOverrideConfigurator
 *
 */
public abstract class AbstractConfigurator implements Configurator {

    private final URL configuratorUrl;

    public AbstractConfigurator(URL url) {
        if (url == null) {
            throw new IllegalArgumentException("configurator url == null");
        }
        this.configuratorUrl = url;
    }

    @Override
    public URL getUrl() {
        return configuratorUrl;
    }

    @Override
    public URL configure(URL url) {
        if (configuratorUrl.getHost() == null || url == null || url.getHost() == null) {
            return url;
        }
        // If override url has port, means it is a provider address. We want to control a specific provider with this override url, it may take effect on the specific provider instance or on consumers holding this provider instance.
        if (configuratorUrl.getPort() != 0) {
            if (url.getPort() == configuratorUrl.getPort()) {
                return configureIfMatch(url.getHost(), url);
            }
        } else {// override url don't have a port, means the ip override url specify is a consumer address or 0.0.0.0
            // 1.If it is a consumer ip address, the intention is to control a specific consumer instance, it must takes effect at the consumer side, any provider received this override url should ignore;
            // 2.If the ip is 0.0.0.0, this override url can be used on consumer, and also can be used on provider
            if (url.getParameter(Constants.SIDE_KEY, Constants.PROVIDER).equals(Constants.CONSUMER)) {
                return configureIfMatch(NetUtils.getLocalHost(), url);// NetUtils.getLocalHost is the ip address consumer registered to registry.
            } else if (url.getParameter(Constants.SIDE_KEY, Constants.CONSUMER).equals(Constants.PROVIDER)) {
                return configureIfMatch(Constants.ANYHOST_VALUE, url);// take effect on all providers, so address must be 0.0.0.0, otherwise it won't flow to this if branch
            }
        }
        return url;
    }

    private URL configureIfMatch(String host, URL url) {
        if (Constants.ANYHOST_VALUE.equals(configuratorUrl.getHost()) || host.equals(configuratorUrl.getHost())) {
            String configApplication = configuratorUrl.getParameter(Constants.APPLICATION_KEY,
                    configuratorUrl.getUsername());
            String currentApplication = url.getParameter(Constants.APPLICATION_KEY, url.getUsername());
            if (configApplication == null || Constants.ANY_VALUE.equals(configApplication)
                    || configApplication.equals(currentApplication)) {
                Set<String> conditionKeys = new HashSet<String>();
                conditionKeys.add(Constants.CATEGORY_KEY);
                conditionKeys.add(Constants.CHECK_KEY);
                conditionKeys.add(Constants.DYNAMIC_KEY);
                conditionKeys.add(Constants.ENABLED_KEY);
                for (Map.Entry<String, String> entry : configuratorUrl.getParameters().entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (key.startsWith("~") || Constants.APPLICATION_KEY.equals(key) || Constants.SIDE_KEY.equals(key)) {
                        conditionKeys.add(key);
                        if (value != null && !Constants.ANY_VALUE.equals(value)
                                && !value.equals(url.getParameter(key.startsWith("~") ? key.substring(1) : key))) {
                            return url;
                        }
                    }
                }
                return doConfigure(url, configuratorUrl.removeParameters(conditionKeys));
            }
        }
        return url;
    }

    /**
     * Sort by host, priority
     * 1. the url with a specific host ip should have higher priority than 0.0.0.0
     * 2. if two url has the same host, compare by priority value；
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Configurator o) {
        if (o == null) {
            return -1;
        }

        int ipCompare = getUrl().getHost().compareTo(o.getUrl().getHost());
        if (ipCompare == 0) {//host is the same, sort by priority
            int i = getUrl().getParameter(Constants.PRIORITY_KEY, 0),
                    j = o.getUrl().getParameter(Constants.PRIORITY_KEY, 0);
            return Integer.compare(i, j);
        } else {
            return ipCompare;
        }


    }

    protected abstract URL doConfigure(URL currentUrl, URL configUrl);

}
