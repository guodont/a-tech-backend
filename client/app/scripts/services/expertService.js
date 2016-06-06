/**
 * Created by j on 2016/6/4.
 */
'use strict';
angular.module('clientApp')
  .service('expertService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 获取所有专家
    self.getExperts = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/experts' + '?pageSize=10&page=' + params.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
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

    // 添加专家
    self.addExpert = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/expert',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          category: params.category,
          professional: params.professional,
          duty: params.duty,
          introduction: params.introduction,
          service:params.service,
          company:params.company
        }
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

