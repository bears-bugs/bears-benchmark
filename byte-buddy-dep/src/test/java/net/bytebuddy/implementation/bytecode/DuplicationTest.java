package net.bytebuddy.implementation.bytecode;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.test.utility.MockitoRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class DuplicationTest {

    private final StackSize stackSize;

    private final int opcode;

    @Rule
    public TestRule mockitoRule = new MockitoRule(this);

    @Mock
    private TypeDefinition typeDefinition;

    @Mock
    private MethodVisitor methodVisitor;

    @Mock
    private Implementation.Context implementationContext;

    public DuplicationTest(StackSize stackSize, int opcode) {
        this.stackSize = stackSize;
        this.opcode = opcode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {StackSize.ZERO, Opcodes.NOP},
                {StackSize.SINGLE, Opcodes.DUP},
                {StackSize.DOUBLE, Opcodes.DUP2}
        });
    }

    @Before
    public void setUp() throws Exception {
        when(typeDefinition.getStackSize()).thenReturn(stackSize);
    }

    @After
    public void tearDown() throws Exception {
        verifyZeroInteractions(implementationContext);
    }

    @Test
    public void testDuplication() throws Exception {
        StackManipulation stackManipulation = Duplication.of(typeDefinition);
        assertThat(stackManipulation.isValid(), is(true));
        StackManipulation.Size size = stackManipulation.apply(methodVisitor, implementationContext);
        assertThat(size.getSizeImpact(), is(stackSize.getSize()));
        assertThat(size.getMaximalSize(), is(stackSize.getSize()));
        if (stackSize != StackSize.ZERO) {
            verify(methodVisitor).visitInsn(opcode);
        }
        verifyNoMoreInteractions(methodVisitor);
    }
}
