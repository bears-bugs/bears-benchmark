/**
 * 
 */

var wffClientCRUDUtil = new function() {

	var encoder = wffGlobal.encoder;
	var decoder = wffGlobal.decoder;

	var getStringFromBytes = function(utf8Bytes) {
		return decoder.decode(new Uint8Array(utf8Bytes));
	};

	var invokeTask = function(nameValues) {
		
		var taskNameValue = nameValues[0];
		// var taskName = getStringFromBytes(taskNameValue.name);
		var taskValue = taskNameValue.values[0][0];
		console.log('taskNameValue.name', taskNameValue.name);
		
		if (taskValue == wffGlobal.taskValues.ATTRIBUTE_UPDATED) {
			console.log('taskValue == "ATTRIBUTE_UPDATED"');

			for (var i = 1; i < nameValues.length; i++) {

				console.log('i', i);
				console.log('nameValues[i].values', nameValues[i].values);

				var attrNameValue = getStringFromBytes(nameValues[i].name);
				console.log('attrNameValue', attrNameValue);
				var indexOfSeparator = attrNameValue.indexOf('=');

				var attrNameValueArry = wffTagUtil
						.splitAttrNameValue(attrNameValue);
				var attrName = attrNameValueArry[0];
				var attrValue = attrNameValueArry[1];

				// if (indexOfSeparator != -1) {
				// attrName = attrNameValue.substring(0, indexOfSeparator);
				// attrValue = attrNameValue.substring(indexOfSeparator + 1,
				// attrNameValue.length);
				// } else {
				// attrName = attrNameValue;
				// attrValue = '';
				// }

				console.log('attrName', attrName, 'attrValue', attrValue);

				// var tagId = wffBMUtil
				// .getIntFromOptimizedBytes(nameValues[i].name);
				var wffIds = nameValues[i].values;

				// var tagName = getStringFromBytes(wffIds[0]);

				for (var j = 0; j < wffIds.length; j++) {
					console.log('j', j);

					var wffId = wffTagUtil.getWffIdFromWffIdBytes(wffIds[j]);

					var applicableTag = wffTagUtil.getTagByWffId(wffId);

					if (indexOfSeparator != -1) {
						//value attribute doesn't work with setAttribute method
						//should be called before setAttribute method
						applicableTag[attrName] = attrValue;
						applicableTag.setAttribute(attrName, attrValue);
					} else {
						//value attribute doesn't work with setAttribute method
						//should be called before setAttribute method
						applicableTag[attrName] = "";
						applicableTag.setAttribute(attrName, "");
					}
				}

			}

		} else if (taskValue == wffGlobal.taskValues.REMOVED_ATTRIBUTE) {
			console.log('taskValue == "REMOVED_ATTRIBUTE"');

			for (var i = 1; i < nameValues.length; i++) {

				console.log('i', i);
				console.log('nameValues[i].values', nameValues[i].values);

				var attrName = getStringFromBytes(nameValues[i].name);

				console.log('attrName', attrName);

				// var tagId = wffBMUtil
				// .getIntFromOptimizedBytes(nameValues[i].name);
				var wffIds = nameValues[i].values;

				// var tagName = getStringFromBytes(wffIds[0]);

				for (var j = 0; j < wffIds.length; j++) {
					console.log('j', j);

					var wffId = wffTagUtil.getWffIdFromWffIdBytes(wffIds[j]);

					var applicableTag = wffTagUtil.getTagByWffId(wffId);

					applicableTag.removeAttribute(attrName);

				}

			}

		} else if (taskValue == wffGlobal.taskValues.APPENDED_CHILD_TAG
				|| taskValue == wffGlobal.taskValues.APPENDED_CHILDREN_TAGS) {

			for (var i = 1; i < nameValues.length; i++) {
				var wffId = wffTagUtil
						.getWffIdFromWffIdBytes(nameValues[i].name);
				var values = nameValues[i].values;
				var tagName = getStringFromBytes(values[0]);

				var parent = wffTagUtil.getTagByTagNameAndWffId(tagName, wffId);

				for (var j = 1; j < values.length; j++) {
					// var htmlString = getStringFromBytes(values[j]);

					// var div = document.createElement('div');
					// div.innerHTML = htmlString;
					// var htmlNodes = div.firstChild;

					var htmlNodes = wffTagUtil
							.createTagFromWffBMBytes(values[j]);

					parent.appendChild(htmlNodes);
				}
			}
		} else if (taskValue == wffGlobal.taskValues.REMOVED_TAGS) {

			console.log('wffGlobal.taskValues.REMOVED_TAGS nameValues.length '
					+ nameValues.length);

			for (var i = 1; i < nameValues.length; i++) {
				var wffId = wffTagUtil
						.getWffIdFromWffIdBytes(nameValues[i].name);
				var values = nameValues[i].values;
				var tagName = getStringFromBytes(values[0]);

				var tagToRemove = wffTagUtil.getTagByTagNameAndWffId(tagName,
						wffId);

				console.log('tagToRemove', tagToRemove, 'tagName', tagName,
						'wffId', wffId, 'count i', i);
				var parent = tagToRemove.parentNode;
				parent.removeChild(tagToRemove);

			}

		} else if (taskValue == wffGlobal.taskValues.REMOVED_ALL_CHILDREN_TAGS) {

			console.log('wffGlobal.taskValues.REMOVED_ALL_CHILDREN_TAGS');

			for (var i = 1; i < nameValues.length; i++) {
				var wffId = wffTagUtil
						.getWffIdFromWffIdBytes(nameValues[i].name);
				var values = nameValues[i].values;
				var tagName = getStringFromBytes(values[0]);

				var parentTag = wffTagUtil.getTagByTagNameAndWffId(tagName,
						wffId);
				while (parentTag.firstChild) {
					parentTag.removeChild(parentTag.firstChild);
				}
			}

		} else if (taskValue == wffGlobal.taskValues.MOVED_CHILDREN_TAGS) {

			console.log('wffGlobal.taskValues.MOVED_CHILDREN_TAGS nameValues.length '
							+ nameValues.length);

			for (var i = 1; i < nameValues.length; i++) {
				
				var currentParentWffId = wffTagUtil
						.getWffIdFromWffIdBytes(nameValues[i].name);
				var values = nameValues[i].values;				
				
				var currentParentTagName = getStringFromBytes(values[0]);

				var currentParentTag = wffTagUtil.getTagByTagNameAndWffId(
						currentParentTagName, currentParentWffId);
				
				var childTag = null;
				//if NoTag then length will be zero
				if (values[2].length == 0) {
					childTag = wffTagUtil.createTagFromWffBMBytes(values[3]);
				} else {
					var childTagName = getStringFromBytes(values[2]);
					var childWffId = wffTagUtil.getWffIdFromWffIdBytes(values[1]);
					
					childTag = wffTagUtil.getTagByTagNameAndWffId(childTagName,
							childWffId);
					if (typeof childTag !== 'undefined') {
						console.log('childTag !== undefined', childTag);
						var previousParent = childTag.parentNode;
						console.log('childTag.parentNode', previousParent);
						if (typeof previousParent !== 'undefined') {						
							previousParent.removeChild(childTag);
						}
					} else {
						console.log('childTag === undefined', childTag);
						childTag = wffTagUtil.createTagFromWffBMBytes(values[3]);
					}
				}				
				currentParentTag.appendChild(childTag);
			}

		} else if (taskValue == wffGlobal.taskValues.ADDED_ATTRIBUTES) {
			console.log('taskValue == "ADDED_ATTRIBUTES"');

			for (var i = 1; i < nameValues.length; i++) {

				var nameValue = nameValues[i];
				if (nameValue.name[0] == wffGlobal.taskValues.MANY_TO_ONE) {
					var tagName = getStringFromBytes(nameValue.values[0]);
					var wffId = wffTagUtil
							.getWffIdFromWffIdBytes(nameValue.values[1]);

					var applicableTag = wffTagUtil.getTagByTagNameAndWffId(
							tagName, wffId);

					for (var j = 2; j < nameValue.values.length; j++) {

						var attrNameValue = getStringFromBytes(nameValue.values[j]);

						var attrNameValueArry = wffTagUtil
								.splitAttrNameValue(attrNameValue);
						var attrName = attrNameValueArry[0];
						var attrValue = attrNameValueArry[1];
						//value attribute doesn't work with setAttribute method
						//should be called before setAttribute method
						applicableTag[attrName] = attrValue;
						applicableTag.setAttribute(attrName, attrValue);
					}
				}

			}

		} else if (taskValue == wffGlobal.taskValues.REMOVED_ATTRIBUTES) {
			console.log('taskValue == "REMOVED_ATTRIBUTES"');

			for (var i = 1; i < nameValues.length; i++) {

				var nameValue = nameValues[i];
				if (nameValue.name[0] == wffGlobal.taskValues.MANY_TO_ONE) {
					var tagName = getStringFromBytes(nameValue.values[0]);
					var wffId = wffTagUtil
							.getWffIdFromWffIdBytes(nameValue.values[1]);

					var applicableTag = wffTagUtil.getTagByTagNameAndWffId(
							tagName, wffId);

					for (var j = 2; j < nameValue.values.length; j++) {

						var attrName = getStringFromBytes(nameValue.values[j]);

						applicableTag.removeAttribute(attrName);
					}
				}

			}

		} else if (taskValue == wffGlobal.taskValues.ADDED_INNER_HTML) {

			console.log('wffGlobal.taskValues.ADDED_INNER_HTML');
			
			var tagName = getStringFromBytes(nameValues[1].name);
			
			var wffId = wffTagUtil
					.getWffIdFromWffIdBytes(nameValues[1].values[0]);
			
			var parentTag = wffTagUtil.getTagByTagNameAndWffId(tagName,
					wffId);
			
			// it should be case sensitive node.innerHTML
			// parentTag.innerHTML = innerHtml;
			// inner html will not work with table

			while (parentTag.firstChild) {
				parentTag.removeChild(parentTag.firstChild);
			}

			for (var i = 2; i < nameValues.length; i++) {
				
				var values = nameValues[i].values;

				var htmlNodes = wffTagUtil.createTagFromWffBMBytes(nameValues[i].name);
				
				//if length is 1 then there is an existing tag with this id
				if (values.length == 1 && values[0].length == 1) {
					console.log('values.length == 3');
					var existingTag = wffTagUtil.getTagByTagNameAndWffId(
							htmlNodes.nodeName, htmlNodes
									.getAttribute("data-wff-id"));
					if (existingTag) {
						var parentOfExistingTag = existingTag.parentNode;
						parentOfExistingTag.removeChild(existingTag);
					}
					
				}
				
				parentTag.appendChild(htmlNodes);
			}

		} else if (taskValue == wffGlobal.taskValues.INSERTED_BEFORE_TAG) {

			console.log('wffGlobal.taskValues.INSERTED_BEFORE_TAG');

			for (var i = 1; i < nameValues.length; i++) {
				var wffId = wffTagUtil
						.getWffIdFromWffIdBytes(nameValues[i].name);
				var values = nameValues[i].values;
				var tagName = getStringFromBytes(values[0]);
				// var innerHtml = getStringFromBytes(values[1]);

				// console.log('innerHtml', innerHtml);

				var parentTag = wffTagUtil.getTagByTagNameAndWffId(tagName,
						wffId);
				
				var beforeTagName = getStringFromBytes(values[2]);
				var beforeTagWffId = wffTagUtil.getWffIdFromWffIdBytes(values[3]);
				
				var beforeTag = wffTagUtil.getTagByTagNameAndWffId(beforeTagName,
						beforeTagWffId);
				
				
				var htmlNodes = wffTagUtil.createTagFromWffBMBytes(values[1]);
				
				//if length is 3 then there is an existing tag with this id
				if (values.length == 5) {
					console.log('values.length == 5');
					var existingTag = wffTagUtil.getTagByTagNameAndWffId(
							htmlNodes.nodeName, htmlNodes
									.getAttribute("data-wff-id"));
					var parentOfExistingTag = existingTag.parentNode;
					parentOfExistingTag.removeChild(existingTag);
					
				}
				
				parentTag.insertBefore(htmlNodes, beforeTag);
			}

		} else if (taskValue == wffGlobal.taskValues.RELOAD_BROWSER) {
			location.reload(true);
		} else if (taskValue == wffGlobal.taskValues.RELOAD_BROWSER_FROM_CACHE) {
			location.reload();
		} else if (taskValue == wffGlobal.taskValues.EXECURE_JS) {
			
			var js = getStringFromBytes(taskNameValue.values[1]);
			
			if (window.execScript) {
				window.execScript(js);
			} else {
				eval(js);
			}
			
		} else if (taskValue == wffGlobal.taskValues.COPY_INNER_TEXT_TO_VALUE) {
			
			console.log('wffGlobal.taskValues.COPY_INNER_TEXT_TO_VALUE');
			
			var tagName = getStringFromBytes(nameValues[1].name);
			
			var wffId = wffTagUtil
					.getWffIdFromWffIdBytes(nameValues[1].values[0]);
			
			var parentTag = wffTagUtil.getTagByTagNameAndWffId(tagName,
					wffId);
			
			var d = document.createElement('div');
			d.innerHTML = parentTag.outerHTML;
			parentTag.value = d.childNodes[0].innerText;
		} else if (taskValue == wffGlobal.taskValues.SET_BM_OBJ_ON_TAG
				|| taskValue == wffGlobal.taskValues.SET_BM_ARR_ON_TAG) {
			
			var tagName = getStringFromBytes(nameValues[1].name);
			
			var wffId = wffTagUtil
							.getWffIdFromWffIdBytes(nameValues[1].values[0]);
			
			var tag = wffTagUtil.getTagByTagNameAndWffId(tagName, wffId);
			
			var ky = getStringFromBytes(nameValues[1].values[1]);
			
			var bmObjOrArrBytes = nameValues[1].values[2];
			
			var jsObjOrArr;
			
			if (taskValue == wffGlobal.taskValues.SET_BM_OBJ_ON_TAG) {
				jsObjOrArr = new JsObjectFromBMBytes(bmObjOrArrBytes, true);
			} else {
				jsObjOrArr = new JsArrayFromBMBytes(bmObjOrArrBytes, true);
			}
			
			var wffObjects = tag['wffObjects'];
			
			if(typeof wffObjects === 'undefined') {
				wffObjects = {};
				tag['wffObjects'] = wffObjects;
			}
			
			wffObjects[ky] = jsObjOrArr;
			
		} else if (taskValue == wffGlobal.taskValues.DEL_BM_OBJ_OR_ARR_FROM_TAG) {
			var tagName = getStringFromBytes(nameValues[1].name);
			var wffId = wffTagUtil
					.getWffIdFromWffIdBytes(nameValues[1].values[0]);
			
			var tag = wffTagUtil.getTagByTagNameAndWffId(tagName, wffId);
			
			var ky = getStringFromBytes(nameValues[1].values[1]);
			
			var wffObjects = tag['wffObjects'];
			
			if(typeof wffObjects !== 'undefined') {
				delete wffObjects[ky];
			}
		} 
		
		return true;

		// else if (taskValue == 'DA') {
		//
		// for (var i = 1; i < nameValues.length; i++) {
		//
		// var tagId = wffBMUtil
		// .getIntFromOptimizedBytes(nameValues[i].name);
		// var attrNames = nameValues[i].values;
		//
		// var tagName = getStringFromBytes(attrNames[0]);
		//
		// for (var j = 1; j < attrNames.length; j++) {
		//
		// var attrName = getStringFromBytes(attrNames[j]);
		//
		// var applicableTag = wffTagUtil.getTagByTagNameAndWffId(
		// tagName, tagId);
		//
		// applicableTag.removeAttribute(attrName);
		// console.log('attr removed');
		// }
		//
		// }
		// }

	};
	
	this.invokeTasks = function(wffBMBytes) {
		
		var nameValues = wffBMUtil.parseWffBinaryMessageBytes(wffBMBytes);
		var taskNameValue = nameValues[0];
		
		
		if (taskNameValue.name[0] == wffGlobal.taskValues.TASK) {
			
			console.log('TASK');
			
			invokeTask(nameValues);
			
		} else if (taskNameValue.name[0] == wffGlobal.taskValues.TASK_OF_TASKS) {
			
			console.log('TASK_OF_TASKS');
			
			var tasksBM = taskNameValue.values;
			
			for (var i = 0; i < tasksBM.length; i++) {
				var taskNameValues = wffBMUtil.parseWffBinaryMessageBytes(tasksBM[i]);
				invokeTask(taskNameValues);
			}
			
		} else {
			return false;
		}
		return true;
	};

	this.getAttributeUpdates = function(wffBMBytes) {
		var nameValue = wffBMUtil.parseWffBinaryMessageBytes(wffBMBytes)[1];
	};

};