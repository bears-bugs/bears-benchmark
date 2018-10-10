package valuestreams.pipeline;

import java.util.function.Predicate;

public class ValidateOperation<T> implements Operation<T, T> {
    private final Predicate<T> validator;

    public ValidateOperation(Predicate<T> validator) {
        this.validator = validator;
    }

    @Override
    public OperationType getType() {
        return OperationType.VALIDATE;
    }

    @Override
    public T apply(T value) {
        return validator.test(value) ? value : null;
    }
}
