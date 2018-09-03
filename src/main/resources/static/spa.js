
/* retrieves list of doctors and updates list */
function getDoctors (){

	axios.get('/doctors')
	.then(function (response) {
		console.log(response.data._embedded.doctors);
		vm.doctors = response.data._embedded.doctors;
	})
	.catch(function(error) {

	})
}

/* posts a doctor */
function postDoctor(){
	myDoctor = {
		"firstName":"John",
		"lastName":"Holliday",
	}
	axios.post('/doctors', myDoctor)
	.then(function(response){
		console.log(response);
		vm.getDoctors();
	})
	.catch(function(error){

	})	
}


/* retrieves list of locations and updates list */
function getLocations (){

	axios.get('/locations')
	.then(function (response) {
		console.log(response.data._embedded.locations);
		vm.locations = response.data._embedded.locations;
	})
	.catch(function(error) {

	})
}

/* main application */
var vm = new Vue({
	el: '#app',
	data: {
		message: "Hi vue!",
		doctors: [],
		locations: [],
	},
	methods: {
		getDoctors: getDoctors,
		postDoctor: postDoctor,
		getLocations: getLocations,
	}

})
