/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core;

import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.Function;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;
import io.lettuce.core.api.reactive.RedisHashReactiveCommands;
import io.lettuce.core.api.reactive.RedisKeyReactiveCommands;
import io.lettuce.core.api.reactive.RedisSetReactiveCommands;
import io.lettuce.core.api.reactive.RedisSortedSetReactiveCommands;
import io.lettuce.core.internal.LettuceAssert;

/**
 * Scan command support exposed through {@link Flux}.
 * <p>
 * {@link ScanStream} uses reactive command interfaces to scan over keys ({@code SCAN}), sets ({@code SSCAN}), sorted sets (
 * {@code ZSCAN}), and hashes ({@code HSCAN}).
 * <p>
 * Use {@link ScanArgs#limit(long)} to set the batch size.
 * <p>
 * Data structure scanning is progressive and stateful and demand-aware. It supports full iterations (until all received cursors
 * are exhausted) and premature termination. Subsequent scan commands to fetch the cursor data get only issued if the subscriber
 * signals demand.
 *
 * @author Mark Paluch
 * @since 5.1
 */
public abstract class ScanStream {

    private ScanStream() {
    }

    /**
     * Sequentially iterate over keys in the keyspace. This method uses {@code SCAN} to perform an iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<K> scan(RedisKeyReactiveCommands<K, V> commands) {
        return scan(commands, Optional.empty());
    }

    /**
     * Sequentially iterate over keys in the keyspace. This method uses {@code SCAN} to perform an iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param scanArgs the scan arguments, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<K> scan(RedisKeyReactiveCommands<K, V> commands, ScanArgs scanArgs) {

        LettuceAssert.notNull(scanArgs, "ScanArgs must not be null");

        return scan(commands, Optional.of(scanArgs));
    }

    private static <K, V> Flux<K> scan(RedisKeyReactiveCommands<K, V> commands, Optional<ScanArgs> scanArgs) {

        LettuceAssert.notNull(commands, "RedisKeyCommands must not be null");

        return Flux.create(sink -> {

            Mono<KeyScanCursor<K>> res = scanArgs.map(commands::scan).orElseGet(commands::scan);

            scan(sink, res, c -> scanArgs.map(it -> commands.scan(c, it)).orElseGet(() -> commands.scan(c)), //
                    KeyScanCursor::getKeys);
        });
    }

    /**
     * Sequentially iterate over entries in a hash identified by {@code key}. This method uses {@code HSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the hash to scan.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<KeyValue<K, V>> hscan(RedisHashReactiveCommands<K, V> commands, K key) {
        return hscan(commands, key, Optional.empty());
    }

    /**
     * Sequentially iterate over entries in a hash identified by {@code key}. This method uses {@code HSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the hash to scan.
     * @param scanArgs the scan arguments, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<KeyValue<K, V>> hscan(RedisHashReactiveCommands<K, V> commands, K key, ScanArgs scanArgs) {

        LettuceAssert.notNull(scanArgs, "ScanArgs must not be null");

        return hscan(commands, key, Optional.of(scanArgs));
    }

    private static <K, V> Flux<KeyValue<K, V>> hscan(RedisHashReactiveCommands<K, V> commands, K key,
            Optional<ScanArgs> scanArgs) {

        LettuceAssert.notNull(commands, "RedisHashReactiveCommands must not be null");
        LettuceAssert.notNull(key, "Key must not be null");

        return Flux.create(sink -> {

            Mono<MapScanCursor<K, V>> res = scanArgs.map(it -> commands.hscan(key, it)).orElseGet(() -> commands.hscan(key));

            scan(sink,
                    res,
                    c -> scanArgs.map(it -> commands.hscan(key, c, it)).orElseGet(() -> commands.hscan(key, c)), //
                    c -> {
                        Iterator<KeyValue<K, V>> iterator = c.getMap().entrySet().stream()
                                .map(me -> KeyValue.fromNullable(me.getKey(), me.getValue())).iterator();
                        return () -> iterator;
                    });
        });

    }

    /**
     * Sequentially iterate over elements in a set identified by {@code key}. This method uses {@code SSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the set to scan.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<V> sscan(RedisSetReactiveCommands<K, V> commands, K key) {
        return sscan(commands, key, Optional.empty());
    }

    /**
     * Sequentially iterate over elements in a set identified by {@code key}. This method uses {@code SSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the set to scan.
     * @param scanArgs the scan arguments, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<V> sscan(RedisSetReactiveCommands<K, V> commands, K key, ScanArgs scanArgs) {

        LettuceAssert.notNull(scanArgs, "ScanArgs must not be null");

        return sscan(commands, key, Optional.of(scanArgs));
    }

    private static <K, V> Flux<V> sscan(RedisSetReactiveCommands<K, V> commands, K key, Optional<ScanArgs> scanArgs) {

        LettuceAssert.notNull(commands, "RedisSetReactiveCommands must not be null");
        LettuceAssert.notNull(key, "Key must not be null");

        return Flux.create(sink -> {

            Mono<ValueScanCursor<V>> res = scanArgs.map(it -> commands.sscan(key, it)).orElseGet(() -> commands.sscan(key));

            scan(sink, res, c -> scanArgs.map(it -> commands.sscan(key, c, it)).orElseGet(() -> commands.sscan(key, c)), //
                    ValueScanCursor::getValues);
        });
    }

    /**
     * Sequentially iterate over elements in a set identified by {@code key}. This method uses {@code SSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the sorted set to scan.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<ScoredValue<V>> zscan(RedisSortedSetReactiveCommands<K, V> commands, K key) {
        return zscan(commands, key, Optional.empty());
    }

    /**
     * Sequentially iterate over elements in a set identified by {@code key}. This method uses {@code SSCAN} to perform an
     * iterative scan.
     *
     * @param commands the commands interface, must not be {@literal null}.
     * @param key the sorted set to scan.
     * @param scanArgs the scan arguments, must not be {@literal null}.
     * @param <K> Key type.
     * @param <V> Value type.
     * @return a new {@link Flux}.
     */
    public static <K, V> Flux<ScoredValue<V>> zscan(RedisSortedSetReactiveCommands<K, V> commands, K key, ScanArgs scanArgs) {

        LettuceAssert.notNull(scanArgs, "ScanArgs must not be null");

        return zscan(commands, key, Optional.of(scanArgs));
    }

