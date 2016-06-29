/**
 * Created by Administrator on 2016/6/19.
 */

'use strict';

angular.module('clientApp')
  .service('commentService', ['$http', '$cookies','$cookieStore', 'apiUrl', function ($http, $cookies,$cookieStore, apiUrl) {

    var self = this;

    // 获取所有评论
    self.getComments = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/comments?pageSize=20&page=' + params.curPage + '&status=' + params.status,
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

    // 删除评论
    self.deleteComment = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/comment/' + params.id,
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
