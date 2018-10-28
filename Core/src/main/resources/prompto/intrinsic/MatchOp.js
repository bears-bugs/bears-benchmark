function MatchOp(name) {
    this.name = name;
    return this;
};

MatchOp.prototype.toString = function() {
    return this.name;
};

MatchOp.prototype.toTranspiled = function() {
    return "new MatchOp('" + this.name + "')";
};

MatchOp.EQUALS = new MatchOp("EQUALS");
MatchOp.ROUGHLY = new MatchOp("ROUGHLY");
MatchOp.CONTAINS = new MatchOp("CONTAINS");
MatchOp.HAS = new MatchOp("HAS");
MatchOp.IN = new MatchOp("IN");
MatchOp.CONTAINED = new MatchOp("CONTAINED");
MatchOp.GREATER = new MatchOp("GREATER");
MatchOp.LESSER = new MatchOp("LESSER");
