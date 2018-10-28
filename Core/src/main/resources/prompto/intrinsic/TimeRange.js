
function TimeRange(first, last) {
    Range.call(this, first, last);
    return this;
}

TimeRange.prototype = Object.create(Range.prototype);
TimeRange.prototype.constructor = TimeRange;

Object.defineProperty(TimeRange.prototype, "length", {
    get: function() {
        return 1 + (this.last.valueOf() - this.first.valueOf())/1000;
    }
});

TimeRange.prototype.item = function(idx) {
    var result = this.first.valueOf() + (idx-1)*1000;
    if(result > this.last.valueOf()) {
        throw new RangeError();
    }
    return new LocalTime(result);
};

TimeRange.prototype.has = function(value) {
    var int = value.valueOf();
    return int >= this.first.valueOf() && int <= this.last.valueOf();
};
