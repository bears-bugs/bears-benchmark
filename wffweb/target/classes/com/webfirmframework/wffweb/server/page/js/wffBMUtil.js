/**
 * this is wff binary message version 1 implementation
 */

var wffBMUtil = new function() {

	/**
	 * gets the wff binary message bytes from the given array of name-values
	 * objects
	 * 
	 * @param nameValues
	 *            array of name-values, name-values is an object in the
	 *            structure of {name:[], values:[[]]}, both name and values
	 *            should contain bytes
	 * @returns {Array}
	 */
	this.getWffBinaryMessageBytes = function(nameValues) {

		var maxNoOfNameBytes = 0;
		var maxNoOfValuesBytes = 0;

		for (var i = 0; i < nameValues.length; i++) {
			var name = nameValues[i].name;
			var values = nameValues[i].values;

			if (name.length > maxNoOfNameBytes) {
				maxNoOfNameBytes = name.length;
			}

			var maxValuesBytesLength = 0;

			for (var j = 0; j < values.length; j++) {
				maxValuesBytesLength += values[j].length;
			}

			var maxBytesLengthFromTotalBytes = getLengthOfOptimizedBytesFromInt(maxValuesBytesLength);

			var maxBytesLengthForAllValues = values.length
					* maxBytesLengthFromTotalBytes;

			var totalNoOfBytesForAllValues = maxValuesBytesLength
					+ maxBytesLengthForAllValues;

			if (totalNoOfBytesForAllValues > maxNoOfValuesBytes) {
				maxNoOfValuesBytes = totalNoOfBytesForAllValues;
			}

		}

		var maxNoNameLengthBytes = getLengthOfOptimizedBytesFromInt(maxNoOfNameBytes);

		var maxNoValueLengthBytes = getLengthOfOptimizedBytesFromInt(maxNoOfValuesBytes);

		var wffBinaryMessageBytes = [];
		wffBinaryMessageBytes.push(maxNoNameLengthBytes);
		wffBinaryMessageBytes.push(maxNoValueLengthBytes);

		for (var i = 0; i < nameValues.length; i++) {
			var name = nameValues[i].name;

			var nameLegthBytes = getLastBytesFromInt(name.length,
					maxNoNameLengthBytes);

			concatArrayValues(wffBinaryMessageBytes, nameLegthBytes);

			concatArrayValues(wffBinaryMessageBytes, name);

			var values = nameValues[i].values;

			if (values.length == 0) {
				concatArrayValues(wffBinaryMessageBytes, getLastBytesFromInt(0,
						maxNoValueLengthBytes));
			} else {

				var valueLegth = 0;
				for (var l = 0; l < values.length; l++) {
					valueLegth += values[l].length;
				}

				valueLegth += (maxNoValueLengthBytes * values.length);

				var valueLegthBytes = getLastBytesFromInt(valueLegth,
						maxNoValueLengthBytes);

				concatArrayValues(wffBinaryMessageBytes, valueLegthBytes);

				for (var m = 0; m < values.length; m++) {
					var value = values[m];

					valueLegthBytes = getLastBytesFromInt(value.length,
							maxNoValueLengthBytes);

					concatArrayValues(wffBinaryMessageBytes, valueLegthBytes);

					concatArrayValues(wffBinaryMessageBytes, value);
				}
			}

		}
		return wffBinaryMessageBytes;
	};

	this.parseWffBinaryMessageBytes = function(message) {
		var nameValues = [];

		var nameLengthBytesLength = message[0];
		var valueLengthBytesLength = message[1];

		for (var messageIndex = 2; messageIndex < message.length; messageIndex++) {

			var nameValue = {};

			var nameLengthBytes = [];
			concatArrayValuesFromPosition(nameLengthBytes, message,
					messageIndex, nameLengthBytesLength);

			messageIndex = messageIndex + nameLengthBytesLength;

			var fromByteArray = getIntFromOptimizedBytes(nameLengthBytes);
			var nameBytes = [];

			concatArrayValuesFromPosition(nameBytes, message, messageIndex,
					fromByteArray);

			messageIndex = messageIndex + nameBytes.length;

			nameValue.name = nameBytes;

			var valueLengthBytes = [];
			concatArrayValuesFromPosition(valueLengthBytes, message,
					messageIndex, valueLengthBytesLength);

			messageIndex = messageIndex + valueLengthBytesLength;
			fromByteArray = getIntFromOptimizedBytes(valueLengthBytes);

			var valueBytes = [];

			concatArrayValuesFromPosition(valueBytes, message, messageIndex,
					fromByteArray);

			messageIndex = messageIndex + valueBytes.length - 1;

			// process array values
			var extractEachValueBytes = extractValuesFromValueBytes(
					valueLengthBytesLength, valueBytes);
			nameValue.values = extractEachValueBytes;

			nameValues.push(nameValue);
		}

		return nameValues;
	};

	/**
	 * extracts each value to an array, each value is an array of bytes.
	 * 
	 * @param valueLengthBytesLength
	 *            (Usually it is 4, whenever 32 bit integer is used)
	 * @param valueBytes
	 * @returns {Array} of {Array}
	 */
	var extractValuesFromValueBytes = function(valueLengthBytesLength, valueBytes) {

		var values = [];

		for (var i = 0; i < valueBytes.length; i++) {
			var valueLengthBytes = [];

			concatArrayValuesFromPosition(valueLengthBytes, valueBytes, i,
					valueLengthBytesLength);

			var valueLength = getIntFromOptimizedBytes(valueLengthBytes);

			var value = [];

			concatArrayValuesFromPosition(value, valueBytes, i
					+ valueLengthBytesLength, valueLength);

			values.push(value);
			i = i + valueLengthBytesLength + valueLength - 1;

		}
		return values;
	};

	/**
	 * var array1 = [1]; var array2 = [2]; array1.concat(array2); will not work,
	 * console.log(array1); will print [1]
	 */
	var concatArrayValues = function(appendToArray, valuesToAppend) {
		for (var a = 0; a < valuesToAppend.length; a++) {
			appendToArray.push(valuesToAppend[a]);
		}
	};

	/**
	 * 
	 * @param appendToArray
	 *            to which the given values (from position to length) will be
	 *            appended
	 * @param valuesFrom
	 *            values (an array) to append
	 * @param position
	 *            from which the values to be copied
	 * @param length
	 *            upto the given length the values will be appended
	 */
	var concatArrayValuesFromPosition = function(appendToArray, valuesFrom,
			position, length) {
		var uptoIndex = position + length;
		for (var a = position; a < uptoIndex; a++) {
			appendToArray.push(valuesFrom[a]);
		}
	};

	/**
	 * @param bytes
	 *            the bytes to convert to integer value
	 * @returns the integer value from the given bytes
	 */
	var getIntFromBytes = function(bytes) {
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
				| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	};

	/**
	 * @param value
	 * @returns {Array} the bytes for the given integer value
	 */
	var getBytesFromInt = function(value) {
		var bytes = [ (value >> 24), (value >> 16), (value >> 8), value ];
		return bytes;
	};

	/**
	 * @param bytes
	 *            the optimized bytes from which the integer value will be
	 *            obtained
	 * @return the integer value from the given bytes
	 */
	var getIntFromOptimizedBytes = function(bytes) {
		if (bytes.length == 4) {
			return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
					| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
		} else if (bytes.length == 3) {
			return (bytes[0] & 0xFF) << 16 | (bytes[1] & 0xFF) << 8
					| (bytes[2] & 0xFF);
		} else if (bytes.length == 2) {
			return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
		} else if (bytes.length == 1) {
			return (bytes[0] & 0xFF);
		}
		return bytes[0] << 24 | (bytes[1] & 0xFF) << 16
				| (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	};

	this.getIntFromOptimizedBytes = getIntFromOptimizedBytes;

	/**
	 * @param value
	 *            the integer value to be converted to optimized bytes.
	 *            Optimized bytes means the minimum bytes required to represent
	 *            the given integer value.
	 * @return the bytes for the corresponding integer given.
	 */
	var getOptimizedBytesFromInt = function(value) {

		var zerothIndex = (value >> 24);
		var firstIndex = (value >> 16);
		var secondIndex = (value >> 8);
		var thirdIndex = value;

		if (zerothIndex == 0) {

			if (firstIndex == 0) {

				if (secondIndex == 0) {

					return [ thirdIndex ];
				}

				return [ secondIndex, thirdIndex ];
			}

			return [ firstIndex, secondIndex, thirdIndex ];
		}

		return [ zerothIndex, firstIndex, secondIndex, thirdIndex ];
	};

	this.getOptimizedBytesFromInt = getOptimizedBytesFromInt;

	/**
	 * @param value
	 *            the integer value to be converted to optimized bytes.
	 *            Optimized bytes means the minimum bytes required to represent
	 *            the given integer value.
	 * @return the array length of the bytes for the corresponding integer
	 *         given.
	 * 
	 */
	var getLengthOfOptimizedBytesFromInt = function(value) {

		var zerothIndex = (value >> 24);
		var firstIndex = (value >> 16);
		var secondIndex = (value >> 8);
		var thirdIndex = value;

		if (zerothIndex == 0) {
			if (firstIndex == 0) {
				if (secondIndex == 0) {
					return 1;
				}
				return 2;
			}
			return 3;
		}

		return 4;
	};

	/**
	 * @param value
	 *            the integer value to be converted to optimized bytes.
	 *            Optimized bytes means the minimum bytes required to represent
	 *            the given integer value.
	 * @param lastNoOfBytes
	 *            the last no of bytes to be returned. Expected inputs are 1, 2,
	 *            3 or 4.
	 * @return the bytes for the corresponding integer given.
	 */
	var getLastBytesFromInt = function(value, lastNoOfBytes) {

		var zerothIndex = (value >> 24);
		var firstIndex = (value >> 16);
		var secondIndex = (value >> 8);
		var thirdIndex = value;

		if (lastNoOfBytes == 1) {
			return [ thirdIndex ];
		} else if (lastNoOfBytes == 2) {
			return [ secondIndex, thirdIndex ];
		} else if (lastNoOfBytes == 3) {
			return [ firstIndex, secondIndex, thirdIndex ];
		}

		return [ zerothIndex, firstIndex, secondIndex, thirdIndex ];
	};

	/**
	 * 
	 * @param doubleValue
	 *            number type value. JavaScript Numbers are Always 64-bit
	 *            Floating Point.
	 * @returns {Int8Array}
	 */
	var getBytesFromDouble = function(doubleValue) {
		var arrayBuff = new ArrayBuffer(8);
		var float64 = new Float64Array(arrayBuff);
		float64[0] = doubleValue;
		var uin = new Int8Array(arrayBuff);
		
		//uin.reverse is not supported in IE
		return Array.from(uin).reverse();
	};

	this.getBytesFromDouble = getBytesFromDouble;

	/**
	 * 
	 * @param bytes
	 *            the bytes for double value. i.e. the bytes of long IEEE 754
	 *            standard value.
	 * @returns the number
	 */
	var getDoubleFromOptimizedBytes = function(bytes) {
		var buffer = new ArrayBuffer(8);
		var int8Array = new Int8Array(buffer);

		for (var i = 0; i < bytes.length; i++) {
			// int8Array.reverse(); is not supported in IE so initialized in reverse order
			int8Array[i] = bytes[bytes.length - 1 - i];
		}


		return new Float64Array(buffer)[0];
	};

	this.getDoubleFromOptimizedBytes = getDoubleFromOptimizedBytes;

};
