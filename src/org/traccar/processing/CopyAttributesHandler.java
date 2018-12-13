/*
 * Copyright 2016 - 2017 Anton Tananaev (anton@traccar.org)
 * Copyright 2016 - 2017 Andrey Kunitsyn (andrey@traccar.org)
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
package org.traccar.processing;

import org.traccar.BaseDataHandler;
import org.traccar.Context;
import org.traccar.model.Position;

public class CopyAttributesHandler extends BaseDataHandler {

    private Position getLastPosition(long deviceId) {
        if (Context.getIdentityManager() != null) {
            return Context.getIdentityManager().getLastPosition(deviceId);
        }
        return null;
    }

    @Override
    protected Position handlePosition(Position position) {
        String attributesString = Context.getDeviceManager().lookupAttributeString(
                position.getDeviceId(), "processing.copyAttributes", "", true);
        Position last = getLastPosition(position.getDeviceId());
        if (attributesString.isEmpty()) {
            attributesString = Position.KEY_DRIVER_UNIQUE_ID;
        } else {
            attributesString += "," + Position.KEY_DRIVER_UNIQUE_ID;
        }
        if (last != null) {
            for (String attribute : attributesString.split("[ ,]")) {
                if (last.getAttributes().containsKey(attribute) && !position.getAttributes().containsKey(attribute)) {
                    position.getAttributes().put(attribute, last.getAttributes().get(attribute));
                }
            }
        }
        return position;
    }

}
