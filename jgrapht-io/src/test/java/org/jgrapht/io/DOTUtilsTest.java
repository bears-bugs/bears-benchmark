/*
 * (C) Copyright 2018, by Mariusz Smykula and Contributors.
 *
 * JGraphT : a free Java graph-theory library
 *
 * See the CONTRIBUTORS.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the
 * GNU Lesser General Public License v2.1 or later
 * which is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1-standalone.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR LGPL-2.1-or-later
 */
package org.jgrapht.io;

import org.junit.*;

import static org.junit.Assert.*;

public class DOTUtilsTest
{

    @Test
    public void shouldAcceptIdWithDigits()
    {
        String idWithDigit = "id3";
        boolean isValid = DOTUtils.isValidID(idWithDigit);
        assertTrue(isValid);
    }

    @Test
    public void shouldRejectIdThatStartsWithDigit()
    {
        String idThatStartsWithDigit = "3id";
        boolean isValid = DOTUtils.isValidID(idThatStartsWithDigit);
        assertFalse(isValid);
    }

    @Test
    public void shouldAcceptIdThatStartWithUnderscore()
    {
        String idThatStartsWithUnderscore = "_id3";
        boolean isValid = DOTUtils.isValidID(idThatStartsWithUnderscore);
        assertTrue(isValid);
    }
}
