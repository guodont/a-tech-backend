'use strict';
/**
 * 广告管理控制器
 */
angular.module('clientApp')
  .controller('AdvCtrl', function ($scope, $routeParams, $cookieStore, $rootScope, $http, alertService, $location, advService, categoryService, cloudUrl, apiUrl) {


    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    $('.ui.dropdown')
      .dropdown({
        onChange: function (value, text, $selectedItem) {
          console.log(value);
          $('#position').attr("value", value);
          $scope.position = value;
        }
      })
    ;
    $scope.getAdvs = function () {
      advService.getAdvs(
        {
          curPage: $scope.curPage
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
          $scope.name = adv.name;
          $scope.description = adv.description;
          editor.setValue(adv.description);
          $scope.url = adv.url;
          $scope.image = adv.image;
          $scope.position = adv.position;
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

    $scope.getUploadToken = function () {
      $http({
        method: 'GET',
        url: apiUrl + '/admin/uploadToken',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")}
      })
        .then(function (res) {
          $scope.uploadToken = res.data.success.message;
          console.log($scope.uploadToken);
          $scope.initUpload();
        }, function (res) {
          console.log("uploadToken获取失败");
        });
    };

    $scope.getUploadToken();

    $scope.initUpload = function () {
      var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',
        browse_button: 'pickfiles',
        uptoken: $scope.uploadToken,
        get_new_uptoken: false,
        unique_names: true,
        save_key: false,
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

            console.log(up);
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
    };

  });
