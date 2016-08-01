/**
 * Created by guodont on 16/4/21.
 */
'use strict';

angular.module('clientApp')
  .service('signupService', ['$http', '$cookies','$cookieStore','apiUrl','ToKenHeader', function ( $http, $cookies,$cookieStore, apiUrl,ToKenHeader) {

    var self = this;

    // 注册
    self.signup = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/admin/signup',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: params
      })
        .then(function (res) {
          if (typeof (success) === 'function') {
            success(res);
          }
        }, function (res) {
          if (typeof (error) === 'function') {
            error(res);
          }
        });
    };

    // 登录
    self.login = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/admin/login',
        data: params
      })
        .then(function (res) {
          if (typeof (success) === 'function') {
            success(res);
          }
        }, function (res) {
          if (typeof (error) === 'function') {
            error(res);
          }
        });
    };

    // 是否登录
    self.isAuthenticated = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/admin/isauthenticated',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: params
      })
        .then(function (res) {
          if (typeof (success) === 'function') {
            success(res);
          }
        }, function (res) {
          if (typeof (error) === 'function') {
            error(res);
          }
        });
    };


    return self;
  }]);
