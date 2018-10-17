/*
 * Copyright 2016 Anton Tananaev (anton@traccar.org)
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
package org.traccar.events;

import java.util.Collection;
import java.util.Collections;

import org.traccar.BaseEventHandler;
import org.traccar.model.Event;
import org.traccar.model.Position;

public class AlertEventHandler extends BaseEventHandler {

    @Override
    protected Collection<Event> analyzePosition(Position position) {
        Object alarm = position.getAttributes().get(Position.KEY_ALARM);
        if (alarm != null) {
            Event event = new Event(Event.TYPE_ALARM, position.getDeviceId(), position.getId());
            event.set(Position.KEY_ALARM, (String) alarm);
            return Collections.singleton(event);
        }
        return null;
    }

}
