/**
 * Created by guodont on 16/4/21.
 */
'use strict';

angular.module('clientApp')
  .service('questionService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 更新问题
    self.updateQuestion = function (params, success, error) {
      $http({
        method: 'PUT',
        url: apiUrl + '/question/' + params.questionId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
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
    
    // 更新问题
    self.getQuestion = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/question/' + params.questionId,
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

    // 获取所有问题
    self.getQuestions = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/foradmin/questions' + '?pageSize=20&page=' + params.curPage,
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

    // 删除问题
    self.deleteQuestion = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/question/' + params.id,
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
