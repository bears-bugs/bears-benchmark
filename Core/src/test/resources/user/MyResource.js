var contents = {};

function MyResource() {
	this.path = null;
	return this;
}

MyResource.prototype.isReadable = function() {
	return true;
};

MyResource.prototype.isWritable = function() {
	return true;
};

MyResource.prototype.close = function() {
};

MyResource.prototype.readFully = function() {
	return contents[this.path] || null;
};

MyResource.prototype.writeFully = function(data) {
    contents[this.path] = data;
};

MyResource.prototype.readLine = function() {
    if(!this.lines) {
        var full = this.readFully();
        if(full)
            this.lines = full.split("\n");
        else
            this.lines = [];
    }
    if(this.lines.length>0)
        return this.lines.pop(0);
    else
        return null;
};

MyResource.prototype.writeLine = function(data) {
    var full = this.readFully() || "";
    if(full.length>0)
        full += "\n";
    full += data;
    contents[this.path] = full;
};


Object.defineProperty(MyResource.prototype, "content", {
    set: function(value) {
        if(typeof(value)===typeof(""))
            contents[this.path] = value;
    }
});

exports.MyResource = MyResource;
