/*
 * these methods are for wffweb developers
 */
window.wffAsync = new function() {

	var encoder = wffGlobal.encoder;

	this.callbackFunctions = {};
	var uuid = 0;
	this.generateUUID = function() {
		return (++uuid).toString();
	};

	this.serverMethod = function(methodName, jsObject) {

		console.log('methodName', methodName);

		this.invoke = function(callback) {

			console.log('callback', callback);

			var callbackFunId;

			if (typeof callback === "function") {

				callbackFunId = wffAsync.generateUUID();

				wffAsync.callbackFunctions[callbackFunId] = callback;

				console.log('callback', callback);

			} else if (typeof callback === "undefined") {
				// NOP
			} else {
				throw "invoke function takes function argument";
			}

			var taskNameValue = wffTaskUtil.getTaskNameValue(
					wffGlobal.taskValues.TASK,
					wffGlobal.taskValues.INVOKE_CUSTOM_SERVER_METHOD);

			var methodNameBytes = encoder.encode(methodName);

			var values = [];

			if (typeof jsObject !== "undefined") {

				if (typeof jsObject === "object") {
					var argumentBMObject = new WffBMObject(jsObject);
					var argBytes = argumentBMObject.getBMBytes();
					values.push(argBytes);
				} else {
					throw "argument value should be an object";
				}
			}

			var nameValue = {
				'name' : methodNameBytes,
				'values' : values
			};

			var nameValues = [ taskNameValue, nameValue ];

			if (typeof callbackFunId !== "undefined") {
				var nameValueCallbackFun = {
					'name' : encoder.encode(callbackFunId),
					'values' : []
				};
				nameValues.push(nameValueCallbackFun);
			}

			var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);

			wffWS.send(wffBM);

		};

		return this;
	};

};

// sample usage
//
// wffAsync.serverMethod('methodName', {'key1':'hi'}).invoke(function(obj) {
// console.log('yes success2', obj);
// });
//
