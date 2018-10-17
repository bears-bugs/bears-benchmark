/*
 * Copyright 2015 Anton Tananaev (anton@traccar.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar.protocol;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.traccar.BaseProtocol;
import org.traccar.TrackerServer;
import org.traccar.model.Command;

import java.nio.ByteOrder;
import java.util.List;

public class NoranProtocol extends BaseProtocol {

    public NoranProtocol() {
        super("noran");
        setSupportedCommands(
                Command.TYPE_POSITION_SINGLE,
                Command.TYPE_POSITION_PERIODIC,
                Command.TYPE_POSITION_STOP,
                Command.TYPE_ENGINE_STOP,
                Command.TYPE_ENGINE_RESUME);
    }

    @Override
    public void initTrackerServers(List<TrackerServer> serverList) {
        TrackerServer server = new TrackerServer(new ConnectionlessBootstrap(), getName()) {
            @Override
            protected void addSpecificHandlers(ChannelPipeline pipeline) {
                pipeline.addLast("objectEncoder", new NoranProtocolEncoder());
                pipeline.addLast("objectDecoder", new NoranProtocolDecoder(NoranProtocol.this));
            }
        };
        server.setEndianness(ByteOrder.LITTLE_ENDIAN);
        serverList.add(server);
    }

}
