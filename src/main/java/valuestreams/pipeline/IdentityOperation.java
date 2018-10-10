package valuestreams.pipeline;

public class IdentityOperation<T> implements Operation<T, T> {

    @Override
    public OperationType getType() {
        return OperationType.IDENTITY;
    }

    @Override
    public T apply(T value) {
        return value;
    }
}
