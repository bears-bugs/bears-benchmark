package valuestreams.pipeline;

import valuestreams.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/*
 * This implementation follows the example given in
 * https://stackoverflow.com/a/8681806
 */

public class Pipeline<I, O> {
    private List<Operation<?, ?>> operations;

    private Pipeline() {}

    /**
     * Initializes a pipeline with the given input class type.
     * The created pipeline does not perform any transformation
     * on the value and just returns it, and its output type
     * is the same as its input type.
     * @param inputType The class of the input value
     * @param <T> The type of the input value (inferred from the
     *           class parameter)
     * @return A new pipeline
     * {@code Pipeline.input(String.class);}
     */
    public static <T> Pipeline<T, T> input(Class<T> inputType) {
        Pipeline<T, T> pipeline = new Pipeline<>();
        pipeline.operations = Collections.singletonList(new IdentityOperation<T>());
        return pipeline;
    }

    /**
     * Initializes a pipeline, but the type must be specified
     * explicitly in the function call. The created pipeline
     * does not perform any transformation on the value and
     * just returns it, and its output type is the same as
     * its input type.
     * @param <T> The type of the input value
     * @return A new pipeline
     * {@code Pipeline.<String>input()}
     */
    public static <T> Pipeline<T, T> input() {
        Pipeline<T, T> pipeline = new Pipeline<>();
        pipeline.operations = Collections.singletonList(new IdentityOperation<T>());
        return pipeline;
    }

    /**
     * Initializes a pipeline with a starting operation.
     * @param operation The first operation of the pipeline
     * @param <T> The input type of the pipeline
     * @param <R> The output type of the pipeline
     * @return A new pipeline
     * {@code Pipeline.input(new MappingOperation<String, Integer>(Integer::valueOf));}
     */
    public static <T, R> Pipeline<T, R> input(Operation<T, R> operation) {
        Pipeline<T, R> pipeline = new Pipeline<>();
        pipeline.operations = Collections.singletonList(operation);
        return pipeline;
    }

    /**
     * Creates a new pipeline which contains the operations
     * of the previous pipeline with the given operation
     * chained to the end.
     * @param operation The operation to append to the pipeline
     * @param <R> The return type of the operation
     * @return A new extended pipeline
     */
    public <R> Pipeline<I, R> chain(Operation<O, R> operation) {
        Pipeline<I, R> extendedPipeline = new Pipeline<>();
        extendedPipeline.operations = new ArrayList<>(operations);
        extendedPipeline.operations.add(operation);
        return extendedPipeline;
    }

    /**
     * Chains a map operation to the pipeline.
     * @param mapper The map function
     * @param <R> The return type of the operation
     * @return A new extended pipeline
     */
    public <R> Pipeline<I, R> map(Function<O, R> mapper) {
        return chain(new MapOperation<>(mapper));
    }

    /**
     * Chains a validate operation to the pipeline.
     * @param validator The validation predicate
     * @return A new extended pipeline
     */
    public Pipeline<I, O> validate(Predicate<O> validator) {
        return chain(new ValidateOperation<>(validator));
    }

    /**
     * Applies the pipeline on an input.
     * @param input The input value
     * @return A Value object which contains the final
     * value of the pipeline if everything succeeded, or
     * an empty one otherwise.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Value<O> apply(I input) {
        Object source = input;
        Object target = null;

        for (Operation p : operations) {
            target = p.apply(source);
            source = target;

            if (source == null) {
                break;
            }
        }

        return target != null ? Value.of((O) target) : Value.empty();
    }

    public CompletableFuture<Value<O>> applyAsync(I input) {
        return CompletableFuture.supplyAsync(() -> apply(input));
    }
}
