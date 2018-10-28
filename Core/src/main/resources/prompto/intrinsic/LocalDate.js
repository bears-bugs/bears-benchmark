function LocalDate(value) {
    var date = new Date(value);
    date.__proto__ = LocalDate.prototype;
    return date;
}

LocalDate.parse = function(value) {
    return new LocalDate(value);
};

LocalDate.prototype.__proto__ = Date.prototype;

LocalDate.prototype.toString = function() {
    return this.toISOString().substring(0, 10);
};

LocalDate.prototype.getText = LocalDate.prototype.toString;

LocalDate.prototype.equals = function(other) {
    return other instanceof LocalDate && this.valueOf() == other.valueOf();
};

LocalDate.prototype.gt = function(other) {
    return other instanceof LocalDate && this.valueOf() > other.valueOf();
};


LocalDate.prototype.gte = function(other) {
    return other instanceof LocalDate && this.valueOf() >= other.valueOf();
};


LocalDate.prototype.lt = function(other) {
    return other instanceof LocalDate && this.valueOf() < other.valueOf();
};


LocalDate.prototype.lte = function(other) {
    return other instanceof LocalDate && this.valueOf() <= other.valueOf();
};

LocalDate.prototype.addPeriod = function (period) {
    var result = new LocalDate();
    var year = this.getUTCFullYear() + (period.years || 0);
    result.setUTCFullYear(year);
    var month = this.getUTCMonth() + (period.months || 0);
    result.setUTCMonth(month);
    var day = this.getUTCDate() + ((period.weeks || 0) * 7) + (period.days || 0);
    result.setUTCDate(day);
    return result;
};


LocalDate.prototype.subtractDate = function(value) {
    var data = [];
    data[0] = this.getUTCFullYear() - value.getUTCFullYear();
    data[1] = this.getUTCMonth() - value.getUTCMonth();
    data[3] = this.getUTCDate() - value.getUTCDate();
    return new Period(data);
};



LocalDate.prototype.subtractPeriod = function(value) {
    var date = new LocalDate();
    var year = this.getUTCFullYear() - (value.years || 0);
    date.setUTCFullYear(year);
    var month = this.getUTCMonth() - (value.months || 0);
    date.setUTCMonth(month);
    var day = this.getUTCDate() - ((value.weeks || 0) * 7) - (value.days || 0);
    date.setUTCDate(day);
    return date;
};


LocalDate.prototype.getYear = function(value) {
    return this.getUTCFullYear();
};

LocalDate.prototype.getMonth = function(value) {
    return this.getUTCMonth() + 1;
};


LocalDate.prototype.getDayOfMonth = function(value) {
    return this.getUTCDate();
};


LocalDate.prototype.getDayOfYear = function() {
    var first = new Date(this.getUTCFullYear(), 0, 1, 0, 0, 0, 0);
    var numDays = (this - first) / (1000 * 60 * 60 * 24);
    return 1 + Math.floor(numDays);
};
