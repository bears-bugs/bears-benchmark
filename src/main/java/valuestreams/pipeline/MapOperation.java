package valuestreams.pipeline;

import java.util.function.Function;

public class MapOperation<T, R> implements Operation<T, R> {
    private final Function<T, R> mapper;

    public MapOperation(Function<T, R> mapper) {
        this.mapper = mapper;
    }

    @Override
    public OperationType getType() {
        return OperationType.MAP;
    }

    @Override
    public R apply(T value) {
        return mapper.apply(value);
    }
}
