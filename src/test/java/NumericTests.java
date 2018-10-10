import org.junit.jupiter.api.Test;
import valuestreams.DoubleValue;
import valuestreams.IntegerValue;
import valuestreams.NumericalValue;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class NumericTests {

    @Test
    void testValue() {
        Supplier<NumericalValue<Integer>> reset = () -> IntegerValue.of(450);
        NumericalValue<Integer> subject = reset.get();
        assertNotNull(subject);

        assertFalse(subject.isEmpty());
        assertTrue(IntegerValue.empty().isEmpty());

        // no failure
        assertFalse(subject.lessThan(500).isEmpty());
        assertFalse(subject.greaterThan(200).isEmpty());
        assertFalse(subject.lessThanOrEquals(450).lessThanOrEquals(451).isEmpty());
        assertFalse(subject.greaterThanOrEquals(450).isEmpty());
        assertFalse(subject.isEqualTo(450).isEmpty());
        assertFalse(subject.betweenInclusive(450, 452).isEmpty());
        assertFalse(subject.betweenExclusive(449, 452).isEmpty());
        assertFalse(subject.validate(i -> true).isEmpty());

        // yes failure
        assertTrue(subject.isEqualTo(455).isEmpty());
        subject = reset.get();

        assertTrue(subject.greaterThan(450).isEmpty());
        subject = reset.get();

        assertTrue(subject.betweenExclusive(450, 452).isEmpty());
        subject = reset.get();

        assertTrue(subject.lessThan(200).isEmpty());
        subject = reset.get();

        assertTrue(subject.lessThan(500).greaterThan(450).isEmpty());
        subject = reset.get();

        assertTrue(subject.validate(i -> false).isEmpty());
    }

    @Test
    void testMapping() {
        assertFalse(IntegerValue.of(2).map(i -> i * 5).isEqualTo(10).isEmpty());
    }

    @Test
    void testCastMapping() {
        IntegerValue integerValue = IntegerValue.of(1);
        DoubleValue doubleValue = DoubleValue.of(1.5);

        assertFalse(integerValue.castMap(i -> i + 0.2D).isEqualTo(1.2).isEmpty());
        assertFalse(integerValue.castMap(Long::valueOf).isEqualTo(1L).isEmpty());
        assertFalse(doubleValue.castMap(Double::intValue).isEqualTo(1).isEmpty());
    }
}
