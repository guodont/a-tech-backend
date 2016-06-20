'use strict';
/**
 * 专家信息控制器
 */
angular.module('clientApp')
  .controller('ExpertInfoCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, expertInfoService, categoryService, cloudUrl, apiUrl, $cookieStore) {


    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;
    $scope.imageData = "null";

    $scope.getArticlesByExpert = function () {
      expertInfoService.getArticlesByExpert(
        {
          curPage: $scope.curPage
        },
        function (res) {
          console.log(res.data);
          $scope.articles = res.data;
        },
        function (res) {
          console.log("文章获取失败");
        }
      );
    };

    $scope.getArticlesByExpert();

    $scope.getAlbumsByExpert = function () {
      expertInfoService.getAlbumsByExpert(
        {
          curPage: $scope.curPage
        },
        function (res) {
          console.log(res.data);
          $scope.albums = res.data;
        },
        function (res) {
          console.log("相册获取失败");
        }
      );
    };

    $scope.getAlbumsByExpert();
    
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

  });
