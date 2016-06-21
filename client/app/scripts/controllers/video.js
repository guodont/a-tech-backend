////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

'use strict';
/**
 * 视频管理控制器
 */
angular.module('clientApp')
  .controller('VideoCtrl', function ($scope, $routeParams, $rootScope, $http, alertService, $location, videoService, categoryService, cloudUrl, apiUrl, $cookieStore) {


    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

    // var editor = new Simditor({
    //   textarea: $('#editor')
    //   //optional options
    // });

    $scope.getVideos = function () {
      videoService.getVideos(
        {
          curPage: $scope.curPage
        },
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

    $scope.getVideoInfo = function () {
      videoService.getVideo(
        {
          videoId: $routeParams.id
        },
        function (res) {
          console.log(res.data);
          var video = res.data;
          $scope.name = video.name;
          $scope.description = video.description;
          editor.setValue(video.description);
          $scope.path = video.path;
          $scope.categoryId = video.category.id;
          // $scope.$apply();
        },
        function (res) {
          alertService.add('error', res.data.success.message);
        }
      );
    };

    if ($routeParams.id != null) {
      $scope.getVideoInfo();
    }

    $scope.addVideo = function () {
      videoService.addVideo(
        {
          name: $scope.name,
          // content: $scope.content,
          description: editor.getValue(),
          path: $scope.path,
          categoryId: $scope.categoryId,
          thumbnail: $scope.imageData
        },
        function (res) {
          $scope.subject = '';
          $scope.description = '';
          alertService.add('success', res.data.success.message);
          $location.path('/video/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'description' || key == 'categoryId') {
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

    $scope.updateVideo = function () {
      videoService.updateVideo(
        {
          videoId: $routeParams.id,
          name: $scope.name,
          description: editor.getValue(),
          path: $scope.path,
          categoryId: $scope.categoryId,
          thumbnail: $scope.imageData
        },
        function (res) {
          $scope.subject = '';
          $scope.description = '';
          alertService.add('success', res.data.success.message);
          $location.path('/video/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'description' || key == 'categoryId') {
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

    $scope.getCategories('VIDEO');  // 获取文章分类

    $scope.addCategory = function () {
      categoryService.addCategory(
        {
          parentId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.imageData,
          type: 'VIDEO'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          // $('.at-add-category').modal('hide'); // 隐藏模态框
          $scope.getCategories('VIDEO');  // 获取视频分类
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
          $scope.initUploadImage();
        }, function (res) {
          console.log("uploadToken获取失败");
        });
    };

    $scope.getUploadToken();

    $scope.uploadStatus = '等待选择文件';

    $scope.initUpload = function () {
      var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',
        browse_button: 'pickfiles',
        uptoken: $scope.uploadToken,
        get_new_uptoken: false,
        unique_names: true,
        save_key: false,
        domain: cloudUrl,
        container: 'upload-container',
        max_file_size: '4096mb',
        flash_swf_url: 'path/of/plupload/Moxie.swf',
        max_retries: 3,
        dragdrop: true,
        drop_element: 'container',
        chunk_size: '5mb',
        auto_start: true,
        init: {
          'FilesAdded': function (up, files) {
            console.log("文件添加");
            $scope.uploadStatus = '文件添加';
          },
          'BeforeUpload': function (up, file) {
            console.log("准备上传");
            $scope.uploadStatus = '准备上传';
          },
          'UploadProgress': function (up, file) {
            // progress.setProgress(file.percent + "%", file.speed, chunk_size);
            console.log(file.percent + "%");
            console.log(file.speed);
            var filePercent = file.percent + "%";
            $scope.uploadStatus = '上传进度:' + filePercent + ' 速度:' + plupload.formatSize(file.speed).toUpperCase() + '/s';
            $scope.$apply();
            $('#example2').progress({
              percent: file.percent
            });
          },
          'UploadComplete': function () {
            console.log("上传成功");
            $scope.uploadStatus = '上传成功';
          },
          'FileUploaded': function (up, file, info) {
            $scope.uploadStatus = '上传完毕';
            console.log("上传完毕");
            var domain = up.getOption('domain');
            var res = JSON.parse(info);
            var sourceLink = domain + res.key; //获取上传成功后的文件的Url
            $scope.sourceLink = sourceLink;
            $scope.path = res.key;
          },
          'Error': function (up, err, errTip) {
            console.log("上传出错");
            $scope.uploadStatus = '上传出错';
          }
        }
      });
    };


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
