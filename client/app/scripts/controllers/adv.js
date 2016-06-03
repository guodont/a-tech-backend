'use strict';
/**
 * 广告管理控制器
 */
angular.module('clientApp')
  .controller('AdvCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, advService, categoryService, cloudUrl) {


    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $scope.getAdvs = function () {
      advService.getAdvs(
        {
          curPage : $scope.curPage
        },
        function (res) {
          console.log(res.data);
          $scope.advs = res.data;
        },
        function (res) {
          console.log("广告获取失败");
        }
      );
    };

    $scope.getAdvs();

    $scope.getAdvInfo = function () {
      advService.getAdv(
        {
          advId: $routeParams.id
        },
        function (res) {
          console.log(res.data);
          var adv = res.data;
          $scope.title = adv.title;
          $scope.content = adv.content;
          editor.setValue(adv.content);
          $scope.tag = adv.tag;
          $scope.sort = adv.sort;
          $scope.imageData = adv.image;
          $scope.cateImage = cloudUrl + adv.image;
          $scope.categoryId = adv.category.id;
          // $scope.$apply();
        },
        function (res) {
          alertService.add('error', res.data.success.message);
        }
      );
    };

    if ($routeParams.id != null) {
      $scope.getAdvInfo();
    }

    $scope.addAdv = function () {
      advService.addAdv(
        {
          name: $scope.name,
          description: $scope.description,
          url: $scope.url,
          sort: $scope.sort,
          image: $scope.imageData,
          position: $scope.position
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $location.path('/adv/list');
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

    $scope.updateAdv = function () {
      advService.updateAdv(
        {
          advId: $routeParams.id,
          name: $scope.name,
          description: $scope.description,
          url: $scope.url,
          sort: $scope.sort,
          image: $scope.imageData,
          position: $scope.position
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $location.path('/adv/list');
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

    $scope.deleteAdv = function (id) {
      advService.deleteAdv({
          id: id
        },
        function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getAdvs();
        }, function (res) {
          alertService.add('success', res.data.error.message);
        });
    };

    $scope.images = [];


    var uploader = Qiniu.uploader({
      runtimes: 'html5,flash,html4',      // 上传模式,依次退化
      browse_button: 'pickfiles',         // 上传选择的点选按钮，**必需**
      // 在初始化时，uptoken, uptoken_url, uptoken_func 三个参数中必须有一个被设置
      // 切如果提供了多个，其优先级为 uptoken > uptoken_url > uptoken_func
      // 其中 uptoken 是直接提供上传凭证，uptoken_url 是提供了获取上传凭证的地址，如果需要定制获取 uptoken 的过程则可以设置 uptoken_func
      uptoken: 'K8eqr1qikcsa2OXUkg_gMxIX16cCPR9U8yULRKDr:uOVzmGKrnT9yDhX3AYrjcpTbies=:eyJzY29wZSI6Im5rMTEwLWltYWdlcyIsImRlYWRsaW5lIjoxNDY0NzgwMjY2LCJzYXZlS2V5IjoicWluaXVfY2xvdWRfc3RvcmFnZV8xNDY0Nzc2NjY2IiwibWltZUxpbWl0IjoiaW1hZ2VcLyoifQ==', // uptoken 是上传凭证，由其他程序生成
      // TODO
      // uptoken_url: 'http://cloud.workerhub.cn//api/quick_start/simple_image_example_token.php',         // Ajax 请求 uptoken 的 Url，**强烈建议设置**（服务端提供）
      // uptoken_func: function(file){    // 在需要获取 uptoken 时，该方法会被调用
      //    do something
      // return uptoken;
      // },
      get_new_uptoken: false,             // 设置上传文件的时候是否每次都重新获取新的 uptoken
      // downtoken_url: '/downtoken',
      // Ajax请求downToken的Url，私有空间时使用,JS-SDK 将向该地址POST文件的key和domain,服务端返回的JSON必须包含`url`字段，`url`值为该文件的下载地址
      unique_names: true,              // 默认 false，key 为文件名。若开启该选项，JS-SDK 会为每个文件自动生成key（文件名）
      save_key: false,                  // 默认 false。若在服务端生成 uptoken 的上传策略中指定了 `sava_key`，则开启，SDK在前端将不对key进行任何处理
      domain: 'http://storage.workerhub.cn/',     // bucket 域名，下载资源时用到，**必需**
      container: 'upload-container',             // 上传区域 DOM ID，默认是 browser_button 的父元素，
      max_file_size: '100mb',             // 最大文件体积限制
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
          console.log("上传中" + up);
        },
        'FileUploaded': function (up, file, info) {
          console.log(info);
          var domain = up.getOption('domain');
          var res = JSON.parse(info);
          var sourceLink = domain + res.key; //获取上传成功后的文件的Url

          $scope.images.push(sourceLink);

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
  });
