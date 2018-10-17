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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.traccar.BaseEventHandler;
import org.traccar.Context;
import org.traccar.database.GeofenceManager;
import org.traccar.model.Device;
import org.traccar.model.Event;
import org.traccar.model.Position;

public class GeofenceEventHandler extends BaseEventHandler {

    private GeofenceManager geofenceManager;

    public GeofenceEventHandler() {
        geofenceManager = Context.getGeofenceManager();
    }

    @Override
    protected Collection<Event> analyzePosition(Position position) {
        Device device = Context.getIdentityManager().getDeviceById(position.getDeviceId());
        if (device == null) {
            return null;
        }
        if (!Context.getIdentityManager().isLatestPosition(position) || !position.getValid()) {
            return null;
        }

        List<Long> currentGeofences = geofenceManager.getCurrentDeviceGeofences(position);
        List<Long> oldGeofences = new ArrayList<>();
        if (device.getGeofenceIds() != null) {
            oldGeofences.addAll(device.getGeofenceIds());
        }
        List<Long> newGeofences = new ArrayList<>(currentGeofences);
        newGeofences.removeAll(oldGeofences);
        oldGeofences.removeAll(currentGeofences);

        device.setGeofenceIds(currentGeofences);

        Collection<Event> events = new ArrayList<>();
        for (long geofenceId : newGeofences) {
            long calendarId = geofenceManager.getGeofence(geofenceId).getCalendarId();
            if (calendarId == 0 || Context.getCalendarManager().getCalendar(calendarId) == null
                    || Context.getCalendarManager().getCalendar(calendarId).checkMoment(position.getFixTime())) {
                Event event = new Event(Event.TYPE_GEOFENCE_ENTER, position.getDeviceId(), position.getId());
                event.setGeofenceId(geofenceId);
                events.add(event);
            }
        }
        for (long geofenceId : oldGeofences) {
            long calendarId = geofenceManager.getGeofence(geofenceId).getCalendarId();
            if (calendarId == 0 || Context.getCalendarManager().getCalendar(calendarId) == null
                    || Context.getCalendarManager().getCalendar(calendarId).checkMoment(position.getFixTime())) {
                Event event = new Event(Event.TYPE_GEOFENCE_EXIT, position.getDeviceId(), position.getId());
                event.setGeofenceId(geofenceId);
                events.add(event);
            }
        }
        return events;
    }
}
