'use strict';
/**
 * 专家管理控制器
 */
angular.module('clientApp')
  .controller('ExpertCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore,expertService) {

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
          console.log("专家分类获取失败");
        }
      );
    };

    $scope.getCategories('EXPERT');  // 获取专家分类

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

    $scope.addCategory = function () {
      categoryService.addCategory(
        {
          parentId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.imageData,
          type: 'EXPERT'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          // $('.at-add-category').modal('hide'); // 隐藏模态框
          $scope.getCategories('EXPERT');  // 获取专家分类
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
          userId: $scope.userId,
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
