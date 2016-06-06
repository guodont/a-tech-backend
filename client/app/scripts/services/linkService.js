/**
 * Created by j on 2016/6/2.
 */
'use strict';
angular.module('clientApp')
  .service('linkService', ['$http', '$cookies', '$cookieStore','apiUrl', function ( $http, $cookies,$cookieStore, apiUrl) {

    var self = this;

    // 添加link
    self.addLink = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/link',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          url: params.url,
          image: params.image,
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
//删除link
    self.deleteLink = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl +'/link/' + params.linkId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
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
    // 更新link
    self.updateLink = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/link/' + params.linkId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          url: params.url,
          image: params.image,
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
    // 获取所有link
    self.getLinks = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/links' + '?pageSize=20&page=' + params.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
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
