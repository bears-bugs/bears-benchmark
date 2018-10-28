function $Root() {
    this.mutable = false;
    this.storable = this.storable || null;
    this.category = [];
    return this;
}

$Root.prototype.instanceOf = function(type) {
    return this.category.indexOf(type)>=0;
};

$Root.prototype.toString = function() {
    var names = Object.getOwnPropertyNames(this).filter(function(name) {
        return name!=="dbId" && name!=="mutable" && name!=="storable" && name!=="category" && typeof(this[name])!='function';
    }, this);
    var vals = names.map(function (name) {
        return name + ':' + this[name];
    }, this);
    return "{" + vals.join(", ") + "}";
};

$Root.prototype.getText = $Root.prototype.toString;


$Root.prototype.setMember = function(name, value, mutable, isEnum) {
    if(!this.mutable || (value && value.mutable && !mutable))
        throw new NotMutableError();
    this[name] = value;
    if(this.storable) {
        if(isEnum && value)
            value = value.name;
        this.storable.setData(name, value);
    }
};

$Root.prototype.fromStored = function(stored) {
    for(name in this) {
        if(name==='mutable' || name==='storable' || name==='category' || typeof(this[name]) === 'function')
            continue;
        var value = stored.getData(name);
        var method = this["load$" + name];
        this[name] = method ? method(value) : value;
    }
    this.dbId = stored.getData("dbId");
};

$Root.prototype.collectStorables = function(storablesToAdd) {
    if(this.storable) {
        if(!this.dbId)
            this.dbId = this.storable.getOrCreateDbId();
        storablesToAdd.add(this.storable);
        // TODO: traverse object tree
    }

};

$Root.prototype.collectDbIds = function(idsToDelete) {
    if(this.dbId)
        idsToDelete.add(this.dbId);
    // TODO: traverse object tree
};
