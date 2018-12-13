/*
 * Copyright 2011-2018 the original author or authors.
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
package io.lettuce.core.protocol;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.channels.ClosedChannelException;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import edu.umd.cs.mtc.MultithreadedTestCase;
import edu.umd.cs.mtc.TestFramework;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.RedisException;
import io.lettuce.core.codec.Utf8StringCodec;
import io.lettuce.core.internal.LettuceFactories;
import io.lettuce.core.output.StatusOutput;
import io.lettuce.core.resource.ClientResources;
import io.lettuce.test.ConnectionTestUtil;
import io.netty.channel.*;
import io.netty.handler.codec.EncoderException;
import io.netty.util.concurrent.ImmediateEventExecutor;

/**
 * @author Mark Paluch
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultEndpointUnitTests {

    private Queue<RedisCommand<String, String, ?>> queue = LettuceFactories.newConcurrentQueue(1000);

    private DefaultEndpoint sut;

    private final Command<String, String, String> command = new Command<>(CommandType.APPEND, new StatusOutput<>(
            new Utf8StringCodec()), null);

    @Mock
    private Channel channel;

    @Mock
    private ConnectionFacade connectionFacade;

    @Mock
    private ConnectionWatchdog connectionWatchdog;

    @Mock
    private ClientResources clientResources;

    private ChannelPromise promise;

    @BeforeAll
    static void beforeClass() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CommandHandler.class.getName());
        loggerConfig.setLevel(Level.ALL);
    }

    @AfterAll
    static void afterClass() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext();
        Configuration config = ctx.getConfiguration();
        LoggerConfig loggerConfig = config.getLoggerConfig(CommandHandler.class.getName());
        loggerConfig.setLevel(null);
    }

    @BeforeEach
    void before() {

        promise = new DefaultChannelPromise(channel);
        when(channel.writeAndFlush(any())).thenAnswer(invocation -> {
            if (invocation.getArguments()[0] instanceof RedisCommand) {
                queue.add((RedisCommand) invocation.getArguments()[0]);
            }

            if (invocation.getArguments()[0] instanceof Collection) {
                queue.addAll((Collection) invocation.getArguments()[0]);
            }
            return promise;
        });

        when(channel.write(any())).thenAnswer(invocation -> {
            if (invocation.getArguments()[0] instanceof RedisCommand) {
                queue.add((RedisCommand) invocation.getArguments()[0]);
            }

            if (invocation.getArguments()[0] instanceof Collection) {
                queue.addAll((Collection) invocation.getArguments()[0]);
            }
            return promise;
        });

        sut = new DefaultEndpoint(ClientOptions.create(), clientResources);
        sut.setConnectionFacade(connectionFacade);
    }

    @Test
    void writeConnectedShouldWriteCommandToChannel() {

        when(channel.isActive()).thenReturn(true);

        sut.notifyChannelActive(channel);
        sut.write(command);

        assertThat(ConnectionTestUtil.getQueueSize(sut)).isEqualTo(1);
        verify(channel).writeAndFlush(command);
    }

    @Test
    void writeDisconnectedShouldBufferCommands() {

        sut.write(command);

        assertThat(ConnectionTestUtil.getDisconnectedBuffer(sut)).contains(command);

        verify(channel, never()).writeAndFlush(any());
    }

    @Test
    void notifyChannelActiveActivatesFacade() {

        sut.notifyChannelActive(channel);

        verify(connectionFacade).activated();
    }

    @Test
    void notifyChannelActiveArmsConnectionWatchdog() {

        sut.registerConnectionWatchdog(connectionWatchdog);

        sut.notifyChannelActive(channel);

        verify(connectionWatchdog).arm();
    }

    @Test
    void notifyChannelInactiveDeactivatesFacade() {

        sut.notifyChannelInactive(channel);

        verify(connectionFacade).deactivated();
    }

    @Test
    void notifyExceptionShouldStoreException() {

        sut.notifyException(new IllegalStateException());
        sut.write(command);

        assertThat(command.exception).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void notifyChannelActiveClearsStoredException() {

        sut.notifyException(new IllegalStateException());
        sut.notifyChannelActive(channel);
        sut.write(command);

        assertThat(command.exception).isNull();
    }

    @Test
    void notifyDrainQueuedCommandsShouldBufferCommands() {

        Queue<RedisCommand<?, ?, ?>> q = LettuceFactories.newConcurrentQueue(100);
        q.add(command);

        sut.notifyDrainQueuedCommands(() -> q);

        assertThat(ConnectionTestUtil.getDisconnectedBuffer(sut)).contains(command);
        verify(channel, never()).write(any());
    }

    @Test
    void notifyDrainQueuedCommandsShouldWriteCommands() {

        when(channel.isActive()).thenReturn(true);

        Queue<RedisCommand<?, ?, ?>> q = LettuceFactories.newConcurrentQueue(100);
        q.add(command);

        sut.notifyChannelActive(channel);
        sut.notifyDrainQueuedCommands(() -> q);

        verify(channel).write(command);
        verify(channel).flush();

    }

    @Test
    void shouldCancelCommandsOnEncoderException() {

        when(channel.isActive()).thenReturn(true);
        sut.notifyChannelActive(channel);

        DefaultChannelPromise promise = new DefaultChannelPromise(channel, ImmediateEventExecutor.INSTANCE);

        when(channel.writeAndFlush(any())).thenAnswer(invocation -> {
            if (invocation.getArguments()[0] instanceof RedisCommand) {
                queue.add((RedisCommand) invocation.getArguments()[0]);
            }

            if (invocation.getArguments()[0] instanceof Collection) {
                queue.addAll((Collection) invocation.getArguments()[0]);
            }
            return promise;
        });

        promise.setFailure(new EncoderException("foo"));

        sut.write(command);

        assertThat(command.exception).isInstanceOf(EncoderException.class);
    }

    @Test
    void writeShouldRejectCommandsInDisconnectedState() {

        sut = new DefaultEndpoint(ClientOptions.builder() //
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS) //
                .build(), clientResources);

        try {
            sut.write(command);
            fail("Missing RedisException");
        } catch (RedisException e) {
            assertThat(e).hasMessageContaining("Commands are rejected");
        }
    }

    @Test
    void writeShouldRejectCommandsInClosedState() {

        sut.close();

        try {
            sut.write(command);
            fail("Missing RedisException");
        } catch (RedisException e) {
            assertThat(e).hasMessageContaining("Connection is closed");
        }
    }

    @Test
    void writeWithoutAutoReconnectShouldRejectCommandsInDisconnectedState() {

        sut = new DefaultEndpoint(ClientOptions.builder() //
                .autoReconnect(false) //
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.DEFAULT) //
                .build(), clientResources);

        try {
            sut.write(command);
            fail("Missing RedisException");
        } catch (RedisException e) {
            assertThat(e).hasMessageContaining("Commands are rejected");
        }
    }

    @Test
    void closeCleansUpResources() {

        ChannelFuture future = mock(ChannelFuture.class);
        when(future.isSuccess()).thenReturn(true);
        when(channel.close()).thenReturn(future);

        sut.notifyChannelActive(channel);
        sut.registerConnectionWatchdog(connectionWatchdog);

        sut.close();

        verify(channel).close();
        verify(connectionWatchdog).prepareClose();
    }

    @Test
    void closeAllowsOnlyOneCall() {

        ChannelFuture future = mock(ChannelFuture.class);
        when(future.isSuccess()).thenReturn(true);
        when(channel.close()).thenReturn(future);

        sut.notifyChannelActive(channel);
        sut.registerConnectionWatchdog(connectionWatchdog);

        sut.close();
        sut.close();

        verify(channel).close();
        verify(connectionWatchdog).prepareClose();
    }

    @Test
    void retryListenerCompletesSuccessfullyAfterDeferredRequeue() {

        DefaultEndpoint.RetryListener listener = DefaultEndpoint.RetryListener.newInstance(sut, command);

        ChannelFuture future = mock(ChannelFuture.class);
        EventLoop eventLoopGroup = mock(EventLoop.class);

        when(future.isSuccess()).thenReturn(false);
        when(future.cause()).thenReturn(new ClosedChannelException());
        when(channel.eventLoop()).thenReturn(eventLoopGroup);
        when(channel.close()).thenReturn(mock(ChannelFuture.class));

        sut.notifyChannelActive(channel);
        sut.closeAsync();

        listener.operationComplete(future);

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(eventLoopGroup).submit(runnableCaptor.capture());

        runnableCaptor.getValue().run();

        assertThat(command.exception).isInstanceOf(RedisException.class);
    }

    @Test
    void retryListenerDoesNotRetryCompletedCommands() {

        DefaultEndpoint.RetryListener listener = DefaultEndpoint.RetryListener.newInstance(sut, command);

        when(channel.eventLoop()).thenReturn(mock(EventLoop.class));

        command.complete();
        promise.tryFailure(new Exception());

        listener.operationComplete(promise);

        verify(channel, never()).writeAndFlush(command);
    }

    @Test
    void testMTCConcurrentConcurrentWrite() throws Throwable {
        TestFramework.runOnce(new MTCConcurrentConcurrentWrite(command, clientResources));
    }

    /**
     * Test of concurrent access to locks. Two concurrent writes.
     */
    static class MTCConcurrentConcurrentWrite extends MultithreadedTestCase {

        private final Command<String, String, String> command;
        private TestableEndpoint handler;

        MTCConcurrentConcurrentWrite(Command<String, String, String> command, ClientResources clientResources) {

            this.command = command;

            handler = new TestableEndpoint(ClientOptions.create(), clientResources) {

                @Override
                protected <C extends RedisCommand<?, ?, T>, T> void writeToBuffer(C command) {

                    waitForTick(2);

                    Object sharedLock = ReflectionTestUtils.getField(this, "sharedLock");
                    AtomicLong writers = (AtomicLong) ReflectionTestUtils.getField(sharedLock, "writers");
                    assertThat(writers.get()).isEqualTo(2);
                    waitForTick(3);
                    super.writeToBuffer(command);
                }
            };
        }

        public void thread1() {

            waitForTick(1);
            handler.write(command);
        }

        public void thread2() {

            waitForTick(1);
            handler.write(command);
        }
    }

    static class TestableEndpoint extends DefaultEndpoint {

        /**
         * Create a new {@link DefaultEndpoint}.
         *
         * @param clientOptions client options for this connection, must not be {@literal null}.
         * @param clientResources client resources for this connection, must not be {@literal null}.
         */
        TestableEndpoint(ClientOptions clientOptions, ClientResources clientResources) {
            super(clientOptions, clientResources);
        }
    }
}
