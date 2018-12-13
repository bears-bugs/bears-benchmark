appService.service("ConfigService", ['$resource', '$q', function ($resource, $q) {
    var config_source = $resource("", {}, {
        load_namespace: {
            method: 'GET',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName'
        },
        load_public_namespace_for_associated_namespace: {
            method: 'GET',
            isArray: false,
            url: '/envs/:env/apps/:appId/clusters/:clusterName/namespaces/:namespaceName/associated-public-namespace'
        },
        load_all_namespaces: {
            method: 'GET',
            isArray: true,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces'
        },
        find_items: {
            method: 'GET',
            isArray: true,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/items'
        },
        modify_items: {
            method: 'PUT',
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/items'
        },
        diff: {
            method: 'POST',
            url: '/namespaces/:namespaceName/diff',
            isArray: true
        },
        sync_item: {
            method: 'PUT',
            url: '/apps/:appId/namespaces/:namespaceName/items',
            isArray: false
        },
        create_item: {
            method: 'POST',
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/item'
        },
        update_item: {
            method: 'PUT',
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/item'
        },
        delete_item: {
            method: 'DELETE',
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/items/:itemId'
        }
    });

    return {
        load_namespace: function (appId, env, clusterName, namespaceName) {
            var d = $q.defer();
            config_source.load_namespace({
                                             appId: appId,
                                             env: env,
                                             clusterName: clusterName,
                                             namespaceName: namespaceName
                                         }, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },
        load_public_namespace_for_associated_namespace: function (env, appId, clusterName, namespaceName) {
            var d = $q.defer();
            config_source.load_public_namespace_for_associated_namespace({
                                                                             env: env,
                                                                             appId: appId,
                                                                             clusterName: clusterName,
                                                                             namespaceName: namespaceName
                                                                         }, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },
        load_all_namespaces: function (appId, env, clusterName) {
            var d = $q.defer();
            config_source.load_all_namespaces({
                                                  appId: appId,
                                                  env: env,
                                                  clusterName: clusterName
                                              }, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        find_items: function (appId, env, clusterName, namespaceName, orderBy) {
            var d = $q.defer();
            config_source.find_items({
                                         appId: appId,
                                         env: env,
                                         clusterName: clusterName,
                                         namespaceName: namespaceName,
                                         orderBy: orderBy
                                     }, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        modify_items: function (appId, env, clusterName, namespaceName, model) {
            var d = $q.defer();
            config_source.modify_items({
                                           appId: appId,
                                           env: env,
                                           clusterName: clusterName,
                                           namespaceName: namespaceName
                                       },
                                       model, function (result) {
                    d.resolve(result);

                }, function (result) {
                    d.reject(result);
                });
            return d.promise;
        },

        diff: function (namespaceName, sourceData) {
            var d = $q.defer();
            config_source.diff({
                                   namespaceName: namespaceName
                               }, sourceData, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        sync_items: function (appId, namespaceName, sourceData) {
            var d = $q.defer();
            config_source.sync_item({
                                        appId: appId,
                                        namespaceName: namespaceName
                                    }, sourceData, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        create_item: function (appId, env, clusterName, namespaceName, item) {
            var d = $q.defer();
            config_source.create_item({
                                          appId: appId,
                                          env: env,
                                          clusterName: clusterName,
                                          namespaceName: namespaceName
                                      }, item, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        update_item: function (appId, env, clusterName, namespaceName, item) {
            var d = $q.defer();
            config_source.update_item({
                                          appId: appId,
                                          env: env,
                                          clusterName: clusterName,
                                          namespaceName: namespaceName
                                      }, item, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        },

        delete_item: function (appId, env, clusterName, namespaceName, itemId) {
            var d = $q.defer();
            config_source.delete_item({
                                          appId: appId,
                                          env: env,
                                          clusterName: clusterName,
                                          namespaceName: namespaceName,
                                          itemId: itemId
                                      }, function (result) {
                d.resolve(result);
            }, function (result) {
                d.reject(result);
            });
            return d.promise;
        }
    }

}]);
