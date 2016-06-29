'use strict';
/**
 * 管理员管理控制器
 */
angular.module('clientApp')
  .controller('AdminCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore, expertService, signupService) {

    $scope.selectType = '';
    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;


    $scope.getAdmins = function (type) {
      $http({
        method: 'GET',
        url: apiUrl + '/admins' + '?pageSize=20&page=' + $scope.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.users = res.data;
        }, function (res) {
          console.log("管理员数据获取失败");
        });
    };

    $scope.getAdmins('');

    $scope.deleteAdmin = function (id) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/admin/' + id,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getAdmins('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };


    $scope.signup = function() {

      var payload = {
        email : $scope.email,
        password : $scope.password,
        phone : $scope.phone,
        name : $scope.name
      };

      signupService.signup(
        payload,
        function (res) {
          alertService.add('success', '管理员添加成功');
          $scope.getAdmins('');
        },
        function (res) {
          if(res.status === 400) {
            angular.forEach(res.data, function(value, key) {
              if(key === 'email' || key === 'password') {
                alertService.add('danger', key + ' : ' + value);
              } else {
                alertService.add('danger', value.message);
              }
            });
          }
          if(status === 500) {
            alertService.add('danger', 'Internal server error!');
          }
        }
      );
    };
  });
