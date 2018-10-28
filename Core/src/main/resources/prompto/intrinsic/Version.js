function Version(major, minor, fix) {
    this.major = major;
    this.minor = minor;
    this.fix = fix;
    return this;
}

Version.Parse = function(text) {
    var d1 = text.indexOf('.');
    var major = parseInt(text.substring(0, d1));
    var d2 = text.indexOf('.', d1 + 1);
    var minor = parseInt(text.substring(d1 + 1, d2));
    var fix = parseInt(text.substring(d2 + 1));
    return new Version(major, minor, fix);
};


Version.prototype.equals = function(obj) {
    return obj instanceof Version && this.asInt() == obj.asInt();
};


Version.prototype.toString = function() {
    return "" + this.major + "." + this.minor + "." + this.fix;
};

Version.prototype.getText = Version.prototype.toString;


Version.prototype.asInt = function() {
    return (this.major << 24) | (this.minor << 16) | this.fix;
};

Version.prototype.gt = function(other) {
    return other instanceof Version && this.asInt() > other.asInt();
};


Version.prototype.gte = function(other) {
    return other instanceof Version && this.asInt() >= other.asInt();
};

Version.prototype.lt = function(other) {
    return other instanceof Version && this.asInt() < other.asInt();
};

Version.prototype.lte = function(other) {
    return other instanceof Version && this.asInt() <= other.asInt();
};


Version.prototype.cmp = function(value) {
    var a = this.asInt();
    var b = value.asInt();
    return a > b ? 1 : (a == b ? 0 : -1);
};
