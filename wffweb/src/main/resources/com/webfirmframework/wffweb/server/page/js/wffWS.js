
var wffWS = new function() {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	var webSocket;

	this.openSocket = function(wsUrl) {

		// Ensures only one connection is open at a time
		if (typeof webSocket !== 'undefined'
				&& webSocket.readyState !== WebSocket.CLOSED 
				&& webSocket.readyState !== WebSocket.CLOSING) {
			console.log("WebSocket is already opened.");
			return;
		}

		// Create a new instance of websocket
		webSocket = new WebSocket(wsUrl);

		// this is required to send binary data
		webSocket.binaryType = 'arraybuffer';

		webSocket.onopen = function(event) {
			try {
				wffBMClientEvents.wffRemovePrevBPInstance();

				if (typeof event.data === 'undefined') {
					return;
				}

				var binary = new Int8Array(event.data);
				
				if (binary.length < 4) {
					//invalid wff bm message so not to process
					return;
				}
				
				wffClientCRUDUtil.invokeTasks(binary);
			}catch(e){
				wffLog(e);
			}
		};

		webSocket.onmessage = function(event) {
			try {
				var binary = new Int8Array(event.data);
				console.log(binary);
				
				if (binary.length < 4) {
					//invalid wff bm message so not to process
					return;
				}

				// for (var i = 0; i < binary.length; i++) {
				// console.log(i, binary[i]);
				// }

				var executed = wffClientMethods.exePostFun(binary);

				if (!executed) {
					wffClientCRUDUtil.invokeTasks(binary);
				}

				// var wffMessage = wffBMUtil.parseWffBinaryMessageBytes(binary);
				// console.log('wffMessage', wffMessage);
				//
				// for (var i = 0; i < wffMessage.length; i++) {
				// var decodedString = decoder.decode(new Uint8Array(
				// wffMessage[i].name));
				// console.log('decodedString', decodedString,
				// 'wffMessage[i].values.length', wffMessage[i].values.length);
				// console.log('values');
				// for (var j = 0; j < wffMessage[i].values.length; j++) {
				// console.log('value', decoder.decode(new Uint8Array(
				// wffMessage[i].values[j])));
				// }
				// }

				// var string = new TextDecoder("utf-8").decode(binary);
				// console.log(string, string);

				// var binary = new Uint8Array(event.data.‌​length);
				// for (var i = 0; i < event.data.length; i++) {
				// console.log(i, event.data[i]);
				// // binary[i] = event.data[i];
				// }
				// console.dir(binary);
				// sendBinary(binary.bu‌​ffer);

				// console.log('going to writeResponse');
				// writeResponse(event.data);

				// var uint8array = new TextEncoder("utf-8").encode("¢");
				// var string = new TextDecoder("utf-8").decode(uint8array);
			}catch(e){
				wffLog(e);
			}
		};

		webSocket.onclose = function(event) {
			console.log("onclose", event);
			setTimeout(function() {
				if (typeof webSocket === 'undefined' || webSocket.readyState == 3) {
					console.log("2 seconds loop");
					wffWS.openSocket(wffGlobal.WS_URL);
				}
			}, wffGlobal.WS_RECON);
		};
	};

	/**
	 * Sends the bytes to the server
	 */
	this.send = function(bytes) {
		webSocket.send(new Int8Array(bytes));
	};

	this.closeSocket = function() {
		try {
			if (typeof webSocket !== 'undefined' 
				&& webSocket.readyState !== WebSocket.CONNECTING
				&& webSocket.readyState !== WebSocket.CLOSED) {
				webSocket.close();
			}
		} catch(e){}
	};

};
