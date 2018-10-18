package net.bytebuddy.implementation.bytecode;

import net.bytebuddy.description.type.TypeDefinition;

import java.util.Arrays;
import java.util.Collection;

/**
 * Represents the size of a Java type on the operand stack.
 */
public enum StackSize {

    /**
     * An empty stack size.
     */
    ZERO(0),

    /**
     * A single slot stack size.
     */
    SINGLE(1),

    /**
     * A double slot stack size which is required by {@code long} and {@code double} values.
     */
    DOUBLE(2);

    /**
     * The size of the stack this instance represents.
     */
    private final int size;

    /**
     * Creates a new stack size.
     *
     * @param size The size of the stack this instance represents.
     */
    StackSize(int size) {
        this.size = size;
    }

    /**
     * Finds the operand stack size of a given Java type.
     *
     * @param type The type of interest.
     * @return The given type's operand stack size.
     */
    public static StackSize of(Class<?> type) {
        if (type == void.class) {
            return ZERO;
        } else if (type == double.class || type == long.class) {
            return DOUBLE;
        } else {
            return SINGLE;
        }
    }

    /**
     * Represents a numeric size as a {@link StackSize}.
     *
     * @param size The size to represent. Must be {@code 0}, {@code 1} or {@code 2}.
     * @return A stack size representation for the given value.
     */
    public static StackSize of(int size) {
        switch (size) {
            case 0:
                return ZERO;
            case 1:
                return SINGLE;
            case 2:
                return DOUBLE;
            default:
                throw new IllegalArgumentException("Unexpected stack size value: " + size);
        }
    }

    /**
     * Computes the stack size of all supplied types.
     *
     * @param typeDefinition The types for which to compute the size.
     * @return The total size of all types.
     */
    public static int of(TypeDefinition... typeDefinition) {
        return of(Arrays.asList(typeDefinition));
    }

    /**
     * Computes the stack size of all supplied types.
     *
     * @param typeDefinitions The types for which to compute the size.
     * @return The total size of all types.
     */
    public static int of(Collection<? extends TypeDefinition> typeDefinitions) {
        int size = 0;
        for (TypeDefinition typeDefinition : typeDefinitions) {
            size += typeDefinition.getStackSize().getSize();
        }
        return size;
    }

    /**
     * The numeric value of this stack size representation.
     *
     * @return An integer representing the operand stack size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Creates an instance of a
     * {@link StackManipulation.Size}
     * that describes a stack growth of this size.
     *
     * @return A stack size growth by the size represented by this stack size.
     */
    public StackManipulation.Size toIncreasingSize() {
        return new StackManipulation.Size(getSize(), getSize());
    }

    /**
     * Creates an instance of a
     * {@link StackManipulation.Size}
     * that describes a stack decrease of this size.
     *
     * @return A stack size decrease by the size represented by this stack size.
     */
    public StackManipulation.Size toDecreasingSize() {
        return new StackManipulation.Size(-1 * getSize(), 0);
    }

    /**
     * Determines the maximum of two stack size representations.
     *
     * @param stackSize The other stack size representation.
     * @return The maximum of this and the other stack size.
     */
    public StackSize maximum(StackSize stackSize) {
        switch (this) {
            case ZERO:
                return stackSize;
            case SINGLE:
                switch (stackSize) {
                    case DOUBLE:
                        return stackSize;
                    case SINGLE:
                    case ZERO:
                        return this;
                    default:
                        throw new AssertionError();
                }
            case DOUBLE:
                return this;
            default:
                throw new AssertionError();
        }
    }
}
