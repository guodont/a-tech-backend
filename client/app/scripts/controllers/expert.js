'use strict';
/**
 * 专家管理控制器
 */
angular.module('clientApp')
  .controller('ExpertCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore,expertService) {

    $scope.getExperts = function () {

      $http({
        method: 'GET',
        url: apiUrl + '/experts',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.experts = res.data;
        }, function (res) {
          console.log("专家数据获取失败");
        });
    };

    $scope.getExperts('');

    $scope.addExpert = function () {
      expertService.addExpert(
        {
          name: $scope.name,
          category: $scope.category,
          professional: $scope.professional,
          duty: $scope.duty,
          introduction: $scope.introduction,
          service:$scope.service,
          company:$scope.company
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $location.path('/expert/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'content' || key == 'categoryId') {
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
  });
