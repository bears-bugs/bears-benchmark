
function CharacterRange(first, last) {
    IntegerRange.call(this, first.charCodeAt(0), last.charCodeAt(0));
    return this;
}

CharacterRange.prototype = Object.create(IntegerRange.prototype);
CharacterRange.prototype.constructor = CharacterRange;

CharacterRange.prototype.has = function(value) {
    var int = value.charCodeAt(0);
    return int>=this.first && int<=this.last;
};


CharacterRange.prototype.item = function(idx) {
    return String.fromCharCode(this.first + idx - 1);
};

