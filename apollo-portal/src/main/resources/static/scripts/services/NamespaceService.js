appService.service("NamespaceService", ['$resource', '$q', function ($resource, $q) {
    var namespace_source = $resource("", {}, {
        find_public_namespaces: {
            method: 'GET',
            isArray: true,
            url: '/appnamespaces/public'
        },
        createNamespace: {
            method: 'POST',
            url: '/apps/:appId/namespaces',
            isArray: false
        },
        createAppNamespace: {
            method: 'POST',
            url: '/apps/:appId/appnamespaces?appendNamespacePrefix=:appendNamespacePrefix',
            isArray: false
        },
        getNamespacePublishInfo: {
            method: 'GET',
            url: '/apps/:appId/namespaces/publish_info'
        },
        deleteNamespace: {
            method: 'DELETE',
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName'
        },
        getPublicAppNamespaceAllNamespaces: {
            method: 'GET',
            url: '/envs/:env/appnamespaces/:publicNamespaceName/namespaces',
            isArray: true
        },
        loadAppNamespace: {
            method: 'GET',
            url: '/apps/:appId/appnamespaces/:namespaceName'
        },
        deleteAppNamespace: {
            method: 'DELETE',
            url: '/apps/:appId/appnamespaces/:namespaceName'
        }
    });

    function find_public_namespaces() {
        var d = $q.defer();
        namespace_source.find_public_namespaces({}, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });
        return d.promise;
    }

    function createNamespace(appId, namespaceCreationModel) {
        var d = $q.defer();
        namespace_source.createNamespace({
                                             appId: appId
                                         }, namespaceCreationModel, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });
        return d.promise;
    }

    function createAppNamespace(appId, appnamespace, appendNamespacePrefix) {
        var d = $q.defer();
        namespace_source.createAppNamespace({
            appId: appId,
            appendNamespacePrefix: appendNamespacePrefix
          }, appnamespace, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });
        return d.promise;
    }

    function getNamespacePublishInfo(appId) {
        var d = $q.defer();
        namespace_source.getNamespacePublishInfo({
                                                     appId: appId
                                                 }, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });

        return d.promise;
    }

    function deleteNamespace(appId, env, clusterName, namespaceName) {
        var d = $q.defer();
        namespace_source.deleteNamespace({
                                             appId: appId,
                                             env: env,
                                             clusterName: clusterName,
                                             namespaceName: namespaceName
                                         },
                                         function (result) {
                                             d.resolve(result);
                                         },
                                         function (result) {
                                             d.reject(result);
                                         });

        return d.promise;
    }

    function getPublicAppNamespaceAllNamespaces(env, publicNamespaceName, page, size) {
        var d = $q.defer();
        namespace_source.getPublicAppNamespaceAllNamespaces({
                                                                env: env,
                                                                publicNamespaceName: publicNamespaceName,
                                                                page: page,
                                                                size: size
                                                            }, function (result) {
            d.resolve(result);
        }, function (result) {
            d.reject(result);
        });

        return d.promise;

    }

    function loadAppNamespace(appId, namespaceName) {
        var d = $q.defer();
        namespace_source.loadAppNamespace({
                                             appId: appId,
                                             namespaceName: namespaceName
                                         },
                                         function (result) {
                                             d.resolve(result);
                                         },
                                         function (result) {
                                             d.reject(result);
                                         });

        return d.promise;
    }

    function deleteAppNamespace(appId, namespaceName) {
        var d = $q.defer();
        namespace_source.deleteAppNamespace({
                                             appId: appId,
                                             namespaceName: namespaceName
                                         },
                                         function (result) {
                                             d.resolve(result);
                                         },
                                         function (result) {
                                             d.reject(result);
                                         });

        return d.promise;
    }

    return {
        find_public_namespaces: find_public_namespaces,
        createNamespace: createNamespace,
        createAppNamespace: createAppNamespace,
        getNamespacePublishInfo: getNamespacePublishInfo,
        deleteNamespace: deleteNamespace,
        getPublicAppNamespaceAllNamespaces: getPublicAppNamespaceAllNamespaces,
        loadAppNamespace: loadAppNamespace,
        deleteAppNamespace: deleteAppNamespace
    }

}]);
