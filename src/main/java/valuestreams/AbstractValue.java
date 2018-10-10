package valuestreams;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;

/**
 * An wrapper around a value of type T. This class
 * provides only the basic functionality of checking
 * presence/absence of a value, and value retrieval
 * methods. Value checking and transformation functions
 * are provided by the sub-classes which extend this
 * one.
 * @param <T> The type of the encapsulated value.
 */
abstract class AbstractValue<T> {
    protected T value;

    protected AbstractValue(T value) {
        this.value = Objects.requireNonNull(value);
    }

    protected AbstractValue() { this.value = null; }

    /**
     * Checks the presence of a value (i.e. not null).
     * @return True if the value is not null, false
     * otherwise.
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * Checks the absence of a value (i.e. null).
     * @return False if the value is null, true
     * otherwise.
     */
    public boolean isEmpty() {
        return value == null;
    }

    /**
     * Gets the underlying value of this container or
     * raises an exception if none is found.
     * @param exceptionClass The exception to be thrown
     *                       if the value is absent.
     * @param message A message to be supplied to the
     *                exception.
     * @param <E> An exception to throw in case of failure.
     * @return The encapsulated value, if present.
     * @throws E If the value is missing.
     */
    public <E extends Exception> T getValueOrThrow(Class<E> exceptionClass, String message) throws E {
        if (isEmpty()) {
            try {
                Constructor<E> constructor = exceptionClass.getConstructor(String.class);
                throw constructor.newInstance(message);
            } catch (IllegalAccessException | InstantiationException |
                    NoSuchMethodException | InvocationTargetException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
        return value;
    }

    /**
     * Gets the underlying value of this container.
     * @return The encapsulated value, if present. Null
     * otherwise.
     */
    public T getNullable() {
        return this.value;
    }

    /**
     * Converts the value to an optional.
     * @return Optional instance containing the encapsulated
     * value, or an empty Optional if none is present.
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(this.value);
    }

    /**
     * Wraps the value in a Value instance.
     * @return A Value instance which containing the
     * encapsulated value, or an empty Value
     * if no value is present.
     */
    public Value<T> asGenericValue() {
        return isEmpty() ? Value.empty() : Value.of(this.value);
    }

    /**
     * The value-stream equivalent of toString().
     * @return A StringValue instance containing the
     * encapsulated value, or an empty StringValue
     * if no value is present.
     */
    public StringValue asStringValue() {
        return StringValue.of(this.value.toString());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + (value == null ? "null" : value.toString()) + "]";
    }
}
