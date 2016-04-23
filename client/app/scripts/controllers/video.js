

'use strict';
/**
 * @author llz
 *视频管理控制器
 */
angular.module('clientApp')
  .controller('VideoCtrl', function ($scope, $rootScope, $http, alertService, $location, videoService) {

    $scope.getVideos = function () {
      videoService.getVideos(
        {},
        function (res) {
          console.log(res.data);
          $scope.videos = res.data;
        },
        function (res) {
          console.log("视频获取失败");
        }
      );
    };

    $scope.getVideos();

    $scope.deleteVideo = function (id) {
      videoService.deleteVideo({
          videoId: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getVideos();
        }, function (res) {
          alertService.add('success', res.data.error.message);

        });
    };

    $scope.addVideo = function () {
      videoService.addVideo(
        {
          name: $scope.name,
          description: $scope.description,
          path: $scope.path,
          categoryId: $scope.categoryId
        },
        function (res) {
          $scope.name = '';
          $scope.description = '';
          $scope.path = '';
          $scope.categoryId = '';
          alertService.add('success', res.data.success.message);
          $location.path('/video/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'description' || key == 'categoryId' || key === 'path') {
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
