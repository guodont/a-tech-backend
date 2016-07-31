
'use strict';
/**
 * 交易管理控制器
 */
angular.module('clientApp')
  .controller('TradeCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, questionService,$cookieStore, apiUrl, categoryService) {

    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $scope.tradeType = $location.search().tradeType ? $location.search().tradeType : '';

    $scope.status = $location.search().status ? $location.search().status : '';

    $scope.getTrades = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/foradmin/trades' + '?pageSize=20&page=' + $scope.curPage + '&tradeType=' + $scope.tradeType + '&status=' + $scope.status,
        data: {
          // categoryType: params.type
        },
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.trades = res.data;
        }, function (res) {
          console.log("交易数据获取失败");
        });
    };

    $scope.getTrades();

    $scope.getTradeInfo = function () {
      questionService.getTrade(
        {
          questionId: $routeParams.id
        },
        function (res) {
          console.log(res.data);
          var question = res.data;
          $scope.title = question.title;
          $scope.content = question.content;
          $scope.tag = question.tag;
          $scope.sort = question.sort;
          $scope.image = question.image;
          $scope.categoryId = question.category.id;
        },
        function (res) {
          alertService.add('error', res.data.success.message);
        }
      );
    };

    if ($routeParams.id != null) {
      $scope.getTradeInfo();
    }

    $scope.deleteTrade = function (trade) {
      $http({
        method: 'DELETE',
        url: apiUrl + '/trade/' + trade.id,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getTrades();
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };


    // 审核交易
    $scope.auditTrade = function (trade) {
      $scope.curTrade = trade;
      console.log("审核交易");
      // $('.at-audit-trade').modal('show');
      // $('.modal-backdrop').show();
    };

    // 拒绝审核
    $scope.auditWithRefuse = function (trade) {
      // TODO
      console.log("拒绝审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/trade/' + trade.id + '/auditfail',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getTrades();
          $('.at-audit-trade').modal('hide');
          $('.modal-backdrop').hide();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };

    // 通过审核
    $scope.auditWithPass = function (trade) {
      // TODO
      console.log("通过审核");
      $http({
        method: 'PUT',
        url: apiUrl + '/trade/' + trade.id + '/auditpass',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          console.log(res.data);
          alertService.add('success', res.data.success.message);
          $scope.getTrades();
          $('.at-audit-trade').modal('hide');
          $('.modal-backdrop').hide();
        }, function (res) {
          console.log(res.data);
          alertService.add('error', res.data.success.message);
        });
    };
    $scope.getUploadToken = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/admin/uploadToken',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          $scope.uploadToken = res.data.success.message;
          console.log($scope.uploadToken);
          $scope.initUploadImage();
        }, function (res) {
          console.log("uploadToken获取失败");
        });
    };

    $scope.getUploadToken();

    $scope.initUploadImage = function () {
      var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',
        browse_button: 'pickImages',
        uptoken: $scope.uploadToken,
        get_new_uptoken: false,
        unique_names: true,
        save_key: false,
        domain: 'http://storage.workerhub.cn/',     // bucket 域名，下载资源时用到，**必需**
        container: 'upload-image-container',             // 上传区域 DOM ID，默认是 browser_button 的父元素，
        max_file_size: '3mb',             // 最大文件体积限制
        flash_swf_url: 'path/of/plupload/Moxie.swf',  //引入 flash,相对路径
        max_retries: 3,                     // 上传失败最大重试次数
        dragdrop: true,                     // 开启可拖曳上传
        drop_element: 'container',          // 拖曳上传区域元素的 ID，拖曳文件或文件夹后可触发上传
        chunk_size: '4mb',                  // 分块上传时，每块的体积
        auto_start: true,                   // 选择文件后自动上传，若关闭需要自己绑定事件触发上传,
        init: {
          'FilesAdded': function (up, files) {
            plupload.each(files, function (file) {
              // 文件添加进队列后,处理相关的事情
            });
          },
          'BeforeUpload': function (up, file) {
            // 每个文件上传前,处理相关的事情
            console.log("每个文件上传前,处理相关的事情");
          },
          'UploadProgress': function (up, file) {
            // 每个文件上传时,处理相关的事情

            console.log(up);
          },
          'FileUploaded': function (up, file, info) {
            console.log(info);
            var domain = up.getOption('domain');
            var res = JSON.parse(info);
            var sourceLink = domain + res.key; //获取上传成功后的文件的Url

            $scope.cateImage = sourceLink;
            $scope.imageData = res.key;

            console.log($scope.images);
            $scope.$apply();

          },
          'Error': function (up, err, errTip) {
            //上传出错时,处理相关的事情
            console.log(errTip);
          },
          'UploadComplete': function () {
            //队列文件处理完毕后,处理相关的事情
          },
          'Key': function (up, file) {
            // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
            // 该配置必须要在 unique_names: false , save_key: false 时才生效
            var key = "";
            // do something with key here
            return key
          }
        }
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
          console.log("视频分类获取失败");
        }
      );
    };

    $scope.getCategories('TRADE');  // 获取文章分类

    $scope.addCategory = function () {
      categoryService.addCategory(
        {
          parentId: 0,
          name: $scope.name,
          sort: $scope.sort,
          // image: $scope.imageData,
          type: 'TRADE'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          // $('.at-add-category').modal('hide'); // 隐藏模态框
          $scope.getCategories('TRADE');  // 获取视频分类
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
  });
