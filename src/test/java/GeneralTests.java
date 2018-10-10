import org.junit.jupiter.api.Test;
import valuestreams.Value;

import static org.junit.jupiter.api.Assertions.*;

public class GeneralTests {
    @Test
    void testCreation() {
        assertNotNull(Value.of("test"));
        assertFalse(Value.of("test").isEmpty());
        assertTrue(Value.empty().isEmpty());
    }

    @Test
    void testValidation() {
        assertFalse(Value.of("test").validate(s -> true).isEmpty());
        assertTrue(Value.of("test").validate(s -> false).isEmpty());
    }

    @Test
    void testMapping() {
        assertFalse(Value.of("test").map(String::length).validate(l -> l == 4).isEmpty());
        assertTrue(Value.of("test").map(String::length).validate(l -> l != 4).isEmpty());
        assertTrue(Value.of("test").validate(s -> false).map(String::length).isEmpty());
    }

    @Test
    void testGet() throws Exception {
        assertEquals(1, Value.of(1).getValueOrThrow(Exception.class, "unknown").intValue());
        assertTrue(Value.of(1).toOptional().isPresent());
        assertFalse(Value.empty().toOptional().isPresent());
    }
}
