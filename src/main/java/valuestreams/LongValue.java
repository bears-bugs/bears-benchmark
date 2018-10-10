package valuestreams;

public class LongValue extends NumericalValue<Long> {
    public LongValue(Long value) {
        super(value);
    }

    public LongValue() {
    }

    public static LongValue of(Long value) {
        return new LongValue(value);
    }

    public static LongValue empty() {
        return new LongValue();
    }

    public static LongValue fromString(String str) {
        try {
            return LongValue.of(Long.parseLong(str));
        } catch (NumberFormatException ignored) {
            return LongValue.empty();
        }
    }
}
