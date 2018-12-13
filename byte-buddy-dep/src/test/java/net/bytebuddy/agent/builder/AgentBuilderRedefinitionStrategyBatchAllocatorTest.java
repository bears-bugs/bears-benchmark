package net.bytebuddy.agent.builder;

import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static net.bytebuddy.test.utility.FieldByFieldComparison.hasPrototype;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AgentBuilderRedefinitionStrategyBatchAllocatorTest {

    @Test
    public void testForTotalEmpty() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = AgentBuilder.RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE;
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Collections.<Class<?>>emptyList()).iterator();
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testForTotal() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = AgentBuilder.RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE;
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testForFixed() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(2);
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testForFixedFactory() throws Exception {
        assertThat(AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize.ofSize(1),
                hasPrototype((AgentBuilder.RedefinitionStrategy.BatchAllocator) new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(1)));
        assertThat(AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize.ofSize(0),
                hasPrototype((AgentBuilder.RedefinitionStrategy.BatchAllocator) AgentBuilder.RedefinitionStrategy.BatchAllocator.ForTotal.INSTANCE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForFixedFactoryIllegal() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize.ofSize(-1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGrouping() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class),
                ElementMatchers.is(Void.class)).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testEmptyGrouping() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class))
                .batch(Collections.<Class<?>>emptyList()).iterator();
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMinimum() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class),
                ElementMatchers.is(Void.class)).withMinimum(3).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMinimumExcess() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class),
                ElementMatchers.is(Void.class)).withMinimum(10).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMinimumChunked() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class),
                ElementMatchers.is(Void.class)).withMinimum(2).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void testMinimumCannotRemove() throws Exception {
        new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class))
                .withMinimum(2)
                .batch(Collections.<Class<?>>singletonList(Object.class))
                .iterator()
                .remove();
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void testMinimumNonPositive() throws Exception {
        new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)).withMinimum(0);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMaximum() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)
                .or(ElementMatchers.is(Void.class))).withMaximum(1).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMaximumExcess() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)
                .or(ElementMatchers.is(Void.class))).withMaximum(10).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMaximumChunked() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.any())
                .withMaximum(2)
                .batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class))
                .iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void testMaximumCannotRemove() throws Exception {
        new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class))
                .withMaximum(2)
                .batch(Collections.<Class<?>>singletonList(Object.class))
                .iterator()
                .remove();
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("unchecked")
    public void testMaximumNonPositive() throws Exception {
        new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)).withMaximum(0);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRange() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)
                .or(ElementMatchers.is(Void.class))).withinRange(1, 1).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRangeExcess() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class)
                .or(ElementMatchers.is(Void.class))).withinRange(1, 10).batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class)).iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRangeChunked() throws Exception {
        Iterator<? extends List<Class<?>>> batches = new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.any())
                .withinRange(1, 2)
                .batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class))
                .iterator();
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(batches.hasNext(), is(true));
        assertThat(batches.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(batches.hasNext(), is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    @SuppressWarnings("unchecked")
    public void testRangeCannotRemove() throws Exception {
        new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForMatchedGrouping(ElementMatchers.is(Object.class))
                .withinRange(1, 2)
                .batch(Collections.<Class<?>>singletonList(Object.class))
                .iterator()
                .remove();
    }

    @Test
    public void testPartitioningWithoutReminder() throws Exception {
        Iterator<? extends List<Class<?>>> iterator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Partitioning(2)
                .batch(Arrays.<Class<?>>asList(Object.class, Void.class))
                .iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testPartitioningWithReminder() throws Exception {
        Iterator<? extends List<Class<?>>> iterator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Partitioning(2)
                .batch(Arrays.<Class<?>>asList(Object.class, Void.class, String.class))
                .iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.<Class<?>>asList(Object.class, Void.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testPartitioningWithReminderAndNoRegularPartition() throws Exception {
        Iterator<? extends List<Class<?>>> iterator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Partitioning(2)
                .batch(Collections.<Class<?>>singletonList(Object.class))
                .iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testPartitioningEmpty() throws Exception {
        Iterator<? extends List<Class<?>>> iterator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Partitioning(2)
                .batch(Collections.<Class<?>>emptyList())
                .iterator();
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSlicingUnderflow() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Slicing(1,
                1,
                new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(2));
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.asList(Object.class, String.class, Void.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Object.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(String.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSlicingOverflow() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Slicing(3,
                3,
                new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(2));
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.asList(Object.class, String.class, Void.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.asList(Object.class, String.class, Void.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSlicingDynamicMatch() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Slicing(1,
                3,
                new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(2));
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.asList(Object.class, String.class, Void.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.asList(Object.class, String.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testSlicingMatch() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator batchAllocator = new AgentBuilder.RedefinitionStrategy.BatchAllocator.Slicing(2,
                2,
                new AgentBuilder.RedefinitionStrategy.BatchAllocator.ForFixedSize(2));
        Iterator<? extends List<Class<?>>> iterator = batchAllocator.batch(Arrays.asList(Object.class, String.class, Void.class)).iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Arrays.asList(Object.class, String.class)));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(Collections.<Class<?>>singletonList(Void.class)));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPartitioningIllegalArgument() throws Exception {
        AgentBuilder.RedefinitionStrategy.BatchAllocator.Partitioning.of(0);
    }
}
