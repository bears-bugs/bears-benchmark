function multiplyArray(items, count) {
    var result = [];
    while(--count>=0) {
        result = result.concat(items);
    }
    return result;
}
