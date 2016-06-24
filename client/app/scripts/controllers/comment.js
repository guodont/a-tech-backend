////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';
/**
 * 评论管理控制器
 */
angular.module('clientApp')
  .controller('CommentCtrl', function ($scope, $http, alertService, $location, commentService, apiUrl, $cookieStore) {
    $scope.selectType = '';
    $scope.getComments = function () {
      commentService.getComments(
        function (res) {
          console.log(res.data);
          $scope.comments = res.data;
        },
        function (res) {
          console.log("评论获取失败");
        }
      );
    };

    $scope.getComments('');

    $scope.deleteComment = function (id) {
      commentService.deleteComment({
          id: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getComments('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };

    $scope.$watch($scope.selectType, $scope.getComments($scope.selectType))

    // 审核问题
    $scope.auditComment = function (comment) {
      $scope.curComment = comment;
      console.log("审核评论");
    };

    // 拒绝审核
    $scope.auditWithRefuse = function (comment) {
      // TODO
      console.log("拒绝审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/comment/' + comment.id + '/auditfail',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getComments();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };

    // 通过审核
    $scope.auditWithPass = function (comment) {
      // TODO
      console.log("通过审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/comment/' + comment.id + '/auditpass',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getComments();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };

    function changeType() {
      console.log("123");
    }

  });
