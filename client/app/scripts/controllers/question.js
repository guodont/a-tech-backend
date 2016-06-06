////////
// This sample is published as part of the blog question at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';
/**
 * 问题管理控制器
 */
angular.module('clientApp')
  .controller('QuestionCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, questionService, apiUrl, $cookieStore) {

    $scope.getQuestions = function () {
      questionService.getQuestions(
        {},
        function (res) {
          console.log(res.data);
          $scope.questions = res.data;
        },
        function (res) {
          console.log("问题获取失败");
        }
      );
    };

    $scope.getQuestions();

    $scope.getQuestionInfo = function () {
      questionService.getQuestion(
        {
          questionId: $routeParams.id
        },
        function (res) {
          console.log(res.data);
          var question = res.data;
          $scope.title = question.title;
          $scope.content = question.content;
          $scope.tag = question.tag;
          $scope.sort = question.sort;
          $scope.image = question.image;
          $scope.categoryId = question.category.id;
        },
        function (res) {
          alertService.add('error', res.data.success.message);
        }
      );
    };

    if ($routeParams.id != null) {
      $scope.getQuestionInfo();
    }

    $scope.deleteQuestion = function (id) {
      questionService.deleteQuestion({
          id: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getQuestions();
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };

    // 审核问题
    $scope.auditQuestion = function (question) {
      $scope.curQuestion = question;
      console.log("审核问题");
    };

    // 拒绝审核
    $scope.auditWithRefuse = function (question) {
      // TODO
      console.log("拒绝审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/question/' + question.id + '/auditfail',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getQuestions();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };

    // 通过审核
    $scope.auditWithPass = function (question) {
      // TODO
      console.log("通过审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/question/' + question.id + '/auditpass',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getQuestions();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };

    // 指派给专家
    $scope.assignToexpert = function (question) {
      // TODO
      console.log("指派给专家");
    };

  });
