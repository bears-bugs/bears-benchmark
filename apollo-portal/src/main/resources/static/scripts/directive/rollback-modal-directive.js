directive_module.directive('rollbackmodal', rollbackModalDirective);

function rollbackModalDirective(AppUtil, EventManager, ReleaseService, toastr) {
    return {
        restrict: 'E',
        templateUrl: '../../views/component/rollback-modal.html',
        transclude: true,
        replace: true,
        scope: {
            appId: '=',
            env: '=',
            cluster: '='
        },
        link: function (scope) {

            scope.showRollbackAlertDialog = showRollbackAlertDialog;

            EventManager.subscribe(EventManager.EventType.PRE_ROLLBACK_NAMESPACE,
            function (context) {
                preRollback(context.namespace);
            });

            EventManager.subscribe(EventManager.EventType.ROLLBACK_NAMESPACE,
                                   function (context) {
                                       rollback();
                                   });

            function preRollback(namespace) {
                scope.toRollbackNamespace = namespace;
                //load latest two active releases
                ReleaseService.findActiveReleases(scope.appId,
                                                  scope.env,
                                                  scope.cluster,
                                                  scope.toRollbackNamespace.baseInfo.namespaceName,
                                                  0, 2)
                    .then(function (result) {
                        if (result.length <= 1) {
                            toastr.error("没有可以回滚的发布历史");
                            return;
                        }
                        scope.toRollbackNamespace.firstRelease = result[0];
                        scope.toRollbackNamespace.secondRelease = result[1];

                        ReleaseService.compare(scope.env,
                                               scope.toRollbackNamespace.firstRelease.id,
                                               scope.toRollbackNamespace.secondRelease.id)
                            .then(function (result) {
                                scope.toRollbackNamespace.releaseCompareResult = result.changes;

                                AppUtil.showModal('#rollbackModal');
                            })
                    });
            }

            function rollback() {
                scope.toRollbackNamespace.rollbackBtnDisabled = true;
                ReleaseService.rollback(scope.env,
                                        scope.toRollbackNamespace.firstRelease.id)
                    .then(function (result) {
                        toastr.success("回滚成功");
                        scope.toRollbackNamespace.rollbackBtnDisabled = false;
                        AppUtil.hideModal('#rollbackModal');
                        EventManager.emit(EventManager.EventType.REFRESH_NAMESPACE,
                                          {
                                              namespace:scope.toRollbackNamespace
                                          });
                    }, function (result) {
                        scope.toRollbackNamespace.rollbackBtnDisabled = false;
                        AppUtil.showErrorMsg(result, "回滚失败");
                    })
            }

            function showRollbackAlertDialog() {
                AppUtil.hideModal("#rollbackModal");
                AppUtil.showModal("#rollbackAlertDialog");
            }
        }
    }
}


