////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:MenuCtrl
 * @description
 * # MenuCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('MenuCtrl', function ($scope, $http, userService, $location, apiUrl, $cookies, $cookieStore) {
    $scope.user = userService;

    $scope.logout = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/admin/logout',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          if (res.data.hasOwnProperty('success')) {
            $cookies.isLoggedIn = false;
            $cookieStore.put('isLoggedIn', 0);
            userService.username = '';
            $location.path('/login');
          }
        }, function (res) {
          console.log("uploadToken获取失败");
        });
    };

    // $scope.$watch('user.username', function (newVal) {
    //   if (newVal === '') {
    //     $scope.isLoggedIn = false;
    //   } else {
    //     $scope.username = newVal;
    //     $scope.isLoggedIn = true;
    //   }
    // });

    if ($cookieStore.get("isLoggedIn") == '0') {
      console.log("未登录");
      $location.path("/login");
      $scope.isLoggedIn = false;

    } else {
      console.log("已登录");
      $scope.isLoggedIn = true;

    }
  });
