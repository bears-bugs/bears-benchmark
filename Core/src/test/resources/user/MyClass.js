function MyClass() {
	this.$id = null;
	this.$name = null;
	this.$display = null;
	return this;
};

Object.defineProperty(MyClass.prototype, "id", {
	get : function() {
		return this.$id;
	},
	set : function(value) {
		this.$id = value;
		this.computeDisplay();
	}
});

Object.defineProperty(MyClass.prototype, "name", {
	get : function() {
		return this.$name;
	},
	set : function(value) {
		this.$name = value;
		this.computeDisplay();
	}
});

Object.defineProperty(MyClass.prototype, "display", {
    get : function() {
        return this.$display;
    },
    set : function(value) {}
});

MyClass.prototype.computeDisplay = function() {
	this.$display = "/id=" + this.$id + "/name=" + this.$name;
};

MyClass.prototype.getDisplay = function() {
	return this.display;
};

MyClass.prototype.printDisplay = function() {
	process.stdout.write(this.display);
};

MyClass.boolValue = function() {
	return true;
};

MyClass.intValue = function() {
	return 123;
};

MyClass.intObject = function() {
	return 123;
};

MyClass.longValue = function() {
	return 9876543210;
};

MyClass.longObject = function() {
	return 9876543210;
};

MyClass.characterValue = function() {
	return 'Z';
};

exports.MyClass = MyClass;

