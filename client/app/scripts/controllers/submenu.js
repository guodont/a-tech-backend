'use strict';

angular.module('clientApp')
  .controller('SubMenuCtrl', function ($scope, $http, $interval, userService, $location, apiUrl, $cookies, $cookieStore, deskNotice) {

    $scope.questionMessageCount = $cookieStore.get("questionMessageCount") ? $cookieStore.get("questionMessageCount") : '';
    $scope.tradeMessageCount = $cookieStore.get("tradeMessageCount") ? $cookieStore.get("tradeMessageCount") : '';
    $scope.commentMessageCount = $cookieStore.get("commentMessageCount") ? $cookieStore.get("commentMessageCount") : '';

    // $cookieStore.put('questionMessageCount', 5);
    // $cookieStore.put('tradeMessageCount', 4);
    // $cookieStore.put('commentMessageCount', 6);

    $scope.lookMessage = function (type) {
      if (type == 'question') {
        $cookieStore.put('questionMessageCount', '');
        $scope.questionMessageCount = '';
      } else if (type == 'trade') {
        $cookieStore.put('tradeMessageCount', '');
        $scope.tradeMessageCount = '';
      } else if (type == 'comment') {
        $cookieStore.put('commentMessageCount', '');
        $scope.commentMessageCount = '';
      }
    };

    $scope.getNewMessages = function () {

      $http({
        method: 'GET',
        url: apiUrl + '/new/questions',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          if (res.data.length > 0) {
            $scope.newQuestions = res.data;
            $cookieStore.put('questionMessageCount', res.data.length + '+');
            console.log('新问题数:' + res.data.length);
            if ($scope.questionMessageCount != '')
              deskNotice.showDeskNotice('有' + res.data.length + '+ 新的问题,请尽快审核', '点击查看', 'question');
          }

        }, function (res) {
          console.log("新问题获取失败");
        });

      $http({
        method: 'GET',
        url: apiUrl + '/new/trades',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          if (res.data.length > 0) {
            $scope.newTrades = res.data;
            $cookieStore.put('tradeMessageCount', res.data.length + '+');
            console.log('新交易数:' + res.data.length);
            if ($scope.tradeMessageCount != '')
              deskNotice.showDeskNotice('有' + res.data.length + '+ 新的交易,请尽快审核', '点击查看', 'trade');

          }
        }, function (res) {
          console.log("新交易获取失败");
        });

      $http({
        method: 'GET',
        url: apiUrl + '/new/comments',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          if (res.data.length > 0) {
            $scope.newComments = res.data;
            $cookieStore.put('commentMessageCount', res.data.length + '+');
            console.log('新评论数:' + res.data.length);
            if ($scope.commentMessageCount != '')
              deskNotice.showDeskNotice('有' + res.data.length + '+ 新的评论,请尽快审核', '点击查看', 'comment');
          }
        }, function (res) {
          console.log("新评论获取失败");
        });

    };

    $scope.getNewMessages();

    $interval(function () {
      $scope.getNewMessages();
      console.log('获取新消息');
      $scope.questionMessageCount = $cookieStore.get("questionMessageCount") ? $cookieStore.get("questionMessageCount") : '';
      $scope.tradeMessageCount = $cookieStore.get("tradeMessageCount") ? $cookieStore.get("tradeMessageCount") : '';
      $scope.commentMessageCount = $cookieStore.get("commentMessageCount") ? $cookieStore.get("commentMessageCount") : '';
    }, 1000 * 5 * 60);  // 5分钟更新一次

  });
