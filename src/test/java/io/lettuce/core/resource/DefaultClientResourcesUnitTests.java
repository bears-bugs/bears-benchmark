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
package io.lettuce.core.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import reactor.test.StepVerifier;
import io.lettuce.core.event.Event;
import io.lettuce.core.event.EventBus;
import io.lettuce.core.metrics.CommandLatencyCollector;
import io.lettuce.core.metrics.DefaultCommandLatencyCollectorOptions;
import io.lettuce.test.Futures;
import io.lettuce.test.resource.FastShutdown;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.Timer;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;

/**
 * @author Mark Paluch
 */
class DefaultClientResourcesUnitTests {

    @Test
    void testDefaults() throws Exception {

        DefaultClientResources sut = DefaultClientResources.create();

        assertThat(sut.commandLatencyCollector()).isNotNull();
        assertThat(sut.commandLatencyCollector().isEnabled()).isTrue();

        EventExecutorGroup eventExecutors = sut.eventExecutorGroup();
        NioEventLoopGroup eventLoopGroup = sut.eventLoopGroupProvider().allocate(NioEventLoopGroup.class);

        eventExecutors.next().submit(mock(Runnable.class));
        eventLoopGroup.next().submit(mock(Runnable.class));

        assertThat(sut.shutdown(0, 0, TimeUnit.SECONDS).get()).isTrue();

        assertThat(eventExecutors.isTerminated()).isTrue();
        assertThat(eventLoopGroup.isTerminated()).isTrue();

        Future<Boolean> shutdown = sut.eventLoopGroupProvider().shutdown(0, 0, TimeUnit.SECONDS);
        assertThat(shutdown.get()).isTrue();

        assertThat(sut.commandLatencyCollector().isEnabled()).isFalse();
    }

    @Test
    void testBuilder() throws Exception {

        DefaultClientResources sut = DefaultClientResources.builder().ioThreadPoolSize(4).computationThreadPoolSize(4)
                .commandLatencyCollectorOptions(DefaultCommandLatencyCollectorOptions.disabled()).build();

        EventExecutorGroup eventExecutors = sut.eventExecutorGroup();
        NioEventLoopGroup eventLoopGroup = sut.eventLoopGroupProvider().allocate(NioEventLoopGroup.class);

        assertThat(eventExecutors.iterator()).hasSize(4);
        assertThat(eventLoopGroup.executorCount()).isEqualTo(4);
        assertThat(sut.ioThreadPoolSize()).isEqualTo(4);
        assertThat(sut.commandLatencyCollector()).isNotNull();
        assertThat(sut.commandLatencyCollector().isEnabled()).isFalse();

        assertThat(sut.shutdown(0, 0, TimeUnit.MILLISECONDS).get()).isTrue();
    }

    @Test
    void testDnsResolver() {

        DirContextDnsResolver dirContextDnsResolver = new DirContextDnsResolver("8.8.8.8");

        DefaultClientResources sut = DefaultClientResources.builder().dnsResolver(dirContextDnsResolver).build();

        assertThat(sut.dnsResolver()).isEqualTo(dirContextDnsResolver);
    }

    @Test
    void testProvidedResources() {

        EventExecutorGroup executorMock = mock(EventExecutorGroup.class);
        EventLoopGroupProvider groupProviderMock = mock(EventLoopGroupProvider.class);
        Timer timerMock = mock(Timer.class);
        EventBus eventBusMock = mock(EventBus.class);
        CommandLatencyCollector latencyCollectorMock = mock(CommandLatencyCollector.class);
        NettyCustomizer nettyCustomizer = mock(NettyCustomizer.class);

        DefaultClientResources sut = DefaultClientResources.builder().eventExecutorGroup(executorMock)
                .eventLoopGroupProvider(groupProviderMock).timer(timerMock).eventBus(eventBusMock)
                .commandLatencyCollector(latencyCollectorMock).nettyCustomizer(nettyCustomizer).build();

        assertThat(sut.eventExecutorGroup()).isSameAs(executorMock);
        assertThat(sut.eventLoopGroupProvider()).isSameAs(groupProviderMock);
        assertThat(sut.timer()).isSameAs(timerMock);
        assertThat(sut.eventBus()).isSameAs(eventBusMock);
        assertThat(sut.nettyCustomizer()).isSameAs(nettyCustomizer);

        assertThat(Futures.get(sut.shutdown())).isTrue();

        verifyZeroInteractions(executorMock);
        verifyZeroInteractions(groupProviderMock);
        verifyZeroInteractions(timerMock);
        verify(latencyCollectorMock).isEnabled();
        verifyNoMoreInteractions(latencyCollectorMock);
    }

