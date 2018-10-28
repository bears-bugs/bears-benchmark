function Any() {
	return this;
}

Any.prototype.getText = Any.prototype.toString;

exports.Any = Any;