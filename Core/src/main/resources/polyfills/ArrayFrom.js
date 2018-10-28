if (!Array.from) {
  Array.from = (function () {
    return function (arrayLike) { 
    	if(Set && arrayLike instanceof Set) {
    		arrayLike = arrayLike.values();
    	}
    	if(arrayLike.next) {
    		var len = arrayLike.length;  
    		var A = new Array(len);
    		var k = 0;  
    		var next = arrayLike.next();
    		while(!next.done) {
    			A[k++] = next.value;
    			next = arrayLike.next();
    		}
    	} else {
    		var items = Object(arrayLike); 
    		var len = items.length;  
    		var A = new Array(len);
    		var k = 0;  
    		while (k < len) {
    			A[k] = items[k++]; 
    		}
      }
      return A;
    };
  }());
}