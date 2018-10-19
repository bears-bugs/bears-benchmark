package net.bytebuddy.description.method;

import net.bytebuddy.build.CachedReturnPlugin;
import net.bytebuddy.description.ByteCodeElement;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.TypeVariableSource;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.annotation.AnnotationList;
import net.bytebuddy.description.annotation.AnnotationValue;
import net.bytebuddy.description.enumeration.EnumerationDescription;
import net.bytebuddy.description.modifier.ModifierContributor;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.description.type.TypeList;
import net.bytebuddy.description.type.TypeVariableToken;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaConstant;
import net.bytebuddy.utility.JavaType;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureWriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.not;
import static net.bytebuddy.matcher.ElementMatchers.ofSort;

/**
 * Implementations of this interface describe a Java method, i.e. a method or a constructor. Implementations of this
 * interface must provide meaningful {@code equal(Object)} and {@code hashCode()} implementations.
 */
public interface MethodDescription extends TypeVariableSource,
        ModifierReviewable.ForMethodDescription,
        NamedElement.WithGenericName,
        ByteCodeElement,
        ByteCodeElement.TypeDependant<MethodDescription.InDefinedShape, MethodDescription.Token> {

    /**
     * The internal name of a Java constructor.
     */
    String CONSTRUCTOR_INTERNAL_NAME = "<init>";

    /**
     * The internal name of a Java static initializer.
     */
    String TYPE_INITIALIZER_INTERNAL_NAME = "<clinit>";

    /**
     * The type initializer of any representation of a type initializer.
     */
    int TYPE_INITIALIZER_MODIFIER = Opcodes.ACC_STATIC;

    /**
     * Represents any undefined property of a type description that is instead represented as {@code null} in order
     * to resemble the Java reflection API which returns {@code null} and is intuitive to many Java developers.
     */
    InDefinedShape UNDEFINED = null;

    /**
     * Returns the return type of the described method.
     *
     * @return The return type of the described method.
     */
    TypeDescription.Generic getReturnType();

    /**
     * Returns a list of this method's parameters.
     *
     * @return A list of this method's parameters.
     */
    ParameterList<?> getParameters();

    /**
     * Returns the exception types of the described method.
     *
     * @return The exception types of the described method.
     */
    TypeList.Generic getExceptionTypes();

    /**
     * Returns this method's actual modifiers as it is present in a class file, i.e. includes a flag if this method
     * is marked {@link Deprecated}.
     *
     * @return The method's actual modifiers.
     */
    int getActualModifiers();

    /**
     * Returns this method's actual modifiers as it is present in a class file, i.e. includes a flag if this method
     * is marked {@link Deprecated} and adjusts the modifiers for being abstract or not.
     *
     * @param manifest {@code true} if the method should be treated as non-abstract.
     * @return The method's actual modifiers.
     */
    int getActualModifiers(boolean manifest);

    /**
     * Returns this method's actual modifiers as it is present in a class file, i.e. includes a flag if this method
     * is marked {@link Deprecated} and adjusts the modifiers for being abstract or not. Additionally, this method
     * resolves a required minimal visibility.
     *
     * @param manifest   {@code true} if the method should be treated as non-abstract.
     * @param visibility The minimal visibility to enforce for this method.
     * @return The method's actual modifiers.
     */
    int getActualModifiers(boolean manifest, Visibility visibility);

    /**
     * Checks if this method description represents a constructor.
     *
     * @return {@code true} if this method description represents a constructor.
     */
    boolean isConstructor();

    /**
     * Checks if this method description represents a method, i.e. not a constructor or a type initializer.
     *
     * @return {@code true} if this method description represents a Java method.
     */
    boolean isMethod();

    /**
     * Checks if this method is a type initializer.
     *
     * @return {@code true} if this method description represents a type initializer.
     */
    boolean isTypeInitializer();

    /**
     * Verifies if a method description represents a given loaded method.
     *
     * @param method The method to be checked.
     * @return {@code true} if this method description represents the given loaded method.
     */
    boolean represents(Method method);

    /**
     * Verifies if a method description represents a given loaded constructor.
     *
     * @param constructor The constructor to be checked.
     * @return {@code true} if this method description represents the given loaded constructor.
     */
    boolean represents(Constructor<?> constructor);

    /**
     * Verifies if this method describes a virtual method, i.e. a method that is inherited by a sub type of this type.
     *
     * @return {@code true} if this method is virtual.
     */
    boolean isVirtual();

    /**
     * Returns the size of the local variable array that is required for this method, i.e. the size of all parameters
     * if they were loaded on the stack including a reference to {@code this} if this method represented a non-static
     * method.
     *
     * @return The size of this method on the operand stack.
     */
    int getStackSize();

    /**
     * Checks if this method represents a default (defender) method.
     *
     * @return {@code true} if this method is a default method.
     */
    boolean isDefaultMethod();

    /**
     * Checks if this method can be called using the {@code INVOKESPECIAL} for a given type.
     *
     * @param typeDescription The type o
     * @return {@code true} if this method can be called using the {@code INVOKESPECIAL} instruction
     * using the given type.
     */
    boolean isSpecializableFor(TypeDescription typeDescription);

    /**
     * Returns the method's default annotation value or {@code null} if no default value is defined for this method.
     *
     * @return The method's default annotation value or {@code null} if no default value is defined for this method.
     */
    AnnotationValue<?, ?> getDefaultValue();

    /**
     * Returns the default value but casts it to the given type. If the type differs from the value, a
     * {@link java.lang.ClassCastException} is thrown.
     *
     * @param type The type to cast the default value to.
     * @param <T>  The type to cast the default value to.
     * @return The casted default value.
     */
    <T> T getDefaultValue(Class<T> type);

    /**
     * Asserts if this method is invokable on an instance of the given type, i.e. the method is an instance method or
     * a constructor and the method is visible to the type and can be invoked on the given instance.
     *
     * @param typeDescription The type to check.
     * @return {@code true} if this method is invokable on an instance of the given type.
     */
    boolean isInvokableOn(TypeDescription typeDescription);

    /**
     * Checks if this method is a valid bootstrap method for an invokedynamic call.
     *
     * @return {@code true} if this method is a valid bootstrap method for an invokedynamic call.
     */
    boolean isInvokeBootstrap();

    /**
     * Checks if this method is a valid bootstrap method for an invokedynamic call.
     *
     * @param arguments The arguments to the bootstrap method represented by instances of primitive wrapper types,
     *                  {@link String}, {@link TypeDescription} and {@link JavaConstant} values.
     * @return {@code true} if this method is a valid bootstrap method for an invokedynamic call.
     */
    boolean isInvokeBootstrap(List<?> arguments);

    /**
     * Checks if this method is a valid bootstrap method for an constantdynamic call.
     *
     * @return {@code true} if this method is a valid bootstrap method for an constantdynamic call.
     */
    boolean isConstantBootstrap();

    /**
     * Checks if this method is a valid bootstrap method for an constantdynamic call.
     *
     * @param arguments The arguments to the bootstrap method represented by instances of primitive wrapper types,
     *                  {@link String}, {@link TypeDescription} and {@link JavaConstant} values.
     * @return {@code true} if this method is a valid bootstrap method for an constantdynamic call.
     */
    boolean isConstantBootstrap(List<?> arguments);

    /**
     * Checks if this method is capable of defining a default annotation value.
     *
     * @return {@code true} if it is possible to define a default annotation value for this method.
     */
    boolean isDefaultValue();

    /**
     * Checks if the given value can describe a default annotation value for this method.
     *
     * @param annotationValue The value that describes the default annotation value for this method.
     * @return {@code true} if the given value can describe a default annotation value for this method.
     */
    boolean isDefaultValue(AnnotationValue<?, ?> annotationValue);

    /**
     * Returns this methods receiver type. A receiver type is undefined for {@code static} methods
     * where {@code null} is returned. Other than a receiver type that is provided by the Java reflection
     * API, Byte Buddy is capable of extracting annotations on type parameters of receiver types when
     * directly accessing a class file. Therefore, a receiver type might be parameterized.
     *
     * @return This method's (annotated) receiver type.
     */
    TypeDescription.Generic getReceiverType();

    /**
     * Returns a signature token representing this method.
     *
     * @return A signature token representing this method.
     */
    SignatureToken asSignatureToken();

    /**
     * Returns a type token that represents this method's raw return and parameter types.
     *
     * @return A type token that represents this method's raw return and parameter types.
     */
    TypeToken asTypeToken();

    /**
     * Validates that the supplied type token can implement a bridge method to this method.
     *
     * @param typeToken A type token representing a potential bridge method to this method.
     * @return {@code true} if the supplied type token can represent a bridge method to this method.
     */
    boolean isBridgeCompatible(TypeToken typeToken);

    /**
     * Represents a method description in its generic shape, i.e. in the shape it is defined by a generic or raw type.
     */
    interface InGenericShape extends MethodDescription {

        /**
         * {@inheritDoc}
         */
        TypeDescription.Generic getDeclaringType();

        /**
         * {@inheritDoc}
         */
        ParameterList<ParameterDescription.InGenericShape> getParameters();
    }

    /**
     * Represents a method in its defined shape, i.e. in the form it is defined by a class without its type variables being resolved.
     */
    interface InDefinedShape extends MethodDescription {

        /**
         * {@inheritDoc}
         */
        TypeDescription getDeclaringType();

        /**
         * {@inheritDoc}
         */
        ParameterList<ParameterDescription.InDefinedShape> getParameters();

        /**
         * An abstract base implementation of a method description in its defined shape.
         */
        abstract class AbstractBase extends MethodDescription.AbstractBase implements InDefinedShape {

            /**
             * {@inheritDoc}
             */
            public InDefinedShape asDefined() {
                return this;
            }

            /**
             * {@inheritDoc}
             */
            public TypeDescription.Generic getReceiverType() {
                if (isStatic()) {
                    return TypeDescription.Generic.UNDEFINED;
                } else if (isConstructor()) {
                    TypeDescription declaringType = getDeclaringType(), enclosingDeclaringType = getDeclaringType().getEnclosingType();
                    if (enclosingDeclaringType == null) {
                        return TypeDescription.Generic.OfParameterizedType.ForGenerifiedErasure.of(declaringType);
                    } else {
                        return declaringType.isStatic()
                                ? enclosingDeclaringType.asGenericType()
                                : TypeDescription.Generic.OfParameterizedType.ForGenerifiedErasure.of(enclosingDeclaringType);
                    }
                } else {
                    return TypeDescription.Generic.OfParameterizedType.ForGenerifiedErasure.of(getDeclaringType());
                }
            }
        }
    }

    /**
     * An abstract base implementation of a method description.
     */
    abstract class AbstractBase extends TypeVariableSource.AbstractBase implements MethodDescription {

        /**
         * A merger of all method modifiers that are visible in the Java source code.
         */
        private static final int SOURCE_MODIFIERS = Modifier.PUBLIC
                | Modifier.PROTECTED
                | Modifier.PRIVATE
                | Modifier.ABSTRACT
                | Modifier.STATIC
                | Modifier.FINAL
                | Modifier.SYNCHRONIZED
                | Modifier.NATIVE;

        /**
         * {@inheritDoc}
         */
        public int getStackSize() {
            return getParameters().asTypeList().getStackSize() + (isStatic() ? 0 : 1);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isMethod() {
            return !isConstructor() && !isTypeInitializer();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstructor() {
            return CONSTRUCTOR_INTERNAL_NAME.equals(getInternalName());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isTypeInitializer() {
            return TYPE_INITIALIZER_INTERNAL_NAME.equals(getInternalName());
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Method method) {
            return equals(new ForLoadedMethod(method));
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Constructor<?> constructor) {
            return equals(new ForLoadedConstructor(constructor));
        }

        /**
         * {@inheritDoc}
         */
        public String getName() {
            return isMethod()
                    ? getInternalName()
                    : getDeclaringType().asErasure().getName();
        }

        /**
         * {@inheritDoc}
         */
        public String getActualName() {
            return isMethod()
                    ? getName()
                    : EMPTY_NAME;
        }

        /**
         * {@inheritDoc}
         */
        public String getDescriptor() {
            StringBuilder descriptor = new StringBuilder().append('(');
            for (TypeDescription parameterType : getParameters().asTypeList().asErasures()) {
                descriptor.append(parameterType.getDescriptor());
            }
            return descriptor.append(')').append(getReturnType().asErasure().getDescriptor()).toString();
        }

        /**
         * {@inheritDoc}
         */
        public String getGenericSignature() {
            try {
                SignatureWriter signatureWriter = new SignatureWriter();
                boolean generic = false;
                for (TypeDescription.Generic typeVariable : getTypeVariables()) {
                    signatureWriter.visitFormalTypeParameter(typeVariable.getSymbol());
                    boolean classBound = true;
                    for (TypeDescription.Generic upperBound : typeVariable.getUpperBounds()) {
                        upperBound.accept(new TypeDescription.Generic.Visitor.ForSignatureVisitor(classBound
                                ? signatureWriter.visitClassBound()
                                : signatureWriter.visitInterfaceBound()));
                        classBound = false;
                    }
                    generic = true;
                }
                for (TypeDescription.Generic parameterType : getParameters().asTypeList()) {
                    parameterType.accept(new TypeDescription.Generic.Visitor.ForSignatureVisitor(signatureWriter.visitParameterType()));
                    generic = generic || !parameterType.getSort().isNonGeneric();
                }
                TypeDescription.Generic returnType = getReturnType();
                returnType.accept(new TypeDescription.Generic.Visitor.ForSignatureVisitor(signatureWriter.visitReturnType()));
                generic = generic || !returnType.getSort().isNonGeneric();
                TypeList.Generic exceptionTypes = getExceptionTypes();
                if (!exceptionTypes.filter(not(ofSort(TypeDefinition.Sort.NON_GENERIC))).isEmpty()) {
                    for (TypeDescription.Generic exceptionType : exceptionTypes) {
                        exceptionType.accept(new TypeDescription.Generic.Visitor.ForSignatureVisitor(signatureWriter.visitExceptionType()));
                        generic = generic || !exceptionType.getSort().isNonGeneric();
                    }
                }
                return generic
                        ? signatureWriter.toString()
                        : NON_GENERIC_SIGNATURE;
            } catch (GenericSignatureFormatError ignored) {
                return NON_GENERIC_SIGNATURE;
            }
        }

        /**
         * {@inheritDoc}
         */
        public int getActualModifiers() {
            return getModifiers() | (getDeclaredAnnotations().isAnnotationPresent(Deprecated.class)
                    ? Opcodes.ACC_DEPRECATED
                    : EMPTY_MASK);
        }

        /**
         * {@inheritDoc}
         */
        public int getActualModifiers(boolean manifest) {
            return manifest
                    ? getActualModifiers() & ~(Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE)
                    : getActualModifiers() & ~Opcodes.ACC_NATIVE | Opcodes.ACC_ABSTRACT;
        }

        /**
         * {@inheritDoc}
         */
        public int getActualModifiers(boolean manifest, Visibility visibility) {
            return ModifierContributor.Resolver.of(Collections.singleton(getVisibility().expandTo(visibility))).resolve(getActualModifiers(manifest));
        }

        /**
         * {@inheritDoc}
         */
        public boolean isVisibleTo(TypeDescription typeDescription) {
            return (isVirtual() || getDeclaringType().asErasure().isVisibleTo(typeDescription))
                    && (isPublic()
                    || typeDescription.equals(getDeclaringType().asErasure())
                    || isProtected() && getDeclaringType().asErasure().isAssignableFrom(typeDescription)
                    || !isPrivate() && typeDescription.isSamePackage(getDeclaringType().asErasure()));
        }

        /**
         * {@inheritDoc}
         */
        public boolean isAccessibleTo(TypeDescription typeDescription) {
            return (isVirtual() || getDeclaringType().asErasure().isVisibleTo(typeDescription))
                    && (isPublic()
                    || typeDescription.equals(getDeclaringType().asErasure())
                    || !isPrivate() && typeDescription.isSamePackage(getDeclaringType().asErasure()))
                    || isPrivate() && typeDescription.isNestMateOf(getDeclaringType().asErasure());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isVirtual() {
            return !(isConstructor() || isPrivate() || isStatic() || isTypeInitializer());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isDefaultMethod() {
            return !isAbstract() && !isBridge() && getDeclaringType().isInterface();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isSpecializableFor(TypeDescription targetType) {
            if (isStatic()) { // Static private methods are never specializable, check static property first
                return false;
            } else if (isPrivate() || isConstructor()) {
                return getDeclaringType().equals(targetType);
            } else {
                return !isAbstract() && getDeclaringType().asErasure().isAssignableFrom(targetType);
            }
        }

        /**
         * {@inheritDoc}
         */
        public <T> T getDefaultValue(Class<T> type) {
            return type.cast(getDefaultValue());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isInvokableOn(TypeDescription typeDescription) {
            return !isStatic()
                    && !isTypeInitializer()
                    && isVisibleTo(typeDescription)
                    && (isVirtual()
                    ? getDeclaringType().asErasure().isAssignableFrom(typeDescription)
                    : getDeclaringType().asErasure().equals(typeDescription));
        }

        /**
         * Checks if this method is a bootstrap method while expecting the supplied type as a type representation.
         *
         * @param typeType The type of the bootstrap method's type representation.
         * @return {@code true} if this method is a bootstrap method assuming the supplied type representation.
         */
        private boolean isBootstrap(TypeDescription typeType) {
            TypeList parameterTypes = getParameters().asTypeList().asErasures();
            switch (parameterTypes.size()) {
                case 0:
                    return false;
                case 1:
                    return parameterTypes.getOnly().represents(Object[].class);
                case 2:
                    return JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().isAssignableTo(parameterTypes.get(0))
                            && parameterTypes.get(1).represents(Object[].class);
                case 3:
                    return JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().isAssignableTo(parameterTypes.get(0))
                            && (parameterTypes.get(1).represents(Object.class) || parameterTypes.get(1).represents(String.class))
                            && (parameterTypes.get(2).represents(Object[].class) || parameterTypes.get(2).isAssignableFrom(typeType));
                default:
                    return JavaType.METHOD_HANDLES_LOOKUP.getTypeStub().isAssignableTo(parameterTypes.get(0))
                            && (parameterTypes.get(1).represents(Object.class) || parameterTypes.get(1).represents(String.class))
                            && parameterTypes.get(2).isAssignableFrom(typeType);
            }
        }

        /**
         * Checks if this method is a bootstrap method given the supplied arguments. This method does not implement a full check but assumes that
         * {@link MethodDescription.AbstractBase#isBootstrap(TypeDescription)} is invoked, as well.
         *
         * @param arguments The arguments to the bootstrap method.
         * @return {@code true} if this method is a bootstrap method for the supplied arguments..
         */
        private boolean isBootstrap(List<?> arguments) {
            for (Object argument : arguments) {
                if (argument == null) {
                    throw new IllegalArgumentException("The null value is not a bootstrap constant");
                }
                Class<?> argumentType = argument.getClass();
                if (!(argumentType == String.class
                        || argumentType == Integer.class
                        || argumentType == Long.class
                        || argumentType == Float.class
                        || argumentType == Double.class
                        || TypeDescription.class.isAssignableFrom(argumentType)
                        || JavaConstant.class.isAssignableFrom(argumentType))) {
                    throw new IllegalArgumentException("Not a Java constant representation: " + argument);
                }
            }
            TypeList parameterTypes = getParameters().asTypeList().asErasures();
            if (parameterTypes.size() < 4) {
                return arguments.isEmpty() || parameterTypes.get(parameterTypes.size() - 1).represents(Object[].class);
            } else {
                int index = 4;
                Iterator<?> argumentIterator = arguments.iterator();
                for (TypeDescription parameterType : parameterTypes.subList(3, parameterTypes.size())) {
                    boolean finalParameterCheck = !argumentIterator.hasNext();
                    if (!finalParameterCheck) {
                        Object argument = argumentIterator.next();
                        finalParameterCheck = !((argument instanceof JavaConstant) && ((JavaConstant) argument).getType().isAssignableTo(parameterType))
                                && !(parameterType.represents(Class.class) && argument instanceof TypeDescription && !((TypeDescription) argument).isPrimitive())
                                && !(parameterType.represents(String.class) && argument.getClass() == String.class)
                                && !(parameterType.represents(int.class) && argument.getClass() == Integer.class)
                                && !(parameterType.represents(long.class) && argument.getClass() == Long.class)
                                && !(parameterType.represents(float.class) && argument.getClass() == Float.class)
                                && !(parameterType.represents(double.class) && argument.getClass() == Double.class);
                    }
                    if (finalParameterCheck) {
                        return index == parameterTypes.size() && parameterType.represents(Object[].class);
                    }
                    index++;
                }
                return true;
            }
        }

        /**
         * {@inheritDoc}
         */
        public boolean isInvokeBootstrap() {
            TypeDescription returnType = getReturnType().asErasure();
            if ((isMethod() && (!isStatic()
                    || !(JavaType.CALL_SITE.getTypeStub().isAssignableFrom(returnType) || JavaType.CALL_SITE.getTypeStub().isAssignableTo(returnType))))
                    || (isConstructor() && !JavaType.CALL_SITE.getTypeStub().isAssignableFrom(getDeclaringType().asErasure()))) {
                return false;
            }
            return isBootstrap(JavaType.METHOD_TYPE.getTypeStub());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isInvokeBootstrap(List<?> arguments) {
            return isInvokeBootstrap() && isBootstrap(arguments);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstantBootstrap() {
            ParameterList<?> parameters = getParameters();
            return !parameters.isEmpty()
                    && getParameters().get(0).getType().asErasure().equals(JavaType.METHOD_HANDLES_LOOKUP.getTypeStub())
                    && isBootstrap(TypeDescription.CLASS);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstantBootstrap(List<?> arguments) {
            return isConstantBootstrap() && isBootstrap(arguments);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isDefaultValue() {
            return !isConstructor()
                    && !isStatic()
                    && getReturnType().asErasure().isAnnotationReturnType()
                    && getParameters().isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isDefaultValue(AnnotationValue<?, ?> annotationValue) {
            if (!isDefaultValue()) {
                return false;
            }
            TypeDescription returnType = getReturnType().asErasure();
            Object value = annotationValue.resolve();
            return (returnType.represents(boolean.class) && value instanceof Boolean)
                    || (returnType.represents(byte.class) && value instanceof Byte)
                    || (returnType.represents(char.class) && value instanceof Character)
                    || (returnType.represents(short.class) && value instanceof Short)
                    || (returnType.represents(int.class) && value instanceof Integer)
                    || (returnType.represents(long.class) && value instanceof Long)
                    || (returnType.represents(float.class) && value instanceof Float)
                    || (returnType.represents(double.class) && value instanceof Double)
                    || (returnType.represents(String.class) && value instanceof String)
                    || (returnType.isAssignableTo(Enum.class) && value instanceof EnumerationDescription && isEnumerationType(returnType, (EnumerationDescription) value))
                    || (returnType.isAssignableTo(Annotation.class) && value instanceof AnnotationDescription && isAnnotationType(returnType, (AnnotationDescription) value))
                    || (returnType.represents(Class.class) && value instanceof TypeDescription)
                    || (returnType.represents(boolean[].class) && value instanceof boolean[])
                    || (returnType.represents(byte[].class) && value instanceof byte[])
                    || (returnType.represents(char[].class) && value instanceof char[])
                    || (returnType.represents(short[].class) && value instanceof short[])
                    || (returnType.represents(int[].class) && value instanceof int[])
                    || (returnType.represents(long[].class) && value instanceof long[])
                    || (returnType.represents(float[].class) && value instanceof float[])
                    || (returnType.represents(double[].class) && value instanceof double[])
                    || (returnType.represents(String[].class) && value instanceof String[])
                    || (returnType.isAssignableTo(Enum[].class) && value instanceof EnumerationDescription[] && isEnumerationType(returnType.getComponentType(), (EnumerationDescription[]) value))
                    || (returnType.isAssignableTo(Annotation[].class) && value instanceof AnnotationDescription[] && isAnnotationType(returnType.getComponentType(), (AnnotationDescription[]) value))
                    || (returnType.represents(Class[].class) && value instanceof TypeDescription[]);
        }

        /**
         * Checks if the supplied enumeration descriptions describe the correct enumeration type.
         *
         * @param enumerationType        The enumeration type to check for.
         * @param enumerationDescription The enumeration descriptions to check.
         * @return {@code true} if all enumeration descriptions represent the enumeration type in question.
         */
        private static boolean isEnumerationType(TypeDescription enumerationType, EnumerationDescription... enumerationDescription) {
            for (EnumerationDescription anEnumerationDescription : enumerationDescription) {
                if (!anEnumerationDescription.getEnumerationType().equals(enumerationType)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Checks if the supplied enumeration descriptions describe the correct annotation type.
         *
         * @param annotationType        The annotation type to check for.
         * @param annotationDescription The annotation descriptions to check.
         * @return {@code true} if all annotation descriptions represent the annotation type in question.
         */
        private static boolean isAnnotationType(TypeDescription annotationType, AnnotationDescription... annotationDescription) {
            for (AnnotationDescription anAnnotationDescription : annotationDescription) {
                if (!anAnnotationDescription.getAnnotationType().equals(annotationType)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public TypeVariableSource getEnclosingSource() {
            return isStatic()
                    ? TypeVariableSource.UNDEFINED
                    : getDeclaringType().asErasure();
        }

        /**
         * {@inheritDoc}
         */
        public <T> T accept(TypeVariableSource.Visitor<T> visitor) {
            return visitor.onMethod(this.asDefined());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isGenerified() {
            return !getTypeVariables().isEmpty();
        }

        /**
         * {@inheritDoc}
         */
        public MethodDescription.Token asToken(ElementMatcher<? super TypeDescription> matcher) {
            TypeDescription.Generic receiverType = getReceiverType();
            return new MethodDescription.Token(getInternalName(),
                    getModifiers(),
                    getTypeVariables().asTokenList(matcher),
                    getReturnType().accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)),
                    getParameters().asTokenList(matcher),
                    getExceptionTypes().accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)),
                    getDeclaredAnnotations(),
                    getDefaultValue(),
                    receiverType == null
                            ? TypeDescription.Generic.UNDEFINED
                            : receiverType.accept(new TypeDescription.Generic.Visitor.Substitutor.ForDetachment(matcher)));
        }

        /**
         * {@inheritDoc}
         */
        public SignatureToken asSignatureToken() {
            return new SignatureToken(getInternalName(), getReturnType().asErasure(), getParameters().asTypeList().asErasures());
        }

        /**
         * {@inheritDoc}
         */
        public TypeToken asTypeToken() {
            return new TypeToken(getReturnType().asErasure(), getParameters().asTypeList().asErasures());
        }

        /**
         * {@inheritDoc}
         */
        public boolean isBridgeCompatible(TypeToken typeToken) {
            List<TypeDescription> types = getParameters().asTypeList().asErasures(), bridgeTypes = typeToken.getParameterTypes();
            if (types.size() != bridgeTypes.size()) {
                return false;
            }
            for (int index = 0; index < types.size(); index++) {
                if (!types.get(index).equals(bridgeTypes.get(index)) && (types.get(index).isPrimitive() || bridgeTypes.get(index).isPrimitive())) {
                    return false;
                }
            }
            TypeDescription returnType = getReturnType().asErasure(), bridgeReturnType = typeToken.getReturnType();
            return returnType.equals(bridgeReturnType) || (!returnType.isPrimitive() && !bridgeReturnType.isPrimitive());
        }

        @Override
        public int hashCode() {
            int hashCode = 17 + getDeclaringType().hashCode();
            hashCode = 31 * hashCode + getInternalName().hashCode();
            hashCode = 31 * hashCode + getReturnType().asErasure().hashCode();
            return 31 * hashCode + getParameters().asTypeList().asErasures().hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof MethodDescription)) {
                return false;
            }
            MethodDescription methodDescription = (MethodDescription) other;
            return getInternalName().equals(methodDescription.getInternalName())
                    && getDeclaringType().equals(methodDescription.getDeclaringType())
                    && getReturnType().asErasure().equals(methodDescription.getReturnType().asErasure())
                    && getParameters().asTypeList().asErasures().equals(methodDescription.getParameters().asTypeList().asErasures());
        }

        /**
         * {@inheritDoc}
         */
        public String toGenericString() {
            StringBuilder stringBuilder = new StringBuilder();
            int modifiers = getModifiers() & SOURCE_MODIFIERS;
            if (modifiers != EMPTY_MASK) {
                stringBuilder.append(Modifier.toString(modifiers)).append(' ');
            }
            if (isMethod()) {
                stringBuilder.append(getReturnType().getActualName()).append(' ');
                stringBuilder.append(getDeclaringType().asErasure().getActualName()).append('.');
            }
            stringBuilder.append(getName()).append('(');
            boolean first = true;
            for (TypeDescription.Generic typeDescription : getParameters().asTypeList()) {
                if (!first) {
                    stringBuilder.append(',');
                } else {
                    first = false;
                }
                stringBuilder.append(typeDescription.getActualName());
            }
            stringBuilder.append(')');
            TypeList.Generic exceptionTypes = getExceptionTypes();
            if (!exceptionTypes.isEmpty()) {
                stringBuilder.append(" throws ");
                first = true;
                for (TypeDescription.Generic typeDescription : exceptionTypes) {
                    if (!first) {
                        stringBuilder.append(',');
                    } else {
                        first = false;
                    }
                    stringBuilder.append(typeDescription.getActualName());
                }
            }
            return stringBuilder.toString();
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            int modifiers = getModifiers() & SOURCE_MODIFIERS;
            if (modifiers != EMPTY_MASK) {
                stringBuilder.append(Modifier.toString(modifiers)).append(' ');
            }
            if (isMethod()) {
                stringBuilder.append(getReturnType().asErasure().getActualName()).append(' ');
                stringBuilder.append(getDeclaringType().asErasure().getActualName()).append('.');
            }
            stringBuilder.append(getName()).append('(');
            boolean first = true;
            for (TypeDescription typeDescription : getParameters().asTypeList().asErasures()) {
                if (!first) {
                    stringBuilder.append(',');
                } else {
                    first = false;
                }
                stringBuilder.append(typeDescription.getActualName());
            }
            stringBuilder.append(')');
            TypeList exceptionTypes = getExceptionTypes().asErasures();
            if (!exceptionTypes.isEmpty()) {
                stringBuilder.append(" throws ");
                first = true;
                for (TypeDescription typeDescription : exceptionTypes) {
                    if (!first) {
                        stringBuilder.append(',');
                    } else {
                        first = false;
                    }
                    stringBuilder.append(typeDescription.getActualName());
                }
            }
            return stringBuilder.toString();
        }
    }

    /**
     * An implementation of a method description for a loaded constructor.
     */
    class ForLoadedConstructor extends InDefinedShape.AbstractBase implements ParameterDescription.ForLoadedParameter.ParameterAnnotationSource {

        /**
         * The loaded constructor that is represented by this instance.
         */
        private final Constructor<?> constructor;

        /**
         * Creates a new immutable method description for a loaded constructor.
         *
         * @param constructor The loaded constructor to be represented by this method description.
         */
        public ForLoadedConstructor(Constructor<?> constructor) {
            this.constructor = constructor;
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription getDeclaringType() {
            return TypeDescription.ForLoadedType.of(constructor.getDeclaringClass());
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReturnType() {
            return TypeDescription.Generic.VOID;
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("parameters")
        public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
            return ParameterList.ForLoadedExecutable.of(constructor, this);
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getExceptionTypes() {
            return new TypeList.Generic.OfConstructorExceptionTypes(constructor);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstructor() {
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isTypeInitializer() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Method method) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Constructor<?> constructor) {
            return this.constructor.equals(constructor) || equals(new MethodDescription.ForLoadedConstructor(constructor));
        }

        /**
         * {@inheritDoc}
         */
        public String getName() {
            return constructor.getName();
        }

        /**
         * {@inheritDoc}
         */
        public int getModifiers() {
            return constructor.getModifiers();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isSynthetic() {
            return constructor.isSynthetic();
        }

        /**
         * {@inheritDoc}
         */
        public String getInternalName() {
            return CONSTRUCTOR_INTERNAL_NAME;
        }

        /**
         * {@inheritDoc}
         */
        public String getDescriptor() {
            return Type.getConstructorDescriptor(constructor);
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationValue<?, ?> getDefaultValue() {
            return AnnotationValue.UNDEFINED;
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("declaredAnnotations")
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.ForLoadedAnnotations(constructor.getDeclaredAnnotations());
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getTypeVariables() {
            return TypeList.Generic.ForLoadedTypes.OfTypeVariables.of(constructor);
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReceiverType() {
            TypeDescription.Generic receiverType = TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveReceiverType(constructor);
            return receiverType == null
                    ? super.getReceiverType()
                    : receiverType;
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("parameterAnnotations")
        public Annotation[][] getParameterAnnotations() {
            return constructor.getParameterAnnotations();
        }
    }

    /**
     * An implementation of a method description for a loaded method.
     */
    class ForLoadedMethod extends InDefinedShape.AbstractBase implements ParameterDescription.ForLoadedParameter.ParameterAnnotationSource {

        /**
         * The loaded method that is represented by this instance.
         */
        private final Method method;

        /**
         * Creates a new immutable method description for a loaded method.
         *
         * @param method The loaded method to be represented by this method description.
         */
        public ForLoadedMethod(Method method) {
            this.method = method;
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription getDeclaringType() {
            return TypeDescription.ForLoadedType.of(method.getDeclaringClass());
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReturnType() {
            if (TypeDescription.AbstractBase.RAW_TYPES) {
                return TypeDescription.Generic.OfNonGenericType.ForLoadedType.of(method.getReturnType());
            }
            return new TypeDescription.Generic.LazyProjection.ForLoadedReturnType(method);
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("parameters")
        public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
            return ParameterList.ForLoadedExecutable.of(method, this);
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getExceptionTypes() {
            if (TypeDescription.AbstractBase.RAW_TYPES) {
                return new TypeList.Generic.ForLoadedTypes(method.getExceptionTypes());
            }
            return new TypeList.Generic.OfMethodExceptionTypes(method);
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstructor() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isTypeInitializer() {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public boolean isBridge() {
            return method.isBridge();
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Method method) {
            return this.method.equals(method) || equals(new MethodDescription.ForLoadedMethod(method));
        }

        /**
         * {@inheritDoc}
         */
        public boolean represents(Constructor<?> constructor) {
            return false;
        }

        /**
         * {@inheritDoc}
         */
        public String getName() {
            return method.getName();
        }

        /**
         * {@inheritDoc}
         */
        public int getModifiers() {
            return method.getModifiers();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isSynthetic() {
            return method.isSynthetic();
        }

        /**
         * {@inheritDoc}
         */
        public String getInternalName() {
            return method.getName();
        }

        /**
         * {@inheritDoc}
         */
        public String getDescriptor() {
            return Type.getMethodDescriptor(method);
        }

        /**
         * Returns the loaded method that is represented by this method description.
         *
         * @return The loaded method that is represented by this method description.
         */
        public Method getLoadedMethod() {
            return method;
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("declaredAnnotations")
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.ForLoadedAnnotations(method.getDeclaredAnnotations());
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationValue<?, ?> getDefaultValue() {
            Object value = method.getDefaultValue();
            return value == null
                    ? AnnotationValue.UNDEFINED
                    : AnnotationDescription.ForLoadedAnnotation.asValue(value, method.getReturnType());
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getTypeVariables() {
            if (TypeDescription.AbstractBase.RAW_TYPES) {
                return new TypeList.Generic.Empty();
            }
            return TypeList.Generic.ForLoadedTypes.OfTypeVariables.of(method);
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReceiverType() {
            if (TypeDescription.AbstractBase.RAW_TYPES) {
                return super.getReceiverType();
            }
            TypeDescription.Generic receiverType = TypeDescription.Generic.AnnotationReader.DISPATCHER.resolveReceiverType(method);
            return receiverType == null
                    ? super.getReceiverType()
                    : receiverType;
        }

        /**
         * {@inheritDoc}
         */
        @CachedReturnPlugin.Enhance("parameterAnnotations")
        public Annotation[][] getParameterAnnotations() {
            return method.getParameterAnnotations();
        }
    }

    /**
     * A latent method description describes a method that is not attached to a declaring
     * {@link TypeDescription}.
     */
    class Latent extends InDefinedShape.AbstractBase {

        /**
         * The type that is declaring this method.
         */
        private final TypeDescription declaringType;

        /**
         * The internal name of this method.
         */
        private final String internalName;

        /**
         * The modifiers of this method.
         */
        private final int modifiers;

        /**
         * A tokenized list representing the method's type variables.
         */
        private final List<? extends TypeVariableToken> typeVariables;

        /**
         * The return type of this method.
         */
        private final TypeDescription.Generic returnType;

        /**
         * The parameter tokens describing this method.
         */
        private final List<? extends ParameterDescription.Token> parameterTokens;

        /**
         * This method's exception types.
         */
        private final List<? extends TypeDescription.Generic> exceptionTypes;

        /**
         * The annotations of this method.
         */
        private final List<? extends AnnotationDescription> declaredAnnotations;

        /**
         * The default value of this method or {@code null} if no default annotation value is defined.
         */
        private final AnnotationValue<?, ?> defaultValue;

        /**
         * The receiver type of this method or {@code null} if the receiver type is defined implicitly.
         */
        private final TypeDescription.Generic receiverType;

        /**
         * Creates a new latent method description. All provided types are attached to this instance before they are returned.
         *
         * @param declaringType The declaring type of the method.
         * @param token         A token representing the method's shape.
         */
        public Latent(TypeDescription declaringType, MethodDescription.Token token) {
            this(declaringType,
                    token.getName(),
                    token.getModifiers(),
                    token.getTypeVariableTokens(),
                    token.getReturnType(),
                    token.getParameterTokens(),
                    token.getExceptionTypes(),
                    token.getAnnotations(),
                    token.getDefaultValue(),
                    token.getReceiverType());
        }

        /**
         * Creates a new latent method description. All provided types are attached to this instance before they are returned.
         *
         * @param declaringType       The type that is declaring this method.
         * @param internalName        The internal name of this method.
         * @param modifiers           The modifiers of this method.
         * @param typeVariables       The type variables of the described method.
         * @param returnType          The return type of this method.
         * @param parameterTokens     The parameter tokens describing this method.
         * @param exceptionTypes      This method's exception types.
         * @param declaredAnnotations The annotations of this method.
         * @param defaultValue        The default value of this method or {@code null} if no default annotation value is defined.
         * @param receiverType        The receiver type of this method or {@code null} if the receiver type is defined implicitly.
         */
        public Latent(TypeDescription declaringType,
                      String internalName,
                      int modifiers,
                      List<? extends TypeVariableToken> typeVariables,
                      TypeDescription.Generic returnType,
                      List<? extends ParameterDescription.Token> parameterTokens,
                      List<? extends TypeDescription.Generic> exceptionTypes,
                      List<? extends AnnotationDescription> declaredAnnotations,
                      AnnotationValue<?, ?> defaultValue,
                      TypeDescription.Generic receiverType) {
            this.declaringType = declaringType;
            this.internalName = internalName;
            this.modifiers = modifiers;
            this.typeVariables = typeVariables;
            this.returnType = returnType;
            this.parameterTokens = parameterTokens;
            this.exceptionTypes = exceptionTypes;
            this.declaredAnnotations = declaredAnnotations;
            this.defaultValue = defaultValue;
            this.receiverType = receiverType;
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getTypeVariables() {
            return TypeList.Generic.ForDetachedTypes.attachVariables(this, typeVariables);
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReturnType() {
            return returnType.accept(TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(this));
        }

        /**
         * {@inheritDoc}
         */
        public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
            return new ParameterList.ForTokens(this, parameterTokens);
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getExceptionTypes() {
            return TypeList.Generic.ForDetachedTypes.attach(this, exceptionTypes);
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationList getDeclaredAnnotations() {
            return new AnnotationList.Explicit(declaredAnnotations);
        }

        /**
         * {@inheritDoc}
         */
        public String getInternalName() {
            return internalName;
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription getDeclaringType() {
            return declaringType;
        }

        /**
         * {@inheritDoc}
         */
        public int getModifiers() {
            return modifiers;
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationValue<?, ?> getDefaultValue() {
            return defaultValue;
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReceiverType() {
            return receiverType == null
                    ? super.getReceiverType()
                    : receiverType.accept(TypeDescription.Generic.Visitor.Substitutor.ForAttachment.of(this));
        }

        /**
         * A method description that represents the type initializer.
         */
        public static class TypeInitializer extends InDefinedShape.AbstractBase {

            /**
             * The type for which the type initializer should be represented.
             */
            private final TypeDescription typeDescription;

            /**
             * Creates a new method description representing the type initializer.
             *
             * @param typeDescription The type for which the type initializer should be represented.
             */
            public TypeInitializer(TypeDescription typeDescription) {
                this.typeDescription = typeDescription;
            }

            /**
             * {@inheritDoc}
             */
            public TypeDescription.Generic getReturnType() {
                return TypeDescription.Generic.VOID;
            }

            /**
             * {@inheritDoc}
             */
            public ParameterList<ParameterDescription.InDefinedShape> getParameters() {
                return new ParameterList.Empty<ParameterDescription.InDefinedShape>();
            }

            /**
             * {@inheritDoc}
             */
            public TypeList.Generic getExceptionTypes() {
                return new TypeList.Generic.Empty();
            }

            /**
             * {@inheritDoc}
             */
            public AnnotationValue<?, ?> getDefaultValue() {
                return AnnotationValue.UNDEFINED;
            }

            /**
             * {@inheritDoc}
             */
            public TypeList.Generic getTypeVariables() {
                return new TypeList.Generic.Empty();
            }

            /**
             * {@inheritDoc}
             */
            public AnnotationList getDeclaredAnnotations() {
                return new AnnotationList.Empty();
            }

            /**
             * {@inheritDoc}
             */
            public TypeDescription getDeclaringType() {
                return typeDescription;
            }

            /**
             * {@inheritDoc}
             */
            public int getModifiers() {
                return TYPE_INITIALIZER_MODIFIER;
            }

            /**
             * {@inheritDoc}
             */
            public String getInternalName() {
                return MethodDescription.TYPE_INITIALIZER_INTERNAL_NAME;
            }
        }
    }

    /**
     * A method description that represents a given method but with substituted method types.
     */
    class TypeSubstituting extends AbstractBase implements InGenericShape {

        /**
         * The type that declares this type-substituted method.
         */
        private final TypeDescription.Generic declaringType;

        /**
         * The represented method description.
         */
        private final MethodDescription methodDescription;

        /**
         * A visitor that is applied to the method type.
         */
        private final TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor;

        /**
         * Creates a method description with substituted method types.
         *
         * @param declaringType     The type that is declaring the substituted method.
         * @param methodDescription The represented method description.
         * @param visitor           A visitor that is applied to the method type.
         */
        public TypeSubstituting(TypeDescription.Generic declaringType,
                                MethodDescription methodDescription,
                                TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            this.declaringType = declaringType;
            this.methodDescription = methodDescription;
            this.visitor = visitor;
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReturnType() {
            return methodDescription.getReturnType().accept(visitor);
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getTypeVariables() {
            return methodDescription.getTypeVariables().accept(visitor).filter(ElementMatchers.ofSort(TypeDefinition.Sort.VARIABLE));
        }

        /**
         * {@inheritDoc}
         */
        public ParameterList<ParameterDescription.InGenericShape> getParameters() {
            return new ParameterList.TypeSubstituting(this, methodDescription.getParameters(), visitor);
        }

        /**
         * {@inheritDoc}
         */
        public TypeList.Generic getExceptionTypes() {
            return new TypeList.Generic.ForDetachedTypes(methodDescription.getExceptionTypes(), visitor);
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationValue<?, ?> getDefaultValue() {
            return methodDescription.getDefaultValue();
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getReceiverType() {
            TypeDescription.Generic receiverType = methodDescription.getReceiverType();
            return receiverType == null
                    ? TypeDescription.Generic.UNDEFINED
                    : receiverType.accept(visitor);
        }

        /**
         * {@inheritDoc}
         */
        public AnnotationList getDeclaredAnnotations() {
            return methodDescription.getDeclaredAnnotations();
        }

        /**
         * {@inheritDoc}
         */
        public TypeDescription.Generic getDeclaringType() {
            return declaringType;
        }

        /**
         * {@inheritDoc}
         */
        public int getModifiers() {
            return methodDescription.getModifiers();
        }

        /**
         * {@inheritDoc}
         */
        public String getInternalName() {
            return methodDescription.getInternalName();
        }

        /**
         * {@inheritDoc}
         */
        public InDefinedShape asDefined() {
            return methodDescription.asDefined();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isConstructor() {
            return methodDescription.isConstructor();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isMethod() {
            return methodDescription.isMethod();
        }

        /**
         * {@inheritDoc}
         */
        public boolean isTypeInitializer() {
            return methodDescription.isTypeInitializer();
        }
    }

    /**
     * A token representing a method's properties detached from a type.
     */
    class Token implements ByteCodeElement.Token<Token> {

        /**
         * The internal name of the represented method.
         */
        private final String name;

        /**
         * The modifiers of the represented method.
         */
        private final int modifiers;

        /**
         * A list of tokens representing the method's type variables.
         */
        private final List<? extends TypeVariableToken> typeVariableTokens;

        /**
         * The return type of the represented method.
         */
        private final TypeDescription.Generic returnType;

        /**
         * The parameter tokens of the represented method.
         */
        private final List<? extends ParameterDescription.Token> parameterTokens;

        /**
         * The exception types of the represented method.
         */
        private final List<? extends TypeDescription.Generic> exceptionTypes;

        /**
         * The annotations of the represented method.
         */
        private final List<? extends AnnotationDescription> annotations;

        /**
         * The default value of the represented method or {@code null} if no such value exists.
         */
        private final AnnotationValue<?, ?> defaultValue;

        /**
         * The receiver type of the represented method or {@code null} if the receiver type is implicit.
         */
        private final TypeDescription.Generic receiverType;

        /**
         * Creates a new method token representing a constructor without any parameters, exception types, type variables or annotations.
         * All types must be represented in an detached format.
         *
         * @param modifiers The constructor's modifiers.
         */
        public Token(int modifiers) {
            this(MethodDescription.CONSTRUCTOR_INTERNAL_NAME, modifiers, TypeDescription.Generic.VOID);
        }

        /**
         * Creates a new method token representing a method without any parameters, exception types, type variables or annotations.
         * All types must be represented in an detached format.
         *
         * @param name       The name of the method.
         * @param modifiers  The modifiers of the method.
         * @param returnType The return type of the method.
         */
        public Token(String name, int modifiers, TypeDescription.Generic returnType) {
            this(name, modifiers, returnType, Collections.<TypeDescription.Generic>emptyList());
        }

        /**
         * Creates a new method token with simple values. All types must be represented in an detached format.
         *
         * @param name           The internal name of the represented method.
         * @param modifiers      The modifiers of the represented method.
         * @param returnType     The return type of the represented method.
         * @param parameterTypes The parameter types of this method.
         */
        public Token(String name, int modifiers, TypeDescription.Generic returnType, List<? extends TypeDescription.Generic> parameterTypes) {
            this(name,
                    modifiers,
                    Collections.<TypeVariableToken>emptyList(),
                    returnType,
                    new ParameterDescription.Token.TypeList(parameterTypes),
                    Collections.<TypeDescription.Generic>emptyList(),
                    Collections.<AnnotationDescription>emptyList(),
                    AnnotationValue.UNDEFINED,
                    TypeDescription.Generic.UNDEFINED);
        }

        /**
         * Creates a new token for a method description. All types must be represented in an detached format.
         *
         * @param name               The internal name of the represented method.
         * @param modifiers          The modifiers of the represented method.
         * @param typeVariableTokens The type variables of the the represented method.
         * @param returnType         The return type of the represented method.
         * @param parameterTokens    The parameter tokens of the represented method.
         * @param exceptionTypes     The exception types of the represented method.
         * @param annotations        The annotations of the represented method.
         * @param defaultValue       The default value of the represented method or {@code null} if no such value exists.
         * @param receiverType       The receiver type of the represented method or {@code null} if the receiver type is implicit.
         */
        public Token(String name,
                     int modifiers,
                     List<? extends TypeVariableToken> typeVariableTokens,
                     TypeDescription.Generic returnType,
                     List<? extends ParameterDescription.Token> parameterTokens,
                     List<? extends TypeDescription.Generic> exceptionTypes,
                     List<? extends AnnotationDescription> annotations,
                     AnnotationValue<?, ?> defaultValue,
                     TypeDescription.Generic receiverType) {
            this.name = name;
            this.modifiers = modifiers;
            this.typeVariableTokens = typeVariableTokens;
            this.returnType = returnType;
            this.parameterTokens = parameterTokens;
            this.exceptionTypes = exceptionTypes;
            this.annotations = annotations;
            this.defaultValue = defaultValue;
            this.receiverType = receiverType;
        }

        /**
         * Returns the internal name of the represented method.
         *
         * @return The internal name of the represented method.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the modifiers of the represented method.
         *
         * @return The modifiers of the represented method.
         */
        public int getModifiers() {
            return modifiers;
        }

        /**
         * Returns the type variables of this method token.
         *
         * @return A a list of tokens representing the method's type variables.
         */
        public TokenList<TypeVariableToken> getTypeVariableTokens() {
            return new TokenList<TypeVariableToken>(typeVariableTokens);
        }

        /**
         * Returns the return type of the represented method.
         *
         * @return The return type of the represented method.
         */
        public TypeDescription.Generic getReturnType() {
            return returnType;
        }

        /**
         * Returns the parameter tokens of the represented method.
         *
         * @return The parameter tokens of the represented method.
         */
        public TokenList<ParameterDescription.Token> getParameterTokens() {
            return new TokenList<ParameterDescription.Token>(parameterTokens);
        }

        /**
         * Returns the exception types of the represented method.
         *
         * @return The exception types of the represented method.
         */
        public TypeList.Generic getExceptionTypes() {
            return new TypeList.Generic.Explicit(exceptionTypes);
        }

        /**
         * Returns the annotations of the represented method.
         *
         * @return The annotations of the represented method.
         */
        public AnnotationList getAnnotations() {
            return new AnnotationList.Explicit(annotations);
        }

        /**
         * Returns the default value of the represented method.
         *
         * @return The default value of the represented method or {@code null} if no such value exists.
         */
        public AnnotationValue<?, ?> getDefaultValue() {
            return defaultValue;
        }

        /**
         * Returns the receiver type of this token or {@code null} if the receiver type is implicit.
         *
         * @return The receiver type of this token or {@code null} if the receiver type is implicit.
         */
        public TypeDescription.Generic getReceiverType() {
            return receiverType;
        }

        /**
         * {@inheritDoc}
         */
        public Token accept(TypeDescription.Generic.Visitor<? extends TypeDescription.Generic> visitor) {
            return new Token(name,
                    modifiers,
                    getTypeVariableTokens().accept(visitor),
                    returnType.accept(visitor),
                    getParameterTokens().accept(visitor),
                    getExceptionTypes().accept(visitor),
                    annotations,
                    defaultValue,
                    receiverType == null
                            ? TypeDescription.Generic.UNDEFINED
                            : receiverType.accept(visitor));
        }

        /**
         * Creates a signature token that represents the method that is represented by this token.
         *
         * @param declaringType The declaring type of the method that this token represents.
         * @return A signature token representing this token.
         */
        public SignatureToken asSignatureToken(TypeDescription declaringType) {
            TypeDescription.Generic.Visitor<TypeDescription> visitor = new TypeDescription.Generic.Visitor.Reducing(declaringType, typeVariableTokens);
            List<TypeDescription> parameters = new ArrayList<TypeDescription>(parameterTokens.size());
            for (ParameterDescription.Token parameter : parameterTokens) {
                parameters.add(parameter.getType().accept(visitor));
            }
            return new SignatureToken(name, returnType.accept(visitor), parameters);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + modifiers;
            result = 31 * result + typeVariableTokens.hashCode();
            result = 31 * result + returnType.hashCode();
            result = 31 * result + parameterTokens.hashCode();
            result = 31 * result + exceptionTypes.hashCode();
            result = 31 * result + annotations.hashCode();
            result = 31 * result + (defaultValue != null ? defaultValue.hashCode() : 0);
            result = 31 * result + (receiverType != null ? receiverType.hashCode() : 0);
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (other == null || getClass() != other.getClass()) {
                return false;
            }
            Token token = (Token) other;
            return modifiers == token.modifiers
                    && name.equals(token.name)
                    && typeVariableTokens.equals(token.typeVariableTokens)
                    && returnType.equals(token.returnType)
                    && parameterTokens.equals(token.parameterTokens)
                    && exceptionTypes.equals(token.exceptionTypes)
                    && annotations.equals(token.annotations)
                    && (defaultValue != null ? defaultValue.equals(token.defaultValue) : token.defaultValue == null)
                    && (receiverType != null ? receiverType.equals(token.receiverType) : token.receiverType == null);
        }

        @Override
        public String toString() {
            return "MethodDescription.Token{" +
                    "name='" + name + '\'' +
                    ", modifiers=" + modifiers +
                    ", typeVariableTokens=" + typeVariableTokens +
                    ", returnType=" + returnType +
                    ", parameterTokens=" + parameterTokens +
                    ", exceptionTypes=" + exceptionTypes +
                    ", annotations=" + annotations +
                    ", defaultValue=" + defaultValue +
                    ", receiverType=" + receiverType +
                    '}';
        }
    }

    /**
     * A token representing a method's name and raw return and parameter types.
     */
    class SignatureToken {

        /**
         * The internal name of the represented method.
         */
        private final String name;

        /**
         * The represented method's raw return type.
         */
        private final TypeDescription returnType;

        /**
         * The represented method's raw parameter types.
         */
        private final List<? extends TypeDescription> parameterTypes;

        /**
         * Creates a new type token.
         *
         * @param name           The internal name of the represented method.
         * @param returnType     The represented method's raw return type.
         * @param parameterTypes The represented method's raw parameter types.
         */
        public SignatureToken(String name, TypeDescription returnType, List<? extends TypeDescription> parameterTypes) {
            this.name = name;
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
        }

        /**
         * Returns the internal name of the represented method.
         *
         * @return The internal name of the represented method.
         */
        public String getName() {
            return name;
        }

        /**
         * Returns this token's return type.
         *
         * @return This token's return type.
         */
        public TypeDescription getReturnType() {
            return returnType;
        }

        /**
         * Returns this token's parameter types.
         *
         * @return This token's parameter types.
         */
        @SuppressWarnings("unchecked")
        public List<TypeDescription> getParameterTypes() {
            return (List<TypeDescription>) parameterTypes;
        }

        /**
         * Returns this signature token as a type token.
         *
         * @return This signature token as a type token.
         */
        public TypeToken asTypeToken() {
            return new TypeToken(returnType, parameterTypes);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + returnType.hashCode();
            result = 31 * result + parameterTypes.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof SignatureToken)) {
                return false;
            }
            SignatureToken signatureToken = (SignatureToken) other;
            return name.equals(signatureToken.name)
                    && returnType.equals(signatureToken.returnType)
                    && parameterTypes.equals(signatureToken.parameterTypes);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder().append(returnType).append(' ').append(name).append('(');
            boolean first = true;
            for (TypeDescription parameterType : parameterTypes) {
                if (first) {
                    first = false;
                } else {
                    stringBuilder.append(',');
                }
                stringBuilder.append(parameterType);
            }
            return stringBuilder.append(')').toString();
        }
    }

    /**
     * A token representing a method's erased return and parameter types.
     */
    class TypeToken {

        /**
         * The represented method's raw return type.
         */
        private final TypeDescription returnType;

        /**
         * The represented method's raw parameter types.
         */
        private final List<? extends TypeDescription> parameterTypes;

        /**
         * Creates a new type token.
         *
         * @param returnType     The represented method's raw return type.
         * @param parameterTypes The represented method's raw parameter types.
         */
        public TypeToken(TypeDescription returnType, List<? extends TypeDescription> parameterTypes) {
            this.returnType = returnType;
            this.parameterTypes = parameterTypes;
        }

        /**
         * Returns this token's return type.
         *
         * @return This token's return type.
         */
        public TypeDescription getReturnType() {
            return returnType;
        }

        /**
         * Returns this token's parameter types.
         *
         * @return This token's parameter types.
         */
        @SuppressWarnings("unchecked")
        public List<TypeDescription> getParameterTypes() {
            return (List<TypeDescription>) parameterTypes;
        }

        @Override
        public int hashCode() {
            int result = returnType.hashCode();
            result = 31 * result + parameterTypes.hashCode();
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            } else if (!(other instanceof TypeToken)) {
                return false;
            }
            TypeToken typeToken = (TypeToken) other;
            return returnType.equals(typeToken.returnType) && parameterTypes.equals(typeToken.parameterTypes);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder().append('(');
            for (TypeDescription parameterType : parameterTypes) {
                stringBuilder.append(parameterType.getDescriptor());
            }
            return stringBuilder.append(')').append(returnType.getDescriptor()).toString();
        }
    }
}
