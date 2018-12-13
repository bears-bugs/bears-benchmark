/*
 * Copyright 2015 - 2016 Anton Tananaev (anton@traccar.org)
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
package org.traccar.helper;

import org.traccar.model.Position;

import java.util.AbstractMap;
import java.util.Map;

public final class ObdDecoder {

    private ObdDecoder() {
    }

    private static final int MODE_CURRENT = 0x01;
    private static final int MODE_FREEZE_FRAME = 0x02;
    private static final int MODE_CODES = 0x03;

    private static final int PID_ENGINE_LOAD = 0x04;
    private static final int PID_COOLANT_TEMPERATURE = 0x05;
    private static final int PID_ENGINE_RPM = 0x0C;
    private static final int PID_VEHICLE_SPEED = 0x0D;
    private static final int PID_THROTTLE_POSITION = 0x11;
    private static final int PID_MIL_DISTANCE = 0x21;
    private static final int PID_FUEL_LEVEL = 0x2F;
    private static final int PID_DISTANCE_CLEARED = 0x31;

    public static Map.Entry<String, Object> decode(int mode, String value) {
        switch (mode) {
            case MODE_CURRENT:
            case MODE_FREEZE_FRAME:
                return decodeData(
                        Integer.parseInt(value.substring(0, 2), 16),
                        Integer.parseInt(value.substring(2), 16), true);
            case MODE_CODES:
                return decodeCodes(value);
            default:
                return null;
        }
    }

    private static Map.Entry<String, Object> createEntry(String key, Object value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    public static Map.Entry<String, Object> decodeCodes(String value) {
        StringBuilder codes = new StringBuilder();
        for (int i = 0; i < value.length() / 4; i++) {
            int numValue = Integer.parseInt(value.substring(i * 4, (i + 1) * 4), 16);
            codes.append(' ');
            switch (numValue >> 14) {
                case 1:
                    codes.append('C');
                    break;
                case 2:
                    codes.append('B');
                    break;
                case 3:
                    codes.append('U');
                    break;
                default:
                    codes.append('P');
                    break;
            }
            codes.append(String.format("%04X", numValue & 0x3FFF));
        }
        if (codes.length() > 0) {
            return createEntry(Position.KEY_DTCS, codes.toString().trim());
        } else {
            return null;
        }
    }

    public static Map.Entry<String, Object> decodeData(int pid, int value, boolean convert) {
        switch (pid) {
            case PID_ENGINE_LOAD:
                return createEntry("engineLoad", convert ? value * 100 / 255 : value);
            case PID_COOLANT_TEMPERATURE:
                return createEntry("coolantTemperature", convert ? value - 40 : value);
            case PID_ENGINE_RPM:
                return createEntry(Position.KEY_RPM, convert ? value / 4 : value);
            case PID_VEHICLE_SPEED:
                return createEntry(Position.KEY_OBD_SPEED, value);
            case PID_THROTTLE_POSITION:
                return createEntry("throttle", convert ? value * 100 / 255 : value);
            case PID_MIL_DISTANCE:
                return createEntry("milDistance", value);
            case PID_FUEL_LEVEL:
                return createEntry(Position.KEY_FUEL_LEVEL, convert ? value * 100 / 255 : value);
            case PID_DISTANCE_CLEARED:
                return createEntry("clearedDistance", value);
            default:
                return null;
        }
    }

}