    private static <K, V> Flux<ScoredValue<V>> zscan(RedisSortedSetReactiveCommands<K, V> commands, K key,
            Optional<ScanArgs> scanArgs) {

        LettuceAssert.notNull(commands, "RedisSortedSetReactiveCommands must not be null");
        LettuceAssert.notNull(key, "Key must not be null");

        return Flux.create(sink -> {

            Mono<ScoredValueScanCursor<V>> res = scanArgs.map(it -> commands.zscan(key, it)).orElseGet(
                    () -> commands.zscan(key));

            scan(sink, res, c -> scanArgs.map(it -> commands.zscan(key, c, it)).orElseGet(() -> commands.zscan(key, c)), //
                    ScoredValueScanCursor::getValues);
        });
    }

    private static <V, C extends ScanCursor> void scan(FluxSink<V> sink, Mono<C> initialCursor,
            Function<ScanCursor, Mono<C>> scanFunction, Function<C, Iterable<V>> manyMapper) {

        new SubscriptionAdapter<>(sink, initialCursor, scanFunction, manyMapper).register();
    }

    /**
     * Adapter for {@link FluxSink} to dispatch multiple {@link reactor.core.CoreSubscriber} considering subscription demand.
     *
     * @param <T> item type.
     * @param <C> cursor type.
     */
    static class SubscriptionAdapter<T, C extends ScanCursor> implements Completable {

        @SuppressWarnings("rawtypes")
        private static final AtomicReferenceFieldUpdater<SubscriptionAdapter, ScanSubscriber> SUBSCRIBER = AtomicReferenceFieldUpdater
                .newUpdater(SubscriptionAdapter.class, ScanSubscriber.class, "currentSubscription");

        @SuppressWarnings("rawtypes")
        private static final AtomicIntegerFieldUpdater<SubscriptionAdapter> STATUS = AtomicIntegerFieldUpdater.newUpdater(
                SubscriptionAdapter.class, "status");

        private static final int STATUS_ACTIVE = 0;
        private static final int STATUS_TERMINATED = 0;

        // Access via SUBSCRIBER.
        @SuppressWarnings("unused")
        private volatile ScanSubscriber<T, C> currentSubscription;
        private volatile boolean canceled;

        // Access via STATUS.
        @SuppressWarnings("unused")
        private volatile int status = STATUS_ACTIVE;

        private final FluxSink<T> sink;
        private final Context context;
        private final Mono<C> initial;
        private final Function<ScanCursor, Mono<C>> scanFunction;
        private final Function<C, Iterable<T>> manyMapper;

        SubscriptionAdapter(FluxSink<T> sink, Mono<C> initial, Function<ScanCursor, Mono<C>> scanFunction,
                Function<C, Iterable<T>> manyMapper) {

            this.sink = sink;
            this.context = sink.currentContext();
            this.initial = initial;
            this.scanFunction = scanFunction;
            this.manyMapper = manyMapper;

        }

        /**
         * Register cancel and onDemand callbacks.
         */
        public void register() {
            this.sink.onRequest(this::onDemand);
            this.sink.onCancel(this::canceled);
        }

