directive_module.directive('rulesmodal', rulesModalDirective);

function rulesModalDirective(toastr, AppUtil, EventManager, InstanceService) {
    return {
        restrict: 'E',
        templateUrl: '../../views/component/gray-release-rules-modal.html',
        transclude: true,
        replace: true,
        scope: {
            appId: '=',
            env: '=',
            cluster: '='
        },
        link: function (scope) {

            scope.completeEditBtnDisable = false;

            scope.batchAddIPs = batchAddIPs;
            scope.addRules = addRules;
            scope.removeRule = removeRule;
            scope.completeEditItem = completeEditItem;
            scope.cancelEditItem = cancelEditItem;
            scope.initSelectIps = initSelectIps;

            EventManager.subscribe(EventManager.EventType.EDIT_GRAY_RELEASE_RULES,
                                   function (context) {
                                       var branch = context.branch;
                                       scope.branch = branch;

                                       if (branch.editingRuleItem.clientIpList && branch.editingRuleItem.clientIpList[0] == '*'){
                                           branch.editingRuleItem.ApplyToAllInstances = true;
                                       }else {
                                           branch.editingRuleItem.ApplyToAllInstances = false;
                                       }


                                       $('.rules-ip-selector').select2({
                                                                           placeholder: "从实例列表中选择",
                                                                           allowClear: true
                                                                       });

                                       AppUtil.showModal('#rulesModal');
                                   });

            $('.rules-ip-selector').on('select2:select', function () {
                addRules(scope.branch);
            });

            function addRules(branch) {
                var newRules, selector = $('.rules-ip-selector');

                newRules = selector.select2('data');

                var parsedIPs = [];
                newRules.forEach(function (rule) {
                    parsedIPs.push(rule.text);
                });

                selector.select2("val", "");
                addRuleItemIP(branch, parsedIPs);
                scope.$apply();
            }

            function batchAddIPs(branch, newIPs) {
                if (!newIPs) {
                    return;
                }
                addRuleItemIP(branch, newIPs.split(','));
            }

            function addRuleItemIP(branch, newIps) {
                var oldIPs = branch.editingRuleItem.draftIpList;
                if (newIps && newIps.length > 0) {
                    newIps.forEach(function (IP) {
                        if (!AppUtil.checkIPV4(IP)) {
                            toastr.error("不合法的IP地址:" + IP);
                        } else if (oldIPs.indexOf(IP) < 0) {
                            oldIPs.push(IP);
                        }
                    })
                }
                //remove IP:all
                oldIPs.forEach(function (IP, index) {
                    if (IP == "*") {
                        oldIPs.splice(index, 1);
                    }
                });

            }

            function removeRule(ruleItem, IP) {

                ruleItem.draftIpList.forEach(function (existedRule, index) {
                    if (existedRule == IP) {
                        ruleItem.draftIpList.splice(index, 1);
                    }
                })

            }

            function completeEditItem(branch) {
                scope.completeEditBtnDisable = true;

                if (!branch.editingRuleItem.clientAppId) {
                    toastr.error("灰度的AppId不能为空");
                    scope.completeEditBtnDisable = false;
                    return;
                }

                if (branch.editingRuleItem.isNew && branch.rules && branch.rules.ruleItems) {
                    var errorRuleItem = false;
                    branch.rules.ruleItems.forEach(function (ruleItem) {
                        if (ruleItem.clientAppId == branch.editingRuleItem.clientAppId) {
                            toastr.error("已经存在AppId=" + branch.editingRuleItem.clientAppId + "的规则");
                            errorRuleItem = true;
                        }
                    });
                    if (errorRuleItem) {
                        scope.completeEditBtnDisable = false;
                        return;
                    }
                }

                if (!branch.editingRuleItem.ApplyToAllInstances) {
                    if (branch.editingRuleItem.draftIpList.length == 0) {
                        toastr.error("IP列表不能为空");
                        scope.completeEditBtnDisable = false;
                        return;
                    } else {
                        branch.editingRuleItem.clientIpList = branch.editingRuleItem.draftIpList;
                    }
                } else {
                    branch.editingRuleItem.clientIpList = ['*'];
                }

                if (!branch.rules) {
                    branch.rules = {
                        appId: scope.appId,
                        clusterName: scope.cluster,
                        namespaceName: branch.baseInfo.namespaceName,
                        branchName: branch.baseInfo.clusterName
                    };
                }

                if (!branch.rules.ruleItems) {
                    branch.rules.ruleItems = [];
                }

                if (branch.editingRuleItem.isNew) {
                    branch.rules.ruleItems.push(branch.editingRuleItem);
                }

                branch.editingRuleItem = undefined;
                scope.toAddIPs = '';

                AppUtil.hideModal('#rulesModal');

                EventManager.emit(EventManager.EventType.UPDATE_GRAY_RELEASE_RULES,
                                  {
                                      branch: branch
                                  }, branch.baseInfo.namespaceName);
                scope.completeEditBtnDisable = false;
            }

            function cancelEditItem(branch) {
                branch.editingRuleItem.isEdit = false;
                branch.editingRuleItem = undefined;
                scope.toAddIPs = '';
                AppUtil.hideModal('#rulesModal');
            }

            $('#rulesModal').on('shown.bs.modal', function (e) {
                initSelectIps();
            });

            function initSelectIps() {
                scope.selectIps = [];
                if (!scope.branch.parentNamespace.isPublic ||
                    scope.branch.parentNamespace.isLinkedNamespace) {

                    scope.branch.editingRuleItem.clientAppId = scope.branch.baseInfo.appId;
                }

                if (!scope.branch.editingRuleItem.clientAppId) {
                    return;
                }
                InstanceService.findInstancesByNamespace(scope.appId,
                                                         scope.env,
                                                         scope.cluster,
                                                         scope.branch.baseInfo.namespaceName,
                                                         scope.branch.editingRuleItem.clientAppId,
                                                         0,
                                                         2000)
                    .then(function (result) {
                        scope.selectIps = result.content;
                    });
            }

        }
    }
}


