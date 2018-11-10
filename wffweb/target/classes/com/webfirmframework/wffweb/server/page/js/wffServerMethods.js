var wffTaskUtil = new function () {
	
	var encoder = wffGlobal.encoder;
	
	this.getTaskNameValue = function(nameByte, valueByte) {
//		var tUtf8Bytes = encoder.encode(name);
//		var cUtf8Bytes = encoder.encode(value);
		var taskNameValue = {
			'name' : [nameByte],
			'values' : [ [valueByte] ]
		};
		return taskNameValue;
	};
};

var wffServerMethods = new function () {

	var encoder = wffGlobal.encoder;
	
	this.invokeAsync = function(event, tag, attr) {
		console.log('invokeAsync tag', tag);
		var taskNameValue = wffTaskUtil.getTaskNameValue(wffGlobal.taskValues.TASK, wffGlobal.taskValues.INVOKE_ASYNC_METHOD);

		
		var attrBytes = encoder.encode(attr);
		
		var nameValue = {'name':wffTagUtil.getWffIdBytesFromTag(tag), 'values':[attrBytes]};
		var nameValues = [taskNameValue, nameValue];
		var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);
		wffWS.send(wffBM);
	};	
	//never ever rename ia
	this.ia = this.invokeAsync ;
	
	this.invokeAsyncWithPreFun = function(event, tag, attr, preFun) {
		console.log('invokeAsync tag', tag);
		
		if (preFun(event, tag)) {
			var taskNameValue = wffTaskUtil.getTaskNameValue(wffGlobal.taskValues.TASK, wffGlobal.taskValues.INVOKE_ASYNC_METHOD);

			
			var attrBytes = encoder.encode(attr);
			
			var nameValue = {'name':wffTagUtil.getWffIdBytesFromTag(tag), 'values':[attrBytes]};
			var nameValues = [taskNameValue, nameValue];
			var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);
			wffWS.send(wffBM);
		}
		
	};
	//never ever rename iawpf
	this.iawpf = this.invokeAsyncWithPreFun;
	
	this.invokeAsyncWithPreFilterFun = function(event, tag, attr, preFun, filterFun) {
		console.log('invokeAsyncWithPreFilterFun tag', tag);
		
		if (preFun(event, tag)) {
			var taskNameValue = wffTaskUtil.getTaskNameValue(wffGlobal.taskValues.TASK, wffGlobal.taskValues.INVOKE_ASYNC_METHOD);

			
			var attrBytes = encoder.encode(attr);
			
			var jsObject = filterFun(event, tag);
			var argumentBMObject = new WffBMObject(jsObject);
			var argBytes = argumentBMObject.getBMBytes();
			
			var nameValue = {'name':wffTagUtil.getWffIdBytesFromTag(tag), 'values':[attrBytes, argBytes]};
			var nameValues = [taskNameValue, nameValue];
			var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);
			
			
			
			wffWS.send(wffBM);
		}
		
	};	
	//never ever rename iawpff
	this.iawpff = this.invokeAsyncWithPreFilterFun;
	
	this.invokeAsyncWithFilterFun = function(event, tag, attr, filterFun) {
		console.log('invokeAsyncWithFilterFun tag', tag);
		
		var taskNameValue = wffTaskUtil.getTaskNameValue(wffGlobal.taskValues.TASK, wffGlobal.taskValues.INVOKE_ASYNC_METHOD);

		
		var attrBytes = encoder.encode(attr);
		
		var jsObject = filterFun(event, tag);
		var argumentBMObject = new WffBMObject(jsObject);
		var argBytes = argumentBMObject.getBMBytes();
		
		var nameValue = {'name':wffTagUtil.getWffIdBytesFromTag(tag), 'values':[attrBytes, argBytes]};
		var nameValues = [taskNameValue, nameValue];
		var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);
		
		
		
		wffWS.send(wffBM);
		
	};
	//never ever rename iawff
	this.iawff = this.invokeAsyncWithFilterFun;
	
	//TODO
//	this.invokeAsyncWithPreFilterPostFun = function(tag, attr, preFun, filterFun, postFun) {
//		console.log('invokeAsyncWithPreFilterFun tag', tag);
//		
//		if (preFun(tag)) {
//			var taskNameValue = wffTaskUtil.getTaskNameValue(wffGlobal.taskValues.TASK, wffGlobal.taskValues.INVOKE_ASYNC_METHOD);
//
//			
//			var attrBytes = encoder.encode(attr);
//			
//			var argumentBMObject = filterFun(tag);
//			
//			var argBytes = argumentBMObject.getBMBytes();
//			
//			var nameValue = {'name':wffTagUtil.getWffIdBytesFromTag(tag), 'values':[attrBytes, argBytes]};
//			var nameValues = [taskNameValue, nameValue];
//			var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);
//			
//			
//			
//			wffWS.send(wffBM);
//		}
//		
//	};

	
};
//never ever rename ia
var wffSM = wffServerMethods;

