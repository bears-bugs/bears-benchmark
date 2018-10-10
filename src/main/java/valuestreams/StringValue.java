package valuestreams;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public class StringValue extends AbstractValue<String> {
    private StringValue(String value) {
        super(value);
    }

    private StringValue() {
    }

    public static StringValue of(String value) {
        return new StringValue(value);
    }

    public static StringValue empty() {
        return new StringValue();
    }

    public StringValue validate(Predicate<String> validator) {
        if (!isEmpty()) {
            this.value = validator.test(this.value) ? this.value : null;
        }

        return this;
    }

    public StringValue matches(String regex) {
        return this.validate(s -> s.matches(regex));
    }

    public StringValue contains(CharSequence sequence) {
        return this.validate(s -> s.contains(sequence));
    }

    public StringValue lengthAtLeast(int min) {
        return this.validate(s -> s.length() >= min);
    }

    public StringValue lengthAtMost(int max) {
        return this.validate(s -> s.length() <= max);
    }

    public StringValue lengthBetween(int min, int max) {
        return this.validate(s -> s.length() >= min && s.length() <= max);
    }

    public StringValue map(Function<String, String> mapper) {
        Objects.requireNonNull(mapper);

        if (!isEmpty()) {
            this.value = Objects.requireNonNull(mapper.apply(this.value));
        }

        return this;
    }

    public StringValue isEqualTo(String other) {
        return validate(s -> s.equals(other));
    }

    public Value<String> asGenericValue() {
        return isEmpty() ? Value.empty() : Value.of(this.value);
    }

    public DoubleValue asDouble() {
        if (isEmpty()) {
            return DoubleValue.empty();
        }

        return DoubleValue.fromString(this.value);
    }

    public IntegerValue asInteger() {
        if (isEmpty()) {
            return IntegerValue.empty();
        }

        return IntegerValue.fromString(this.value);
    }

    public LongValue asLong() {
        if (isEmpty()) {
            return LongValue.empty();
        }

        return LongValue.fromString(this.value);
    }
}
