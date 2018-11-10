
var wffBMClientEvents = new function() {

	window.wffRemovePrevBPInstanceInvoked = false;
	
	
	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;
	
	var wffRemoveBPInstance = function(wffInstanceId) {
		var nameValues = [];

		var wffInstanceIdBytes = encoder.encode(wffInstanceId);

		//taskNameValue
		var tnv = wffTaskUtil.getTaskNameValue(
				wffGlobal.taskValues.TASK,
				wffGlobal.taskValues.REMOVE_BROWSER_PAGE);

		nameValues.push(tnv);

		var nameValue = {
			'name' : wffInstanceIdBytes,
			'values' : []
		};

		nameValues.push(nameValue);

		var wffBM = wffBMUtil.getWffBinaryMessageBytes(nameValues);

		wffWS.send(wffBM);
	};
	
	this.wffRemoveBPInstance = wffRemoveBPInstance;

	this.wffRemovePrevBPInstance = function() {
		
		console.log('wffRemovePrevBPInstance ');
		console.log('wffGlobal.REMOVE_PREV_BP_ON_INITTAB ' + wffGlobal.REMOVE_PREV_BP_ON_INITTAB);
		console.log('window.wffRemovePrevBPInstanceInvoked ' + window.wffRemovePrevBPInstanceInvoked);
		
		if (!wffGlobal.REMOVE_PREV_BP_ON_INITTAB || 
				window.wffRemovePrevBPInstanceInvoked) {
			return;
		}
		
		window.wffRemovePrevBPInstanceInvoked = true;
		console.log('sessionStorage WFF_INSTANCE_ID ' + sessionStorage.getItem('WFF_INSTANCE_ID'));
		

		var wffInstanceId = sessionStorage.getItem('WFF_INSTANCE_ID');
		
		if (typeof wffInstanceId !== "undefined"
				&& wffInstanceId !== wffGlobal.INSTANCE_ID) {
			
			console.log('wffInstanceId != INSTANCE_ID ');

			wffRemoveBPInstance(wffInstanceId);

		}
		
		sessionStorage.setItem('WFF_INSTANCE_ID', wffGlobal.INSTANCE_ID);
	};
	
	

};