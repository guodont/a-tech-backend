////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';

/**
 * @ngdoc overview
 * @name clientApp
 * @description
 * # clientApp
 *
 * Main module of the application.
 */
angular
  .module('clientApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'ui.router'
  ])
  // .constant('apiUrl', 'http://localhost:9000/api/v1')
  .constant('apiUrl', 'http://sxnk110.workerhub.cn:9000/api/v1')
  // .constant('hostUrl', 'http://localhost:9000')
  .constant('hostUrl', 'http://sxnk110.workerhub.cn:9000')
  .constant('cloudUrl', 'http://storage.workerhub.cn/')
  .constant('ToKenHeader', 'X-AUTH-TOKEN')
  .config(function ($routeProvider, $stateProvider, $locationProvider) {
    // $stateProvider
    //   .state('home', {
    //     url: '/',
    //     views: {
    //       'header': {
    //         templateUrl: 'include/head.html'
    //       },
    //       'content': {
    //         templateUrl: 'views/welcome.html',
    //         controller: 'MainCtrl'
    //       },
    //       'bottom': {
    //         templateUrl: 'include/foot.html'
    //       }
    //     }
    //   }).state('article', {
    //     url: '/article/list',
    //     views: {
    //       'header': {
    //         templateUrl: 'include/head.html'
    //       },
    //       'content': {
    //         templateUrl: 'views/article/list.html',
    //         controller: 'ArticleCtrl'
    //       },
    //       'bottom': {
    //         templateUrl: 'include/foot.html'
    //       }
    //     }
    //   })
    //   .state('login', {
    //     url: '/login',
    //     views: {
    //       'header': {
    //         template: '<h1 style="margin: 45px;">农科110后台管理系统</h1>'
    //       },
    //       'content': {
    //         templateUrl: 'views/login.html',
    //         controller: 'LoginCtrl'
    //       },
    //       'bottom': {
    //         template: '<p style="margin: 45px;">山西科技报刊总社版权所有 晋ICP备08000801号</p>'
    //       }
    //     }
    //   });
    $routeProvider
      .when('/', {
        templateUrl: 'views/welcome.html',
        controller: 'MainCtrl'
      })
      .when('/signup', {
        templateUrl: 'views/signup.html',
        controller: 'SignupCtrl'
      })
      .when('/dashboard', {
        templateUrl: 'views/dashboard.html',
        controller: 'DashboardCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .when('/addpost', {
        templateUrl: 'views/addpost.html',
        controller: 'AddpostCtrl'
      })
      .when('/viewpost/:postId', {
        templateUrl: 'views/viewpost.html',
        controller: 'ViewpostCtrl'
      })
      .when('/article/add', {
        templateUrl: 'views/article/add.html',
        controller: 'ArticleCtrl'
      })
      .when('/article/:id/update', {
        templateUrl: 'views/article/update.html',
        controller: 'ArticleCtrl'
      })
      .when('/article/list', {
        templateUrl: 'views/article/list.html',
        controller: 'ArticleCtrl'
      })
      .when('/article/categories', {
        templateUrl: 'views/article/categories.html',
        controller: 'ArticleCtrl'
      })
      .when('/category/add', {
        templateUrl: 'views/category/add.html',
        controller: 'CategoryCtrl'
      })
      .when('/category/list', {
        templateUrl: 'views/category/list.html',
        controller: 'CategoryCtrl'
      })
      .when('/video/add', {
        templateUrl: 'views/video/add.html',
        controller: 'VideoCtrl'
      })
      .when('/video/list', {
        templateUrl: 'views/video/list.html',
        controller: 'VideoCtrl'
      })
      .when('/video/categories', {
        templateUrl: 'views/video/categories.html',
        controller: 'VideoCtrl'
      })
      .when('/question/list', {
        templateUrl: 'views/question/list.html',
        controller: 'QuestionCtrl'
      })
      .when('/user/list', {
        templateUrl: 'views/user/list.html',
        controller: 'UserCtrl'
      })
      .when('/trade/list', {
        templateUrl: 'views/trade/list.html',
        controller: 'TradeCtrl'
      })
      .when('/adv/add', {
        templateUrl: 'views/adv/add.html',
        controller: 'AdvCtrl'
      })
      .when('/adv/:id/update', {
        templateUrl: 'views/adv/update.html',
        controller: 'AdvCtrl'
      })
      .when('/adv/list', {
        templateUrl: 'views/adv/list.html',
        controller: 'AdvCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  })
  .directive('observe', function () {
    return {
      restrice: 'EA',
      controller: function ($scope, pagerConfig, $location) {
        $scope.currentPage = $location.search().currentPage ? parseInt($location.search().currentPage) : 1;

        $scope.selectPage = function (index) {
          $location.search('currentPage', index);
        };
        $scope.getCurPage = function () {
          return $scope.currentPage;
        };
        $scope.next = function () {
          if ($scope.isLast()) {
            return;
          }
          $scope.selectPage($scope.currentPage + 1);
        };
        $scope.provie = function () {
          if ($scope.isFirst()) return
          $scope.selectPage($scope.currentPage - 1);
        }
        $scope.first = function () {
          $scope.selectPage(1);
        }
        $scope.last = function () {
          $scope.selectPage($scope.totalPages - 1);
        }
        $scope.isFirst = function () {
          return $scope.currentPage <= 1;
        };
        $scope.isLast = function () {
          return $scope.currentPage >= $scope.totalPages - 1;
        }
        $scope.getText = function (key) {
          return pagerConfig.text[key];
        };


      },
      link: function (scope, ele, attrs) {

        scope.itemsPerpage = attrs.itemsperpage || 1;
        scope.listSizes = attrs.listsizes;
        attrs.$observe('totalitems', function (val) {
          scope.totalItems = val;
        })
      },
      templateUrl: '../include/page.html'
    }
  }).constant('pagerConfig', {
    text: {
      'first': '首页',
      'provie': '上一页',
      'next': '下一页',
      'last': '尾页',
    }
  })
  .run(runBlock);

function runBlock($http) {
  $http.defaults.headers.get = {
    // "Access-Control-Allow-Origin": "*",
    // "Access-Control-Allow-Headers": "X-Requested-With,Content-Type,Accept",
    // "Access-Control-Allow-Methods": "PUT,POST,GET,DELETE,OPTIONS",
    'Content-Type': 'application/json;charset=utf-8',
    'Accept': 'application/json'
  };
  $http.defaults.headers.post = {
    'Content-Type': 'application/json;charset=utf-8',
    'Accept': 'application/json'
  };
  $http.defaults.headers.put = {
    'Content-Type': 'application/json;charset=utf-8',
    'Accept': 'application/json'
  };
}
