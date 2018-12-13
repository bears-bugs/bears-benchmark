/*
 * Copyright 2012 - 2017 Anton Tananaev (anton@traccar.org)
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

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.traccar.BaseProtocolDecoder;
import org.traccar.Context;
import org.traccar.DeviceSession;
import org.traccar.helper.BitUtil;
import org.traccar.helper.Parser;
import org.traccar.helper.PatternBuilder;
import org.traccar.helper.UnitsConverter;
import org.traccar.model.CellTower;
import org.traccar.model.Network;
import org.traccar.model.Position;
import org.traccar.model.WifiAccessPoint;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gl200TextProtocolDecoder extends BaseProtocolDecoder {

    private boolean ignoreFixTime;

    public Gl200TextProtocolDecoder(Gl200Protocol protocol) {
        super(protocol);

        ignoreFixTime = Context.getConfig().getBoolean(getProtocolName() + ".ignoreFixTime");
    }

    private static final Pattern PATTERN_ACK = new PatternBuilder()
            .text("+ACK:GT")
            .expression("...,")                  // type
            .number("([0-9A-Z]{2}xxxx),")        // protocol version
            .number("(d{15}|x{14}),")            // imei
            .any().text(",")
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(xxxx)")                    // counter
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_INF = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GTINF,")
            .number("[0-9A-Z]{2}xxxx,")          // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("(?:[0-9A-Z]{17},)?")    // vin
            .expression("(?:[^,]+)?,")           // device name
            .number("(xx),")                     // state
            .expression("(?:[0-9Ff]{20})?,")     // iccid
            .number("(d{1,2}),")                 // rssi
            .number("d{1,2},")
            .expression("[01],")                 // external power
            .number("([d.]+)?,")                 // odometer or external power
            .number("d*,")                       // backup battery or lightness
            .number("(d+.d+),")                  // battery
            .expression("([01]),")               // charging
            .number("(?:d),")                    // led
            .number("(?:d)?,")                   // gps on need
            .number("(?:d)?,")                   // gps antenna type
            .number("(?:d)?,").optional()        // gps antenna state
            .number("d{14},")                    // last fix time
            .groupBegin()
            .number("(d+),")                     // battery percentage
            .number("[d.]*,")                    // flash type / power
            .number("(-?[d.]+)?,,,")             // temperature
            .or()
            .expression("(?:[01])?,").optional() // pin15 mode
            .number("(d+)?,")                    // adc1
            .number("(d+)?,").optional()         // adc2
            .number("(xx)?,")                    // digital input
            .number("(xx)?,")                    // digital output
            .number("[-+]dddd,")                 // timezone
            .expression("[01],")                 // daylight saving
            .groupEnd()
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(xxxx)")                    // counter
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_VER = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GTVER,")
            .number("[0-9A-Z]{2}xxxx,")          // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,")                // device name
            .expression("([^,]*),")              // device type
            .number("(xxxx),")                   // firmware version
            .number("(xxxx),")                   // hardware version
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd),")             // time (hhmmss)
            .number("(xxxx)")                    // counter
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_LOCATION = new PatternBuilder()
            .number("(d{1,2})?,")                // hdop
            .number("(d{1,3}.d)?,")              // speed
            .number("(d{1,3})?,")                // course
            .number("(-?d{1,5}.d)?,")            // altitude
            .number("(-?d{1,3}.d{6})?,")         // longitude
            .number("(-?d{1,2}.d{6})?,")         // latitude
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(d+)?,")                    // mcc
            .number("(d+)?,")                    // mnc
            .groupBegin()
            .number("(d+),")                     // lac
            .number("(d+),")                     // cid
            .or()
            .number("(x+)?,")                    // lac
            .number("(x+)?,")                    // cid
            .groupEnd()
            .number("(?:d+|(d+.d))?,")           // odometer
            .compile();

    private static final Pattern PATTERN_OBD = new PatternBuilder()
            .text("+RESP:GTOBD,")
            .number("[0-9A-Z]{2}xxxx,")          // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("(?:[0-9A-Z]{17})?,")    // vin
            .expression("[^,]{0,20},")           // device name
            .expression("[01],")                 // report type
            .number("x{1,8},")                   // report mask
            .expression("(?:[0-9A-Z]{17})?,")    // vin
            .number("[01],")                     // obd connect
            .number("(?:d{1,5})?,")              // obd voltage
            .number("(?:x{8})?,")                // support pids
            .number("(d{1,5})?,")                // engine rpm
            .number("(d{1,3})?,")                // speed
            .number("(-?d{1,3})?,")              // coolant temp
            .number("(d+.?d*|Inf|NaN)?,")        // fuel consumption
            .number("(d{1,5})?,")                // dtcs cleared distance
            .number("(?:d{1,5})?,")
            .expression("([01])?,")              // obd connect
            .number("(d{1,3})?,")                // number of dtcs
            .number("(x*),")                     // dtcs
            .number("(d{1,3})?,")                // throttle
            .number("(?:d{1,3})?,")              // engine load
            .number("(d{1,3})?,")                // fuel level
            .expression("(?:[0-9A],)?")          // obd protocol
            .number("(d+),")                     // odometer
            .expression(PATTERN_LOCATION.pattern())
            .number("(d{1,7}.d)?,")              // odometer
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_FRI = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GTFRI,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("(?:([0-9A-Z]{17}),)?")  // vin
            .expression("[^,]*,")                // device name
            .number("(d+)?,")                    // power
            .number("d{1,2},")                   // report type
            .number("d{1,2},")                   // count
            .expression("((?:")
            .expression(PATTERN_LOCATION.pattern())
            .expression(")+)")
            .groupBegin()
            .number("(d{1,7}.d)?,").optional()   // odometer
            .number("(d{1,3})?,")                // battery
            .or()
            .number("(d{1,7}.d)?,")              // odometer
            .number("(d{5}:dd:dd)?,")            // hour meter
            .number("(x+)?,")                    // adc 1
            .number("(x+)?,")                    // adc 2
            .number("(d{1,3})?,")                // battery
            .number("(?:(xx)(xx)(xx))?,")        // device status
            .number("(d+)?,")                    // rpm
            .number("(?:d+.?d*|Inf|NaN)?,")      // fuel consumption
            .number("(d+)?,")                    // fuel level
            .groupEnd()
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_ERI = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GTERI,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,")                // device name
            .number("x{8},")                     // mask
            .number("(d+)?,")                    // power
            .number("d{1,2},")                   // report type
            .number("d{1,2},")                   // count
            .expression("((?:")
            .expression(PATTERN_LOCATION.pattern())
            .expression(")+)")
            .number("(d{1,7}.d)?,")              // odometer
            .number("(d{5}:dd:dd)?,")            // hour meter
            .number("(x+)?,")                    // adc 1
            .number("(x+)?,").optional()         // adc 2
            .number("(d{1,3})?,")                // battery
            .number("(?:(xx)(xx)(xx))?,")        // device status
            .expression("(.*)")                  // additional data
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_IGN = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GTIG[NF],")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,")                // device name
            .number("d+,")                       // ignition off duration
            .expression(PATTERN_LOCATION.pattern())
            .number("(d{5}:dd:dd)?,")            // hour meter
            .number("(d{1,7}.d)?,")              // odometer
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_IDA = new PatternBuilder()
            .text("+RESP:GTIDA,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,,")               // device name
            .number("([^,]+),")                  // rfid
            .expression("[01],")                 // report type
            .number("1,")                        // count
            .expression(PATTERN_LOCATION.pattern())
            .number("(d+.d),")                   // odometer
            .text(",,,,")
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_WIF = new PatternBuilder()
            .text("+RESP:GTWIF,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,")                // device name
            .number("(d+),")                     // count
            .number("((?:x{12},-?d+,,,,)+),,,,") // wifi
            .number("(d{1,3}),")                 // battery
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_GSM = new PatternBuilder()
            .text("+RESP:GTGSM,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("(?:STR|CTN|NMR|RTL),")  // fix type
            .expression("(.*)")                  // cells
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF):GT...,")
            .number("(?:[0-9A-Z]{2}xxxx)?,")     // protocol version
            .number("(d{15}|x{14}),")            // imei
            .expression("[^,]*,")                // device name
            .number("d*,")
            .number("(d{1,2}),")                 // report type
            .number("d{1,2},")                   // count
            .expression(PATTERN_LOCATION.pattern())
            .groupBegin()
            .number("(d{1,7}.d)?,").optional()   // odometer
            .number("(d{1,3})?,")                // battery
            .or()
            .number("(d{1,7}.d)?,")              // odometer
            .groupEnd()
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)")  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private static final Pattern PATTERN_BASIC = new PatternBuilder()
            .text("+").expression("(?:RESP|BUFF)").text(":")
            .expression("GT...,")
            .number("(?:[0-9A-Z]{2}xxxx)?,").optional() // protocol version
            .number("(d{15}|x{14}),")            // imei
            .any()
            .number("(d{1,2})?,")                // hdop
            .number("(d{1,3}.d)?,")              // speed
            .number("(d{1,3})?,")                // course
            .number("(-?d{1,5}.d)?,")            // altitude
            .number("(-?d{1,3}.d{6})?,")         // longitude
            .number("(-?d{1,2}.d{6})?,")         // latitude
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(d+),")                     // mcc
            .number("(d+),")                     // mnc
            .number("(x+),")                     // lac
            .number("(x+),").optional(4)         // cell
            .any()
            .number("(dddd)(dd)(dd)")            // date (yyyymmdd)
            .number("(dd)(dd)(dd)").optional(2)  // time (hhmmss)
            .text(",")
            .number("(xxxx)")                    // count number
            .text("$").optional()
            .compile();

    private Object decodeAck(Channel channel, SocketAddress remoteAddress, String sentence, String type) {
        Parser parser = new Parser(PATTERN_ACK, sentence);
        if (parser.matches()) {
            String protocolVersion = parser.next();
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
            if (deviceSession == null) {
                return null;
            }
            if (type.equals("HBD")) {
                if (channel != null) {
                    parser.skip(6);
                    channel.write("+SACK:GTHBD," + protocolVersion + "," + parser.next() + "$", remoteAddress);
                }
            } else {
                Position position = new Position();
                position.setProtocol(getProtocolName());
                position.setDeviceId(deviceSession.getDeviceId());
                getLastLocation(position, parser.nextDateTime());
                position.setValid(false);
                position.set(Position.KEY_RESULT, "Command " + type + " accepted");
                return position;
            }
        }
        return null;
    }

    private Position initPosition(Parser parser, Channel channel, SocketAddress remoteAddress) {
        if (parser.matches()) {
            DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
            if (deviceSession != null) {
                Position position = new Position();
                position.setProtocol(getProtocolName());
                position.setDeviceId(deviceSession.getDeviceId());
                return position;
            }
        }
        return null;
    }

    private void decodeDeviceTime(Position position, Parser parser) {
        if (parser.hasNext(6)) {
            if (ignoreFixTime) {
                position.setTime(parser.nextDateTime());
            } else {
                position.setDeviceTime(parser.nextDateTime());
            }
        }
    }

    private Object decodeInf(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_INF, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        switch (parser.nextHexInt()) {
            case 0x16:
            case 0x1A:
            case 0x12:
                position.set(Position.KEY_IGNITION, false);
                position.set(Position.KEY_MOTION, true);
                break;
            case 0x11:
                position.set(Position.KEY_IGNITION, false);
                position.set(Position.KEY_MOTION, false);
                break;
            case 0x21:
                position.set(Position.KEY_IGNITION, true);
                position.set(Position.KEY_MOTION, false);
                break;
            case 0x22:
                position.set(Position.KEY_IGNITION, true);
                position.set(Position.KEY_MOTION, true);
                break;
            case 0x41:
                position.set(Position.KEY_MOTION, false);
                break;
            case 0x42:
                position.set(Position.KEY_MOTION, true);
                break;
            default:
                break;
        }

        position.set(Position.KEY_RSSI, parser.nextInt());

        parser.next(); // odometer or external power

        position.set(Position.KEY_BATTERY, parser.nextDouble(0));
        position.set(Position.KEY_CHARGE, parser.nextInt(0) == 1);

        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt());

        position.set(Position.PREFIX_TEMP + 1, parser.next());

        position.set(Position.PREFIX_ADC + 1, parser.next());
        position.set(Position.PREFIX_ADC + 2, parser.next());

        position.set(Position.KEY_INPUT, parser.next());
        position.set(Position.KEY_OUTPUT, parser.next());

        getLastLocation(position, parser.nextDateTime());

        position.set(Position.KEY_INDEX, parser.nextHexInt(0));

        return position;
    }

    private Object decodeVer(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_VER, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        position.set("deviceType", parser.next());
        position.set(Position.KEY_VERSION_FW, parser.nextHexInt(0));
        position.set(Position.KEY_VERSION_HW, parser.nextHexInt(0));

        getLastLocation(position, parser.nextDateTime());

        return position;
    }

    private void decodeLocation(Position position, Parser parser) {
        int hdop = parser.nextInt(0);
        position.setValid(hdop > 0);
        position.set(Position.KEY_HDOP, hdop);

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble(0)));
        position.setCourse(parser.nextDouble(0));
        position.setAltitude(parser.nextDouble(0));

        if (parser.hasNext(8)) {
            position.setValid(true);
            position.setLongitude(parser.nextDouble(0));
            position.setLatitude(parser.nextDouble(0));
            position.setTime(parser.nextDateTime());
        } else {
            getLastLocation(position, null);
        }

        if (parser.hasNext(6)) {
            int mcc = parser.nextInt(0);
            int mnc = parser.nextInt(0);
            if (parser.hasNext(2)) {
                position.setNetwork(new Network(CellTower.from(mcc, mnc, parser.nextInt(0), parser.nextInt(0))));
            }
            if (parser.hasNext(2)) {
                position.setNetwork(new Network(CellTower.from(mcc, mnc, parser.nextHexInt(0), parser.nextHexInt(0))));
            }
        }

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);
    }

    private Object decodeObd(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_OBD, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        position.set(Position.KEY_RPM, parser.nextInt());
        position.set(Position.KEY_OBD_SPEED, parser.nextInt());
        position.set(Position.PREFIX_TEMP + 1, parser.nextInt());
        position.set(Position.KEY_FUEL_CONSUMPTION, parser.next());
        position.set("dtcsClearedDistance", parser.nextInt());
        position.set("odbConnect", parser.nextInt(0) == 1);
        position.set("dtcsNumber", parser.nextInt());
        position.set("dtcsCodes", parser.next());
        position.set(Position.KEY_THROTTLE, parser.nextInt());
        position.set(Position.KEY_FUEL_LEVEL, parser.nextInt());
        position.set(Position.KEY_OBD_ODOMETER, parser.nextInt(0) * 1000);

        decodeLocation(position, parser);

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);

        decodeDeviceTime(position, parser);

        return position;
    }

    private Object decodeCan(Channel channel, SocketAddress remoteAddress, String sentence) throws ParseException {
        Position position = new Position();
        position.setProtocol(getProtocolName());

        int index = 0;
        String[] values = sentence.split(",");

        index += 1; // header
        index += 1; // protocol version

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, values[index++]);
        position.setDeviceId(deviceSession.getDeviceId());

        index += 1; // device name
        index += 1; // report type
        index += 1; // canbus state
        long reportMask = Long.parseLong(values[index++], 16);

        if (BitUtil.check(reportMask, 0)) {
            position.set(Position.KEY_VIN, values[index++]);
        }
        if (BitUtil.check(reportMask, 1)) {
            position.set(Position.KEY_IGNITION, Integer.parseInt(values[index++]) > 0);
        }
        if (BitUtil.check(reportMask, 2)) {
            index += 1; // total distance
        }
        if (BitUtil.check(reportMask, 3)) {
            position.set("totalFuelConsumption", Double.parseDouble(values[index++]));
        }
        if (BitUtil.check(reportMask, 5)) {
            position.set(Position.KEY_RPM, Integer.parseInt(values[index++]));
        }
        if (BitUtil.check(reportMask, 4)) {
            position.set(Position.KEY_OBD_SPEED, UnitsConverter.knotsFromKph(Integer.parseInt(values[index++])));
        }
        if (BitUtil.check(reportMask, 6)) {
            position.set(Position.KEY_COOLANT_TEMP, Integer.parseInt(values[index++]));
        }
        if (BitUtil.check(reportMask, 7)) {
            index += 1; // fuel consumption
        }
        if (BitUtil.check(reportMask, 8)) {
            index += 1; // fuel level
        }
        if (BitUtil.check(reportMask, 9)) {
            index += 1; // range
        }
        if (BitUtil.check(reportMask, 10)) {
            if (!values[index++].isEmpty()) {
                position.set(Position.KEY_THROTTLE, Integer.parseInt(values[index - 1]));
            }
        }
        if (BitUtil.check(reportMask, 11)) {
            position.set(Position.KEY_HOURS, Double.parseDouble(values[index++]));
        }
        if (BitUtil.check(reportMask, 12)) {
            index += 1; // driving time
        }
        if (BitUtil.check(reportMask, 13)) {
            index += 1; // idle time
        }
        if (BitUtil.check(reportMask, 14)) {
            index += 1; // idle fuel
        }
        if (BitUtil.check(reportMask, 15)) {
            index += 1; // axle weight
        }
        if (BitUtil.check(reportMask, 16)) {
            index += 1; // tachograph info
        }
        if (BitUtil.check(reportMask, 17)) {
            index += 1; // indicators
        }
        if (BitUtil.check(reportMask, 18)) {
            index += 1; // lights
        }
        if (BitUtil.check(reportMask, 19)) {
            index += 1; // doors
        }
        if (BitUtil.check(reportMask, 20)) {
            index += 1; // total vehicle overspeed time
        }
        if (BitUtil.check(reportMask, 21)) {
            index += 1; // total engine overspeed time
        }
        if (BitUtil.check(reportMask, 29)) {
            index += 1; // expansion
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        if (BitUtil.check(reportMask, 30)) {
            position.setValid(Integer.parseInt(values[index++]) > 0);
            if (!values[index].isEmpty()) {
                position.setSpeed(UnitsConverter.knotsFromKph(Double.parseDouble(values[index++])));
                position.setCourse(Integer.parseInt(values[index++]));
                position.setAltitude(Double.parseDouble(values[index++]));
                position.setLongitude(Double.parseDouble(values[index++]));
                position.setLatitude(Double.parseDouble(values[index++]));
                position.setTime(dateFormat.parse(values[index++]));
            } else {
                index += 6; // no location
                getLastLocation(position, null);
            }
        } else {
            getLastLocation(position, null);
        }

        if (BitUtil.check(reportMask, 31)) {
            index += 4; // cell
        }

        index += 1; // reserved

        if (ignoreFixTime) {
            position.setTime(dateFormat.parse(values[index]));
        } else {
            position.setDeviceTime(dateFormat.parse(values[index]));
        }

        return position;
    }

    private void decodeStatus(Position position, Parser parser) {
        if (parser.hasNext(3)) {
            int ignition = parser.nextHexInt(0);
            if (BitUtil.check(ignition, 4)) {
                position.set(Position.KEY_IGNITION, false);
            } else if (BitUtil.check(ignition, 5)) {
                position.set(Position.KEY_IGNITION, true);
            }
            position.set(Position.KEY_INPUT, parser.nextHexInt(0));
            position.set(Position.KEY_OUTPUT, parser.nextHexInt(0));
        }
    }

    private Object decodeFri(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_FRI, sentence);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        LinkedList<Position> positions = new LinkedList<>();

        String vin = parser.next();
        int power = parser.nextInt(0);

        Parser itemParser = new Parser(PATTERN_LOCATION, parser.next());
        while (itemParser.find()) {
            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            position.set(Position.KEY_VIN, vin);

            decodeLocation(position, itemParser);

            positions.add(position);
        }

        Position position = positions.getLast();

        decodeLocation(position, parser);

        // power value only on some devices
        if (power > 10) {
            position.set(Position.KEY_POWER, power);
        }

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);
        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt());

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);
        position.set(Position.KEY_HOURS, parser.next());
        position.set(Position.PREFIX_ADC + 1, parser.next());
        position.set(Position.PREFIX_ADC + 2, parser.next());
        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt());

        decodeStatus(position, parser);

        position.set(Position.KEY_RPM, parser.nextInt());
        position.set(Position.KEY_FUEL_LEVEL, parser.nextInt());

        decodeDeviceTime(position, parser);
        if (ignoreFixTime) {
            positions.clear();
            positions.add(position);
        }

        return positions;
    }

    private Object decodeEri(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_ERI, sentence);
        if (!parser.matches()) {
            return null;
        }

        DeviceSession deviceSession = getDeviceSession(channel, remoteAddress, parser.next());
        if (deviceSession == null) {
            return null;
        }

        LinkedList<Position> positions = new LinkedList<>();

        int power = parser.nextInt(0);

        Parser itemParser = new Parser(PATTERN_LOCATION, parser.next());
        while (itemParser.find()) {
            Position position = new Position();
            position.setProtocol(getProtocolName());
            position.setDeviceId(deviceSession.getDeviceId());

            decodeLocation(position, itemParser);

            positions.add(position);
        }

        Position position = positions.getLast();

        decodeLocation(position, parser);

        position.set(Position.KEY_POWER, power);
        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);
        position.set(Position.KEY_HOURS, parser.next());
        position.set(Position.PREFIX_ADC + 1, parser.next());
        position.set(Position.PREFIX_ADC + 2, parser.next());
        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt());

        decodeStatus(position, parser);

        int index = 0;
        String[] data = parser.next().split(",");
        if (data.length > 1) {
            int deviceType = Integer.parseInt(data[index++]);
            if (deviceType == 2) {
                int deviceCount = Integer.parseInt(data[index++]);
                for (int i = 1; i <= deviceCount; i++) {
                    index++; // id
                    index++; // type
                    position.set(Position.PREFIX_TEMP + i, (short) Integer.parseInt(data[index++], 16) * 0.0625);
                }
            }
        }

        decodeDeviceTime(position, parser);
        if (ignoreFixTime) {
            positions.clear();
            positions.add(position);
        }

        return positions;
    }

    private Object decodeIgn(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_IGN, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        decodeLocation(position, parser);

        position.set(Position.KEY_HOURS, parser.next());
        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);

        decodeDeviceTime(position, parser);

        return position;
    }

    private Object decodeIda(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_IDA, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        position.set(Position.KEY_DRIVER_UNIQUE_ID, parser.next());

        decodeLocation(position, parser);

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);

        decodeDeviceTime(position, parser);

        return position;
    }

    private Object decodeWif(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_WIF, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        getLastLocation(position, null);

        Network network = new Network();

        parser.nextInt(0); // count
        Matcher matcher = Pattern.compile("([0-9a-fA-F]{12}),(-?\\d+),,,,").matcher(parser.next());
        while (matcher.find()) {
            String mac = matcher.group(1).replaceAll("(..)", "$1:");
            network.addWifiAccessPoint(WifiAccessPoint.from(
                    mac.substring(0, mac.length() - 1), Integer.parseInt(matcher.group(2))));
        }

        position.setNetwork(network);

        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt(0));

        return position;
    }

    private Object decodeGsm(Channel channel, SocketAddress remoteAddress, String sentence) {
        Parser parser = new Parser(PATTERN_GSM, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        getLastLocation(position, null);

        Network network = new Network();

        String[] data = parser.next().split(",");
        for (int i = 0; i < 6; i++) {
            if (!data[i * 6].isEmpty()) {
                network.addCellTower(CellTower.from(
                        Integer.parseInt(data[i * 6]), Integer.parseInt(data[i * 6 + 1]),
                        Integer.parseInt(data[i * 6 + 2], 16), Integer.parseInt(data[i * 6 + 3], 16),
                        Integer.parseInt(data[i * 6 + 4])));
            }
        }

        position.setNetwork(network);

        return position;
    }

    private Object decodeOther(Channel channel, SocketAddress remoteAddress, String sentence, String type) {
        Parser parser = new Parser(PATTERN, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        int reportType = parser.nextInt(0);
        if (type.equals("NMR")) {
            position.set(Position.KEY_MOTION, reportType == 1);
        } else if (type.equals("SOS")) {
            position.set(Position.KEY_ALARM, Position.ALARM_SOS);
        }

        decodeLocation(position, parser);

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);
        position.set(Position.KEY_BATTERY_LEVEL, parser.nextInt(0));

        position.set(Position.KEY_ODOMETER, parser.nextDouble(0) * 1000);

        decodeDeviceTime(position, parser);

        if (Context.getConfig().getBoolean(getProtocolName() + ".ack") && channel != null) {
            channel.write("+SACK:" + parser.next() + "$", remoteAddress);
        }

        return position;
    }

    private Object decodeBasic(Channel channel, SocketAddress remoteAddress, String sentence, String type) {
        Parser parser = new Parser(PATTERN_BASIC, sentence);
        Position position = initPosition(parser, channel, remoteAddress);
        if (position == null) {
            return null;
        }

        int hdop = parser.nextInt(0);
        position.setValid(hdop > 0);
        position.set(Position.KEY_HDOP, hdop);

        position.setSpeed(UnitsConverter.knotsFromKph(parser.nextDouble(0)));
        position.setCourse(parser.nextDouble(0));
        position.setAltitude(parser.nextDouble(0));

        if (parser.hasNext(2)) {
            position.setLongitude(parser.nextDouble(0));
            position.setLatitude(parser.nextDouble(0));
        } else {
            getLastLocation(position, null);
        }

        if (parser.hasNext(6)) {
            position.setTime(parser.nextDateTime());
        }

        if (parser.hasNext(4)) {
            position.setNetwork(new Network(CellTower.from(
                    parser.nextInt(0), parser.nextInt(0), parser.nextHexInt(0), parser.nextHexInt(0))));
        }

        decodeDeviceTime(position, parser);

        switch (type) {
            case "PNA":
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_ON);
                break;
            case "PFA":
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_OFF);
                break;
            case "EPN":
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_RESTORED);
                break;
            case "EPF":
                position.set(Position.KEY_ALARM, Position.ALARM_POWER_CUT);
                break;
            case "BPL":
                position.set(Position.KEY_ALARM, Position.ALARM_LOW_BATTERY);
                break;
            case "STT":
                position.set(Position.KEY_ALARM, Position.ALARM_MOVEMENT);
                break;
            case "SWG":
                position.set(Position.KEY_ALARM, Position.ALARM_GEOFENCE);
                break;
            case "TMP":
            case "TEM":
                position.set(Position.KEY_ALARM, Position.ALARM_TEMPERATURE);
                break;
            case "JDR":
            case "JDS":
                position.set(Position.KEY_ALARM, Position.ALARM_JAMMING);
                break;
            default:
                break;
        }

        return position;
    }

    @Override
    protected Object decode(
            Channel channel, SocketAddress remoteAddress, Object msg) throws Exception {

        String sentence = ((ChannelBuffer) msg).toString(StandardCharsets.US_ASCII);

        int typeIndex = sentence.indexOf(":GT");
        if (typeIndex < 0) {
            return null;
        }

        Object result;
        String type = sentence.substring(typeIndex + 3, typeIndex + 6);
        if (sentence.startsWith("+ACK")) {
            result = decodeAck(channel, remoteAddress, sentence, type);
        } else {
            switch (type) {
                case "INF":
                    result = decodeInf(channel, remoteAddress, sentence);
                    break;
                case "OBD":
                    result = decodeObd(channel, remoteAddress, sentence);
                    break;
                case "CAN":
                    result = decodeCan(channel, remoteAddress, sentence);
                    break;
                case "FRI":
                    result = decodeFri(channel, remoteAddress, sentence);
                    break;
                case "ERI":
                    result = decodeEri(channel, remoteAddress, sentence);
                    break;
                case "IGN":
                case "IGF":
                    result = decodeIgn(channel, remoteAddress, sentence);
                    break;
                case "IDA":
                    result = decodeIda(channel, remoteAddress, sentence);
                    break;
                case "WIF":
                    result = decodeWif(channel, remoteAddress, sentence);
                    break;
                case "GSM":
                    result = decodeGsm(channel, remoteAddress, sentence);
                    break;
                case "VER":
                    result = decodeVer(channel, remoteAddress, sentence);
                    break;
                default:
                    result = decodeOther(channel, remoteAddress, sentence, type);
                    break;
            }

            if (result == null) {
                result = decodeBasic(channel, remoteAddress, sentence, type);
            }

            if (result != null) {
                if (result instanceof Position) {
                    ((Position) result).set(Position.KEY_TYPE, type);
                } else {
                    for (Position p : (List<Position>) result) {
                        p.set(Position.KEY_TYPE, type);
                    }
                }
            }
        }

        return result;
    }

}
