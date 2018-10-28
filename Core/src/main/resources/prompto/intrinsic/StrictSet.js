function StrictSet(values) {
    this.set = new Set(values);
    return this;
}

Object.defineProperty(StrictSet.prototype, "length", {
    get : function() {
        return this.set.size;
    }
});

StrictSet.prototype.toString = function() {
    return "<" + Array.from(this.set.values()).join(", ") + ">";
};


StrictSet.prototype.getText = StrictSet.prototype.toString;


StrictSet.prototype.iterator = function() {
    var iter = this.set.values();
    var item = iter.next();
    return {
        hasNext: function() { return !item.done; },
        next: function() { var value = item.value; item = iter.next(); return value; }
    };
};

StrictSet.prototype.item = function(idx) {
    var iter = this.set.values();
    var item = iter.next();
    while(--idx>=0 && !item.done)
        item = iter.next();
    if(item.done)
        return null;
    else
        return item.value;
};




StrictSet.prototype.addItems = function(items) {
    if(items instanceof StrictSet)
        items = Array.from(items.set.values());
    items.forEach(function(item){
        this.add(item);
    }, this);
    return this; // enable fluid API
};

StrictSet.prototype.addAll = function(items) {
    var result = new StrictSet(this.set);
    result.addItems(items);
    return result;
};


StrictSet.prototype.add = function(value) {
    if(this.has(value))
        return false;
    else {
        this.set.add(value);
        return true;
    }
};

StrictSet.prototype.has = function(value, noCheckEquals) {
    if(this.set.has(value))
        return true;
    if(noCheckEquals)
        return false;
    var iter = this.set.values();
    var item = iter.next();
    while(!item.done) {
        if(value.equals && value.equals(item.value))
            return true;
        item = iter.next();
    }
    return false;
};


StrictSet.prototype.hasAll = function(items, noCheckEquals) {
    if(items instanceof StrictSet)
        items = Array.from(items.set.values());
    for (var i = 0; i < items.length; i++) {
        if (!this.has(items[i], noCheckEquals))
            return false;
    }
    return true;
};


StrictSet.prototype.hasAny = function(items, noCheckEquals) {
    if(items instanceof StrictSet)
        items = Array.from(items.set.values());
    if(noCheckEquals) {
        for (var i = 0; i < items.length; i++) {
            if (this.set.has(items[i]))
                return true;
        }
        return false;
    } else {
        for (var i = 0; i < items.length; i++) {
            if (this.has(items[i]))
                return true;
        }
        return false;
    }
};


StrictSet.prototype.equals = function(other) {
    if(!(other instanceof StrictSet))
        return false;
    else if(this.length!=other.length)
        return false;
    else {
        var iter = this.set.values();
        var item = iter.next();
        while(!item.done) {
            if(!other.has(item.value))
                return false;
            item = iter.next();
        }
        return true;
    }
};

StrictSet.prototype.intersect = function(other) {
    var items = [];
    this.set.forEach( function(item) {
        if(other.has(item))
            items.push(item);
    });
    return new StrictSet(items);
};

StrictSet.prototype.sorted = function(sortFunction) {
    var sorted = Array.from(this.set).sort(sortFunction);
    return new List(false, sorted);
};


StrictSet.prototype.filtered = function(filterFunction) {
    var filtered = Array.from(this.set).filter(filterFunction);
    return new StrictSet(filtered);
};
