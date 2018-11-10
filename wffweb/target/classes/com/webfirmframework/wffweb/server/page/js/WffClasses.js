var getValueTypeByte = function(valueType) {

	if (valueType === '[object String]') {
		return 0;
	} else if (valueType === '[object Number]') {
		return 1;
	} else if (valueType === '[object Undefined]') {
		return 2;
	} else if (valueType === '[object Null]') {
		return 3;
	} else if (valueType === '[object Boolean]') {
		return 4;
	} else if (valueType === '[object Object]') {
		return 5;
	} else if (valueType === '[object Array]') {
		return 6;
	} else if (valueType === '[object RegExp]') {
		return 7;
	} else if (valueType === '[object Function]') {
		return 8;
	} else if (valueType === '[object Int8Array]' 
		|| valueType == "[object Uint8Array]") {
		return 9;
	}

	return -1;
};

var WffBMArray = function(jsArray, outer) {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	this.jsArray = jsArray;
	this.outer = outer;

	var getWffBMArray = function(jsArray, outer) {

		var nameValues = [];

		console.log('outer yes', outer);

		if (typeof outer === 'undefined' || outer) {
			console.log('added outer ');

			// nameValue for representing object or array
			var typeNameValue = {
				name : [ 1 ],
				values : []
			};
			nameValues.push(typeNameValue);
		}

		if (jsArray.length > 0) {

			var arrayValType;

			for (var i = 0; i < jsArray.length; i++) {
				arrayValType = getValueTypeByte(Object.prototype.toString
						.call(jsArray[i]));
				if (arrayValType != 2 && arrayValType != 3) {
					break;
				}

			}

			console.log('arrayValType', arrayValType);

			var nameValue = {
				name : [ arrayValType ]
			};
			nameValues.push(nameValue);

			var values = [];
			nameValue.values = values;

			if (arrayValType == 0) {
				for (var i = 0; i < jsArray.length; i++) {
					if (arrayValType != 2 && arrayValType != 3) {
						values.push(encoder.encode(jsArray[i]));
					} else {
						values.push([]);
					}
				}
			} else if (arrayValType == 1) {

				for (var i = 0; i < jsArray.length; i++) {
					if (arrayValType != 2 && arrayValType != 3) {
						values.push(wffBMUtil.getBytesFromDouble(jsArray[i]));
					} else {
						values.push([]);
					}
				}
			}  else if (arrayValType == 2 
					|| arrayValType == 3) {

				for (var i = 0; i < jsArray.length; i++) {
					values.push([]);
				}
				
			} else if (arrayValType == 4) {
				for (var i = 0; i < jsArray.length; i++) {
					if (arrayValType != 2 && arrayValType != 3) {
						if (jsArray[i]) {
							values.push(true);
						} else {
							values.push(false);
						}
					} else {
						values.push([]);
					}
				}
			} else if (arrayValType == 5) {
				for (var i = 0; i < jsArray.length; i++) {
					values
							.push(new WffBMObject(jsArray[i], false)
									.getBMBytes());
				}
			} else if (arrayValType == 6) {
				for (var i = 0; i < jsArray.length; i++) {
					values.push(new WffBMArray(jsArray[i], false).getBMBytes());
				}
			} else if (arrayValType == 7 || arrayValType == 8) {
				for (var i = 0; i < jsArray.length; i++) {
					if (arrayValType != 2 && arrayValType != 3) {
						values.push(encoder.encode(jsArray[i].toString()));
					} else {
						values.push([]);
					}
				}
			} else if (arrayValType == 9) {
				for (var i = 0; i < jsArray.length; i++) {
					if (typeof jsArray[i] === 'undefined' || jsArray[i] == null) {
						values.push([]);
					} else {
						values.push(new WffBMByteArray(jsArray[i], false)
								.getBMBytes());
					}
				}
			} else {
				values.push([]);
			}
			console.log('values', values);

			// push wff bm array
			// typeByte.push(value);
		}

		return wffBMUtil.getWffBinaryMessageBytes(nameValues);
	};

	this.getBMBytes = function getBMBytes() {
		return getWffBMArray(this.jsArray, this.outer);
	};

	return this;
};

