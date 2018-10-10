package valuestreams;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A generic value wrapper which provides basic
 * validation and mapping functionality.
 * @param <T> The type of the encapsulated value.
 */
public class Value<T> extends AbstractValue<T> {

    protected Value(T value) {
        super(value);
    }

    protected Value() { super(); }

    public static <R> Value<R> of(R value) {
        return new Value<>(value);
    }

    public static <R> Value<R> empty() {
        return new Value<>();
    }

    /**
     * Applies a predicate on the value, and empties
     * it if the predicate returned false.
     * @param validator A predicate whose type matches
     *                  that of the value.
     * @return The same instance.
     */
    public Value<T> validate(Predicate<T> validator) {
        Objects.requireNonNull(validator);

        if (!isEmpty()) {
            if (!validator.test(value)) {
                value = null;
            }
        }

        return this;
    }

    /**
     * Applies a mapper on the value.
     * @param mapper The mapper to applied.
     * @param <R> The return type of the mapper.
     * @return A new instance which contains the
     * mapped value.
     */
    public <R> Value<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return isEmpty() ? Value.empty() : Value.of(mapper.apply(this.value));
    }

    /**
     * The value-stream equivalent of equals().
     * @param other The object to compare against.
     * @return This instance
     */
    public Value<T> isEqualTo(T other) {
        return validate(v -> v.equals(other));
    }
}
