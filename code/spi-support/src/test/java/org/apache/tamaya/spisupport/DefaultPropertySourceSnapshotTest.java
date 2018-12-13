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
package org.apache.tamaya.spisupport;

import org.apache.tamaya.spi.PropertySource;
import org.apache.tamaya.spi.PropertyValue;
import org.apache.tamaya.spisupport.propertysource.SystemPropertySource;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests for {@link DefaultPropertySourceSnapshot}.
 */
public class DefaultPropertySourceSnapshotTest {

    private static final PropertySource myPS = new SystemPropertySource();

    @Test
    public void testOf() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        assertNotNull(ps);
    }

    @Test
    public void testGetName() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        String name = ps.getName();
        assertNotNull(name);
        assertEquals(name, ps.getName());
    }

    @Test
    public void testGetOrdinal() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        assertEquals(PropertySourceComparator.getOrdinal(myPS),
                PropertySourceComparator.getOrdinal(ps));
    }

    @Test
    public void testGet() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        assertNotNull(ps);
        for (Map.Entry<String, PropertyValue> e : myPS.getProperties().entrySet()) {
            assertEquals(ps.get(e.getKey()).getValue(), e.getValue().getValue());
        }
    }

    @Test
    public void testGetProperties() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        assertNotNull(ps);
        assertNotNull(ps.getProperties());
        assertFalse(ps.getProperties().isEmpty());
        for(Map.Entry en:myPS.getProperties().entrySet()){
            assertEquals(en.getValue(), ps.get((String)en.getKey()));
        }
    }

    @Test
    public void testEquals() throws Exception {
        PropertySource ps1 = DefaultPropertySourceSnapshot.of(myPS);
        PropertySource ps2 = DefaultPropertySourceSnapshot.of(myPS);
        assertEquals(ps1.getName(), ps2.getName());
        assertEquals(ps1.getProperties().size(), ps2.getProperties().size());
    }

    @Test
    public void testHashCode() throws Exception {
        boolean alwaysDifferent = true;
        for(int i=0;i<10;i++){
            PropertySource ps1 = DefaultPropertySourceSnapshot.of(myPS);
            PropertySource ps2 = DefaultPropertySourceSnapshot.of(myPS);
            // sometimes not same, because frozenAt in ms maybe different
            if(ps1.hashCode()==ps2.hashCode()){
                alwaysDifferent=false;
                break;
            }
        }
        if(alwaysDifferent){
            fail("HashCode should be same if frozenAt is in the same ms...");
        }
    }

    @Test
    public void testToString() throws Exception {
        PropertySource ps = DefaultPropertySourceSnapshot.of(myPS);
        String toString = ps.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("FrozenPropertySource"));
        assertTrue(toString.contains(myPS.getName()));
    }
}