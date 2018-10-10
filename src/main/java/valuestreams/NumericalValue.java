package valuestreams;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class NumericalValue<T extends Number & Comparable<T>> extends AbstractValue<T> {
    protected NumericalValue(T value) {
        super(value);
    }

    protected NumericalValue() {
    }

    protected static <T extends Number & Comparable<T>> NumericalValue<T> of(T value) {
        return new NumericalValue<>(value);
    }

    protected static <T extends Number & Comparable<T>> NumericalValue<T> fromOptional(Optional<T> optional) {
        return new NumericalValue<>(optional.orElse(null));
    }

    protected static <T extends Number & Comparable<T>> NumericalValue<T> empty() {
        return new NumericalValue<T>();
    }

    public NumericalValue<T> validate(Predicate<T> validator) {
        if (!isEmpty()) {
            this.value = validator.test(value) ? value : null;
        }

        return this;
    }

    public NumericalValue<T> map(Function<T, T> mapper) {
        Objects.requireNonNull(mapper);

        if (!isEmpty()) {
            this.value = Objects.requireNonNull(mapper.apply(this.value));
        }

        return this;
    }

    public <R extends Number & Comparable<R>> NumericalValue<R> castMap(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return isEmpty() ? NumericalValue.empty() : NumericalValue.of(Objects.requireNonNull(mapper.apply(this.value)));
    }

    public NumericalValue<T> isEqualTo(T other) {
        return validate(s -> s.equals(other));
    }

    public NumericalValue<T> lessThan(T upperLimit) {
        return validate(v -> v.compareTo(upperLimit) < 0);
    }

    public NumericalValue<T> lessThanOrEquals(T upperLimit) {
        return validate(v -> v.compareTo(upperLimit) <= 0);
    }

    public NumericalValue<T> greaterThan(T lowerLimit) {
        return validate(v -> v.compareTo(lowerLimit) > 0);
    }

    public NumericalValue<T> greaterThanOrEquals(T lowerLimit) {
        return validate(v -> v.compareTo(lowerLimit) >= 0);
    }

    public NumericalValue<T> betweenInclusive(T lowerLimit, T upperLimit) {
        return validate(v -> v.compareTo(upperLimit) <= 0 && v.compareTo(lowerLimit) >= 0);
    }

    public NumericalValue<T> betweenExclusive(T lowerLimit, T upperLimit) {
        return validate(v -> v.compareTo(upperLimit) < 0 && v.compareTo(lowerLimit) > 0);
    }
}
