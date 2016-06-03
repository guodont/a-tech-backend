
'use strict';

angular.module('clientApp')
  .service('advService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 添加广告
    self.addAdv = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/advertisement',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          description: params.description,
          url: params.url,
          image: params.image,
          position: params.position
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

    // 更新广告
    self.updateAdv = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/advertisement/' + params.advId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          description: params.description,
          url: params.url,
          image: params.image,
          position: params.position
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

    // 获取广告
    self.getAdv = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/advertisement/' + params.advId,
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

    // 获取所有广告
    self.getAdvs = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/advertisements' + '?pageSize=10&page=' + params.curPage,
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

    // 删除广告
    self.deleteAdv = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/advertisement/' + params.id,
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

    return self;
  }]);
