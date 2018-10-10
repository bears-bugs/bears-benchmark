import org.junit.jupiter.api.Test;
import valuestreams.StringValue;

import static org.junit.jupiter.api.Assertions.*;

public class StringTests {
    @Test
    void testValidation() {
        assertFalse(StringValue.of("test").matches("\\w+").isEmpty());
        assertTrue(StringValue.of("test").matches("\\s").isEmpty());

        assertFalse(StringValue.of("test").lengthAtLeast(3).isEmpty());
        assertTrue(StringValue.of("test").lengthAtLeast(5).isEmpty());

        assertFalse(StringValue.of("test").lengthAtMost(4).isEmpty());
        assertTrue(StringValue.of("test").lengthAtMost(3).isEmpty());

        assertFalse(StringValue.of("test").lengthBetween(3, 5).isEmpty());
        assertFalse(StringValue.of("test").lengthBetween(2, 4).isEmpty());
        assertTrue(StringValue.of("test").lengthBetween(5, 6).isEmpty());

        assertFalse(StringValue.of("test").contains("t").isEmpty());
        assertTrue(StringValue.of("test").contains("a").isEmpty());

        assertFalse(StringValue.of("test").isEqualTo("test").isEmpty());
        assertTrue(StringValue.of("test").isEqualTo("hello").isEmpty());
    }

    @Test
    void testMapping() {
        assertFalse(StringValue.of("test").map(String::toUpperCase).isEqualTo("TEST").isEmpty());
        assertFalse(StringValue.of("test").asGenericValue().map(String::length).validate(i -> i == 4).isEmpty());
    }
}
