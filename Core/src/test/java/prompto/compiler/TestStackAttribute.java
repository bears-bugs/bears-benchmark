package prompto.compiler;

import static org.junit.Assert.*;
import static prompto.compiler.IVerifierEntry.VerifierType.*;

import org.junit.Test;

public class TestStackAttribute {

	@Test
	public void testEmpty() {
		StackMapTableAttribute stack = new StackMapTableAttribute();
		assertEquals(0, stack.getMaxStack());
	}

	@Test
	public void testInteger() {
		StackMapTableAttribute stack = new StackMapTableAttribute();
		stack.push(ITEM_Integer.newStackEntry(null));
		assertEquals(1, stack.getMaxStack());
	}

	@Test
	public void testLong() {
		StackMapTableAttribute stack = new StackMapTableAttribute();
		stack.push(ITEM_Long.newStackEntry(null));
		assertEquals(2, stack.getMaxStack());
	}

	@Test
	public void testPop1() {
		StackMapTableAttribute stack = new StackMapTableAttribute();
		stack.push(ITEM_Long.newStackEntry(null));
		assertEquals(ITEM_Long, stack.pop((short)1)[0].getType());
	}

	@Test
	public void testPop2() {
		StackMapTableAttribute stack = new StackMapTableAttribute();
		stack.push(ITEM_Long.newStackEntry(null));
		stack.push(ITEM_Integer.newStackEntry(null));
		IVerifierEntry[] popped = stack.pop((short)2);
		assertEquals(ITEM_Long, popped[0].getType());
		assertEquals(ITEM_Integer, popped[1].getType());
		assertEquals(3, stack.getMaxStack());
		stack.push(ITEM_Long.newStackEntry(null));
		assertEquals(3, stack.getMaxStack());
	}
}