var WffBMByteArray = function(int8Array, outer) {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	this.jsArray = int8Array;
	this.outer = outer;

	var getWffBMByteArray = function(int8Array, outer) {

		var nameValues = [];

		console.log('outer yes', outer);

		if (typeof outer === 'undefined' || outer) {
			console.log('added outer ');

			// nameValue for representing object or array
			var typeNameValue = {
				name : [ 1 ],
				values : []
			};
			nameValues.push(typeNameValue);
		}

		// 10 represents BMValueType.INTERNAL_BYTE type
		var arrayValType = 10;

		console.log('arrayValType', arrayValType);

		var nameValue = {
			name : [ arrayValType ]
		};

		nameValues.push(nameValue);

		nameValue.values = [ int8Array ];

		console.log('WffBMByteArray nameValue.values', nameValue.values);

		return wffBMUtil.getWffBinaryMessageBytes(nameValues);
	};

	this.getBMBytes = function getBMBytes() {
		return getWffBMByteArray(this.jsArray, this.outer);
	};

	return this;
};

var WffBMObject = function(jsObject, outer) {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	this.jsObject = jsObject;
	this.outer = outer;

	var getWffBMObject = function(jsObj, outer) {

		console.log('object outer ', outer);

		var jsObjType = Object.prototype.toString.call(jsObj);
		console.log('jsObjType', jsObjType);

		var nameValues = [];

		if (typeof outer === 'undefined' || outer) {
			// nameValue for representing object or array
			var typeNameValue = {
				name : [ 0 ],
				values : []
			};
			nameValues.push(typeNameValue);
		}

		for ( var k in jsObj) {

			var value = jsObj[k];

			console.log('k', k, 'jsObj[k]', jsObj[k]);
			var valType = getValueTypeByte(Object.prototype.toString
					.call(value));

			console.log(valType);

			var nameValue = {};
			nameValue.name = encoder.encode(k);

			var values = [ [ valType ] ];
			nameValue.values = values;

			var typeByte = [ valType ];

			if (valType == 0) {
				console.log("string");

				values.push(encoder.encode(value));

			} else if (valType == 1) {
				console.log("number");
				values.push(wffBMUtil.getBytesFromDouble(value));

			} else if (valType == 2) {
				console.log("Undefined");
				values.push([]);

			} else if (valType == 3) {
				console.log("Null");
				values.push([]);
			} else if (valType == 4) {
				console.log("Boolean");
				if (value) {
					values.push([ 1 ]);
				} else {
					values.push([ 0 ]);
				}
			} else if (valType == 5) {
				console.log("Object");
				values.push(new WffBMObject(value, false).getBMBytes());

			} else if (valType == 6) {
				console.log("Array");

				// push wff bm array
				// typeByte.push(value);
				if (value.length == 0) {
					values.push([]);
				} else {
					values.push(new WffBMArray(value, false).getBMBytes());
				}

			} else if (valType == 7 || valType == 8) {
				console.log("RegExp");
				values.push(encoder.encode(value.toString()));
			} else if (valType == 9) {
				values.push(new WffBMByteArray(value, false).getBMBytes());
			} else {
				values.push([]);
			}

			nameValues.push(nameValue);

			console.log('nameValue', nameValue);
		}

		console.log('nameValues', nameValues);

		return wffBMUtil.getWffBinaryMessageBytes(nameValues);
	};

	this.getBMBytes = function getBMBytes() {
		return getWffBMObject(this.jsObject, this.outer);
	};
};

