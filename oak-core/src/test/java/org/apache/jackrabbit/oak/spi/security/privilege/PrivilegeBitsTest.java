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
package org.apache.jackrabbit.oak.spi.security.privilege;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.jackrabbit.oak.AbstractSecurityTest;
import org.apache.jackrabbit.oak.api.PropertyState;
import org.apache.jackrabbit.oak.api.Tree;
import org.apache.jackrabbit.oak.api.Type;
import org.apache.jackrabbit.oak.plugins.memory.PropertyStates;
import org.apache.jackrabbit.oak.spi.security.authorization.permission.Permissions;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PrivilegeBitsTest extends AbstractSecurityTest implements PrivilegeConstants {

    private static final long NO_PRIVILEGE = 0;
    private static final PrivilegeBits READ_NODES_PRIVILEGE_BITS = PrivilegeBits.BUILT_IN.get(REP_READ_NODES);

    private static final long[] LONGS = new long[]{1, 2, 13, 199, 512, Long.MAX_VALUE / 2, Long.MAX_VALUE - 1, Long.MAX_VALUE};

    private static long getLongValue(PrivilegeBits bits) {
        String s = bits.toString();
        if (s.indexOf('[') == -1) {
            return Long.parseLong(s.substring(15));
        } else {
            return Long.parseLong(s.substring(s.indexOf('[') + 1, s.indexOf(',')));
        }
    }

    private static PropertyState createPropertyState(long l) {
        return PropertyStates.createProperty("name", Collections.singleton(l), Type.LONGS);
    }

    private static void assertEquivalent(PrivilegeBits a, PrivilegeBits b) {
        assertEquals(a.toString(), b.toString());
    }

    @Test
    public void testLongValue() {
        // empty
        assertEquals(NO_PRIVILEGE, getLongValue(PrivilegeBits.EMPTY));

        // long based privilege bits
        for (long l : LONGS) {
            PrivilegeBits bits = PrivilegeBits.getInstance(createPropertyState(l));
            assertEquals(l, getLongValue(bits));
        }

        // long based privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        long l = getLongValue(pb);
        while (l < Long.MAX_VALUE / 2) {
            l = l << 1;
            pb = pb.nextBits();
            assertEquals(l, getLongValue(pb));
        }

        // other privilege bits: long value not available.
        for (int i = 0; i < 10; i++) {
            pb = pb.nextBits();
            assertEquals(0, getLongValue(pb));
        }

        // modifiable privilege bits
        pb = READ_NODES_PRIVILEGE_BITS;
        for (int i = 0; i < 100; i++) {
            PrivilegeBits modifiable = PrivilegeBits.getInstance(pb);
            assertEquals(getLongValue(pb), getLongValue(modifiable));
            pb = pb.nextBits();
        }
    }

    @Test
    public void testNextBits() {
        // empty
        assertSame(PrivilegeBits.EMPTY, PrivilegeBits.EMPTY.nextBits());

        // long based privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        long l = getLongValue(pb);
        while (l < Long.MAX_VALUE / 2) {
            l = l << 1;
            pb = pb.nextBits();
            assertEquals(l, getLongValue(pb));
        }

        // other privilege bits: long value not available.
        for (int i = 0; i < 10; i++) {
            PrivilegeBits nxt = pb.nextBits();
            assertEquals(nxt, pb.nextBits());
            assertFalse(pb.equals(nxt));
            pb = nxt;
        }

        // modifiable privilege bits
        pb = READ_NODES_PRIVILEGE_BITS;
        for (int i = 0; i < 100; i++) {
            PrivilegeBits modifiable = PrivilegeBits.getInstance(pb);
            try {
                modifiable.nextBits();
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }
            pb = pb.nextBits();
        }
    }

    @Test
    public void testUnmodifiable() {
        assertSame(PrivilegeBits.EMPTY, PrivilegeBits.EMPTY.unmodifiable());

        // other privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance(pb);

        for (int i = 0; i < 100; i++) {
            PrivilegeBits nxt = pb.nextBits();
            assertSame(nxt, nxt.unmodifiable());
            assertEquals(nxt, nxt.unmodifiable());

            mod.add(nxt);
            assertNotSame(mod, mod.unmodifiable());

            pb = nxt;
        }
    }

    @Test
    public void testIncludes() {
        // empty
        assertTrue(PrivilegeBits.EMPTY.includes(PrivilegeBits.EMPTY));

        // other privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance();

        for (int i = 0; i < 100; i++) {

            assertFalse(PrivilegeBits.EMPTY.includes(pb));
            assertTrue(pb.includes(PrivilegeBits.EMPTY));

            mod.add(pb);
            assertTrue(mod.includes(pb));

            PrivilegeBits nxt = pb.nextBits();
            assertTrue(nxt.includes(nxt));
            assertTrue(nxt.includes(PrivilegeBits.getInstance(nxt)));

            assertFalse(pb + " should not include " + nxt, pb.includes(nxt));
            assertFalse(nxt + " should not include " + pb, nxt.includes(pb));
            assertFalse(mod.includes(nxt));
            assertFalse(nxt.includes(mod));

            pb = nxt;
        }
    }

    @Test
    public void testIsEmpty() {
        // empty
        assertTrue(PrivilegeBits.EMPTY.isEmpty());

        // any other bits should not be empty
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance(pb);
        for (int i = 0; i < 100; i++) {
            assertFalse(pb.isEmpty());
            assertFalse(PrivilegeBits.getInstance(pb).isEmpty());

            pb = pb.nextBits();
            mod.add(pb);
            assertFalse(mod.isEmpty());

            PrivilegeBits tmp = PrivilegeBits.getInstance(pb);
            tmp.diff(pb);
            assertTrue(tmp.toString(), tmp.isEmpty());
        }
    }

    @Test
    public void testAdd() {
        // empty
        try {
            PrivilegeBits.EMPTY.add(PrivilegeBits.EMPTY);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }

        // other privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance(pb);

        for (int i = 0; i < 100; i++) {
            try {
                pb.add(PrivilegeBits.EMPTY);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            try {
                pb.add(mod);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            PrivilegeBits nxt = pb.nextBits();
            try {
                pb.add(nxt);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            long before = getLongValue(mod);
            long nxtLong = getLongValue(nxt);

            mod.add(nxt);
            if (getLongValue(nxt) != 0) {
                assertEquals(before | nxtLong, getLongValue(mod));
            }
            assertTrue(mod.includes(nxt));

            PrivilegeBits tmp = PrivilegeBits.getInstance(pb);
            assertTrue(tmp.includes(pb));
            assertFalse(tmp.includes(nxt));
            if (READ_NODES_PRIVILEGE_BITS.equals(pb)) {
                assertTrue(tmp.includes(PrivilegeBits.BUILT_IN.get(PrivilegeConstants.REP_READ_NODES)));
            } else {
                assertFalse(tmp.includes(PrivilegeBits.BUILT_IN.get(PrivilegeConstants.REP_READ_NODES)));
            }
            tmp.add(nxt);
            assertTrue(tmp.includes(pb) && tmp.includes(nxt));
            if (READ_NODES_PRIVILEGE_BITS.equals(pb)) {
                assertTrue(tmp.includes(READ_NODES_PRIVILEGE_BITS));
            } else {
                assertFalse(tmp.includes(READ_NODES_PRIVILEGE_BITS));
            }
            tmp.add(READ_NODES_PRIVILEGE_BITS);
            assertTrue(tmp.includes(READ_NODES_PRIVILEGE_BITS));

            pb = nxt;
        }
    }

    @Test
    public void testDiff() {
        // empty
        try {
            PrivilegeBits.EMPTY.diff(PrivilegeBits.EMPTY);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }

        // other privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance(pb);

        for (int i = 0; i < 100; i++) {
            PrivilegeBits nxt = pb.nextBits();
            try {
                pb.diff(nxt);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            try {
                pb.diff(mod);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            PrivilegeBits before = PrivilegeBits.getInstance(mod);
            mod.diff(nxt);
            assertEquivalent(before, mod);
            mod.add(nxt);
            assertFalse(before.equals(mod));
            mod.diff(nxt);
            assertEquivalent(before, mod);
            mod.add(nxt);

            // diff with same pb must leave original bits empty
            PrivilegeBits tmp = PrivilegeBits.getInstance(pb);
            tmp.add(nxt);
            tmp.add(READ_NODES_PRIVILEGE_BITS);
            tmp.diff(tmp);
            assertEquivalent(PrivilegeBits.EMPTY, tmp);

            tmp = PrivilegeBits.getInstance(pb);
            tmp.add(nxt);
            tmp.add(READ_NODES_PRIVILEGE_BITS);
            tmp.diff(PrivilegeBits.getInstance(tmp));
            assertEquivalent(PrivilegeBits.EMPTY, tmp);

            // diff without intersection -> leave privilege unmodified.
            tmp = PrivilegeBits.getInstance(pb);
            tmp.diff(nxt);
            assertEquivalent(PrivilegeBits.getInstance(pb), tmp);

            // diff with intersection -> privilege must be modified accordingly.
            tmp = PrivilegeBits.getInstance(nxt);
            tmp.add(READ_NODES_PRIVILEGE_BITS);
            assertTrue(tmp.includes(READ_NODES_PRIVILEGE_BITS));
            assertTrue(tmp.includes(nxt));
            tmp.diff(nxt);
            assertEquivalent(READ_NODES_PRIVILEGE_BITS, tmp);
            assertTrue(tmp.includes(READ_NODES_PRIVILEGE_BITS));
            assertFalse(tmp.includes(nxt));

            tmp = PrivilegeBits.getInstance(pb);
            tmp.add(READ_NODES_PRIVILEGE_BITS);
            PrivilegeBits tmp2 = PrivilegeBits.getInstance(pb);
            tmp2.add(nxt);
            PrivilegeBits tmp3 = PrivilegeBits.getInstance(tmp2);
            assertEquivalent(tmp2, tmp3);
            tmp.diff(tmp2);
            if (READ_NODES_PRIVILEGE_BITS.equals(pb)) {
                assertEquivalent(PrivilegeBits.EMPTY, tmp);
            } else {
                assertEquivalent(READ_NODES_PRIVILEGE_BITS, tmp);
            }
            // but pb passed to the diff call must not be modified.
            assertEquivalent(tmp3, tmp2);

            pb = nxt;
        }
    }

    @Test
    public void testAddDifference() {
        // empty
        try {
            PrivilegeBits.EMPTY.addDifference(PrivilegeBits.EMPTY, PrivilegeBits.EMPTY);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }

        // other privilege bits
        PrivilegeBits pb = READ_NODES_PRIVILEGE_BITS;
        PrivilegeBits mod = PrivilegeBits.getInstance(pb);

        for (int i = 0; i < 100; i++) {
            PrivilegeBits nxt = pb.nextBits();
            try {
                pb.addDifference(nxt, mod);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            try {
                pb.addDifference(nxt, READ_NODES_PRIVILEGE_BITS);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }

            PrivilegeBits tmp = PrivilegeBits.getInstance(mod);
            tmp.addDifference(nxt, READ_NODES_PRIVILEGE_BITS);
            mod.add(nxt);
            assertEquivalent(mod, tmp); // since there is diff(nxt, read) which results in nxt

            if (!pb.equals(READ_NODES_PRIVILEGE_BITS)) {
                tmp = PrivilegeBits.getInstance(nxt);
                PrivilegeBits mod2 = PrivilegeBits.getInstance(mod);
                tmp.addDifference(mod2, READ_NODES_PRIVILEGE_BITS);
                assertFalse(nxt.equals(tmp));  // tmp should be modified by addDifference call.
                assertEquivalent(mod2, mod);       // mod2 should not be modified here
                assertTrue(tmp.includes(pb));
                assertFalse(tmp.includes(READ_NODES_PRIVILEGE_BITS));
                assertFalse(tmp.includes(mod));
            }

            tmp = PrivilegeBits.getInstance(nxt);
            PrivilegeBits mod2 = PrivilegeBits.getInstance(mod);
            tmp.addDifference(READ_NODES_PRIVILEGE_BITS, mod2);
            assertEquivalent(nxt, tmp);  // tmp not modified by addDifference call.
            assertEquivalent(mod2, mod); // mod2 should not be modified here
            assertFalse(tmp.includes(pb));
            assertFalse(tmp.includes(READ_NODES_PRIVILEGE_BITS));
            assertFalse(tmp.includes(mod));

            tmp = PrivilegeBits.getInstance(nxt);
            tmp.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
            assertEquivalent(nxt, tmp);  // tmp not modified by addDifference call.
            assertFalse(tmp.includes(READ_NODES_PRIVILEGE_BITS));

            tmp = PrivilegeBits.getInstance(mod);
            tmp.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
            assertEquivalent(mod, tmp);  // tmp not modified by addDifference call.
            assertTrue(tmp.includes(READ_NODES_PRIVILEGE_BITS));

            pb = nxt;
        }
    }

    @Test
    public void testGetInstance() {
        PrivilegeBits pb = PrivilegeBits.getInstance();
        assertEquivalent(PrivilegeBits.EMPTY, pb);
        assertNotSame(PrivilegeBits.EMPTY, pb);
        assertNotSame(pb, pb.unmodifiable());
        pb.add(READ_NODES_PRIVILEGE_BITS);
        pb.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
        pb.diff(READ_NODES_PRIVILEGE_BITS);

        pb = PrivilegeBits.getInstance(PrivilegeBits.EMPTY);
        assertEquivalent(PrivilegeBits.EMPTY, pb);
        assertNotSame(PrivilegeBits.EMPTY, pb);
        assertNotSame(pb, pb.unmodifiable());
        pb.add(READ_NODES_PRIVILEGE_BITS);
        pb.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
        pb.diff(READ_NODES_PRIVILEGE_BITS);

        pb = PrivilegeBits.getInstance(READ_NODES_PRIVILEGE_BITS);
        assertEquivalent(READ_NODES_PRIVILEGE_BITS, pb);
        assertNotSame(READ_NODES_PRIVILEGE_BITS, pb);
        assertNotSame(pb, pb.unmodifiable());
        pb.add(READ_NODES_PRIVILEGE_BITS);
        pb.addDifference(READ_NODES_PRIVILEGE_BITS, PrivilegeBits.EMPTY);
        pb.diff(READ_NODES_PRIVILEGE_BITS);

        pb = PrivilegeBits.EMPTY;
        assertEquivalent(pb, PrivilegeBits.EMPTY);
        assertSame(pb, PrivilegeBits.EMPTY);
        assertSame(pb, pb.unmodifiable());
        try {
            pb.add(READ_NODES_PRIVILEGE_BITS);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
        try {
            pb.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
        try {
            pb.diff(READ_NODES_PRIVILEGE_BITS);
            fail("UnsupportedOperation expected");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }

    @Test
    public void testGetInstanceFromBase() {
        PrivilegeBits pb = PrivilegeBits.getInstance(READ_NODES_PRIVILEGE_BITS);
        pb.add(PrivilegeBits.BUILT_IN.get(PrivilegeConstants.JCR_READ_ACCESS_CONTROL));
        pb.add(PrivilegeBits.BUILT_IN.get(PrivilegeConstants.JCR_NODE_TYPE_MANAGEMENT));

        PrivilegeBits pb2 = PrivilegeBits.getInstance(READ_NODES_PRIVILEGE_BITS,
                PrivilegeBits.BUILT_IN.get(PrivilegeConstants.JCR_READ_ACCESS_CONTROL),
                PrivilegeBits.BUILT_IN.get(PrivilegeConstants.JCR_NODE_TYPE_MANAGEMENT));

        assertEquivalent(pb, pb2);
    }

    @Test
    public void testGetInstanceFromPropertyState() {
        for (long l : LONGS) {
            PropertyState property = createPropertyState(l);
            PrivilegeBits pb = PrivilegeBits.getInstance(property);
            assertEquivalent(pb, PrivilegeBits.getInstance(property));
            assertSame(pb, pb.unmodifiable());

            assertEquivalent(pb, PrivilegeBits.getInstance(pb));
            assertEquivalent(PrivilegeBits.getInstance(pb), pb);
            assertNotSame(pb, PrivilegeBits.getInstance(pb));

            try {
                pb.add(READ_NODES_PRIVILEGE_BITS);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }
            try {
                pb.addDifference(READ_NODES_PRIVILEGE_BITS, READ_NODES_PRIVILEGE_BITS);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }
            try {
                pb.diff(READ_NODES_PRIVILEGE_BITS);
                fail("UnsupportedOperation expected");
            } catch (UnsupportedOperationException e) {
                // success
            }
        }
    }

    @Test
    public void testGetInstanceFromTree() {
        Tree privRoot = root.getTree(PRIVILEGES_PATH);
        try {
            Tree tmp = privRoot.addChild("tmpPrivilege");
            PrivilegeBits tmpBits = PrivilegeBits.getInstance(privRoot.getProperty(REP_NEXT));
            tmpBits.writeTo(tmp);

            Map<Tree, PrivilegeBits> treeToBits = new HashMap<Tree, PrivilegeBits>();
            treeToBits.put(privRoot.getChild(JCR_READ), PrivilegeBits.BUILT_IN.get(JCR_READ));
            treeToBits.put(tmp, tmpBits);
            treeToBits.put(privRoot, tmpBits);

            for (Tree tree : treeToBits.keySet()) {
                assertEquals(treeToBits.get(tree), PrivilegeBits.getInstance(tree));
            }
        } finally {
            root.refresh();
        }
    }

    @Test
    public void testCalculatePermissions() {
        PrivilegeBitsProvider provider = new PrivilegeBitsProvider(root);

        Map<PrivilegeBits, Long> simple = new HashMap<PrivilegeBits, Long>();
        simple.put(PrivilegeBits.EMPTY, Permissions.NO_PERMISSION);
        simple.put(provider.getBits(JCR_READ), Permissions.READ);
        simple.put(provider.getBits(JCR_LOCK_MANAGEMENT), Permissions.LOCK_MANAGEMENT);
        simple.put(provider.getBits(JCR_VERSION_MANAGEMENT), Permissions.VERSION_MANAGEMENT);
        simple.put(provider.getBits(JCR_READ_ACCESS_CONTROL), Permissions.READ_ACCESS_CONTROL);
        simple.put(provider.getBits(JCR_MODIFY_ACCESS_CONTROL), Permissions.MODIFY_ACCESS_CONTROL);
        simple.put(provider.getBits(REP_READ_NODES), Permissions.READ_NODE);
        simple.put(provider.getBits(REP_READ_PROPERTIES), Permissions.READ_PROPERTY);
        simple.put(provider.getBits(REP_USER_MANAGEMENT), Permissions.USER_MANAGEMENT);
        simple.put(provider.getBits(JCR_MODIFY_PROPERTIES), Permissions.SET_PROPERTY);
        simple.put(provider.getBits(REP_ADD_PROPERTIES), Permissions.ADD_PROPERTY);
        simple.put(provider.getBits(REP_ALTER_PROPERTIES), Permissions.MODIFY_PROPERTY);
        simple.put(provider.getBits(REP_REMOVE_PROPERTIES), Permissions.REMOVE_PROPERTY);
        simple.put(provider.getBits(REP_INDEX_DEFINITION_MANAGEMENT), Permissions.INDEX_DEFINITION_MANAGEMENT);
        for (PrivilegeBits pb : simple.keySet()) {
            long expected = simple.get(pb).longValue();
            assertTrue(expected == PrivilegeBits.calculatePermissions(pb, PrivilegeBits.EMPTY, true));
            assertTrue(expected == PrivilegeBits.calculatePermissions(pb, pb, true));
        }

        // jcr:modifyProperty aggregate
        PrivilegeBits add_change = provider.getBits(REP_ADD_PROPERTIES, REP_ALTER_PROPERTIES);
        long permissions = (Permissions.ADD_PROPERTY | Permissions.MODIFY_PROPERTY);
        assertTrue(permissions == PrivilegeBits.calculatePermissions(add_change, PrivilegeBits.EMPTY, true));
        assertTrue(permissions == PrivilegeBits.calculatePermissions(add_change, add_change, true));

        PrivilegeBits add_rm = provider.getBits(REP_ADD_PROPERTIES, REP_REMOVE_PROPERTIES);
        permissions = (Permissions.ADD_PROPERTY | Permissions.REMOVE_PROPERTY);
        assertTrue(permissions == PrivilegeBits.calculatePermissions(add_rm, PrivilegeBits.EMPTY, true));
        assertTrue(permissions == PrivilegeBits.calculatePermissions(add_rm, add_rm, true));

        PrivilegeBits ch_rm = provider.getBits(REP_ALTER_PROPERTIES, REP_REMOVE_PROPERTIES);
        permissions = (Permissions.MODIFY_PROPERTY | Permissions.REMOVE_PROPERTY);
        assertTrue(permissions == PrivilegeBits.calculatePermissions(ch_rm, PrivilegeBits.EMPTY, true));
        assertTrue(permissions == PrivilegeBits.calculatePermissions(ch_rm, add_rm, true));

        // jcr:add aggregate
        PrivilegeBits all = provider.getBits(JCR_ALL);
        assertFalse(Permissions.ALL == PrivilegeBits.calculatePermissions(all, PrivilegeBits.EMPTY, true));
        assertTrue(Permissions.ALL == PrivilegeBits.calculatePermissions(all, all, true));

        // parent aware permissions
        // a) jcr:addChildNodes
        PrivilegeBits addChild = provider.getBits(JCR_ADD_CHILD_NODES);
        assertFalse(Permissions.ADD_NODE == PrivilegeBits.calculatePermissions(addChild, PrivilegeBits.EMPTY, true));
        assertTrue(Permissions.ADD_NODE == PrivilegeBits.calculatePermissions(PrivilegeBits.EMPTY, addChild, true));

        // b) jcr:removeChildNodes and jcr:removeNode
        PrivilegeBits removeChild = provider.getBits(JCR_REMOVE_CHILD_NODES);
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(removeChild, PrivilegeBits.EMPTY, true));
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(PrivilegeBits.EMPTY, removeChild, true));

        PrivilegeBits removeNode = provider.getBits(JCR_REMOVE_NODE);
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(removeNode, PrivilegeBits.EMPTY, true));
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(PrivilegeBits.EMPTY, removeNode, true));

        PrivilegeBits remove = provider.getBits(JCR_REMOVE_CHILD_NODES, JCR_REMOVE_NODE);
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(remove, PrivilegeBits.EMPTY, true));
        assertFalse(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(PrivilegeBits.EMPTY, remove, true));
        assertTrue(Permissions.REMOVE_NODE == PrivilegeBits.calculatePermissions(remove, remove, true));
    }
}