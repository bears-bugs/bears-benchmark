package net.bytebuddy.description;

import net.bytebuddy.description.modifier.*;
import org.objectweb.asm.Opcodes;

/**
 * Implementations of this interface can be described in terms of a Java modifier.
 */
public interface ModifierReviewable {

    /**
     * Representation of the default modifier.
     */
    int EMPTY_MASK = 0;

    /**
     * Returns the modifier that is described by this object.
     *
     * @return The modifier that is described by this object.
     */
    int getModifiers();

    /**
     * Specifies if the modifier described by this object is {@code final}.
     *
     * @return {@code true} if the modifier described by this object is {@code final}.
     */
    boolean isFinal();

    /**
     * Specifies if the modifier described by this object is synthetic.
     *
     * @return {@code true} if the modifier described by this object is synthetic.
     */
    boolean isSynthetic();

    /**
     * Returns this objects synthetic state.
     *
     * @return This objects synthetic state.
     */
    SyntheticState getSyntheticState();

    /**
     * A modifier reviewable for a {@link ByteCodeElement}, i.e. a type, a field or a method.
     */
    interface OfByteCodeElement extends ModifierReviewable {

        /**
         * Specifies if the modifier described by this object is {@code public}.
         *
         * @return {@code true} if the modifier described by this object is {@code public}.
         */
        boolean isPublic();

        /**
         * Specifies if the modifier described by this object is {@code protected}.
         *
         * @return {@code true} if the modifier described by this object is {@code protected}.
         */
        boolean isProtected();

        /**
         * Specifies if the modifier described by this object is package private.
         *
         * @return {@code true} if the modifier described by this object is package private.
         */
        boolean isPackagePrivate();

        /**
         * Specifies if the modifier described by this object is {@code private}.
         *
         * @return {@code true} if the modifier described by this object is {@code private}.
         */
        boolean isPrivate();

        /**
         * Specifies if the modifier described by this object is {@code static}.
         *
         * @return {@code true} if the modifier described by this object is {@code static}.
         */
        boolean isStatic();

        /**
         * Specifies if the modifier described by this object represents the deprecated flag.
         *
         * @return {@code true} if the modifier described by this object represents the deprecated flag.
         */
        boolean isDeprecated();

        /**
         * Return's this byte code element's ownership.
         *
         * @return This byte code element's ownership.
         */
        Ownership getOwnership();

        /**
         * Returns this byte code element's visibility.
         *
         * @return This byte code element's visibility.
         */
        Visibility getVisibility();
    }

    /**
     * A modifier reviewable for a byte code element that can be abstract, i.e. a {@link net.bytebuddy.description.type.TypeDescription}
     * or a {@link net.bytebuddy.description.method.MethodDescription}.
     */
    interface OfAbstraction extends OfByteCodeElement {

        /**
         * Specifies if the modifier described by this object is {@code abstract}.
         *
         * @return {@code true} if the modifier described by this object is {@code abstract}.
         */
        boolean isAbstract();
    }

    /**
     * A modifier reviewable for a byte code element that can represent an enumeration, i.e. a {@link net.bytebuddy.description.field.FieldDescription}
     * that holds an enumeration value or a {@link net.bytebuddy.description.type.TypeDescription} that represents an enumeration.
     */
    interface OfEnumeration extends OfByteCodeElement {

        /**
         * Specifies if the modifier described by this object represents the enum flag.
         *
         * @return {@code true} if the modifier described by this object represents the enum flag.
         */
        boolean isEnum();

        /**
         * Returns this byte code element's enumeration state.
         *
         * @return This byte code element's enumeration state.
         */
        EnumerationState getEnumerationState();
    }

    /**
     * A modifier reviewable for a {@link net.bytebuddy.description.type.TypeDescription}.
     */
    interface ForTypeDefinition extends OfAbstraction, OfEnumeration {

        /**
         * Specifies if the modifier described by this object represents the interface flag.
         *
         * @return {@code true} if the modifier described by this object represents the interface flag.
         */
        boolean isInterface();

        /**
         * Specifies if the modifier described by this object represents the annotation flag.
         *
         * @return {@code true} if the modifier described by this object represents the annotation flag.
         */
        boolean isAnnotation();

        /**
         * Returns this type's manifestation.
         *
         * @return This type's manifestation.
         */
        TypeManifestation getTypeManifestation();
    }

    /**
     * A modifier reviewable for a {@link net.bytebuddy.description.field.FieldDescription}.
     */
    interface ForFieldDescription extends OfEnumeration {

        /**
         * Specifies if the modifier described by this object represents the volatile flag.
         *
         * @return {@code true} if the modifier described by this object represents the volatile flag.
         */
        boolean isVolatile();

        /**
         * Specifies if the modifier described by this object represents the transient flag.
         *
         * @return {@code true} if the modifier described by this object represents the transient flag.
         */
        boolean isTransient();

        /**
         * Returns this field's manifestation.
         *
         * @return This field's manifestation.
         */
        FieldManifestation getFieldManifestation();

