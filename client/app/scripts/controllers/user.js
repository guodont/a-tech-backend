'use strict';
/**
 * 用户管理控制器
 */
angular.module('clientApp')
  .controller('UserCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore, expertService) {

    $scope.selectType = '';
    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;


    $scope.getUsers = function (type) {
      $http({
        method: 'GET',
        url: apiUrl + '/users' + '?pageSize=20&page=' + $scope.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.users = res.data;
        }, function (res) {
          console.log("用户数据获取失败");
        });
    };

    $scope.getUsers('');

    $scope.deleteUser = function (id) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/user/' + id,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getCategories('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };

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

    $scope.toAddExpert = function (user) {
      $scope.curUserid = user.id;
    };

    $scope.addExpert = function () {
      expertService.addExpert(
        {
          userId: $scope.curUserid,
          name: $scope.name,
          categoryId: $scope.categoryId,
          professional: $scope.professional,
          duty: $scope.duty,
          introduction: $scope.introduction,
          service: $scope.service,
          company: $scope.company
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
