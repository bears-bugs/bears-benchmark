/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tamaya.core.testdata;

import org.apache.tamaya.spisupport.propertysource.BasePropertySource;
import org.apache.tamaya.spi.PropertySource;
import org.apache.tamaya.spi.PropertySourceProvider;
import org.apache.tamaya.spi.PropertyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test provider reading properties from classpath:cfg/final/**.properties.
 */
public class TestPropertySourceProvider implements PropertySourceProvider {

    private List<PropertySource> list = new ArrayList<>();

    public TestPropertySourceProvider(){
        list.add(new MyPropertySource());
        list = Collections.unmodifiableList(list);
    }

    @Override
    public Collection<PropertySource> getPropertySources() {
        return list;
    }

    private static class MyPropertySource extends BasePropertySource {

        private Map<String, PropertyValue> properties = new HashMap<>();

        public MyPropertySource() {
            super(200);
            properties.put("name", PropertyValue.createValue("name", "Robin"));
            properties.put("name3", PropertyValue.createValue("name3", "Lukas"));
            properties.put("name4", PropertyValue.createValue("name4", "Sereina"));
            properties.put("name5", PropertyValue.createValue("name5", "Benjamin"));
            properties = Collections.unmodifiableMap(properties);
        }

        @Override
        public String getName() {
            return "final-testdata-properties";
        }

        @Override
        public Map<String, PropertyValue> getProperties() {
            return properties;
        }

    }

}
