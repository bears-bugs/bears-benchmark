
function IntegerRange(first, last) {
    Range.call(this, first, last);
    return this;
}

IntegerRange.prototype = Object.create(Range.prototype);
IntegerRange.prototype.constructor = IntegerRange;

Object.defineProperty(IntegerRange.prototype, "length", {
    get: function() {
        return 1 + this.last - this.first;
    }
});

IntegerRange.prototype.item = function(idx) {
    return this.first + idx - 1;
};

IntegerRange.prototype.has = function(value) {
    var int = Math.floor(value);
    return int==value && int>=this.first && int<=this.last;
};
