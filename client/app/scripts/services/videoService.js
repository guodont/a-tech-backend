
'use strict';

angular.module('clientApp')
  .service('videoService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 添加视频
    self.addVideo = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/article',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          description: params.description,
          path: params.path,
          categoryId: params.categoryId
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

    // 更新视频
    self.updateVideo = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/video/' + params.videoId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          name: params.name,
          description: params.description,
          path: params.path,
          categoryId: params.categoryId
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

    // 更新视频
    self.getVideo = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/video/' + params.videoId,
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

    // 获取所有视频
    self.getVideos = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/videos' + '?pageSize=10&page=' + params.curPage,
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

    // 删除视频
    self.deleteVideo = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/video/' + params.videoId,
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
