function stringToUtf8Buffer(s) {
    var codes = s.split('')
        .map(function (c) {
            return c.charCodeAt(0);
        });
    var size = codes.map(getUtf8CharLength)
        .reduce(function(prev, cur) {
            return prev + cur;
        }, 0);
    var buffer = new ArrayBuffer(size);
    var view = new Uint8Array(buffer);
    var idx = 0;
    codes.forEach(function(c) {
        if (c < 0x80 /* 128 */) {
            /* one byte */
            view[idx++] = c;
        } else if (c < 0x800 /* 2048 */) {
            /* two bytes */
            view[idx++] = 0xc0 /* 192 */ + (c >>> 6);
            view[idx++] = 0x80 /* 128 */ + (c & 0x3f /* 63 */);
        } else if (c < 0x10000 /* 65536 */) {
            /* three bytes */
            view[idx++] = 0xe0 /* 224 */ + (c >>> 12);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 6) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + (c & 0x3f /* 63 */);
        } else if (c < 0x200000 /* 2097152 */) {
            /* four bytes */
            view[idx++] = 0xf0 /* 240 */ + (c >>> 18);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 12) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 6) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + (c & 0x3f /* 63 */);
        } else if (c < 0x4000000 /* 67108864 */) {
            /* five bytes */
            view[idx++] = 0xf8 /* 248 */ + (c >>> 24);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 18) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 12) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 6) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + (c & 0x3f /* 63 */);
        } else /* if (c <= 0x7fffffff) */ { /* 2147483647 */
            /* six bytes */
            view[idx++] = 0xfc /* 252 */ + /* (c >>> 30) may be not safe in ECMAScript! So...: */ (c / 1073741824);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 24) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 18) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 12) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + ((c >>> 6) & 0x3f /* 63 */);
            view[idx++] = 0x80 /* 128 */ + (c & 0x3f /* 63 */);
        }
    });
    return buffer;
}