/*
 * this file must be loaded first.
 */
window.wffGlobal = new function() {

	var wffId = -1;

	this.getUniqueWffIntId = function() {
		return ++wffId;
	};

	this.taskValues = "${TASK_VALUES}";
	this.WS_URL = "${WS_URL}";
	this.INSTANCE_ID = "${INSTANCE_ID}";
	this.REMOVE_PREV_BP_ON_INITTAB = "${REMOVE_PREV_BP_ON_INITTAB}";
	this.REMOVE_PREV_BP_ON_TABCLOSE = "${REMOVE_PREV_BP_ON_TABCLOSE}";
	//reconnect time interval for WebSocket
	this.WS_RECON = "${WS_RECON}";

	if ((typeof TextEncoder) === "undefined") {

		this.encoder = new function TextEncoder(charset) {

			if (charset === "utf-8") {

				this.encode = function(text) {

					var bytes = [];

					for (var i = 0; i < text.length; i++) {
						var charCode = text.charCodeAt(i);
						if (charCode < 0x80) {
							bytes.push(charCode);
						} else if (charCode < 0x800) {
							bytes.push(0xc0 | (charCode >> 6),
									0x80 | (charCode & 0x3f));
						} else if (charCode < 0xd800 || charCode >= 0xe000) {
							bytes.push(0xe0 | (charCode >> 12),
									0x80 | ((charCode >> 6) & 0x3f),
									0x80 | (charCode & 0x3f));
						} else {
							// surrogate pair
							i++;
							charCode = 0x10000 + (((charCode & 0x3ff) << 10) | (text
									.charCodeAt(i) & 0x3ff));
							bytes.push(0xf0 | (charCode >> 18),
									0x80 | ((charCode >> 12) & 0x3f),
									0x80 | ((charCode >> 6) & 0x3f),
									0x80 | (charCode & 0x3f));
						}
					}
					return bytes;
				};
			}

		}("utf-8");

	} else {
		this.encoder = new TextEncoder("utf-8");
	}

	if ((typeof TextDecoder) === "undefined") {

		this.decoder = new function TextDecoder(charset) {

			if (charset === "utf-8") {

				this.decode = function(bytes) {

					var text = '', i;

					for (i = 0; i < bytes.length; i++) {
						var value = bytes[i];

						if (value < 0x80) {
							text += String.fromCharCode(value);
						} else if (value > 0xBF && value < 0xE0) {
							text += String.fromCharCode((value & 0x1F) << 6
									| bytes[i + 1] & 0x3F);
							i += 1;
						} else if (value > 0xDF && value < 0xF0) {
							text += String.fromCharCode((value & 0x0F) << 12
									| (bytes[i + 1] & 0x3F) << 6 | bytes[i + 2]
									& 0x3F);
							i += 2;
						} else {
							// surrogate pair
							var charCode = ((value & 0x07) << 18
									| (bytes[i + 1] & 0x3F) << 12
									| (bytes[i + 2] & 0x3F) << 6 | bytes[i + 3] & 0x3F) - 0x010000;

							text += String.fromCharCode(
									charCode >> 10 | 0xD800,
									charCode & 0x03FF | 0xDC00);
							i += 3;
						}
					}

					return text;
				};
			}

		}("utf-8");

	} else {
		this.decoder = new TextDecoder("utf-8");
	}

};
