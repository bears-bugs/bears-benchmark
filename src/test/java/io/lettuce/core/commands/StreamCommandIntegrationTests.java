/*
 * Copyright 2018 the original author or authors.
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
package io.lettuce.core.commands;

import static io.lettuce.core.protocol.CommandType.XINFO;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.*;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import io.lettuce.core.*;
import io.lettuce.core.XReadArgs.StreamOffset;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.models.stream.PendingMessage;
import io.lettuce.core.models.stream.PendingParser;
import io.lettuce.core.output.NestedMultiOutput;
import io.lettuce.core.protocol.CommandArgs;
import io.lettuce.test.LettuceExtension;
import io.lettuce.test.condition.EnabledOnCommand;

/**
 * @author Mark Paluch
 */
@ExtendWith(LettuceExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@EnabledOnCommand("XADD")
public class StreamCommandIntegrationTests extends TestSupport {

    private final RedisCommands<String, String> redis;

    @Inject
    protected StreamCommandIntegrationTests(RedisCommands<String, String> redis) {
        this.redis = redis;
    }

    @BeforeEach
    void setUp() {
        this.redis.flushall();
    }

    @Test
    void xadd() {

        assertThat(redis.xadd(key, Collections.singletonMap("key", "value"))).endsWith("-0");
        assertThat(redis.xadd(key, "foo", "bar")).isNotEmpty();
    }

    @Test
    void xaddMaxLen() {

        String id = redis.xadd(key, XAddArgs.Builder.maxlen(5), "foo", "bar");

        for (int i = 0; i < 5; i++) {
            redis.xadd(key, XAddArgs.Builder.maxlen(5), "foo", "bar");
        }

        List<StreamMessage<String, String>> messages = redis.xrange(key,
                Range.from(Range.Boundary.including(id), Range.Boundary.unbounded()));

        assertThat(messages).hasSize(5);
    }

    @Test
    void xaddMaxLenEfficientTrimming() {

        String id = redis.xadd(key, XAddArgs.Builder.maxlen(5).approximateTrimming(), "foo", "bar");

        assertThat(id).isNotNull();
    }

    @Test
    void xdel() {

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ids.add(redis.xadd(key, Collections.singletonMap("key", "value")));
        }

        Long deleted = redis.xdel(key, ids.get(0), "123456-0");

        assertThat(deleted).isEqualTo(1);

        List<StreamMessage<String, String>> messages = redis.xrange(key, Range.unbounded());
        assertThat(messages).hasSize(1);
    }

    @Test
    void xtrim() {

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ids.add(redis.xadd(key, Collections.singletonMap("key", "value")));
        }
        redis.xdel(key, ids.get(0), ids.get(2));
        assertThat(redis.xlen(key)).isBetween(8L, 10L);

        redis.xtrim(key, true, 8);

        assertThat(redis.xlen(key)).isLessThanOrEqualTo(10);
    }

    @Test
    void xrange() {

        List<String> ids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {

            Map<String, String> body = new HashMap<>();
            body.put("key-1", "value-1-" + i);
            body.put("key-2", "value-2-" + i);

            ids.add(redis.xadd(key, body));
        }

        List<StreamMessage<String, String>> messages = redis.xrange(key, Range.unbounded());
        assertThat(messages).hasSize(5);

        StreamMessage<String, String> message = messages.get(0);

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("key-1", "value-1-0");
        expectedBody.put("key-2", "value-2-0");

        assertThat(message.getId()).contains("-");
        assertThat(message.getStream()).isEqualTo(key);
        assertThat(message.getBody()).isEqualTo(expectedBody);

        assertThat(redis.xrange(key, Range.unbounded(), Limit.from(2))).hasSize(2);

        List<StreamMessage<String, String>> range = redis.xrange(key, Range.create(ids.get(0), ids.get(1)));

        assertThat(range).hasSize(2);
        assertThat(range.get(0).getBody()).isEqualTo(expectedBody);
    }

    @Test
    void xrevrange() {

        for (int i = 0; i < 5; i++) {

            Map<String, String> body = new HashMap<>();
            body.put("key-1", "value-1-" + i);
            body.put("key-2", "value-2-" + i);

            redis.xadd(key, body);
        }

        List<StreamMessage<String, String>> messages = redis.xrevrange(key, Range.unbounded());
        assertThat(messages).hasSize(5);

        StreamMessage<String, String> message = messages.get(0);

        Map<String, String> expectedBody = new HashMap<>();
        expectedBody.put("key-1", "value-1-4");
        expectedBody.put("key-2", "value-2-4");

        assertThat(message.getId()).contains("-");
        assertThat(message.getStream()).isEqualTo(key);
        assertThat(message.getBody()).isEqualTo(expectedBody);
    }

    @Test
    void xread() {

        String initial1 = redis.xadd("stream-1", Collections.singletonMap("key1", "value1"));
        String initial2 = redis.xadd("stream-2", Collections.singletonMap("key2", "value2"));
        String message1 = redis.xadd("stream-1", Collections.singletonMap("key3", "value3"));
        String message2 = redis.xadd("stream-2", Collections.singletonMap("key4", "value4"));

        List<StreamMessage<String, String>> messages = redis.xread(StreamOffset.from("stream-1", initial1),
                StreamOffset.from("stream-2", initial2));

        StreamMessage<String, String> firstMessage = messages.get(0);

        assertThat(firstMessage.getId()).isEqualTo(message1);
        assertThat(firstMessage.getStream()).isEqualTo("stream-1");
        assertThat(firstMessage.getBody()).containsEntry("key3", "value3");

        StreamMessage<String, String> secondMessage = messages.get(1);

        assertThat(secondMessage.getId()).isEqualTo(message2);
        assertThat(secondMessage.getStream()).isEqualTo("stream-2");
        assertThat(secondMessage.getBody()).containsEntry("key4", "value4");
    }

    @Test
    void xreadTransactional() {

        String initial1 = redis.xadd("stream-1", Collections.singletonMap("key1", "value1"));
        String initial2 = redis.xadd("stream-2", Collections.singletonMap("key2", "value2"));

        redis.multi();
        redis.xadd("stream-1", Collections.singletonMap("key3", "value3"));
        redis.xadd("stream-2", Collections.singletonMap("key4", "value4"));
        redis.xread(StreamOffset.from("stream-1", initial1), XReadArgs.StreamOffset.from("stream-2", initial2));

        TransactionResult exec = redis.exec();

        String message1 = exec.get(0);
        String message2 = exec.get(1);
        List<StreamMessage<String, String>> messages = exec.get(2);

        StreamMessage<String, String> firstMessage = messages.get(0);

        assertThat(firstMessage.getId()).isEqualTo(message1);
        assertThat(firstMessage.getStream()).isEqualTo("stream-1");
        assertThat(firstMessage.getBody()).containsEntry("key3", "value3");

        StreamMessage<String, String> secondMessage = messages.get(1);

        assertThat(secondMessage.getId()).isEqualTo(message2);
        assertThat(secondMessage.getStream()).isEqualTo("stream-2");
        assertThat(secondMessage.getBody()).containsEntry("key4", "value4");
    }

    @Test
    void xgroupCreate() {

        redis.xadd(key, Collections.singletonMap("key", "value"));

        assertThat(redis.xgroupCreate(StreamOffset.latest(key), "group")).isEqualTo("OK");

        List<Object> groups = redis.dispatch(XINFO, new NestedMultiOutput<>(StringCodec.UTF8), new CommandArgs<>(
                StringCodec.UTF8).add("GROUPS").add(key));

        assertThat(groups).isNotEmpty();
    }

    @Test
    void xgroupread() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        redis.xadd(key, Collections.singletonMap("key", "value"));

        List<StreamMessage<String, String>> read1 = redis.xreadgroup(Consumer.from("group", "consumer1"),
                StreamOffset.lastConsumed(key));

        assertThat(read1).hasSize(1);
    }

    @Test
    void xpendingWithGroup() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        String id = redis.xadd(key, Collections.singletonMap("key", "value"));

        redis.xreadgroup(Consumer.from("group", "consumer1"), StreamOffset.lastConsumed(key));

        List<Object> pendingEntries = redis.xpending(key, "group");
        assertThat(pendingEntries).hasSize(4).containsSequence(1L, id, id);
    }

    @Test
    void xpending() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        String id = redis.xadd(key, Collections.singletonMap("key", "value"));

        redis.xreadgroup(Consumer.from("group", "consumer1"), StreamOffset.lastConsumed(key));

        List<Object> pendingEntries = redis.xpending(key, "group", Range.unbounded(), Limit.from(10));

        List<PendingMessage> pendingMessages = PendingParser.parseRange(pendingEntries);
        assertThat(pendingMessages).hasSize(1);

        PendingMessage message = pendingMessages.get(0);
        assertThat(message.getId()).isEqualTo(id);
        assertThat(message.getConsumer()).isEqualTo("consumer1");
        assertThat(message.getRedeliveryCount()).isEqualTo(1);
    }

    @Test
    void xack() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        redis.xadd(key, Collections.singletonMap("key", "value"));

        List<StreamMessage<String, String>> messages = redis.xreadgroup(Consumer.from("group", "consumer1"),
                StreamOffset.lastConsumed(key));

        Long ackd = redis.xack(key, "group", messages.get(0).getId());
        assertThat(ackd).isEqualTo(1);

        List<Object> pendingEntries = redis.xpending(key, "group", Range.unbounded(), Limit.from(10));
        assertThat(pendingEntries).isEmpty();
    }

    @Test
    void xclaim() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        redis.xadd(key, Collections.singletonMap("key", "value"));

        List<StreamMessage<String, String>> messages = redis.xreadgroup(Consumer.from("group", "consumer1"),
                StreamOffset.lastConsumed(key));

        List<StreamMessage<String, String>> claimedMessages = redis.xclaim(key, Consumer.from("group", "consumer2"), 0,
                messages.get(0).getId());

        assertThat(claimedMessages).hasSize(1).contains(messages.get(0));

        assertThat(redis.xpending(key, Consumer.from("group", "consumer1"), Range.unbounded(), Limit.from(10))).isEmpty();
        assertThat(redis.xpending(key, Consumer.from("group", "consumer2"), Range.unbounded(), Limit.from(10))).hasSize(1);
    }

    @Test
    void xclaimWithArgs() {

        String id1 = redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        String id2 = redis.xadd(key, Collections.singletonMap("key", "value"));

        List<StreamMessage<String, String>> messages = redis.xreadgroup(Consumer.from("group", "consumer1"),
                StreamOffset.lastConsumed(key));

        List<StreamMessage<String, String>> claimedMessages = redis.xclaim(key, Consumer.from("group", "consumer2"),
                XClaimArgs.Builder.minIdleTime(0).time(Instant.now().minusSeconds(60)), id1, id2);

        assertThat(claimedMessages).hasSize(1).contains(messages.get(0));

        List<Object> xpending = redis.xpending(key, Consumer.from("group", "consumer2"), Range.unbounded(), Limit.from(10));

        List<PendingMessage> pendingMessages = PendingParser.parseRange(xpending);
        PendingMessage message = pendingMessages.get(0);

        assertThat(message.getMsSinceLastDelivery()).isBetween(50000L, 80000L);
    }

    @Test
    void xgroupDestroy() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");

        assertThat(redis.xgroupDestroy(key, "group")).isTrue();
        assertThat(redis.xgroupDestroy(key, "group")).isFalse();
    }

    @Test
    void xgroupDelconsumer() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");
        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xreadgroup(Consumer.from("group", "consumer1"), StreamOffset.lastConsumed(key));

        assertThat(redis.xgroupDelconsumer(key, Consumer.from("group", "consumer1"))).isTrue();
        assertThat(redis.xgroupDelconsumer(key, Consumer.from("group", "consumer1"))).isFalse();
    }

    @Test
    void xgroupSetid() {

        redis.xadd(key, Collections.singletonMap("key", "value"));
        redis.xgroupCreate(StreamOffset.latest(key), "group");

        assertThat(redis.xgroupSetid(StreamOffset.latest(key), "group")).isEqualTo("OK");
    }
}
