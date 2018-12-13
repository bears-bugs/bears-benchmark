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
package org.traccar.protocol;

import org.traccar.StringProtocolEncoder;
import org.traccar.helper.Log;
import org.traccar.model.Command;

public class Gl200ProtocolEncoder extends StringProtocolEncoder {

    @Override
    protected Object encodeCommand(Command command) {

        initDevicePassword(command, "");

        switch (command.getType()) {
            case Command.TYPE_POSITION_SINGLE:
                return formatCommand(command, "AT+GTRTO={%s},1,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_ENGINE_STOP:
                return formatCommand(command, "AT+GTOUT={%s},1,,,0,0,0,0,0,0,0,,,,,,,FFFF$",
                        Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_ENGINE_RESUME:
                return formatCommand(command, "AT+GTOUT={%s},0,,,0,0,0,0,0,0,0,,,,,,,FFFF$",
                        Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_IDENTIFICATION:
                return formatCommand(command, "AT+GTRTO={%s},8,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            case Command.TYPE_REBOOT_DEVICE:
                return formatCommand(command, "AT+GTRTO={%s},3,,,,,,FFFF$", Command.KEY_DEVICE_PASSWORD);
            default:
                Log.warning(new UnsupportedOperationException(command.getType()));
                break;
        }

        return null;
    }

}
