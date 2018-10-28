function NotMutableError() {
    if (!Error.captureStackTrace)
      this.stack = (new Error()).stack;
    else
      Error.captureStackTrace(this, this.constructor);
    return this;
}
NotMutableError.prototype = Object.create(Error.prototype);
NotMutableError.prototype.constructor = NotMutableError;
NotMutableError.prototype.message = "Not a mutable object!";
NotMutableError.prototype.name = "NotMutableError";
NotMutableError.prototype.promptoName = "NOT_MUTABLE";
NotMutableError.prototype.toString = function() {
	return this.message;
};
NotMutableError.prototype.getText = function() {
	return this.message;
};


function NotStorableError() {
    if (!Error.captureStackTrace)
      this.stack = (new Error()).stack;
    else
      Error.captureStackTrace(this, this.constructor);
    return this;
}
NotStorableError.prototype = Object.create(Error.prototype);
NotStorableError.prototype.constructor = NotStorableError;
NotStorableError.prototype.message = "Not a storable object!";
NotStorableError.prototype.name = "NotStorableError";
NotStorableError.prototype.promptoName = "NOT_STORABLE";
NotStorableError.prototype.toString = function() {
	return this.message;
};
NotStorableError.prototype.getText = function() {
	return this.message;
};

function ReadWriteError() {
    if (!Error.captureStackTrace)
      this.stack = (new Error()).stack;
    else
      Error.captureStackTrace(this, this.constructor);
    return this;
}
ReadWriteError.prototype = Object.create(Error.prototype);
ReadWriteError.prototype.constructor = ReadWriteError;
ReadWriteError.prototype.message = "Read/write error!";
ReadWriteError.prototype.name = "ReadWriteError";
ReadWriteError.prototype.promptoName = "READ_WRITE";
ReadWriteError.prototype.toString = function() {
	return this.message;
};
ReadWriteError.prototype.getText = function() {
	return this.message;
};

function DivideByZeroError() {
    if (!Error.captureStackTrace)
      this.stack = (new Error()).stack;
    else
      Error.captureStackTrace(this, this.constructor);
    return this;
}
DivideByZeroError.prototype = Object.create(Error.prototype);
DivideByZeroError.prototype.constructor = DivideByZeroError;
DivideByZeroError.prototype.message = "Divide by zero!";
DivideByZeroError.prototype.name = "DivideByZeroError";
DivideByZeroError.prototype.promptoName = "DIVIDE_BY_ZERO";
DivideByZeroError.prototype.toString = function() {
	return this.message;
};
DivideByZeroError.prototype.getText = function() {
	return this.message;
};

var NativeErrors = {
    DIVIDE_BY_ZERO: DivideByZeroError,
    INDEX_OUT_OF_RANGE: RangeError,
    NULL_REFERENCE: ReferenceError,
    NOT_MUTABLE: NotMutableError,
    NOT_STORABLE: NotStorableError,
    READ_WRITE: ReadWriteError
};

function translateError(e) {
    if(e.promptoName)
        return e.promptoName;
    else if(e instanceof RangeError)
        return "INDEX_OUT_OF_RANGE";
    else if(e instanceof TypeError)
        return "NULL_REFERENCE";
    else if(e instanceof ReferenceError)
        return "NULL_REFERENCE";
    else
        return "<unknown: " + e.name + ">";
}