/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.spi.security.authorization.accesscontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.security.Privilege;

import org.apache.jackrabbit.oak.namepath.GlobalNameMapper;
import org.apache.jackrabbit.oak.namepath.LocalNameMapper;
import org.apache.jackrabbit.oak.namepath.NameMapper;
import org.apache.jackrabbit.oak.namepath.NamePathMapper;
import org.apache.jackrabbit.oak.namepath.NamePathMapperImpl;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionDefinition;
import org.apache.jackrabbit.oak.spi.security.authorization.restriction.RestrictionProvider;
import org.apache.jackrabbit.oak.spi.security.principal.PrincipalImpl;
import org.apache.jackrabbit.oak.spi.security.privilege.PrivilegeConstants;
import org.junit.Test;

import com.google.common.collect.Lists;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for functionality provided by {@link AbstractAccessControlList}.
 */
public abstract class AbstractAccessControlListTest extends AbstractAccessControlTest {

    protected String getTestPath() {
        return "/testPath";
    }

    protected AbstractAccessControlList createEmptyACL() {
        return createACL(getTestPath(), Collections.<ACE>emptyList(), namePathMapper);
    }

    protected AbstractAccessControlList createACL(@Nonnull ACE... entries) {
        return createACL(getTestPath(), Lists.newArrayList(entries), namePathMapper);
    }

    protected AbstractAccessControlList createACL(@Nonnull List<ACE> entries) {
        return createACL(getTestPath(), entries, namePathMapper);
    }

    protected AbstractAccessControlList createACL(@Nullable String jcrPath,
                                                  @Nonnull ACE... entries) {
        return createACL(jcrPath, Lists.newArrayList(entries), namePathMapper);
    }

    protected AbstractAccessControlList createACL(@Nullable String jcrPath,
                                                           @Nonnull List<ACE> entries,
                                                           @Nonnull NamePathMapper namePathMapper) {
        return createACL(jcrPath, entries, namePathMapper, getRestrictionProvider());
    }

    protected abstract AbstractAccessControlList createACL(@Nullable String jcrPath,
                                                           @Nonnull List<ACE> entries,
                                                           @Nonnull NamePathMapper namePathMapper,
                                                           @Nonnull RestrictionProvider restrictionProvider);

    protected List<ACE> createTestEntries() throws RepositoryException {
        List<ACE> entries = new ArrayList<ACE>(3);
        for (int i = 0; i < 3; i++) {
            entries.add(createEntry(
                    new PrincipalImpl("testPrincipal" + i), true, null, PrivilegeConstants.JCR_READ));
        }
        return entries;
    }

    protected static Privilege[] getAggregatedPrivileges(Privilege... privileges) {
        Set<Privilege> aggr = new HashSet<Privilege>();
        for (Privilege p : privileges) {
            if (p.isAggregate()) {
                aggr.addAll(Arrays.asList(p.getAggregatePrivileges()));
            } else {
                aggr.add(p);
            }
        }
        return aggr.toArray(new Privilege[aggr.size()]);
    }

    @Test
    public void testGetPath() {
        NameMapper nameMapper = new GlobalNameMapper(
                Collections.singletonMap("jr", "http://jackrabbit.apache.org"));
        NamePathMapper npMapper = new NamePathMapperImpl(nameMapper);

        // map of jcr-path to standard jcr-path
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(null, null);
        paths.put(getTestPath(), getTestPath());
        paths.put("/", "/");
        paths.put("/jr:testPath", "/jr:testPath");
        paths.put("/{http://jackrabbit.apache.org}testPath", "/jr:testPath");

        for (String path : paths.keySet()) {
            AbstractAccessControlList acl = createACL(path, Collections.<ACE>emptyList(), npMapper);
            assertEquals(paths.get(path), acl.getPath());
        }
    }

    @Test
    public void testGetOakPath() {
        NamePathMapper npMapper = new NamePathMapperImpl(new LocalNameMapper(
                singletonMap("oak", "http://jackrabbit.apache.org"),
                singletonMap("jcr", "http://jackrabbit.apache.org")));
        // map of jcr-path to oak path
        Map<String, String> paths = new HashMap<String, String>();
        paths.put(null, null);
        paths.put(getTestPath(), getTestPath());
        paths.put("/", "/");
        String oakPath = "/oak:testPath";
        String jcrPath = "/jcr:testPath";
        paths.put(jcrPath, oakPath);
        jcrPath = "/{http://jackrabbit.apache.org}testPath";
        paths.put(jcrPath, oakPath);

        // test if oak-path is properly set.
        for (String path : paths.keySet()) {
            AbstractAccessControlList acl = createACL(path, Collections.<ACE>emptyList(), npMapper);
            assertEquals(paths.get(path), acl.getOakPath());
        }
    }

    @Test
    public void testEmptyAcl() throws RepositoryException {
        AbstractAccessControlList acl = createEmptyACL();

        assertNotNull(acl.getAccessControlEntries());
        assertNotNull(acl.getEntries());

        assertTrue(acl.getAccessControlEntries().length == 0);
        assertEquals(acl.getAccessControlEntries().length, acl.getEntries().size());
        assertEquals(0, acl.size());
        assertTrue(acl.isEmpty());
    }

    @Test
    public void testSize() throws RepositoryException {
        AbstractAccessControlList acl = createACL(createTestEntries());
        assertEquals(3, acl.size());
    }

    @Test
    public void testIsEmpty() throws RepositoryException {
        AbstractAccessControlList acl = createACL(createTestEntries());
        assertFalse(acl.isEmpty());
    }

    @Test
    public void testGetEntries() throws RepositoryException {
        List<ACE> aces = createTestEntries();
        AbstractAccessControlList acl = createACL(aces);

        assertNotNull(acl.getEntries());
        assertNotNull(acl.getAccessControlEntries());

        assertEquals(aces.size(), acl.getEntries().size());
        assertEquals(aces.size(), acl.getAccessControlEntries().length);
        assertTrue(acl.getEntries().containsAll(aces));
        assertTrue(Arrays.asList(acl.getAccessControlEntries()).containsAll(aces));
    }

    @Test
    public void testGetRestrictionNames() throws RepositoryException {
        AbstractAccessControlList acl = createEmptyACL();

        String[] restrNames = acl.getRestrictionNames();
        assertNotNull(restrNames);
        List<String> names = Lists.newArrayList(restrNames);
        for (RestrictionDefinition def : getRestrictionProvider().getSupportedRestrictions(getTestPath())) {
            assertTrue(names.remove(namePathMapper.getJcrName(def.getName())));
        }
        assertTrue(names.isEmpty());
    }

    @Test
    public void testGetRestrictionType() throws RepositoryException {
        AbstractAccessControlList acl = createEmptyACL();
        for (RestrictionDefinition def : getRestrictionProvider().getSupportedRestrictions(getTestPath())) {
            int reqType = acl.getRestrictionType(namePathMapper.getJcrName(def.getName()));

            assertTrue(reqType > PropertyType.UNDEFINED);
            assertEquals(def.getRequiredType().tag(), reqType);
        }
    }

    @Test
    public void testGetRestrictionTypeForUnknownName() throws RepositoryException {
        AbstractAccessControlList acl = createEmptyACL();
        // for backwards compatibility getRestrictionType(String) must return
        // UNDEFINED for a unknown restriction name:
        assertEquals(PropertyType.UNDEFINED, acl.getRestrictionType("unknownRestrictionName"));
    }
}