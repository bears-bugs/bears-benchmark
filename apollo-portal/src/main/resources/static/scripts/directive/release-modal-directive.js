directive_module.directive('releasemodal', releaseModalDirective);

function releaseModalDirective(toastr, AppUtil, EventManager, ReleaseService, NamespaceBranchService) {
    return {
        restrict: 'E',
        templateUrl: '../../views/component/release-modal.html',
        transclude: true,
        replace: true,
        scope: {
            appId: '=',
            env: '=',
            cluster: '='
        },
        link: function (scope) {

            scope.switchReleaseChangeViewType = switchReleaseChangeViewType;
            scope.release = release;

            scope.releaseBtnDisabled = false;
            scope.releaseChangeViewType = 'change';
            scope.releaseComment = '';
            scope.isEmergencyPublish = false;
            
            EventManager.subscribe(EventManager.EventType.PUBLISH_NAMESPACE,
                                   function (context) {

                                       var namespace = context.namespace;
                                       scope.toReleaseNamespace = context.namespace;
                                       scope.isEmergencyPublish = !!context.isEmergencyPublish;

                                       var date = new Date().Format("yyyyMMddhhmmss");
                                       if (namespace.mergeAndPublish) {
                                           namespace.releaseTitle = date + "-gray-release-merge-to-master";
                                       } else if (namespace.isBranch) {
                                           namespace.releaseTitle = date + "-gray";
                                       } else {
                                           namespace.releaseTitle = date + "-release";
                                       }

                                       AppUtil.showModal('#releaseModal');
                                   });

            function release() {
                if (scope.toReleaseNamespace.mergeAndPublish) {
                    mergeAndPublish();
                } else if (scope.toReleaseNamespace.isBranch) {
                    grayPublish();
                } else {
                    publish();
                }

            }

            function publish() {
                scope.releaseBtnDisabled = true;
                ReleaseService.publish(scope.appId, scope.env,
                                       scope.toReleaseNamespace.baseInfo.clusterName,
                                       scope.toReleaseNamespace.baseInfo.namespaceName,
                                       scope.toReleaseNamespace.releaseTitle,
                                       scope.releaseComment,
                                       scope.isEmergencyPublish).then(
                    function (result) {
                        AppUtil.hideModal('#releaseModal');
                        toastr.success("发布成功");

                        scope.releaseBtnDisabled = false;

                        EventManager.emit(EventManager.EventType.REFRESH_NAMESPACE,
                                          {
                                              namespace: scope.toReleaseNamespace
                                          })

                    }, function (result) {
                        scope.releaseBtnDisabled = false;
                        toastr.error(AppUtil.errorMsg(result), "发布失败");

                    }
                );

            }

            function grayPublish() {
                scope.releaseBtnDisabled = true;
                ReleaseService.grayPublish(scope.appId, scope.env,
                                           scope.toReleaseNamespace.parentNamespace.baseInfo.clusterName,
                                           scope.toReleaseNamespace.baseInfo.namespaceName,
                                           scope.toReleaseNamespace.baseInfo.clusterName,
                                           scope.toReleaseNamespace.releaseTitle,
                                           scope.releaseComment,
                                           scope.isEmergencyPublish).then(
                    function (result) {
                        AppUtil.hideModal('#releaseModal');
                        toastr.success("灰度发布成功");

                        scope.releaseBtnDisabled = false;

                        //refresh item status
                        scope.toReleaseNamespace.branchItems.forEach(function (item, index) {
                            if (item.isDeleted) {
                                scope.toReleaseNamespace.branchItems.splice(index, 1);
                            } else {
                                item.isModified = false;
                            }
                        });
                        //reset namespace status
                        scope.toReleaseNamespace.itemModifiedCnt = 0;
                        scope.toReleaseNamespace.lockOwner = undefined;

                        //check rules
                        if (!scope.toReleaseNamespace.rules
                            || !scope.toReleaseNamespace.rules.ruleItems
                            || !scope.toReleaseNamespace.rules.ruleItems.length) {

                            scope.toReleaseNamespace.viewType = 'rule';
                            AppUtil.showModal('#grayReleaseWithoutRulesTips');
                        }

                    }, function (result) {
                        scope.releaseBtnDisabled = false;
                        toastr.error(AppUtil.errorMsg(result), "灰度发布失败");

                    });
            }

            function mergeAndPublish() {

                NamespaceBranchService.mergeAndReleaseBranch(scope.appId,
                                                             scope.env,
                                                             scope.cluster,
                                                             scope.toReleaseNamespace.baseInfo.namespaceName,
                                                             scope.toReleaseNamespace.baseInfo.clusterName,
                                                             scope.toReleaseNamespace.releaseTitle,
                                                             scope.releaseComment,
                                                             scope.isEmergencyPublish,
                                                             scope.toReleaseNamespace.mergeAfterDeleteBranch)
                    .then(function (result) {

                        toastr.success("全量发布成功");

                        EventManager.emit(EventManager.EventType.REFRESH_NAMESPACE,
                                          {
                                              namespace: scope.toReleaseNamespace
                                          })

                    }, function (result) {
                        toastr.error(AppUtil.errorMsg(result), "全量发布失败");
                    });

                AppUtil.hideModal('#releaseModal');
            }

            function switchReleaseChangeViewType(type) {
                scope.releaseChangeViewType = type;
            }
        }
    }
}