var JsObjectFromBMBytes = function(wffBMBytes, outer) {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	var getStringFromBytes = function(utf8Bytes) {
		return wffGlobal.decoder.decode(new Uint8Array(utf8Bytes));
	};

	var nameValues = wffBMUtil.parseWffBinaryMessageBytes(wffBMBytes);

	var i = 0;
	if (typeof outer === 'undefined' || outer) {
		i = 1;
	}

	for (; i < nameValues.length; i++) {
		var nameValue = nameValues[i];
		console.log('nameValue', nameValue);

		var name = getStringFromBytes(nameValue.name);
		var values = nameValue.values;

		if (values[0] == 0) {
			// 0 for string data type
			this[name] = getStringFromBytes(values[1]);
		} else if (values[0] == 1) {
			// 1 for number data type
			this[name] = wffBMUtil.getDoubleFromOptimizedBytes(values[1]);
		} else if (values[0] == 2) {
			// 2 for undefined data type
			this[name] = undefined;
		} else if (values[0] == 3) {
			// 3 for null data type
			this[name] = null;
		} else if (values[0] == 4) {
			// 4 for boolean data type
			this[name] = values[1] == 1 ? true : false;
		} else if (values[0] == 5) {
			// 5 for object data type
			this[name] = new JsObjectFromBMBytes(values[1], false);
		} else if (values[0] == 6) {
			// 6 for array data type
			this[name] = new JsArrayFromBMBytes(values[1], false);

		} else if (values[0] == 7) {
			// 7 for regex data type
			this[name] = new RegExp(getStringFromBytes(values[1]));
		} else if (values[0] == 8) {
			// 8 for function data type
			// for ie window.execScript
			if (window.execScript) {
				this[name] = window.execScript("("
						+ getStringFromBytes(values[1]) + ")");
			} else {
				this[name] = eval("(" + getStringFromBytes(values[1]) + ")");
			}
		} else if (values[0] == 9) {
			// 9 for byte array
			console.log('values[0] == 9 values[1]', values[1]);
			//this[name] = new Int8Array(values[1]);
			this[name] = new JsArrayFromBMBytes(values[1], false);
		} else if (values[0] == 10) {
			// 10 for Int8Array data type, i.e. row byte
			this[name] = new Int8Array(values[1]);
		}

	}

	return this;
};

var JsArrayFromBMBytes = function(wffBMBytes, outer) {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	var getStringFromBytes = function(utf8Bytes) {
		return decoder.decode(new Uint8Array(utf8Bytes));
	};

	var nameValues = wffBMUtil.parseWffBinaryMessageBytes(wffBMBytes);

	var i = 0;
	if (typeof outer === 'undefined' || outer) {
		i = 1;
	}

	var nameValue = nameValues[i];

	var dataType = nameValue.name[0];
	var values = nameValue.values;

	console.log('dataType', dataType);

	var jsArray = [];

	if (dataType == 0) {
		// 0 for string data type

		for (var j = 0; j < values.length; j++) {
			console.log('values[j]'+j, values[j]);
			jsArray.push(getStringFromBytes(values[j]));
		}

	} else if (dataType == 1) {
		// 1 for number data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(wffBMUtil.getDoubleFromOptimizedBytes(values[j]));
		}
	} else if (dataType == 2) {
		// 2 for undefined data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(undefined);
		}
	} else if (dataType == 3) {
		// 3 for null data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(null);
		}
	} else if (dataType == 4) {
		// 4 for boolean data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(values[j] == 1 ? true : false);
		}
	} else if (dataType == 5) {
		// 5 for object data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(new JsObjectFromBMBytes(values[j], false));
		}
	} else if (dataType == 6) {
		for (var j = 0; j < values.length; j++) {
			jsArray.push(new JsArrayFromBMBytes(values[j], false));
		}
	} else if (dataType == 7) {
		// 7 for regex data type
		for (var j = 0; j < values.length; j++) {
			jsArray.push(new RegExp(getStringFromBytes(values[j])));
		}
	} else if (dataType == 8) {
		// 8 for function data type
		for (var j = 0; j < values.length; j++) {
			var fun = getStringFromBytes(values[j]);

			var ary;
			if (window.execScript) {
				ary = [ window.execScript("(" + fun + ")") ];
			} else {
				ary = [ eval("(" + fun + ")") ];
			}
			jsArray.push(ary[0]);
		}
	} else if (dataType == 9) {
		// 9 for Int8Array data type
		if (values.length == 1) {
			jsArray = new JsArrayFromBMBytes(values[0], false);
		} else {
			//it will not invoke, only for future extension
			for (var j = 0; j < values.length; j++) {
//				jsArray.push(new Int8Array(values[j]));
				jsArray.push(new JsArrayFromBMBytes(values[j], false));
			}
		}
	}  else if (dataType == 10) {
		// 10 for Int8Array data type, i.e. row bytes
		if (values.length == 1) {
			jsArray = new Int8Array(values[0]);
		} else {
			//it will not invoke, only for future extension
			for (var j = 0; j < values.length; j++) {
				jsArray.push(new Int8Array(values[j]));
			}
		}
	}

	return jsArray;

};
