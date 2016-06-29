'use strict';

angular.module('clientApp')
  .controller('MessageCtrl', function ($scope, $http, alertService, $location, apiUrl, $cookieStore) {

    $scope.selectType = '';
    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;


    $scope.getMessages = function (type) {
      $http({
        method: 'GET',
        url: apiUrl + '/foradmin/messages' + '?pageSize=20&page=' + $scope.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          $scope.messages = res.data;
        }, function (res) {
          console.log("消息获取失败");
        });
    };

    $scope.getMessages('');
    //
    // $scope.deleteMessage = function (id) {
    //   $http({
    //     method: 'DELETE',
    //     url: apiUrl + '/admin/' + id,
    //     headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
    //   })
    //     .then(function (res) {
    //       alertService.add('success', res.data.success.message);
    //       $scope.getMessages('');
    //     }, function (res) {
    //       alertService.add('success', res.data.error.message);
    //     });
    // };

  });
