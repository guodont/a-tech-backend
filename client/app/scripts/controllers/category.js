////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';
/**
 * 分类管理控制器
 */
angular.module('clientApp')
  .controller('CategoryCtrl', function ($scope, $http, alertService, $location,$routeParams, categoryService) {
    $scope.selectType = '';
    $scope.getCategories = function (type) {
      categoryService.getCategories(
        {
          type: type,
          parentId: 0
        },
        function (res) {
          console.log(res.data);
          $scope.categories = res.data;
        },
        function (res) {
          console.log("分类获取失败");
        }
      );
    };

    $scope.getCategories('');


    if ($routeParams.id != null) {
      $scope.getCategoryInfo();
    }

    $scope.getCategoryInfo = function () {
      categoryService.getCategory(
        {
          categoryId: $routeParams.id
        },
        function (res) {
          var category = res.data;
          $scope.name = category.name;
          $scope.sort = category.sort;
          $scope.image = category.image;
        },
        function (res) {
          alertService.add('error', res.data.success.message);
        }
      );
    };
    
    $scope.addCategory = function (parentId) {
      categoryService.addCategory(
        {
          parentId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.image,
          type: 'ARTICLE'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          $location.path('/category/list');
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

    $scope.updateCategory = function (categoryId) {
      categoryService.updateCategory(
        {
          categoryId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.image,
          type: 'ARTICLE'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          window.history.back();
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

    $scope.deleteCategory = function (id) {
      categoryService.deleteCategory({
          id: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getCategories('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };


  });