        /**
         * Returns this field's persistence.
         *
         * @return This field's persistence.
         */
        FieldPersistence getFieldPersistence();
    }

    /**
     * A modifier reviewable for a {@link net.bytebuddy.description.method.MethodDescription}.
     */
    interface ForMethodDescription extends OfAbstraction {

        /**
         * Specifies if the modifier described by this object is {@code synchronized}.
         *
         * @return {@code true} if the modifier described by this object is {@code synchronized}.
         */
        boolean isSynchronized();

        /**
         * Specifies if the modifier described by this object represents the var args flag.
         *
         * @return {@code true} if the modifier described by this object represents the var args flag.
         */
        boolean isVarArgs();

        /**
         * Specifies if the modifier described by this object is {@code native}.
         *
         * @return {@code true} if the modifier described by this object is {@code native}.
         */
        boolean isNative();

        /**
         * Specifies if the modifier described by this object represents the bridge flag.
         *
         * @return {@code true} if the modifier described by this object represents the bridge flag
         */
        boolean isBridge();

        /**
         * Specifies if the modifier described by this object is {@code strictfp}.
         *
         * @return {@code true} if the modifier described by this object is {@code strictfp}.
         */
        boolean isStrict();

        /**
         * Returns this method's synchronization state.
         *
         * @return This method's synchronization state.
         */
        SynchronizationState getSynchronizationState();

        /**
         * Returns this method's strictness in floating-point computation.
         *
         * @return This method's strictness in floating-point computation.
         */
        MethodStrictness getMethodStrictness();

        /**
         * Returns this method's manifestation.
         *
         * @return This method's manifestation.
         */
        MethodManifestation getMethodManifestation();
    }

    /**
     * A modifier reviewable for a {@link net.bytebuddy.description.method.ParameterDescription}.
     */
    interface ForParameterDescription extends ModifierReviewable {

        /**
         * CSpecifies if the modifier described by this object is mandated.
         *
         * @return {@code true} if the modifier described by this object is mandated.
         */
        boolean isMandated();

        /**
         * Returns this parameter's manifestation.
         *
         * @return This parameter's manifestation.
         */
        ParameterManifestation getParameterManifestation();

        /**
         * Returns this parameter's provisioning state.
         *
         * @return This parameter's provisioning state.
         */
        ProvisioningState getProvisioningState();
    }

    /**
     * An abstract base implementation of a {@link ModifierReviewable} class.
     */
    abstract class AbstractBase implements ForTypeDefinition, ForFieldDescription, ForMethodDescription, ForParameterDescription {

        @Override
        public boolean isAbstract() {
            return matchesMask(Opcodes.ACC_ABSTRACT);
        }

        @Override
        public boolean isFinal() {
            return matchesMask(Opcodes.ACC_FINAL);
        }

        @Override
        public boolean isStatic() {
            return matchesMask(Opcodes.ACC_STATIC);
        }

        @Override
        public boolean isPublic() {
            return matchesMask(Opcodes.ACC_PUBLIC);
        }

        @Override
        public boolean isProtected() {
            return matchesMask(Opcodes.ACC_PROTECTED);
        }

        @Override
        public boolean isPackagePrivate() {
            return !isPublic() && !isProtected() && !isPrivate();
        }

        @Override
        public boolean isPrivate() {
            return matchesMask(Opcodes.ACC_PRIVATE);
        }

        @Override
        public boolean isNative() {
            return matchesMask(Opcodes.ACC_NATIVE);
        }

        @Override
        public boolean isSynchronized() {
            return matchesMask(Opcodes.ACC_SYNCHRONIZED);
        }

        @Override
        public boolean isStrict() {
            return matchesMask(Opcodes.ACC_STRICT);
        }

        @Override
        public boolean isMandated() {
            return matchesMask(Opcodes.ACC_MANDATED);
        }

        @Override
        public boolean isSynthetic() {
            return matchesMask(Opcodes.ACC_SYNTHETIC);
        }

        @Override
        public boolean isBridge() {
            return matchesMask(Opcodes.ACC_BRIDGE);
        }

        @Override
        public boolean isDeprecated() {
            return matchesMask(Opcodes.ACC_DEPRECATED);
        }

        @Override
        public boolean isAnnotation() {
            return matchesMask(Opcodes.ACC_ANNOTATION);
        }

        @Override
        public boolean isEnum() {
            return matchesMask(Opcodes.ACC_ENUM);
        }

        @Override
        public boolean isInterface() {
            return matchesMask(Opcodes.ACC_INTERFACE);
        }

        @Override
        public boolean isTransient() {
            return matchesMask(Opcodes.ACC_TRANSIENT);
        }

        @Override
        public boolean isVolatile() {
            return matchesMask(Opcodes.ACC_VOLATILE);
        }

        @Override
        public boolean isVarArgs() {
            return matchesMask(Opcodes.ACC_VARARGS);
        }

        @Override
        public SyntheticState getSyntheticState() {
            return isSynthetic()
                    ? SyntheticState.SYNTHETIC
                    : SyntheticState.PLAIN;
        }

