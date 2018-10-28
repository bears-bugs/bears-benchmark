/*
* UUID-js: A js library to generate and parse UUIDs, TimeUUIDs and generate
* TimeUUID based on dates for range selections.
* @see http://www.ietf.org/rfc/rfc4122.txt
**/

function UUID() {
}

UUID.maxFromBits = function(bits) {
    return Math.pow(2, bits);
};

UUID.limitUI04 = UUID.maxFromBits(4);
UUID.limitUI06 = UUID.maxFromBits(6);
UUID.limitUI08 = UUID.maxFromBits(8);
UUID.limitUI12 = UUID.maxFromBits(12);
UUID.limitUI14 = UUID.maxFromBits(14);
UUID.limitUI16 = UUID.maxFromBits(16);
UUID.limitUI32 = UUID.maxFromBits(32);
UUID.limitUI40 = UUID.maxFromBits(40);
UUID.limitUI48 = UUID.maxFromBits(48);

UUID.randomUI04 = function() {
    return Math.round(Math.random() * UUID.limitUI04);
};
UUID.randomUI06 = function() {
    return Math.round(Math.random() * UUID.limitUI06);
};
UUID.randomUI08 = function() {
    return Math.round(Math.random() * UUID.limitUI08);
};
UUID.randomUI12 = function() {
    return Math.round(Math.random() * UUID.limitUI12);
};
UUID.randomUI14 = function() {
    return Math.round(Math.random() * UUID.limitUI14);
};
UUID.randomUI16 = function() {
    return Math.round(Math.random() * UUID.limitUI16);
};
UUID.randomUI32 = function() {
    return Math.round(Math.random() * UUID.limitUI32);
};
UUID.randomUI40 = function() {
    return (0 | Math.random() * (1 << 30)) + (0 | Math.random() * (1 << 40 - 30)) * (1 << 30);
};
UUID.randomUI48 = function() {
    return (0 | Math.random() * (1 << 30)) + (0 | Math.random() * (1 << 48 - 30)) * (1 << 30);
};

UUID.paddedString = function(string, length, z) {
    string = String(string);
    z = (!z) ? '0' : z;
    var i = length - string.length;
    for (i; i > 0; i >>>= 1, z += z) {
        if (i & 1) {
            string = z + string;
        }
    }
    return string;
};

UUID.prototype.fromParts = function(timeLow, timeMid, timeHiAndVersion, clockSeqHiAndReserved, clockSeqLow, node) {
    this.version = (timeHiAndVersion >> 12) & 0xF;
    this.hex = UUID.paddedString(timeLow.toString(16), 8)
        + '-'
        + UUID.paddedString(timeMid.toString(16), 4)
        + '-'
        + UUID.paddedString(timeHiAndVersion.toString(16), 4)
        + '-'
        + UUID.paddedString(clockSeqHiAndReserved.toString(16), 2)
        + UUID.paddedString(clockSeqLow.toString(16), 2)
        + '-'
        + UUID.paddedString(node.toString(16), 12);
    return this;
};

UUID.prototype.toString = function() {
    return this.hex;
};

UUID.prototype.getText = UUID.prototype.toString;


UUID.prototype.toBytes = function() {
    var parts = this.hex.split('-');
    var ints = [];
    var intPos = 0;
    var i = 0;
    for (i; i < parts.length; i++) {
        var j = 0;
        for (j; j < parts[i].length; j+=2) {
            ints[intPos++] = parseInt(parts[i].substr(j, 2), 16);
        }
    }
    return ints;
};

UUID.prototype.equals = function(uuid) {
    if (!(uuid instanceof UUID)) {
        return false;
    }
    if (this.hex !== uuid.hex) {
        return false;
    }
    return true;
};

UUID.getTimeFieldValues = function(time) {
    var ts = time - Date.UTC(1582, 9, 15);
    var hm = ((ts / 0x100000000) * 10000) & 0xFFFFFFF;
    return { low: ((ts & 0xFFFFFFF) * 10000) % 0x100000000,
        mid: hm & 0xFFFF, hi: hm >>> 16, timestamp: ts };
};

UUID._create4 = function() {
    return new UUID().fromParts(
        UUID.randomUI32(),
        UUID.randomUI16(),
        0x4000 | UUID.randomUI12(),
        0x80   | UUID.randomUI06(),
        UUID.randomUI08(),
        UUID.randomUI48()
    );
};

UUID._create1 = function() {
    var now = new Date().getTime();
    var sequence = UUID.randomUI14();
    var node = (UUID.randomUI08() | 1) * 0x10000000000 + UUID.randomUI40();
    var tick = UUID.randomUI04();
    var timestamp = 0;
    var timestampRatio = 1/4;

    if (now !== timestamp) {
        if (now < timestamp) {
            sequence++;
        }
        timestamp = now;
        tick = UUID.randomUI04();
    } else if (Math.random() < timestampRatio && tick < 9984) {
        tick += 1 + UUID.randomUI04();
    } else {
        sequence++;
    }

    var tf = UUID.getTimeFieldValues(timestamp);
    var tl = tf.low + tick;
    var thav = (tf.hi & 0xFFF) | 0x1000;

    sequence &= 0x3FFF;
    var cshar = (sequence >>> 8) | 0x80;
    var csl = sequence & 0xFF;

    return new UUID().fromParts(tl, tf.mid, thav, cshar, csl, node);
};

UUID.create = function(version) {
    version = version || 4;
    return this['_create' + version]();
};

UUID.fromTime = function(time, last) {
    last = (!last) ? false : last;
    var tf = UUID.getTimeFieldValues(time);
    var tl = tf.low;
    var thav = (tf.hi & 0xFFF) | 0x1000;  // set version '0001'
    if (last === false) {
        return new UUID().fromParts(tl, tf.mid, thav, 0, 0, 0);
    }
    return new UUID().fromParts(tl, tf.mid, thav, 0x80 | UUID.limitUI06, UUID.limitUI08 - 1, UUID.limitUI48 - 1);

};

UUID.firstFromTime = function(time) {
    return UUID.fromTime(time, false);
};
UUID.lastFromTime = function(time) {
    return UUID.fromTime(time, true);
};

UUID.fromString = function(strId) {
    var p = new RegExp("([0-9a-f]{8})-([0-9a-f]{4})-([0-9a-f]{4})-([0-9a-f]{2})([0-9a-f]{2})-([0-9a-f]{12})");
    var r = p.exec(strId);
    if (r.length==7) {
        r.splice(0, 1);
        var ints = r.map(function (s) {
            return parseInt(s, 16);
        });
        var uuid = new UUID();
        return uuid.fromParts.apply(uuid, ints);
    } else
        throw new Error("Not a valid uuid: " + strId);
};

UUID.fromBytes = function(ints) {
    var str = '';
    var pos = 0;
    var parts = [4, 2, 2, 2, 6];
    var i = 0;

    if (ints.length < 5) {
        return null;
    }
    for (i; i < parts.length; i++) {
        var j = 0;
        for (j; j < parts[i]; j++) {
            var octet = ints[pos++].toString(16);
            if (octet.length === 1) {
                octet = '0' + octet;
            }
            str += octet;
        }
        if (parts[i] !== 6) {
            str += '-';
        }
    }
    return UUID.fromURN(str);
};

UUID.fromBinary = function(binary) {
    var ints = [];
    var i = 0;
    for (i; i < binary.length; i++) {
        ints[i] = binary.charCodeAt(i);
        if (ints[i] > 255 || ints[i] < 0) {
            throw new Error('Unexpected byte in binary data.');
        }
    }
    return UUID.fromBytes(ints);
};
