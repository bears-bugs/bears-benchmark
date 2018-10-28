function isAnInteger(o) {
    return typeof(o) === "number" && o === Math.floor(o);
}

function isADecimal(o) {
    return typeof(o) === "number" && o !== Math.floor(o);
}

function isAText(o) {
    return typeof(o) === 'string' || o instanceof String;
}

function equalObjects(o1, o2) {
    if(Object.is(o1, o2))
        return true;
    else
        return typeof(o1)==='object' && o1.equals && o1.equals(o2);

}

TypeError.prototype.getText = function() { return 'Null reference!'; };
ReferenceError.prototype.getText = function() { return 'Null reference!'; };
RangeError.prototype.getText = function() { return 'Index out of range!'; };
if(!Object.values) { 
	Object.values = function(o) {
	    var values = [];
	    for(name in o) { values.push(o[name]); }
	    return values;
	}; 
}

Boolean.prototype.getText = Boolean.prototype.toString;
Boolean.prototype.equals = function(value) {
	return this == value;
};
Number.prototype.formatInteger = function(format) {
    var value = "000000000000" + this;
    return value.substr(value.length - format.length);
};
Number.prototype.toDecimalString = function() {
    // mimic 0.0######
    var s = this.toString();
    var i = s.indexOf('.');
    if(i>=0) {
        // fix IEEE issue
        i = s.indexOf('000000', i);
        if( i < 0)
            return s;
        else
            return s.substr(0, i);
    } else
        return s + ".0";
};
Number.prototype.getText = Number.prototype.toString;
String.prototype.hasAll = function(items) {
    if(StrictSet && items instanceof StrictSet)
        items = Array.from(items.set.values());
    for(var i=0;i<items.length;i++) {
        if(!this.includes(items[i]))
            return false;
    }
    return true;
};
String.prototype.hasAny = function(items) {
    if(StrictSet && items instanceof StrictSet)
        items = Array.from(items.set.values());
    for(var i=0;i<items.length;i++) {
        if(this.includes(items[i]))
            return true;
    }
    return false;
};
String.prototype.splitToList = function(separator) {
    return new List(false, this.split(separator));
};

String.prototype.slice1Based = function(start, last) {
    if(start) {
        if (start < 0 || start >= this.length)
            throw new RangeError();
        start = start - 1;
    } else
        start = 0;
    if(!last)
        return this.substring(start);
    if(last >= 0) {
        if(last<1 || last>this.length)
            throw new RangeError();
        return this.substring(start, last);
    } else {
        if(last<-this.length)
            throw new RangeError();
        return this.substring(start, this.length + 1 + last)
    }
};
String.prototype.getText = String.prototype.toString;
String.prototype.indexOf1Based = function(value) {
	return 1 + this.indexOf(value);
};

String.prototype.equals = function(value) {
	return this == value;
};


