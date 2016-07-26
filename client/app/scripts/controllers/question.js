'use strict';
/**
 * 问题管理控制器
 */
angular.module('clientApp')
  .controller('QuestionCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, questionService, apiUrl, $cookieStore, categoryService) {

    $scope.status = $location.search().status ? $location.search().status : '';

    $scope.getCategories = function (type) {
      categoryService.getCategories(
        {
          type: type,
          parentId: ""
        },
        function (res) {
          console.log(res.data);
          $scope.categories = res.data;
        },
        function (res) {
          console.log("问题分类获取失败");
        }
      );
    };

    $scope.getCategories('QUESTION');  // 获取问题分类

    $('.ui.dropdown')
      .dropdown({
        // action: 'hide',
        onChange: function (value, text, $selectedItem) {
          console.log(value);
          $('#categoryId').attr("value", value);
          $scope.categoryId = value;
        }
      })
    ;

    $('.ui.assignExpertId')
      .dropdown({
        // action: 'hide',
        onChange: function (value, text, $selectedItem) {
          $('#assignExpertId').attr("value", value);
          console.log("选择专家:");
          console.log(value);
          $scope.assignExpertId = value;
        }
      })
    ;

    $scope.addCategory = function () {
      categoryService.addCategory(
        {
          parentId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.imageData,
          type: 'QUESTION'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          // $('.at-add-category').modal('hide'); // 隐藏模态框
          $scope.getCategories('QUESTION');  // 获取问题分类
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'image') {
                alertService.add('danger', key + ' : ' + value);
              } else {
                alertService.add('danger', value.message);
              }
            });
          } else if (res.status === 401) {
            $location.path('/login');
          } else if (res.status === 500) {
            alertService.add('danger', 'Internal server error!');
          } else {
            alertService.add('danger', res.date);
          }
        }
      );
    };

    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $scope.getQuestions = function () {
      questionService.getQuestions(
        {
          curPage: $scope.curPage,
          status: $scope.status
        },
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
          $scope.images = question.image.split(',');
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

    // 指派给问题 /api/v1/question/:id/assign/:expertId
    $scope.assignToexpert = function (question) {
      console.log("指派给问题");

      $http({
        method: 'PUT',
        url: apiUrl + '/question/' + question.id + '/assign/' + $scope.assignExpertId,
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

    $scope.loadExperts = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/foradmin/experts',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          $scope.experts = res.data;
          console.log($scope.experts);
        }, function (res) {
        });
    };


    $scope.loadExperts();

  });
