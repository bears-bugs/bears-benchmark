if (!Object.is) {
  Object.is = function(v1, v2) {
    if (v1 === v2) {
    	return v1 !== 0 || 1 / v1 === 1 / v2; 
    } else {
      return v1 !== v1 && v2 !== v2;
    }
  };
}