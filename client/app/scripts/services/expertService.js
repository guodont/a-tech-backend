/**
 * Created by j on 2016/6/4.
 */
'use strict';
angular.module('clientApp')
  .service('expertService', ['$http', '$cookies', 'apiUrl', '$cookieStore', 'ToKenHeader', function ($http, $cookies, apiUrl, $cookieStore, ToKenHeader) {

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
        url: apiUrl + '/user/' + params.userId + '/experts',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          categoryId: params.categoryId,
          professional: params.professional,
          duty: params.duty,
          introduction: params.introduction,
          service: params.service,
          company: params.company,
          avatar: params.avatar
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

    // 更新专家信息
    self.updateExpert = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/foradmin/expert/' + params.userId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          categoryId: params.categoryId,
          professional: params.professional,
          duty: params.duty,
          introduction: params.introduction,
          service: params.service,
          company: params.company,
          avatar: params.avatar
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

