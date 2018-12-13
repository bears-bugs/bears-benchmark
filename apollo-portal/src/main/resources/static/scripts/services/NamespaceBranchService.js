appService.service('NamespaceBranchService', ['$resource', '$q', function ($resource, $q) {
    var resource = $resource('', {}, {
        find_namespace_branch: {
            method: 'GET',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches'
        },
        create_branch: {
            method: 'POST',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches'
        },
        delete_branch: {
            method: 'DELETE',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches/:branchName'
        },
        merge_and_release_branch: {
            method: 'POST',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches/:branchName/merge'
        },
        find_branch_gray_rules: {
            method: 'GET',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches/:branchName/rules'
        },
        update_branch_gray_rules: {
            method: 'PUT',
            isArray: false,
            url: '/apps/:appId/envs/:env/clusters/:clusterName/namespaces/:namespaceName/branches/:branchName/rules'
        }

    });

    function find_namespace_branch(appId, env, clusterName, namespaceName) {
        var d = $q.defer();
        resource.find_namespace_branch({
                                           appId: appId,
                                           env: env,
                                           clusterName: clusterName,
                                           namespaceName: namespaceName
                                       },
                                       function (result) {
                                           d.resolve(result);
                                       }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function create_branch(appId, env, clusterName, namespaceName) {
        var d = $q.defer();
        resource.create_branch({
                                   appId: appId,
                                   env: env,
                                   clusterName: clusterName,
                                   namespaceName: namespaceName
                               }, {},
                               function (result) {
                                   d.resolve(result);
                               }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function delete_branch(appId, env, clusterName, namespaceName, branchName) {
        var d = $q.defer();
        resource.delete_branch({
                                   appId: appId,
                                   env: env,
                                   clusterName: clusterName,
                                   namespaceName: namespaceName,
                                   branchName: branchName
                               },
                               function (result) {
                                   d.resolve(result);
                               }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function merge_and_release_branch(appId, env, clusterName, namespaceName,
                                      branchName, title, comment, isEmergencyPublish, deleteBranch) {
        var d = $q.defer();
        resource.merge_and_release_branch({
                                              appId: appId,
                                              env: env,
                                              clusterName: clusterName,
                                              namespaceName: namespaceName,
                                              branchName: branchName,
                                              deleteBranch:deleteBranch
                                          }, {
                                              releaseTitle: title,
                                              releaseComment: comment,
                                              isEmergencyPublish: isEmergencyPublish
                                          },
                                          function (result) {
                                              d.resolve(result);
                                          }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function find_branch_gray_rules(appId, env, clusterName, namespaceName, branchName) {
        var d = $q.defer();
        resource.find_branch_gray_rules({
                                            appId: appId,
                                            env: env,
                                            clusterName: clusterName,
                                            namespaceName: namespaceName,
                                            branchName: branchName
                                        },
                                        function (result) {
                                            d.resolve(result);
                                        }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    function update_branch_gray_rules(appId, env, clusterName,
                                      namespaceName, branchName, newRules) {
        var d = $q.defer();
        resource.update_branch_gray_rules({
                                              appId: appId,
                                              env: env,
                                              clusterName: clusterName,
                                              namespaceName: namespaceName,
                                              branchName: branchName
                                          }, newRules,
                                          function (result) {
                                              d.resolve(result);
                                          }, function (result) {
                d.reject(result);
            });
        return d.promise;
    }

    return {
        findNamespaceBranch: find_namespace_branch,
        createBranch: create_branch,
        deleteBranch: delete_branch,
        mergeAndReleaseBranch: merge_and_release_branch,
        findBranchGrayRules: find_branch_gray_rules,
        updateBranchGrayRules: update_branch_gray_rules
    }
}]);
