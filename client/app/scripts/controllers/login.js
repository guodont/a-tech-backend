////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';

/**
 * @ngdoc function
 * @name clientApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the clientApp
 */
angular.module('clientApp')
  .controller('LoginCtrl', function ($scope, userService, signupService, $cookies,$cookieStore, $location, $log, $http, alertService, apiUrl, hostUrl) {

    $scope.isAuthenticated = function () {
      if (userService.username) {
        $log.debug(userService.username);
        $location.path('/dashboard');
      } else {
        signupService.isAuthenticated(
          {},
          function (res) {
            console.log(res.data);
            if (res.data.hasOwnProperty('success')) {
              userService.username = res.data.success.user;
              $location.path('/dashboard');
            }
          },
          function (res) {
            $location.path('/login');
          }
        );
      }
    };

    $scope.isAuthenticated();

    $scope.login = function () {

      var payload = {
        email: this.email,
        password: this.password
      };

      $http.post(apiUrl + '/admin/login', payload)
        .error(function (data, status) {
          if (status === 400) {
            angular.forEach(data, function (value, key) {
              if (key === 'email' || key === 'password') {
                alertService.add('danger', key + ' : ' + value);
              } else {
                alertService.add('danger', value.message);
              }
            });
          } else if (status === 401) {
            alertService.add('danger', 'Invalid login or password!');
          } else if (status === 500) {
            alertService.add('danger', 'Internal server error!');
          } else {
            alertService.add('danger', data);
          }
        })
        .success(function (data) {
          console.log("登录成功");
          console.log(data);
          if (data.hasOwnProperty('authToken')) {
            $cookies.isLoggedIn = true;
            $cookieStore.put('isLoggedIn', 1);
            $cookieStore.put('authToken', data.authToken);
            // userService.username = data.success.user;
            $location.path('/dashboard');
          }
          console.log($cookieStore.get("authToken"));
        });
    };
  });
