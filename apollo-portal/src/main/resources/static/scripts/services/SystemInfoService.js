appService.service('SystemInfoService', ['$resource', '$q', function ($resource, $q) {
    var system_info_resource = $resource('', {}, {
        load_system_info: {
            method: 'GET',
            url: '/system-info'
        },
        check_health: {
            method: 'GET',
            url: '/system-info/health'
        }
    });
    return {
        load_system_info: function () {
            var d = $q.defer();
            system_info_resource.load_system_info({},
            function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },
        check_health: function (host) {
            var d = $q.defer();
            system_info_resource.check_health({
                host: host
            },
            function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        }
    }
}]);
