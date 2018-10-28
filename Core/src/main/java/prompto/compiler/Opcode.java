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

/* copied from com.sun.tools.classfile */
package prompto.compiler;

import static prompto.compiler.OpcodeKind.*;

import java.util.function.Function;

import prompto.compiler.IVerifierEntry.VerifierType;
import static prompto.compiler.IVerifierEntry.VerifierType.*;

/**
 * See JVMS, chapter 6.
 *
 * <p>In addition to providing all the standard opcodes defined in JVMS,
 * this class also provides legacy support for the PicoJava extensions.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public enum Opcode {
	ILLEGAL(0xFF, UNKNOWN, popsNone(), pushesNone()),
    NOP(0x0, popsNone(), pushesNone()),
    ACONST_NULL(0x1, popsNone(), pushes(ITEM_Null)), 
    ICONST_M1(0x2, popsNone(), pushes(ITEM_Integer)),
    ICONST_0(0x3, popsNone(), pushes(ITEM_Integer)),
    ICONST_1(0x4, popsNone(), pushes(ITEM_Integer)),
    ICONST_2(0x5, popsNone(), pushes(ITEM_Integer)),
    ICONST_3(0x6, popsNone(), pushes(ITEM_Integer)),
    ICONST_4(0x7, popsNone(), pushes(ITEM_Integer)),
    ICONST_5(0x8, popsNone(), pushes(ITEM_Integer)),
    LCONST_0(0x9, popsNone(), pushes(ITEM_Long)),
    LCONST_1(0xa, popsNone(), pushes(ITEM_Long)) /*,
    FCONST_0(0xb),
    FCONST_1(0xc),
    FCONST_2(0xd)*/,
    DCONST_0(0xe, popsNone(), pushes(ITEM_Double)),
    DCONST_1(0xf, popsNone(), pushes(ITEM_Double)),
    BIPUSH(0x10, BYTE, popsNone(), pushes(ITEM_Integer)),
    SIPUSH(0x11, SHORT, popsNone(), pushes(ITEM_Integer)),
    LDC(0x12, CPREF, popsNone(), pushesConstant()),
    LDC_W(0x13, CPREF_W, popsNone(), pushesConstant()), 
    LDC2_W(0x14, CPREF_W, popsNone(), pushesConstant()),
    ILOAD(0x15, LOCAL, popsNone(), pushes(ITEM_Integer)),
    LLOAD(0x16, LOCAL, popsNone(), pushes(ITEM_Long)),
    FLOAD(0x17, LOCAL, popsNone(), pushes(ITEM_Float)),
    DLOAD(0x18, LOCAL, popsNone(), pushes(ITEM_Double)),
    ALOAD(0x19, LOCAL, popsNone(), pushesObject()),
    ILOAD_0(0x1a, popsNone(), pushes(ITEM_Integer)),
    ILOAD_1(0x1b, popsNone(), pushes(ITEM_Integer)),
    ILOAD_2(0x1c, popsNone(), pushes(ITEM_Integer)),
    ILOAD_3(0x1d, popsNone(), pushes(ITEM_Integer)),
    LLOAD_0(0x1e, popsNone(), pushes(ITEM_Long)),
    LLOAD_1(0x1f, popsNone(), pushes(ITEM_Long)),
    LLOAD_2(0x20, popsNone(), pushes(ITEM_Long)),
    LLOAD_3(0x21, popsNone(), pushes(ITEM_Long))/*,
    FLOAD_0(0x22),
    FLOAD_1(0x23),
    FLOAD_2(0x24),
    FLOAD_3(0x25),
    DLOAD_0(0x26),
    DLOAD_1(0x27),
    DLOAD_2(0x28),
    DLOAD_3(0x29) */,
    ALOAD_0(0x2a, popsNone(), pushesObject()),
    ALOAD_1(0x2b, popsNone(), pushesObject()),
    ALOAD_2(0x2c, popsNone(), pushesObject()),
    ALOAD_3(0x2d, popsNone(), pushesObject()) /*,
    IALOAD(0x2e),
    LALOAD(0x2f),
    FALOAD(0x30),
    DALOAD(0x31)*/,
    AALOAD(0x32, pops(2), pushesObject())/*,
    BALOAD(0x33),
    CALOAD(0x34),
    SALOAD(0x35) */,
    ISTORE(0x36, LOCAL, pops(1), pushesNone()),
    LSTORE(0x37, LOCAL, pops(1), pushesNone()),
    FSTORE(0x38, LOCAL, pops(1), pushesNone()),
    DSTORE(0x39, LOCAL, pops(1), pushesNone()),
    ASTORE(0x3a, LOCAL, pops(1), pushesNone()),
    ISTORE_0(0x3b, pops(1), pushesNone()),
    ISTORE_1(0x3c, pops(1), pushesNone()),
    ISTORE_2(0x3d, pops(1), pushesNone()),
    ISTORE_3(0x3e, pops(1), pushesNone())/*,
    LSTORE_0(0x3f),
    LSTORE_1(0x40),
    LSTORE_2(0x41),
    LSTORE_3(0x42),
    FSTORE_0(0x43),
    FSTORE_1(0x44),
    FSTORE_2(0x45),
    FSTORE_3(0x46),
    DSTORE_0(0x47),
    DSTORE_1(0x48),
    DSTORE_2(0x49),
    DSTORE_3(0x4a) */,
    ASTORE_0(0x4b, pops(1), pushesNone()),
    ASTORE_1(0x4c, pops(1), pushesNone()),
    ASTORE_2(0x4d, pops(1), pushesNone()),
    ASTORE_3(0x4e, pops(1), pushesNone()) /*,
    IASTORE(0x4f),
    LASTORE(0x50),
    FASTORE(0x51),
    DASTORE(0x52)*/,
    AASTORE(0x53, pops(3), pushesNone())/*,
    BASTORE(0x54),
    CASTORE(0x55),
    SASTORE(0x56) */,
    POP(0x57, pops(1), pushesNone()),
    POP2(0x58, pops(2), pushesNone()),
    DUP(0x59, pops(1), pushesDuplicate()),
    DUP_X1(0x5a, pops(2), pushesDuplicateDown(1)),
    DUP_X2(0x5b, pops(3), pushesDuplicateDown(2)),
    DUP2(0x5c, pops(2), pushesDuplicateDown(1))/*,
    DUP2_X1(0x5d),
    DUP2_X2(0x5e)*/,
    SWAP(0x5f, pops(2), pushesSwapped()),
    IADD(0x60, pops(2), pushes(ITEM_Integer)),
    LADD(0x61, pops(2), pushes(ITEM_Long))/*,
    FADD(0x62)*/,
    DADD(0x63, pops(2), pushes(ITEM_Double)),
    ISUB(0x64, pops(2), pushes(ITEM_Integer)),
    LSUB(0x65, pops(2), pushes(ITEM_Long))/*,
    FSUB(0x66)*/,
    DSUB(0x67, pops(2), pushes(ITEM_Double))/*,
    IMUL(0x68)*/,
    LMUL(0x69, pops(2), pushes(ITEM_Long))/*,
    FMUL(0x6a)*/,
    DMUL(0x6b, pops(2), pushes(ITEM_Double))/*,
    IDIV(0x6c)*/,
    LDIV(0x6d, pops(2), pushes(ITEM_Long))/*,
    FDIV(0x6e)*/,
    DDIV(0x6f, pops(2), pushes(ITEM_Double))/*,
    IREM(0x70)*/,
    LREM(0x71, pops(2), pushes(ITEM_Long))/*,
    FREM(0x72)*/,
    DREM(0x73, pops(2), pushes(ITEM_Double)),
    INEG(0x74, pops(1), pushes(ITEM_Integer)),
    LNEG(0x75, pops(1), pushes(ITEM_Long))/*,
    FNEG(0x76)*/,
    DNEG(0x77, pops(1), pushes(ITEM_Double))/*,
    ISHL(0x78),
    LSHL(0x79),
    ISHR(0x7a),
    LSHR(0x7b),
    IUSHR(0x7c),
    LUSHR(0x7d)*/,
    IAND(0x7e, pops(2), pushes(ITEM_Integer))/*,
    LAND(0x7f),
    IOR(0x80),
    LOR(0x81),
    IXOR(0x82),
    LXOR(0x83)*/,
    IINC(0x84, LOCAL_BYTE, popsNone(), pushesNone()),
    I2L(0x85, pops(1), pushes(ITEM_Long))/*,
    I2F(0x86),
    I2D(0x87)*/,
    L2I(0x88, pops(1), pushes(ITEM_Integer))/*,
    L2F(0x89)*/,
    L2D(0x8a, pops(1), pushes(ITEM_Double))/*,
    F2I(0x8b),
    F2L(0x8c)*/,
    F2D(0x8d, pops(1), pushes(ITEM_Double))/*,
    D2I(0x8e) */,
    D2L(0x8f, pops(1), pushes(ITEM_Long)) /*,
    D2F(0x90),
    I2B(0x91)*/,
    I2C(0x92, pops(1), pushes(ITEM_Integer))/*,
    I2S(0x93)*/,
    LCMP(0x94, pops(2), pushes(ITEM_Integer))/*,
    FCMPL(0x95),
    FCMPG(0x96),
    DCMPL(0x97)*/,
    DCMPG(0x98, pops(2), pushes(ITEM_Integer)),
    IFEQ(0x99, BRANCH, pops(1), pushesNone()),
    IFNE(0x9a, BRANCH, pops(1), pushesNone()),
    IFLT(0x9b, BRANCH, pops(1), pushesNone()),
    IFGE(0x9c, BRANCH, pops(1), pushesNone()),
    IFGT(0x9d, BRANCH, pops(1), pushesNone()),
    IFLE(0x9e, BRANCH, pops(1), pushesNone()),
    IF_ICMPEQ(0x9f, BRANCH, pops(2), pushesNone()),
    IF_ICMPNE(0xa0, BRANCH, pops(2), pushesNone()),
    IF_ICMPLT(0xa1, BRANCH, pops(2), pushesNone()),
    IF_ICMPGE(0xa2, BRANCH, pops(2), pushesNone()),
    IF_ICMPGT(0xa3, BRANCH, pops(2), pushesNone()),
    IF_ICMPLE(0xa4, BRANCH, pops(2), pushesNone()),
    IF_ACMPEQ(0xa5, BRANCH, pops(2), pushesNone()),
    IF_ACMPNE(0xa6, BRANCH, pops(2), pushesNone()),
    GOTO(0xa7, BRANCH, popsNone(), pushesNone())/*,
    JSR(0xa8, BRANCH),
    RET(0xa9, LOCAL),
    TABLESWITCH(0xaa, DYNAMIC),
    LOOKUPSWITCH(0xab, DYNAMIC)*/,
    IRETURN(0xac, pops(1), pushesNone()),
    LRETURN(0xad, pops(1), pushesNone()),
    FRETURN(0xae, pops(1), pushesNone()),
    DRETURN(0xaf, pops(1), pushesNone()),
    ARETURN(0xb0, pops(1), pushesNone()),
    RETURN(0xb1, popsNone(), pushesNone()),
    GETSTATIC(0xb2, CPREF_W, popsNone(), pushesField()), 
    PUTSTATIC(0xb3, CPREF_W, pops(1), pushesNone()),
    GETFIELD(0xb4, CPREF_W, pops(1), pushesField()), 
    PUTFIELD(0xb5, CPREF_W, pops(2), pushesNone()),
    INVOKEVIRTUAL(0xb6, CPREF_W, popsArguments(false), pushesResult()),
    INVOKESPECIAL(0xb7, CPREF_W, popsArguments(false), pushesResult()),
    INVOKESTATIC(0xb8, CPREF_W, popsArguments(true), pushesResult()),
    INVOKEINTERFACE(0xb9, CPREF_W_UBYTE_ZERO, popsArguments(false), pushesResult()),
    INVOKEDYNAMIC(0xba, CPREF_W_UBYTE_ZERO, popsArguments(true), pushesResult()),
    NEW(0xbb, CPREF_W, popsNone(), pushesObject())/*,
    NEWARRAY(0xbc, ATYPE)*/,
    ANEWARRAY(0xbd, CPREF_W, pops(1), pushesArray())/*,
    ARRAYLENGTH(0xbe)*/,
    ATHROW(0xbf, pops(1), pushesNone()), // TODO to be refined
    CHECKCAST(0xc0, CPREF_W, pops(1), pushesObject())/*,
    INSTANCEOF(0xc1, CPREF_W)*/,
    MONITORENTER(0xc2, pops(1), pushesNone()),
    MONITOREXIT(0xc3, pops(1), pushesNone())/*,
    // wide 0xc4
    MULTIANEWARRAY(0xc5, CPREF_W_UBYTE)*/,
    IFNULL(0xc6, BRANCH, pops(1), pushesNone()),
    IFNONNULL(0xc7, BRANCH, pops(1), pushesNone())/*,
    GOTO_W(0xc8, BRANCH_W),
    JSR_W(0xc9, BRANCH_W),

    // wide opcodes
    ILOAD_W(0xc415, WIDE_CPREF_W),
    LLOAD_W(0xc416, WIDE_CPREF_W),
    FLOAD_W(0xc417, WIDE_CPREF_W),
    DLOAD_W(0xc418, WIDE_CPREF_W),
    ALOAD_W(0xc419, WIDE_CPREF_W),
    ISTORE_W(0xc436, WIDE_CPREF_W),
    LSTORE_W(0xc437, WIDE_CPREF_W),
    FSTORE_W(0xc438, WIDE_CPREF_W),
    DSTORE_W(0xc439, WIDE_CPREF_W),
    ASTORE_W(0xc43a, WIDE_CPREF_W),
    IINC_W(0xc484, WIDE_CPREF_W_SHORT),
    RET_W(0xc4a9, WIDE_CPREF_W) */;

    static interface Popper extends Function<Instruction, Short> {
    }

    static Popper popsNone() {
    	return new Popper() {
    		public Short apply(Instruction i) {
    			return 0;
    		}
    	};
    }
 
    static Popper pops(int count) {
    	return new Popper() {
    		public Short apply(Instruction i) {
    			return (short)count;
    		}
    	};
    }

    static Popper popsArguments(boolean isStatic) {
    	return new Popper() {
    		public Short apply(Instruction i) {
    			return i.getArgumentsCount(isStatic);
    		}
    	};
    }
    
    static interface Pusher {
    	StackEntry[] apply(Instruction i, StackEntry[] popped);
    }
 
    static Pusher pushesNone() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			return new StackEntry[0];
    		}
    	};
    }
    
    static Pusher pushesDuplicate() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
				return new StackEntry[] { popped[0], popped[0] };
    		}
    	};
    }
    
    static Pusher pushesDuplicateDown(int downBy) {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			switch(downBy) {
    				case 1:
        				return new StackEntry[] { popped[1], popped[0], popped[1] };
    				case 2:
        				return new StackEntry[] { popped[2], popped[0], popped[1], popped[2] };
    				default:
    					throw new UnsupportedOperationException();
    			}
    		}
    	};
    }
    
    static Pusher pushesSwapped() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
				return new StackEntry[] { popped[1], popped[0] };
    		}
    	};
    }
    
    static Pusher pushesConstant() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			StackEntry e = i.getConstantStackEntry() ;
    			return e==null ? new StackEntry[0] : new StackEntry[] { e };
    		}
    	};
    }
    
    static Pusher pushesField() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			StackEntry e = i.getFieldStackEntry() ;
    			return e==null ? new StackEntry[0] : new StackEntry[] { e };
    		}
    	};
    }

    static Pusher pushesResult() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			StackEntry e = i.getMethodResultStackEntry() ;
    			return e==null ? new StackEntry[0] : new StackEntry[] { e };
    		}
    	};
    }

    static Pusher pushesObject() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			return new StackEntry[] { ITEM_Object.newStackEntry(i.getClassConstant()) };
    		}
    	};
    }

    static Pusher pushesArray() {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			return new StackEntry[] { ITEM_Object.newStackEntry(i.getClassConstant().toArray()) };
    		}
    	};
    }
    
    static Pusher pushes(VerifierType e) {
    	return new Pusher() {
    		@Override public StackEntry[] apply(Instruction i, StackEntry[] popped) {
    			return new StackEntry[] { e.newStackEntry(null) };
    		}
    	};
    }

 

    public final int opcode;
    public final OpcodeKind kind;
    public final Popper popper;
    public final Pusher pusher;
    
    Opcode(int opcode) {
        this(opcode, NO_OPERANDS, popsNone(), pushesNone());
    }

    Opcode(int opcode, Popper popper, Pusher pusher) {
    	this(opcode, NO_OPERANDS, popper, pusher);
    }

    Opcode(int opcode, OpcodeKind kind, Popper popper, Pusher pusher) {
        this.opcode = opcode;
        this.kind = kind;
        this.popper = popper;
        this.pusher = pusher;
    }
    
    public short getPopped(Instruction i) {
    	return popper.apply(i);
    }
    
	public StackEntry[] getPushed(Instruction instruction, StackEntry[] popped) {
		return pusher.apply(instruction, popped);
	}

    
    public static Opcode get(byte opcode) {
        return stdOpcodes[opcode & 0xFF];
    }

    public static Opcode get(byte opcodePrefix, byte opcode) {
        Opcode[] block = getOpcodeBlock(opcodePrefix);
        return (block == null ? null : block[opcode]);
    }

    private static Opcode[] getOpcodeBlock(int opcodePrefix) {
        switch (opcodePrefix) {
            case 0:
                return stdOpcodes;
            case WIDE:
                return wideOpcodes;
            default:
                return null;
        }

    }

    private static Opcode[] stdOpcodes = new Opcode[256];
    private static Opcode[] wideOpcodes = new Opcode[256];

    static {
        for (Opcode o: values())
            getOpcodeBlock(o.opcode >> 8)[o.opcode & 0xff] = o;
    }

    public static final int WIDE = 0xc4;


}
