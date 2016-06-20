/**
 * Created by llz on 2016/6/20.
 */
'use strict';

angular.module('clientApp')
  .service('expertInfoService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 获取所有专家文章
    self.getArticlesByExpert = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/articles' + '?pageSize=10&page=' + params.curPage,
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

    // 获取所有专家相册
    self.getAlbumsByExpert = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/albums' + '?pageSize=10&page=' + params.curPage,
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
