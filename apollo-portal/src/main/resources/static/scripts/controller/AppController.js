app_module.controller('CreateAppController',
                      ['$scope', '$window', 'toastr', 'AppService', 'AppUtil', 'OrganizationService',
                       createAppController]);

function createAppController($scope, $window, toastr, AppService, AppUtil, OrganizationService) {

    $scope.app = {};
    $scope.submitBtnDisabled = false;

    $scope.create = create;

    init();

    function init() {
        initOrganization();
    }

    function initOrganization() {
        OrganizationService.find_organizations().then(function (result) {
            var organizations = [];
            result.forEach(function (item) {
                var org = {};
                org.id = item.orgId;
                org.text = item.orgName + '(' + item.orgId + ')';
                org.name = item.orgName;
                organizations.push(org);
            });
            $('#organization').select2({
                                           placeholder: '请选择部门',
                                           width: '100%',
                                           data: organizations
                                       });
        }, function (result) {
            toastr.error(AppUtil.errorMsg(result), "load organizations error");
        });
    }

    function create() {
        $scope.submitBtnDisabled = true;

        var selectedOrg = $('#organization').select2('data')[0];

        if (!selectedOrg.id) {
            toastr.warning("请选择部门");
            return;
        }

        $scope.app.orgId = selectedOrg.id;
        $scope.app.orgName = selectedOrg.name;

        // owner
        var owner = $('.ownerSelector').select2('data')[0];
        if (!owner) {
            toastr.warning("请选择应用负责人");
            return;
        }
        $scope.app.ownerName = owner.id;

        //admins
        $scope.app.admins = [];
        var admins = $(".adminSelector").select2('data');
        if (admins) {
            admins.forEach(function (admin) {
                $scope.app.admins.push(admin.id);
            })
        }

        AppService.create($scope.app).then(function (result) {
            toastr.success('创建成功!');
            setInterval(function () {
                $scope.submitBtnDisabled = false;
                $window.location.href = '/config.html?#appid=' + result.appId;
            }, 1000);
        }, function (result) {
            $scope.submitBtnDisabled = false;
            toastr.error(AppUtil.errorMsg(result), '创建失败!');
        });
    }


    $(".J_ownerSelectorPanel").on("select2:select", ".ownerSelector", selectEventHandler);
    var $adminSelectorPanel = $(".J_adminSelectorPanel");
    $adminSelectorPanel.on("select2:select", ".adminSelector", selectEventHandler);
    $adminSelectorPanel.on("select2:unselect", ".adminSelector", selectEventHandler);

    function selectEventHandler() {
        $('.J_owner').remove();

        var owner = $('.ownerSelector').select2('data')[0];

        if (owner) {
            $(".adminSelector").parent().find(".select2-selection__rendered").prepend(
                '<li class="select2-selection__choice J_owner">'
                + owner.text + '</li>')
        }
    }
}
