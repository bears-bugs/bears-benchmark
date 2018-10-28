function utf8BufferToString(buffer) {
    if(buffer instanceof ArrayBuffer)
        buffer = new Uint8Array(buffer);
    var chars = [];
    var idx = 0;
    while(idx<buffer.length) {
        var byte = buffer[idx];
        var code = 0;
        if (byte > 251 && byte < 254 && idx + 5 < buffer.length) {
            /* (byte - 252 << 30) may be not safe in ECMAScript! So...: */
            /* six bytes */
            code = (byte - 252) * 1073741824 + (buffer[idx + 1] - 128 << 24) + (buffer[idx + 2] - 128 << 18)
            + (buffer[idx + 3] - 128 << 12) + (buffer[idx + 4] - 128 << 6) + buffer[idx + 5] - 128;
            idx += 6;
        } else if (byte > 247 && byte < 252 && idx + 4 < buffer.length) {
            /* five bytes */
            code = (byte - 248 << 24) + (buffer[idx + 1] - 128 << 18) + (buffer[idx + 2] - 128 << 12)
            + (buffer[idx + 3] - 128 << 6) + buffer[idx + 4] - 128;
            idx += 5;
        } else if (byte > 239 && byte < 248 && idx + 3 < buffer.length) {
            /* four bytes */
            code = (byte - 240 << 18) + (buffer[idx + 1] - 128 << 12) + (buffer[idx + 2] - 128 << 6)
            + buffer[idx + 3] - 128;
            idx += 4;
        } else if (byte > 223 && byte < 240 && idx + 2 < buffer.length) {
            /* three bytes */
            code = (byte - 224 << 12) + (buffer[idx + 1] - 128 << 6) + buffer[idx + 2] - 128;
            idx += 3;
        } else if (byte > 191 && byte < 224 && idx + 1 < buffer.length) {
            /* two bytes */
            code = (byte - 192 << 6) + buffer[idx + 1] - 128;
            idx += 2;
        } else {
            /* one byte */
            code = byte;
            idx += 1;
        }
        chars.push(String.fromCharCode(code));
    }
    return chars.join("");
};