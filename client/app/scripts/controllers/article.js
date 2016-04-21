////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';
/**
 * 文章管理控制器
 */
angular.module('clientApp')
  .controller('ArticleCtrl', function ($scope, $rootScope, $http, alertService, $location, articleService) {

    $scope.getArticles = function () {
      articleService.getArticles(
        {},
        function (res) {
          console.log(res.data);
          $scope.articles = res.data;
        },
        function (res) {
          console.log("文章获取失败");
        }
      );
    };

    $scope.getArticles();

    $scope.addArticle = function () {
      articleService.addArticle(
        {
          title: $scope.title,
          content: $scope.content,
          tag: $scope.tag,
          sort: $scope.sort,
          image: $scope.image,
          categoryId: $scope.categoryId
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $location.path('/article/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'title' || key === 'content' || key == 'categoryId') {
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
