angular.module('bears-bugs', ['ngRoute', 'ui.bootstrap', 'anguFixedHeaderTable'])
	.config(function($routeProvider, $locationProvider) {
		$routeProvider
			.when('/bug/:project/:id', {
				controller: 'bugController'
			})
			.when('/', {
				controller: 'mainController'
			});
		// configure html5 to get links working on jsfiddle
		$locationProvider.html5Mode(false);
	})
	.directive('keypressEvents', [
		'$document',
		'$rootScope',
		function($document, $rootScope) {
			return {
				restrict: 'A',
				link: function() {
					$document.bind('keydown', function(e) {
						$rootScope.$broadcast('keypress', e);
						$rootScope.$broadcast('keypress:' + e.which, e);
					});
				}
			};
		}
	]).directive('diff', ['$http', function ($http) {
		return {
			restrict: 'A',
			link: function (scope, elem, attrs) {
				scope.$watch("$ctrl.bug.diff", function () {
					var diff2htmlUi = new Diff2HtmlUI({ diff: scope["$ctrl"].bug.diff });
					diff2htmlUi.draw($(elem), {inputFormat: 'java', showFiles: false, matching: 'lines'});
					diff2htmlUi.highlightCode($(elem));
				});
			}
		}
		}])
	.controller('welcomeController', function($uibModalInstance) {
		this.ok = function () {
			$uibModalInstance.close();
		};
	})
	.controller('bugModal', function($rootScope, $uibModalInstance, bug, classifications) {
		var $ctrl = this;
		$ctrl.bug = bug;
		$ctrl.classifications = classifications;

		$rootScope.$on('new_bug', function(e, bug) {
			$ctrl.bug = bug;
		});
		$ctrl.ok = function () {
			$uibModalInstance.close();
		};
		$ctrl.nextBug = function () {
			$rootScope.$emit('next_bug', 'next');
		};
		$ctrl.previousBug = function () {
			$rootScope.$emit('previous_bug', 'next');
		};

		var getName = function (type, key) {
			for(var group in $ctrl.classifications[type]) {
				if ($ctrl.classifications[type][group][key]) {
					if ($ctrl.classifications[type][group][key].fullname) {
						return $ctrl.classifications[type][group][key].fullname;
					} else {
						return $ctrl.classifications[type][group][key].name;
					}
				}
			}
			return null;
		}

	})
	.controller('bugController', function($scope, $location, $rootScope, $routeParams, $uibModal) {
		var $ctrl = $scope;
		$ctrl.classifications = $scope.$parent.classifications;
		$ctrl.bugs = $scope.$parent.filteredBug;
		$ctrl.index = -1;
		$ctrl.bug = null;

		$scope.$watch("$parent.filteredBug", function () {
			$ctrl.bugs = $scope.$parent.filteredBug;
			$ctrl.index = getIndex($routeParams.project, $routeParams.id);
		});
		$scope.$watch("$parent.classifications", function () {
			$ctrl.classifications = $scope.$parent.classifications;
		});

		var getIndex = function (project, bugId) {
			if ($ctrl.bugs == null) {
				return -1;
			}
			for (var i = 0; i < $ctrl.bugs.length; i++) {
				if ($ctrl.bugs[i].repository.name == project && $ctrl.bugs[i].bugId == bugId) {
					return i;
				}
			}
			return -1;
		};

		$scope.$on('$routeChangeStart', function(next, current) {
			$ctrl.index = getIndex(current.params.project, current.params.id);
		});

		var modalInstance = null;
		$scope.$watch("index", function () {
			if ($scope.index != -1) {
				if (welcomeModal != null) {
					welcomeModal.close();
				}
				if (modalInstance == null) {
					modalInstance = $uibModal.open({
						animation: true,
						ariaLabelledBy: 'modal-title',
						ariaDescribedBy: 'modal-body',
						templateUrl: 'modelBug.html',
						controller: 'bugModal',
						controllerAs: '$ctrl',
						size: "lg",
						resolve: {
							bug: function () {
								return $scope.bugs[$scope.index];
							},
							classifications: $scope.classifications
						}
					});
					modalInstance.result.then(function () {
						modalInstance = null;
						$location.path("/");
					}, function () {
						modalInstance = null;
						$location.path("/");
					})
				}
				$rootScope.$emit('new_bug', $scope.bugs[$scope.index]);
			}
		});
		var welcomeModal = null;
		$scope.openWelcome = function () {
			welcomeModal = $uibModal.open({
				animation: true,
				ariaLabelledBy: 'modal-title',
				ariaDescribedBy: 'modal-body',
				templateUrl: 'welcome.html',
				controller: 'welcomeController',
				controllerAs: '$ctrl',
				size: "lg"
			});
			welcomeModal.result.then(function () {
				welcomeModal = null;
			}, function () {
				welcomeModal = null;
			})
		};
		$scope.openWelcome();
		if (gtag) {
			gtag('config', 'UA-5954162-27');
		}

		var nextBug = function () {
			var index  = $scope.index + 1;
			if (index == $ctrl.bugs.length)  {
				index = 0;
			}

			$location.path( "/bug/" + $ctrl.bugs[index].repository.name + "/" + $ctrl.bugs[index].bugId );
			if (gtag) {
				gtag('event', 'next', {
					'event_category': 'Shortcut'
				});
			}
			return false;
		};
		var previousBug = function () {
			var index  = $scope.index - 1;
			if (index < 0) {
				index = $ctrl.bugs.length - 1;
			}

			$location.path( "/bug/" + $ctrl.bugs[index].repository.name + "/" + $ctrl.bugs[index].bugId );
			if (gtag) {
				gtag('event', 'previous', {
					'event_category': 'Shortcut'
				});
			}
			return false;
		};

		$scope.$on('keypress:39', function () {
			$scope.$apply(function () {
				nextBug();
			});
		});
		$scope.$on('keypress:37', function () {
			$scope.$apply(function () {
				previousBug();
			});
		});
		$rootScope.$on('next_bug', nextBug);
		$rootScope.$on('previous_bug', previousBug);
	})
	.controller('mainController', function($scope, $rootScope, $location, $window, $rootScope, $http, $uibModal) {
		$scope.sortType     = ['bugId', 'repository.name']; // set the default sort type
		$scope.sortReverse  = false;
		$scope.match  = "all";
		$scope.filter   = {};
		$scope.pageTitle = "Bears Bugs";

		// create the list of sushi rolls 
		$scope.bugs = [];
		$scope.classifications = [];

		$http.get("data/classification.json").then(function (response) {
			$scope.classifications = response.data;
		});

		$http.get("data/bears-bugs.json").then(function (response) {
			$scope.bugs = response.data;

			var projects = {};
			var nbProjects = 0;

			var buildPairTypes = {};

			var exceptions = {};
			var nbExceptions = 0;

			var bearsVersions = {};

			for (var i = 0; i < $scope.bugs.length; i++) {
				var project = $scope.bugs[i].repository.name;
				if (projects[project] == null) {
					projects[project] = {
						"name": project,
						"fullname": project
					}
					nbProjects++;
				}
				$scope.bugs[i][project] = true;

				if ($scope.bugs[i].projectMetrics.numberModules == 1) {
                    $scope.bugs[i]["singleModule"] = true;
                } else {
                    $scope.bugs[i]["multiModule"] = true;
                }

				var buildPairType = $scope.bugs[i].type;
				if (buildPairTypes[buildPairType] == null) {
					buildPairTypes[buildPairType] = {
						"name": buildPairType,
						"fullname": buildPairType
					}
				}
				$scope.bugs[i][buildPairType] = true;

				for (var j = 0; j < $scope.bugs[i].tests.failureDetails.length; j++) {
					if ($scope.bugs[i].tests.failureDetails[j].isError == false) {
						$scope.bugs[i]["isFailure"] = true;
					} else {
						$scope.bugs[i]["isError"] = true;
					}

					var exception = $scope.bugs[i].tests.failureDetails[j].failureName.substring($scope.bugs[i].tests.failureDetails[j].failureName.lastIndexOf(".") + 1);
					if (exceptions[exception] == null) {
						exceptions[exception] = {
							"name": exception
						}
						nbExceptions++;
					}
					$scope.bugs[i][exception] = true;
				}

				var bearsVersion = $scope.bugs[i].version;
                if (bearsVersions[bearsVersion] == null) {
                    bearsVersions[bearsVersion] = {
                        "name": bearsVersion
                    }
                }
                $scope.bugs[i][bearsVersion] = true;
			}

			projectLabel = "Projects (" + nbProjects + ")";
			$scope.classifications["Project-related info"][projectLabel] = projects;

            $scope.classifications["Travis-related info"]["Build pair types"] = buildPairTypes;

			exceptionLabel = "Exceptions (" + nbExceptions + ")";
			$scope.classifications["Runtime information"][exceptionLabel] = exceptions;

			$scope.classifications["Bears"]["Bears version"] = bearsVersions;

			var element = angular.element(document.querySelector('#menu')); 
			var height = element[0].offsetHeight;

			angular.element(document.querySelector('#mainTable')).css('height', (height-160)+'px');
		});

		$scope.filterName = function (filterKey) {
			for (var j in $scope.classifications) {
				for(var i in $scope.classifications[j]) {
					if ($scope.classifications[j][i][filterKey] != null) {
						if ($scope.classifications[j][i][filterKey].fullname) {
							return $scope.classifications[j][i][filterKey].fullname;
						}
						return $scope.classifications[j][i][filterKey].name;
					}
				}
			}
			return filterKey;
		}

		$scope.openBug = function (bug) {
			$location.path( "/bug/" +  bug.repository.name + "/" + bug.bugId );
		};

		$scope.sort = function (sort) {
			if (sort == $scope.sortType || (sort[0] == 'repository.name' && $scope.sortType[0] == 'repository.name')) {
				$scope.sortReverse = !$scope.sortReverse; 
			} else {
				$scope.sortType = sort;
				$scope.sortReverse = false; 
			}
			return false;
		}

		$scope.countBugs = function (key, filter) {
			if (filter.count) {
				return filter.count;
			}
			var count = 0;
			for(var i = 0; i < $scope.bugs.length; i++) {
				if ($scope.bugs[i][key] === true) {
					count++;
				}
			}
			filter.count = count;
			return count;
		};

		$scope.clickFilter = function (vKey) {
			if (gtag) {
				gtag('event', vKey, {
					'event_category': 'Filter',
					'event_label': $scope.filterNamevKey
				});
			}
		}

		$scope.bugsFilter = function (bug) {
			var allFalse = true;
			for (var i in $scope.filter) {
				if ($scope.filter[i] === true) {
					allFalse = false;
					break;
				}
			}
			if (allFalse) {
				return true;
			}

			for (var i in $scope.filter) {
				if ($scope.filter[i] === true) {
					if (bug[i] === true) {
						if ($scope.match=="any") {
							return true;
						}
					} else if ($scope.match=="all"){
						return false;
					}
				}
			}
			if ($scope.match=="any") {
				return false;
			} else {
				return true;
			}
		};

		$rootScope.$on('new_bug', function(e, bug) {
			var title = "Dissection of " + bug.repository.name + " " + bug.bugId;
			$scope.pageTitle = title;

			if ($window.gtag) {
				$window.gtag('config', 'UA-5954162-27', {'page_path': $location.path(), 'page_title': title});
			}
		});
	});
