function TypeFamily(name) {
    this.name = name;
    return this;
};

TypeFamily.prototype.toTranspiled = function() {
    return "new TypeFamily('" + this.name + "')";
};

// non storable
TypeFamily.BOOLEAN = new TypeFamily("BOOLEAN");
TypeFamily.CHARACTER = new TypeFamily("CHARACTER");
TypeFamily.INTEGER = new TypeFamily("INTEGER");
TypeFamily.DECIMAL = new TypeFamily("DECIMAL");
TypeFamily.TEXT = new TypeFamily("TEXT");
TypeFamily.UUID = new TypeFamily("UUID");
TypeFamily.DATE = new TypeFamily("DATE");
TypeFamily.TIME = new TypeFamily("TIME");
TypeFamily.DATETIME = new TypeFamily("DATETIME");
TypeFamily.PERIOD = new TypeFamily("PERIOD");
TypeFamily.LIST = new TypeFamily("LIST");
TypeFamily.SET = new TypeFamily("SET");
TypeFamily.TUPLE = new TypeFamily("TUPLE");
TypeFamily.RANGE = new TypeFamily("RANGE");
TypeFamily.BLOB = new TypeFamily("BLOB");
TypeFamily.IMAGE = new TypeFamily("IMAGE");
TypeFamily.DOCUMENT = new TypeFamily("DOCUMENT");
TypeFamily.CATEGORY = new TypeFamily("CATEGORY");
TypeFamily.RESOURCE = new TypeFamily("RESOURCE");
TypeFamily.DICTIONARY = new TypeFamily("DICTIONARY");
TypeFamily.ENUMERATED = new TypeFamily("ENUMERATED");
// non storable
TypeFamily.VOID = new TypeFamily("VOID");
TypeFamily.NULL = new TypeFamily("NULL");
TypeFamily.ANY = new TypeFamily("ANY");
TypeFamily.METHOD = new TypeFamily("METHOD");
TypeFamily.CURSOR = new TypeFamily("CURSOR");
TypeFamily.ITERATOR = new TypeFamily("ITERATOR");
TypeFamily.CLASS = new TypeFamily("CLASS");
TypeFamily.TYPE = new TypeFamily("TYPE");
TypeFamily.CODE = new TypeFamily("CODE");
// volatile
TypeFamily.MISSING = new TypeFamily("MISSING");