        void onDemand(long n) {

            if (this.canceled) {
                return;
            }

            long demand = sink.requestedFromDownstream();

            ScanSubscriber<T, C> current = getCurrentSubscriber();

            if (current == null) {
                current = new ScanSubscriber<>(this, sink, context, manyMapper);
                if (SUBSCRIBER.compareAndSet(this, null, current)) {
                    initial.subscribe(current);
                }

                return;
            }

            ScanCursor cursor = current.getCursor();

            if (cursor == null) {
                return;
            }

            current.emitFromBuffer();

            if (!current.exhausted || current.canceled || demand == 0) {
                return;
            }

            if (cursor.isFinished()) {
                chunkCompleted();
                return;
            }

            Mono<C> next = scanFunction.apply(cursor);

            ScanSubscriber<T, C> nextSubscriber = new ScanSubscriber<>(this, sink, context, manyMapper);
            if (SUBSCRIBER.compareAndSet(this, current, nextSubscriber)) {
                next.subscribe(nextSubscriber);
            }
        }

        private void canceled() {

            this.canceled = true;

            ScanSubscriber<T, C> current = getCurrentSubscriber();
            if (current != null) {
                current.cancel();
            }
        }

        @Override
        public void chunkCompleted() {

            if (!canceled) {

                ScanSubscriber<T, C> current = getCurrentSubscriber();
                if (current != null) {

                    ScanCursor cursor = current.getCursor();

                    if (cursor != null) {
                        if (!cursor.isFinished() || !current.exhausted) {
                            onDemand(0);
                            return;
                        }
                    }
                }

                if (terminate()) {
                    sink.complete();
                }
            }
        }

        ScanSubscriber<T, C> getCurrentSubscriber() {
            return SUBSCRIBER.get(this);
        }

        @Override
        public void onError(Throwable throwable) {

            if (!this.canceled && terminate()) {
                sink.error(throwable);
            }
        }

        protected boolean terminate() {
            return STATUS.compareAndSet(this, STATUS_ACTIVE, STATUS_TERMINATED);
        }
    }

    /**
     * {@link reactor.core.CoreSubscriber} for a {@code SCAN} cursor.
     *
     * @param <T> item type.
     * @param <C> cursor type.
     */
    static class ScanSubscriber<T, C extends ScanCursor> extends BaseSubscriber<C> {

        @SuppressWarnings("rawtypes")
        private static final AtomicReferenceFieldUpdater<ScanSubscriber, ScanCursor> CURSOR = AtomicReferenceFieldUpdater
                .newUpdater(ScanSubscriber.class, ScanCursor.class, "cursor");

        private final Completable completable;
        private final FluxSink<T> sink;
        private final Queue<T> buffer = Operators.newQueue();
        private final Context context;
        private final Function<C, Iterable<T>> manyMapper;

        volatile boolean exhausted = false;
        volatile boolean canceled;

        // see CURSOR
        @SuppressWarnings("unused")
        private volatile C cursor;

        ScanSubscriber(Completable completable, FluxSink<T> sink, Context context, Function<C, Iterable<T>> manyMapper) {
            this.completable = completable;
            this.sink = sink;
            this.context = context;
            this.manyMapper = manyMapper;
        }

        @Override
        public Context currentContext() {
            return context;
        }

        @Override
        protected void hookOnNext(C cursor) {

            if (!CURSOR.compareAndSet(this, null, cursor)) {
                Operators.onOperatorError(this, new IllegalStateException("Cannot propagate Cursor"), cursor, context);
                return;
            }

            Iterable<T> iterable = manyMapper.apply(cursor);

            emitDirect(iterable);
        }

        /**
         * Fast-path emission that emits items directly without using an intermediate buffer. Only overflow (more items
         * available than requested) is stored in the buffer.
         *
         * @param iterable
         */
        void emitDirect(Iterable<T> iterable) {

            long demand = sink.requestedFromDownstream();
            long sent = 0;

            if (demand > 0) {
                for (T value : iterable) {

                    if (canceled) {
                        break;
                    }

                    if (demand <= sent) {
                        buffer.add(value);
                        continue;
                    }

                    sent++;

                    next(value);
                }
            }

            exhausted = buffer.isEmpty();
        }

        /**
         * Buffer-based emission polling items from the buffer and emitting until the demand is satisified or the buffer is
         * exhausted.
         */
        void emitFromBuffer() {

            long demand = sink.requestedFromDownstream();
            long sent = 0;

            if (demand > 0) {
                T value;
                while ((value = buffer.poll()) != null) {

                    if (canceled) {
                        break;
                    }

                    sent++;

                    next(value);

                    if (demand <= sent) {
                        break;
                    }
                }
            }

            exhausted = buffer.isEmpty();
        }

        private void next(T value) {
            sink.next(value);
        }

        @Override
        protected void hookOnComplete() {
            completable.chunkCompleted();
        }

        @Override
        protected void hookOnError(Throwable throwable) {
            completable.onError(throwable);
        }

        @Override
        protected void hookOnCancel() {
            this.canceled = true;
        }

        public ScanCursor getCursor() {
            return CURSOR.get(this);
        }
    }

    /**
     * Completion callback interface.
     */
    interface Completable {

        /**
         * Callback if a cursor chunk is completed.
         */
        void chunkCompleted();

        /**
         * Error callback.
         *
         * @param throwable
         */
        void onError(Throwable throwable);
    }
}
