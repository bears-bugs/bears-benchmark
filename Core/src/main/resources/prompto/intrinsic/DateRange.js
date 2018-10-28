
function DateRange(first, last) {
    Range.call(this, first, last);
    return this;
}

DateRange.prototype = Object.create(Range.prototype);
DateRange.prototype.constructor = DateRange;

Object.defineProperty(DateRange.prototype, "length", {
    get: function() {
        var h = this.last.valueOf();
        var l = this.first.valueOf();
        return 1 + ( (h-l)/(24*60*60*1000));
    }
});

DateRange.prototype.item = function(idx) {
    var millis = this.first.valueOf() + (idx-1)*(24*60*60*1000);
    if(millis > this.last.valueOf()) {
        throw new RangeError();
    } else {
        return new LocalDate(millis);
    }
};

DateRange.prototype.has = function(value) {
    var int = value.valueOf();
    return int>=this.first.valueOf() && int<=this.last.valueOf();
};
