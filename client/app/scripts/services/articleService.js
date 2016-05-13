/**
 * Created by guodont on 16/4/21.
 */
'use strict';

angular.module('clientApp')
  .service('articleService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 添加文章
    self.addArticle = function (params, success, error) {
      $http({
        method: 'POST',
        url: apiUrl + '/article',
        headers: {ToKenHeader: $cookieStore.get("authToken")},
        data: {
          title: params.title,
          content: params.content,
          tag: params.tag,
          sort: params.sort,
          image: params.image,
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

    // 更新文章
    self.updateArticle = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/article/' + params.articleId,
        headers: {ToKenHeader: $cookieStore.get("authToken")},
        data: {
          title: params.title,
          content: params.content,
          tag: params.tag,
          sort: params.sort,
          image: params.image,
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

    // 获取所有文章
    self.getArticles = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/articles',
        headers: {ToKenHeader: $cookieStore.get("authToken")}
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

    // 删除文章
    self.deleteArticle = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/article/' + params.id,
        headers: {ToKenHeader: $cookieStore.get("authToken")}
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
