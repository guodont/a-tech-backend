'use strict';
/**
 * 用户管理控制器
 */
angular.module('clientApp')
  .controller('UserCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore) {

    $scope.selectType = '';

    $scope.getUsers = function (type) {

      $http({
        method: 'GET',
        url: apiUrl + '/users',
        data: {
          // categoryType: params.type
        },
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.users = res.data;
        }, function (res) {
          console.log("用户数据获取失败");
        });
    };

    $scope.getUsers('');

    $scope.deleteUser = function (id) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/user/' + id,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getCategories('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };

  });
