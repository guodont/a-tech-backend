/**
 * Created by guodont on 16/4/21.
 */
'use strict';

angular.module('clientApp')
  .service('signupService', ['$http', '$cookies', 'apiUrl', function ( $http, $cookies, apiUrl) {

    var self = this;

    // 注册
    self.signup = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/admin/signup',
        headers: {authenticate: $cookies.token},
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
