/**
 * Created by llz on 2016/4/22.
 */
'use strict';

angular.module('clientApp')
  .service('videoService', ['$http', '$cookies', function ( $http, $cookies) {

    var self = this;

    // 添加视频
    self.addVideo = function (params, success, error) {
      $http({
        method: 'POST',
        url: '/api/v1/video',
        headers: {authenticate: $cookies.token},
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
    // 删除视频
    self.deleteVideo = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: '/api/v1/video/' + params.videoId,
        headers: {authenticate: $cookies.token}
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
        url: '/api/v1/video/' + params.videoId,
        headers: {authenticate: $cookies.token},
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

    // 获取所有视频
    self.getVideos = function (params, success, error) {
      $http({
        method: 'GET',
        url: '/api/v1/videos',
        headers: {authenticate: $cookies.token}
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
