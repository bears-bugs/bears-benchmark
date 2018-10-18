package net.bytebuddy.dynamic.scaffold;

import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class TypeWriterDefaultForInliningInitializationHandlerAppendingFrameWriterNoOpTest {

    @Test
    public void testFrame() throws Exception {
        TypeWriter.Default.ForInlining.InitializationHandler.Appending.FrameWriter.NoOp.INSTANCE.onFrame(0, 0);
        MethodVisitor methodVisitor = mock(MethodVisitor.class);
        TypeWriter.Default.ForInlining.InitializationHandler.Appending.FrameWriter.NoOp.INSTANCE.emitFrame(methodVisitor);
        verifyZeroInteractions(methodVisitor);
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(TypeWriter.Default.ForInlining.InitializationHandler.Appending.FrameWriter.NoOp.class).apply();
    }
}
