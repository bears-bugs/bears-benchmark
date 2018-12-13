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
package io.lettuce.core.event.metrics;

import java.util.Map;

import io.lettuce.core.event.Event;
import io.lettuce.core.metrics.CommandLatencyId;
import io.lettuce.core.metrics.CommandMetrics;

/**
 * Event that transports command latency metrics. This event carries latencies for multiple commands and connections.
 *
 * @author Mark Paluch
 */
public class CommandLatencyEvent implements Event {

    private Map<CommandLatencyId, CommandMetrics> latencies;

    public CommandLatencyEvent(Map<CommandLatencyId, CommandMetrics> latencies) {
        this.latencies = latencies;
    }

    /**
     * Returns the latencies mapped between {@link CommandLatencyId connection/command} and the {@link CommandMetrics metrics}.
     *
     * @return the latency map.
     */
    public Map<CommandLatencyId, CommandMetrics> getLatencies() {
        return latencies;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(latencies);
        return sb.toString();
    }
}
