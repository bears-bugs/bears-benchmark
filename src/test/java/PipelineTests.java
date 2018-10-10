import org.junit.jupiter.api.Test;
import valuestreams.Value;
import valuestreams.pipeline.Pipeline;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PipelineTests {
    @Test
    void basicUsage() {
        Pipeline<String, String> pipeline = Pipeline.input(String.class)
                .map(Integer::valueOf)
                .validate(i -> i > 10)
                .map(Object::toString);

        Value<String> success = pipeline.apply("12");
        Value<String> failIntegerParse = pipeline.apply("a");
        Value<String> failValidation = pipeline.apply("5");

        assertTrue(success.isPresent());
        assertEquals("12", success.getNullable());

        assertFalse(failIntegerParse.isPresent());
        assertFalse(failValidation.isPresent());
    }

    @Test
    void extendingPipeline() {
        Pipeline<String, Integer> base = Pipeline.input(String.class)
                .map(Integer::valueOf)
                .validate(i -> i > 1);

        Pipeline<String, Integer> square = base.map(i -> i*i);
        Pipeline<String, Integer> cube = base.map(i -> i*i*i);

        Value<Integer> squared = square.apply("5");
        Value<Integer> cubed = cube.apply("5");

        assertTrue(squared.isPresent());
        assertTrue(cubed.isPresent());

        assertTrue(squared.getNullable().intValue() != cubed.getNullable().intValue());
    }
}
