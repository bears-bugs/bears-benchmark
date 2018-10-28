function DateTime(date, tzOffset) {
    this.date = date;
    this.tzOffset = tzOffset;
    return this;
}


DateTime.parseOffset = function(text) {
    var hours = parseInt(text.substring(0,2));
    var i = text[2]==':' ? 3 : 2;
    var minutes = parseInt(text.substring(i,i+2));
    return ((hours * 60) + minutes) * 60;
};

DateTime.parseTZOffset = function(text) {
    var i = text.indexOf('Z');
    if(i>0)
        return 0;
    i = text.indexOf('+');
    if(i>0)
        return DateTime.parseOffset(text.substring(i+1));
    i = text.lastIndexOf('-');
    if(i>10) // skip date separator
        return -DateTime.parseOffset(text.substring(i+1));
    return 0;
};

DateTime.parseUTCDate = function(text) {
    var i = text.indexOf('Z');
    if(i<0) {
        i = text.indexOf('+');
        if(i>0)
            text = text.substring(0, i);
        else {
            i = text.lastIndexOf('-');
            if(i>10) // skip date separator
                text = text.substring(0, i);
        }
    }
    return new Date(text);
};


DateTime.parse = function(text) {
    var date = DateTime.parseUTCDate(text);
    var tzOffset = DateTime.parseTZOffset(text);
    return new DateTime(date, tzOffset);
};

DateTime.prototype.addPeriod = function (period) {
    var date = new Date();
    var year = this.date.getUTCFullYear() + (period.years || 0);
    date.setUTCFullYear(year);
    var month = this.date.getUTCMonth() + (period.months || 0);
    date.setUTCMonth(month);
    var day = this.date.getUTCDate() + ((period.weeks || 0) * 7) + (period.days || 0);
    date.setUTCDate(day);
    var hour = this.date.getUTCHours() + (period.hours || 0);
    date.setUTCHours(hour);
    var minute = this.date.getUTCMinutes() + (period.minutes || 0);
    date.setUTCMinutes(minute);
    var second = this.date.getUTCSeconds() + (period.seconds || 0);
    date.setUTCSeconds(second);
    var millis = this.date.getUTCMilliseconds() + (period.millis || 0);
    date.setUTCMilliseconds(millis);
    return new DateTime(date, this.tzOffset);
};


DateTime.prototype.subtractDateTime = function(other) {
    var thisValue = this.date.valueOf() + this.tzOffset*1000;
    var otherValue = other.date.valueOf() + other.tzOffset*1000;
    var numDays = ( thisValue - otherValue)/(24*60*60*1000);
    var data = [];
    data[3] = Math.floor(numDays);
    data[4] = this.date.getUTCHours() - other.date.getUTCHours();
    data[5] = this.date.getUTCMinutes() - other.date.getUTCMinutes();
    data[6] = this.date.getUTCSeconds() - other.date.getUTCSeconds();
    data[7] = this.date.getUTCMilliseconds() - other.date.getUTCMilliseconds();
    return new Period(data);
};
/*
DateTime.prototype.subtractDate = function(value) {
    var numDays = (this.date.valueOf() - value.valueOf())/(24*60*60*1000);
    var data = [];
    data[3] = Math.floor(numDays);
    data[4] = this.date.getUTCHours();
    data[5] = this.date.getUTCMinutes();
    data[6] = this.date.getUTCSeconds();
    data[7] = this.date.getUTCMilliseconds();
    return new Period(data);
};

DateTime.prototype.subtractTime = function(value) {
    var data = [];
    data[0] = this.date.getUTCFullYear();
    data[1] = this.date.getUTCMonth();
    data[3] = this.date.getUTCDate();
    data[4] = this.date.getUTCHours() - value.getUTCHours();
    data[5] = this.date.getUTCMinutes() - value.getUTCMinutes();
    data[6] = this.date.getUTCSeconds() - value.getUTCSeconds();
    data[7] = this.date.getUTCMilliseconds() - value.getUTCMilliseconds();
    return new Period(data);
};
*/
DateTime.prototype.subtractPeriod = function(value) {
    var date = new Date();
    var year = this.date.getUTCFullYear() - (value.years || 0);
    date.setUTCFullYear(year);
    var month = this.date.getUTCMonth() - (value.months || 0);
    date.setUTCMonth(month);
    var day = this.date.getUTCDate() - ((value.weeks || 0) * 7) - (value.days || 0);
    date.setUTCDate(day);
    var hour = this.date.getUTCHours() - (value.hours || 0);
    date.setUTCHours(hour);
    var minute = this.date.getUTCMinutes() - (value.minutes || 0);
    date.setUTCMinutes(minute);
    var second = this.date.getUTCSeconds() - (value.seconds || 0);
    date.setUTCSeconds(second);
    var millis = this.date.getUTCMilliseconds() - (value.millis || 0);
    date.setUTCMilliseconds(millis);
    return new DateTime(date, this.tzOffset);
};



DateTime.prototype.toString = function() {
    var s = this.date.toISOString();
    if(this.tzOffset == 0)
        return s;
    s = s.substring(0, s.length()-1); // truncate trailing 'Z';
    var offset = this.tzOffset;
    if (offset > 0)
        s += "+";
    else {
        offset = -offset;
        s += "-";
    }
    s += ("00" + Math.floor(offset / 3600)).slice(-2);
    s += ":";
    s += ("00" + Math.floor((offset % 3600) / 60)).slice(-2);
    return s;
};


DateTime.prototype.getText = DateTime.prototype.toString;


DateTime.prototype.equals = function(value) {
    return value instanceof DateTime && this.date.valueOf() == value.date.valueOf() && this.tzOffset == value.tzOffset;
};

DateTime.prototype.gt = function(other) {
    return other instanceof DateTime && this.compareTo(other.date, other.tzOffset) > 0;
};


DateTime.prototype.gte = function(other) {
    return other instanceof DateTime && this.compareTo(other.date, other.tzOffset) >= 0;
};


DateTime.prototype.lt = function(other) {
    return other instanceof DateTime && this.compareTo(other.date, other.tzOffset) < 0;
};


DateTime.prototype.lte = function(other) {
    return other instanceof DateTime && this.compareTo(other.date, other.tzOffset) <= 0;
};


DateTime.prototype.compareTo = function(date, tzOffset) {
    var a = this.date.valueOf() + this.tzOffset*60000;
    var b = date.valueOf() + tzOffset*60000;
    return a > b ? 1 : (a == b ? 0 : -1);
};


DateTime.prototype.getYear = function(value) {
    return this.date.getUTCFullYear();
};

DateTime.prototype.getMonth = function(value) {
    return this.date.getUTCMonth() + 1;
};


DateTime.prototype.getDayOfMonth = function(value) {
    return this.date.getUTCDate();
};


DateTime.prototype.getDayOfYear = function() {
    var first = new Date(this.date.getUTCFullYear(), 0, 1, 0, 0, 0, 0);
    var numDays = (this - first) / (1000 * 60 * 60 * 24);
    return 1 + Math.floor(numDays);
};

DateTime.prototype.getHour = function(value) {
    return this.date.getUTCHours();
};


DateTime.prototype.getMinute = function(value) {
    return this.date.getUTCMinutes();
};


DateTime.prototype.getSecond = function(value) {
    return this.date.getUTCSeconds();
};


DateTime.prototype.getMillisecond = function(value) {
    return this.date.getUTCMilliseconds();
};

DateTime.prototype.getTzOffset = function(value) {
    return this.date.tzOffset;
};

DateTime.prototype.getTzName = function(value) {
    return "Z";
};