    @Test
    void mutateResources() {

        EventExecutorGroup executorMock = mock(EventExecutorGroup.class);
        EventLoopGroupProvider groupProviderMock = mock(EventLoopGroupProvider.class);
        Timer timerMock = mock(Timer.class);
        Timer timerMock2 = mock(Timer.class);
        EventBus eventBusMock = mock(EventBus.class);
        CommandLatencyCollector latencyCollectorMock = mock(CommandLatencyCollector.class);

        ClientResources sut = ClientResources.builder().eventExecutorGroup(executorMock)
                .eventLoopGroupProvider(groupProviderMock).timer(timerMock).eventBus(eventBusMock)
                .commandLatencyCollector(latencyCollectorMock).build();

        ClientResources copy = sut.mutate().timer(timerMock2).build();

        assertThat(sut.eventExecutorGroup()).isSameAs(executorMock);
        assertThat(sut.eventLoopGroupProvider()).isSameAs(groupProviderMock);
        assertThat(sut.timer()).isSameAs(timerMock);
        assertThat(copy.timer()).isSameAs(timerMock2).isNotSameAs(timerMock);
        assertThat(sut.eventBus()).isSameAs(eventBusMock);

        assertThat(Futures.get(sut.shutdown())).isTrue();

        verifyZeroInteractions(executorMock);
        verifyZeroInteractions(groupProviderMock);
        verifyZeroInteractions(timerMock);
    }

    @Test
    void testSmallPoolSize() {

        DefaultClientResources sut = DefaultClientResources.builder().ioThreadPoolSize(1).computationThreadPoolSize(1).build();

        EventExecutorGroup eventExecutors = sut.eventExecutorGroup();
        NioEventLoopGroup eventLoopGroup = sut.eventLoopGroupProvider().allocate(NioEventLoopGroup.class);

        assertThat(eventExecutors.iterator()).hasSize(3);
        assertThat(eventLoopGroup.executorCount()).isEqualTo(3);
        assertThat(sut.ioThreadPoolSize()).isEqualTo(3);

        assertThat(Futures.get(sut.shutdown(0, 0, TimeUnit.MILLISECONDS))).isTrue();
    }

    @Test
    void testEventBus() {

        DefaultClientResources sut = DefaultClientResources.create();

        EventBus eventBus = sut.eventBus();
        Event event = mock(Event.class);

        StepVerifier.create(eventBus.get()).then(() -> eventBus.publish(event)).expectNext(event).thenCancel().verify();

        assertThat(Futures.get(sut.shutdown(0, 0, TimeUnit.MILLISECONDS))).isTrue();
    }

    @Test
    void delayInstanceShouldRejectStatefulDelay() {

        assertThatThrownBy(() -> DefaultClientResources.builder().reconnectDelay(Delay.decorrelatedJitter().get()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void reconnectDelayCreatesNewForStatefulDelays() {

        DefaultClientResources resources = DefaultClientResources.builder().reconnectDelay(Delay.decorrelatedJitter()).build();

        Delay delay1 = resources.reconnectDelay();
        Delay delay2 = resources.reconnectDelay();

        assertThat(delay1).isNotSameAs(delay2);

        FastShutdown.shutdown(resources);
    }

    @Test
    void reconnectDelayReturnsSameInstanceForStatelessDelays() {

        DefaultClientResources resources = DefaultClientResources.builder().reconnectDelay(Delay.exponential()).build();

        Delay delay1 = resources.reconnectDelay();
        Delay delay2 = resources.reconnectDelay();

        assertThat(delay1).isSameAs(delay2);

        FastShutdown.shutdown(resources);
    }
}
