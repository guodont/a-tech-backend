
'use strict';

angular.module('clientApp')
  .service('tradeService', ['$http', '$cookies', 'apiUrl','$cookieStore','ToKenHeader', function ( $http, $cookies, apiUrl,$cookieStore,ToKenHeader) {

    var self = this;

    // 获取交易
    self.getTrade = function (params, success, error) {
      $http({
        method: 'GET',
        url: apiUrl + '/trade/' + params.tradeId,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          if (typeof (success) === 'function') {
            success(res);
          }
        }, function (res) {
          if (typeof (error) === 'function') {
            error(res);
          }
        });
    };

    // 删除交易
    self.deleteTrade = function (params, success, error) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/trade/' + params.id,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          if (typeof (success) === 'function') {
            success(res);
          }
        }, function (res) {
          if (typeof (error) === 'function') {
            error(res);
          }
        });
    };

    return self;
  }]);
