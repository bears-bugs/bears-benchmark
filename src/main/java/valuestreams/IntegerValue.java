package valuestreams;

public class IntegerValue extends NumericalValue<Integer> {
    public IntegerValue(Integer value) {
        super(value);
    }

    public IntegerValue() {
    }

    public static IntegerValue of(Integer value) {
        return new IntegerValue(value);
    }

    public static IntegerValue empty() {
        return new IntegerValue();
    }

    public static IntegerValue fromString(String str) {
        try {
            return IntegerValue.of(Integer.parseInt(str));
        } catch (NumberFormatException ignored) {
            return IntegerValue.empty();
        }
    }
}
