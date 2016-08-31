'use strict';
/**
 * 专家管理控制器
 */
angular.module('clientApp')
  .controller('ExpertCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore, expertService) {

    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;

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
          console.log("专家分类获取失败");
        }
      );
    };

    $scope.getCategories('EXPERT');  // 获取专家分类

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

    $scope.addCategory = function () {
      categoryService.addCategory(
        {
          parentId: $scope.categoryId,
          name: $scope.name,
          sort: $scope.sort,
          image: $scope.imageData,
          type: 'EXPERT'
        },
        function (res) {
          console.log(res.data.success.message);
          alertService.add('success', res.data.success.message);
          // $('.at-add-category').modal('hide'); // 隐藏模态框
          $scope.getCategories('EXPERT');  // 获取专家分类
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

    $scope.getExperts = function () {

      $http({
        method: 'GET',
        url: apiUrl + '/experts' + '?categoryId=&pageSize=20&page=' + $scope.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.experts = res.data;
        }, function (res) {
          console.log("专家数据获取失败");
        });
    };

    $scope.getExperts('');

    $scope.addExpert = function () {
      expertService.addExpert(
        {
          userId: $scope.userId,
          name: $scope.name,
          category: $scope.category,
          professional: $scope.professional,
          duty: $scope.duty,
          introduction: $scope.introduction,
          service: $scope.service,
          company: $scope.company
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $location.path('/expert/list');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'content' || key == 'categoryId') {
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

            $scope.cateImage = sourceLink;
            $scope.avatar = res.key;

            console.log($scope.cateImage);
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

    $scope.toUpdateExpert = function (expert) {
      $scope.curUserid = expert.id;
      $scope.name = expert.user.realName;
      $scope.categoryId = expert.category.id;
      $scope.professional = expert.professional;
      $scope.duty = expert.duty;
      $scope.introduction = expert.introduction;
      $scope.remark = expert.remark;
      $scope.service = expert.service;
      $scope.company = expert.company;
      $scope.avatar = expert.user.avatar;
      $scope.cateImage = 'http://storage.workerhub.cn/' + expert.user.avatar;
    };

    $scope.updateExpert = function () {
      expertService.updateExpert(
        {
          userId: $scope.curUserid,
          name: $scope.name,
          categoryId: $scope.categoryId,
          professional: $scope.professional,
          duty: $scope.duty,
          introduction: $scope.introduction,
          service: $scope.service,
          company: $scope.company,
          avatar: $scope.avatar,
          remark: $scope.remark
        },
        function (res) {
          $scope.subject = '';
          $scope.content = '';
          alertService.add('success', res.data.success.message);
          $scope.getExperts('');
        },
        function (res) {
          console.log(res);
          if (res.status === 400) {
            angular.forEach(res.data, function (value, key) {
              if (key === 'name' || key === 'content' || key == 'categoryId') {
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
