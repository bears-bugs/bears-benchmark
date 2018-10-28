function LocalTime(value) {
    var time = value ? new Date(value) : new Date();
    time.__proto__ = LocalTime.prototype;
    return time;
}

LocalTime.parse = function(text) {
    var time = new LocalTime();
    time.setUTCHours(parseInt(text.substring(0,2)));
    time.setUTCMinutes(parseInt(text.substring(3,5)));
    time.setUTCSeconds(parseInt(text.length>6 ? text.substring(6,8) : 0));
    time.setUTCMilliseconds(text.length>9 ? parseInt(text.substring(9,13)) : 0);
    return time;
};

LocalTime.prototype.__proto__ = Date.prototype;

LocalTime.prototype.toString = function() {
    return this.toISOString().substring(11, 23)
};


LocalTime.prototype.getText = LocalTime.prototype.toString;

LocalTime.prototype.equals = function(obj) {
    return obj instanceof LocalTime && this.valueOf() == obj.valueOf();
};


LocalTime.prototype.gt = function(other) {
    return other instanceof LocalTime && this.valueOf() > other.valueOf();
};


LocalTime.prototype.gte = function(other) {
    return other instanceof LocalTime && this.valueOf() >= other.valueOf();
};

LocalTime.prototype.lt = function(other) {
    return other instanceof LocalTime && this.valueOf() < other.valueOf();
};


LocalTime.prototype.lte = function(other) {
    return other instanceof LocalTime && this.valueOf() <= other.valueOf();
};


LocalTime.prototype.addPeriod = function(period) {
    var time = new LocalTime();
    var hour = this.getUTCHours() + (period.hours || 0);
    time.setUTCHours(hour);
    var minute = this.getUTCMinutes() + (period.minutes || 0);
    time.setUTCMinutes(minute);
    var second = this.getUTCSeconds() + (period.seconds || 0);
    time.setUTCSeconds(second);
    var millis = this.getUTCMilliseconds() + (period.millis || 0);
    time.setUTCMilliseconds(millis);
    return time;
};

LocalTime.prototype.subtractTime = function(time) {
    var data = [];
    data[4] = this.getUTCHours() - time.getUTCHours();
    data[5] = this.getUTCMinutes() - time.getUTCMinutes();
    data[6] = this.getUTCSeconds() - time.getUTCSeconds();
    data[7] = this.getUTCMilliseconds() - time.getUTCMilliseconds();
    return new Period(data);
};


LocalTime.prototype.subtractPeriod = function(period) {
    var time = new LocalTime();
    var hour = this.getUTCHours() - (period.hours || 0);
    time.setUTCHours(hour);
    var minute = this.getUTCMinutes() - (period.minutes || 0);
    time.setUTCMinutes(minute);
    var second = this.getUTCSeconds() - (period.seconds || 0);
    time.setUTCSeconds(second);
    var millis = this.getUTCMilliseconds() - (period.millis || 0);
    time.setUTCMilliseconds(millis);
    return time;
};

LocalTime.prototype.getHour = function(value) {
    return this.getUTCHours();
};


LocalTime.prototype.getMinute = function(value) {
    return this.getUTCMinutes();
};


LocalTime.prototype.getSecond = function(value) {
    return this.getUTCSeconds();
};


LocalTime.prototype.getMillisecond = function(value) {
    return this.getUTCMilliseconds();
};
