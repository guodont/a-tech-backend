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
    'ui.bootstrap'
  ])
  .constant('apiUrl', 'http://localhost:9000/api/v1')
  // .constant('apiUrl', 'http://sxnk110.workerhub.cn:9000/api/v1')
  .constant('hostUrl', 'http://localhost:9000')
  // .constant('hostUrl', 'http://sxnk110.workerhub.cn:9000')
  .config(function ($routeProvider) {
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
      .when('/article/list', {
        templateUrl: 'views/article/list.html',
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
      .otherwise({
        redirectTo: '/'
      });
  })
    .run(runBlock);

function runBlock($http) {
  $http.defaults.headers.get = {
    // "Access-Control-Allow-Origin": "*",
    // "Access-Control-Allow-Headers": "X-Requested-With,Content-Type,Accept",
    // "Access-Control-Allow-Methods": "PUT,POST,GET,DELETE,OPTIONS",
    // 'Content-Type': 'application/json;charset=utf-8',
    // 'Accept': 'application/json',
  };
}
