function Range(first, last) {
    this.first = first;
    this.last = last;
    return this;
}

Range.prototype.toString = function() {
    return "[" + this.first + ".." + this.last + "]";
};


Range.prototype.getText = Range.prototype.toString;


Range.prototype.iterator = function() {
    var self = this;
    return {
        idx: 1, // since we are calling item()
        length: self.length,
        hasNext: function() { return this.idx <= this.length },
        next : function() { return self.item(this.idx++); }
    };
};

Range.prototype.equals = function(obj) {
    if(Object.is(this, obj))
        return true;
    else if(Object.getPrototypeOf(this) === Object.getPrototypeOf(obj))
        return equalObjects(this.first, obj.first) && equalObjects(this.last, obj.last);
    else
        return false;
};

Range.prototype.hasAll = function(items) {
    if(typeof(StrictSet) !== 'undefined' && items instanceof StrictSet)
        items = Array.from(items.set.values());
    for (var i = 0; i < items.length; i++) {
        if (!this.has(items[i]))
            return false;
    }
    return true;
};


Range.prototype.slice1Based = function(start, end) {
    this.checkRange(start, end);
    var range = Object.create(this);
    range.first = start ? this.item(start) : this.first;
    range.last = end ? ( end > 0 ? this.item(end) : this.item(this.length + 1 + end) ) : this.last;
    return range;
};


Range.prototype.checkRange = function(start, end) {
    if(start && (start<1 || start>this.length))
        throw new RangeError();
    if(!start)
        start = 1;
    if(end) {
        if(end>=0 && (end<1 || end>this.length))
            throw new RangeError();
        else if(end<-this.length)
            throw new RangeError();
    }
};

Range.prototype.hasAny = function(items) {
    if(typeof(StrictSet) !== 'undefined' && items instanceof StrictSet)
        items = Array.from(items.set.values());
    for (var i = 0; i < items.length; i++) {
        if (this.has(items[i]))
            return true;
    }
    return false;
};

