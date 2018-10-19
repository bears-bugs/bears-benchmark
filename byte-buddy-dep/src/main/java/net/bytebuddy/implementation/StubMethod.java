package net.bytebuddy.implementation;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.InstrumentedType;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.constant.DefaultValue;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import org.objectweb.asm.MethodVisitor;

/**
 * This implementation creates a method stub which does nothing but returning the default value of the return
 * type of the method. These default values are:
 * <ol>
 * <li>The value {@code 0} for all numeric type.</li>
 * <li>The null character for the {@code char} type.</li>
 * <li>{@code false} for the {@code boolean} type.</li>
 * <li>Nothing for {@code void} types.</li>
 * <li>A {@code null} reference for any reference types. Note that this includes primitive wrapper types.</li>
 * </ol>
 */
public enum StubMethod implements Implementation.Composable, ByteCodeAppender {

    /**
     * The singleton instance.
     */
    INSTANCE;

    /**
     * {@inheritDoc}
     */
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    /**
     * {@inheritDoc}
     */
    public ByteCodeAppender appender(Target implementationTarget) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Implementation andThen(Implementation implementation) {
        return implementation;
    }

    /**
     * {@inheritDoc}
     */
    public Composable andThen(Composable implementation) {
        return implementation;
    }

    /**
     * {@inheritDoc}
     */
    public Size apply(MethodVisitor methodVisitor,
                      Context implementationContext,
                      MethodDescription instrumentedMethod) {
        StackManipulation.Size stackSize = new StackManipulation.Compound(
                DefaultValue.of(instrumentedMethod.getReturnType()),
                MethodReturn.of(instrumentedMethod.getReturnType())
        ).apply(methodVisitor, implementationContext);
        return new Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
    }
}