        @Override
        public Visibility getVisibility() {
            int modifiers = getModifiers();
            switch (modifiers & (Opcodes.ACC_PUBLIC | Opcodes.ACC_PROTECTED | Opcodes.ACC_PRIVATE)) {
                case Opcodes.ACC_PUBLIC:
                    return Visibility.PUBLIC;
                case Opcodes.ACC_PROTECTED:
                    return Visibility.PROTECTED;
                case EMPTY_MASK:
                    return Visibility.PACKAGE_PRIVATE;
                case Opcodes.ACC_PRIVATE:
                    return Visibility.PRIVATE;
                default:
                    throw new IllegalStateException("Unexpected modifiers: " + modifiers);
            }
        }

        @Override
        public Ownership getOwnership() {
            return isStatic()
                    ? Ownership.STATIC
                    : Ownership.MEMBER;
        }

        @Override
        public EnumerationState getEnumerationState() {
            return isEnum()
                    ? EnumerationState.ENUMERATION
                    : EnumerationState.PLAIN;
        }

        @Override
        public TypeManifestation getTypeManifestation() {
            int modifiers = getModifiers();
            switch (modifiers & (Opcodes.ACC_ANNOTATION | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT | Opcodes.ACC_FINAL)) {
                case Opcodes.ACC_FINAL:
                    return TypeManifestation.FINAL;
                case Opcodes.ACC_ABSTRACT:
                    return TypeManifestation.ABSTRACT;
                case Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE:
                    return TypeManifestation.INTERFACE;
                case Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE | Opcodes.ACC_ANNOTATION:
                    return TypeManifestation.ANNOTATION;
                case EMPTY_MASK:
                    return TypeManifestation.PLAIN;
                default:
                    throw new IllegalStateException("Unexpected modifiers: " + modifiers);
            }
        }

        @Override
        public FieldManifestation getFieldManifestation() {
            int modifiers = getModifiers();
            switch (modifiers & (Opcodes.ACC_VOLATILE | Opcodes.ACC_FINAL)) {
                case Opcodes.ACC_FINAL:
                    return FieldManifestation.FINAL;
                case Opcodes.ACC_VOLATILE:
                    return FieldManifestation.VOLATILE;
                case EMPTY_MASK:
                    return FieldManifestation.PLAIN;
                default:
                    throw new IllegalStateException("Unexpected modifiers: " + modifiers);
            }
        }

        @Override
        public FieldPersistence getFieldPersistence() {
            int modifiers = getModifiers();
            switch (modifiers & Opcodes.ACC_TRANSIENT) {
                case Opcodes.ACC_TRANSIENT:
                    return FieldPersistence.TRANSIENT;
                case EMPTY_MASK:
                    return FieldPersistence.PLAIN;
                default:
                    throw new IllegalStateException("Unexpected modifiers: " + modifiers);
            }
        }

        @Override
        public SynchronizationState getSynchronizationState() {
            return isSynchronized()
                    ? SynchronizationState.SYNCHRONIZED
                    : SynchronizationState.PLAIN;
        }

        @Override
        public MethodManifestation getMethodManifestation() {
            int modifiers = getModifiers();
            switch (modifiers & (Opcodes.ACC_NATIVE | Opcodes.ACC_ABSTRACT | Opcodes.ACC_FINAL | Opcodes.ACC_BRIDGE)) {
                case Opcodes.ACC_NATIVE | Opcodes.ACC_FINAL:
                    return MethodManifestation.FINAL_NATIVE;
                case Opcodes.ACC_NATIVE:
                    return MethodManifestation.NATIVE;
                case Opcodes.ACC_FINAL:
                    return MethodManifestation.FINAL;
                case Opcodes.ACC_BRIDGE:
                    return MethodManifestation.BRIDGE;
                case Opcodes.ACC_BRIDGE | Opcodes.ACC_FINAL:
                    return MethodManifestation.FINAL_BRIDGE;
                case Opcodes.ACC_ABSTRACT:
                    return MethodManifestation.ABSTRACT;
                case EMPTY_MASK:
                    return MethodManifestation.PLAIN;
                default:
                    throw new IllegalStateException("Unexpected modifiers: " + modifiers);
            }
        }

        @Override
        public MethodStrictness getMethodStrictness() {
            return isStrict()
                    ? MethodStrictness.STRICT
                    : MethodStrictness.PLAIN;
        }

        @Override
        public ParameterManifestation getParameterManifestation() {
            return isFinal()
                    ? ParameterManifestation.FINAL
                    : ParameterManifestation.PLAIN;
        }

        @Override
        public ProvisioningState getProvisioningState() {
            return isMandated()
                    ? ProvisioningState.MANDATED
                    : ProvisioningState.PLAIN;
        }

        /**
         * Checks if a mask is matched by this instance.
         *
         * @param mask The mask to check.
         * @return {@code true} if the mask is matched.
         */
        private boolean matchesMask(int mask) {
            return (getModifiers() & mask) == mask;
        }
    }
}
