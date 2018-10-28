function Blob(data) {
    this.data = data;
    return this;
}

Blob.ofValue = function(value) {
    var binaries = {};
    // create json type-aware object graph and collect binaries
    var values = {}; // need a temporary parent
    value.toJson(values, null, "value", true, binaries);
    var json = JSON.stringify(values["value"]);
    // add it
    binaries["value.json"] = stringToUtf8Buffer(json);
    // zip binaries
    var zipped = Blob.zipDatas(binaries)
    // done
    return new Blob(zipped);
};

Blob.zipDatas = function(datas) {
    var JSZip = require("jszip-sync");
    var zip = new JSZip();
    return zip.sync(function() {
        for (var key in datas)
            zip.file(key, datas[key]);
        var result = null;
        zip.generateAsync({type: "arraybuffer", compression: "DEFLATE"}).
        then(function(value) {
            result = value;
        });
        return result;
    });
};


Blob.readParts = function(data) {
    var JSZip = require("jszip-sync");
    var zip = new JSZip();
    return zip.sync(function() {
        var parts = {};
        zip.loadAsync(data);
        zip.forEach(function (entry) {
            zip.file(entry)
                .async("arraybuffer")
                .then(function(value) {
                    parts[entry] = value;
                });
        });
        return parts;
    });
};

Blob.readValue = function(parts) {
    var data = parts["value.json"] || null;
    if (data == null)
        throw new Error("Expecting a 'value.json' part!");
    var json = utf8BufferToString(data);
    return JSON.parse(json);
};



Blob.prototype.toDocument = function() {
    var parts = Blob.readParts(this.data);
    var value = Blob.readValue(parts);
    var typeName = value["type"] || null;
    if (typeName == null)
        throw new Error("Expecting a 'type' field!");
    var type = eval(typeName)
    if (type != Document)
        throw new Error("Expecting a Document type!");
    value = value["value"] || null;
    if (value == null)
        throw new Error("Expecting a 'value' field!");
    var instance = new type();
    instance.fromJson(value, parts);
    return instance;
};
