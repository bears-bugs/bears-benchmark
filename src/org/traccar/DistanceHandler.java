/*
 * Copyright 2015 Amila Silva
 * Copyright 2016 - 2017 Anton Tananaev (anton@traccar.org)
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
package org.traccar;

import org.traccar.helper.DistanceCalculator;
import org.traccar.model.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DistanceHandler extends BaseDataHandler {

    private final boolean filter;
    private final int coordinatesMinError;
    private final int coordinatesMaxError;

    public DistanceHandler(boolean filter, int coordinatesMinError, int coordinatesMaxError) {
        this.filter = filter;
        this.coordinatesMinError = coordinatesMinError;
        this.coordinatesMaxError = coordinatesMaxError;
    }

    private Position getLastPosition(long deviceId) {
        if (Context.getIdentityManager() != null) {
            return Context.getIdentityManager().getLastPosition(deviceId);
        }
        return null;
    }

    @Override
    protected Position handlePosition(Position position) {

        double distance = 0.0;
        if (position.getAttributes().containsKey(Position.KEY_DISTANCE)) {
            distance = position.getDouble(Position.KEY_DISTANCE);
        }
        double totalDistance = 0.0;

        Position last = getLastPosition(position.getDeviceId());
        if (last != null) {
            totalDistance = last.getDouble(Position.KEY_TOTAL_DISTANCE);
            if (!position.getAttributes().containsKey(Position.KEY_DISTANCE)) {
                distance = DistanceCalculator.distance(
                        position.getLatitude(), position.getLongitude(),
                        last.getLatitude(), last.getLongitude());
                distance = BigDecimal.valueOf(distance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
            }
            if (filter && last.getValid() && last.getLatitude() != 0 && last.getLongitude() != 0) {
                boolean satisfiesMin = coordinatesMinError == 0 || distance > coordinatesMinError;
                boolean satisfiesMax = coordinatesMaxError == 0
                        || distance < coordinatesMaxError || position.getValid();
                if (!satisfiesMin || !satisfiesMax) {
                    position.setLatitude(last.getLatitude());
                    position.setLongitude(last.getLongitude());
                    distance = 0;
                }
            }
        }
        position.set(Position.KEY_DISTANCE, distance);
        totalDistance = BigDecimal.valueOf(totalDistance + distance).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        position.set(Position.KEY_TOTAL_DISTANCE, totalDistance);

        return position;
    }

}
