/**
 * Created by j on 2016/6/2.
 */
'use strict';
angular.module('clientApp')
  .controller('LinkCtrl', function ($scope, $rootScope, $http, alertService, $location, linkService) {

    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $scope.getLinks = function () {
      linkService.getLinks(
        {
          curPage: $scope.curPage
        },
        function (res) {
          console.log(res.data);
          $scope.links = res.data;
        },
        function (res) {
          console.log("链接获取失败");
        }
      );
    };
    $scope.getLinks();

    $scope.deleteLink = function (id) {
      linkService.deleteLink({
          linkId: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getLinks();
        }, function (res) {
          alertService.add('success', res.data.error.message);

        });
    };

    $scope.addLink = function () {
      linkService.addLink(
        {
          name: $scope.name,
          url: $scope.url,
          image: $scope.image,
        },
        function (res) {
          $scope.name = '';
          $scope.url = '';
          $scope.image = '';
          alertService.add('success', res.data.success.message);
          $scope.getLinks();
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'url' || key == 'image') {
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
