function Dictionary(mutable, entries) {
    if(entries)
        Object.getOwnPropertyNames(entries).forEach(function(name) { this[name] = entries[name]; }, this);
    this.mutable = mutable || false;
    return this;
}

Object.defineProperty(Dictionary.prototype, "$keys", {
    get : function() {
        return Object.getOwnPropertyNames(this).filter(function(name) { return name!=="mutable"; });
    }
});


Object.defineProperty(Dictionary.prototype, "length", {
    get : function() {
        return this.$keys.length;
    }
});

Object.defineProperty(Dictionary.prototype, "keys", {
    get : function() {
        return new StrictSet(this.$keys);
    }
});


Object.defineProperty(Dictionary.prototype, "values", {
    get : function() {
        var names = this.$keys.map(function(name) { return this[name]; }, this);
        return new List(false, names);
    }
});

Dictionary.prototype.iterator = function() {
    var self = this;
    var iter = this.keys.iterator();
    return {
        hasNext: iter.hasNext,
        next: function() { var key = iter.next(); return {key: key, value: self[key] }; }
    };
};

Dictionary.prototype.add = function(dict) {
    var result = Object.assign({}, this, dict);
    result.__proto__ = Dictionary.prototype;
    return result;
}


Dictionary.prototype.toString = function() {
    var vals = this.$keys.map(function (name) {
        return '"' + name + '":' + this[name];
    }, this);
    return "{" + vals.join(", ") + "}";
};


Dictionary.prototype.getText = Dictionary.prototype.toString;


Dictionary.prototype.equals = function(dict) {
    var keys = this.$keys;
    if (this.length != dict.length)
        return false;
    for (var i = 0; i < keys.length; i++) {
        var key = keys[i];
        var v1 = this[key] || null;
        var v2 = dict[key] || null;
        if (v1 === v2)
            continue;
        else if (v1 === null || v2 === null)
            return false;
        else if (v1.equals) {
            if (!v1.equals(v2)) {
                return false;
            }
        } else if (v2.equals) {
            if (!v2.equals(v1)) {
                return false;
            }
        } else
            return false;
    }
    return true;
};

Dictionary.prototype.has = function(item) {
    return this.hasOwnProperty(item);
};


Dictionary.prototype.hasAll = function(items) {
    return this.keys.hasAll(items, true);
};

Dictionary.prototype.hasAny = function(item) {
    return this.keys.hasAny(item, true);
};

Dictionary.prototype.item = function(item) {
    if(!item)
        throw new ReferenceError();
    if(!this.hasOwnProperty(item))
        throw new RangeError();
    return this[item];
};


Dictionary.prototype.setItem = function (item, value) {
    if(!this.mutable)
        throw new NotMutableError();
    else if(item==null)
        throw new ReferenceError();
    else
        this[item] = value;
};
