'use strict';

angular.module('clientApp')
  .controller('SubMenuCtrl', function ($scope, $http, userService, $location, apiUrl, $cookies, $cookieStore, questionService) {

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
      } else if(type == 'trade'){
        $cookieStore.put('tradeMessageCount', '');
        $scope.tradeMessageCount = '';
      } else if(type == 'comment'){
        $cookieStore.put('commentMessageCount', '');
        $scope.commentMessageCount = '';
      }
    };
    //
    // $scope.getQuestions = function () {
    //   questionService.getQuestions(
    //     {
    //       curPage: $scope.curPage,
    //       status: $scope.status
    //     },
    //     function (res) {
    //       console.log(res.data);
    //       $scope.questions = res.data;
    //     },
    //     function (res) {
    //       console.log("问题获取失败");
    //     }
    //   );
    // };
    //
    // $scope.getQuestions();

  });
