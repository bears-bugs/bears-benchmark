package valuestreams.pipeline;

public interface Operation<T, R> {
    OperationType getType();
    R apply(T value);
}
