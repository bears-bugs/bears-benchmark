function Tuple(mutable, items) {
	List.call(this, mutable, items);
	return this;
}

Tuple.prototype = Object.create(List.prototype);
Tuple.prototype.constructor = Tuple;

Tuple.prototype.add = function(items) {
    if(typeof(StrictSet) !== 'undefined' && items instanceof StrictSet)
        items = Array.from(items.set.values());
    var concat = new Tuple(false);
    concat.addItems(this);
    concat.addItems(items);
    return concat;
};

Tuple.prototype.equals = function(o) {
    o = o || null;
    if(this===o) {
        return true;
    }
    if(!(o instanceof Tuple) || this.length !== o.length) {
        return false;
    }
    for(var i=0;i<this.length;i++) {
        if(!equalObjects(this[i], o[i])) {
            return false;
        }
    }
    return true;
};

Tuple.prototype.toString = function() {
	return "(" + this.join(", ") + ")";
};

Tuple.prototype.getText = Tuple.prototype.toString;