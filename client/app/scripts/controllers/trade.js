
'use strict';
/**
 * 交易管理控制器
 */
angular.module('clientApp')
  .controller('TradeCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, questionService,$cookieStore, apiUrl) {

    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $scope.getTrades = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/trades' + '?pageSize=20&page=' + $scope.curPage,
        data: {
          // categoryType: params.type
        },
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.trades = res.data;
        }, function (res) {
          console.log("交易数据获取失败");
        });
    };

    $scope.getTrades();

    $scope.getTradeInfo = function () {
      questionService.getTrade(
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
      $scope.getTradeInfo();
    }

  });
