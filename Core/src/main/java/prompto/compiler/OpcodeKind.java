/*
 * Copyright (c) 2009, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

/* copied from com.sun.tools.classfile.Instruction.Kind */

package prompto.compiler;


/** The kind of an instruction, as determined by the position, size and
 *  types of its operands. */
public enum OpcodeKind {
    /** Opcode is not followed by any operands. */
    NO_OPERANDS(1),
    /** Opcode is followed by a byte indicating a type. */
    ATYPE(2),
    /** Opcode is followed by a 2-byte branch offset. */
    BRANCH(3),
    /** Opcode is followed by a 4-byte branch offset. */
    BRANCH_W(5),
    /** Opcode is followed by a signed byte value. */
    BYTE(2),
    /** Opcode is followed by a 1-byte index into the constant pool. */
    CPREF(2),
    /** Opcode is followed by a 2-byte index into the constant pool. */
    CPREF_W(3),
    /** Opcode is followed by a 2-byte index into the constant pool,
     *  an unsigned byte value. */
    CPREF_W_UBYTE(4),
    /** Opcode is followed by a 2-byte index into the constant pool.,
     *  an unsigned byte value, and a zero byte. */
    CPREF_W_UBYTE_ZERO(5),
    /** Opcode is followed by variable number of operands, depending
     * on the instruction.*/
    DYNAMIC(-1),
    /** Opcode is followed by a 1-byte reference to a local variable. */
    LOCAL(2),
    /** Opcode is followed by a 1-byte reference to a local variable,
     *  and a signed byte value. */
    LOCAL_BYTE(3),
    /** Opcode is followed by a signed short value. */
    SHORT(3),
    /** Wide opcode is not followed by any operands. */
    WIDE_NO_OPERANDS(2, 2),
    /** Wide opcode is followed by a 2-byte index into the constant pool. */
    WIDE_CPREF_W(2, 4),
    /** Wide opcode is followed by a 2-byte index into the constant pool,
     *  and a signed short value. */
    WIDE_CPREF_W_SHORT(2, 6),
    /** Opcode was not recognized. */
    UNKNOWN(1);

    public final int width;  // opcode length	
    public final int length; // full length in bytes, or -1 if it depends on the instruction

    OpcodeKind(int length) {
        this(1, length);
    }
    
    OpcodeKind(int width, int length) {
    	this.width = width;
        this.length = length;
    }

};