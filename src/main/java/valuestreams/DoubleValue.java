package valuestreams;

public class DoubleValue extends NumericalValue<Double> {
    public DoubleValue(Double value) {
        super(value);
    }

    public DoubleValue() {
    }

    public static DoubleValue of(double value) {
        return new DoubleValue(value);
    }

    public static DoubleValue empty() {
        return new DoubleValue();
    }

    public static DoubleValue fromString(String str) {
        try {
            return DoubleValue.of(Double.parseDouble(str));
        } catch (NumberFormatException ignored) {
            return DoubleValue.empty();
        }
    }
}
