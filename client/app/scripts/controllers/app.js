'use strict';
/**
 * app更新管理控制器
 */
angular.module('clientApp')
  .controller('AppCtrl', function ($scope, $http, alertService, $location, categoryService, apiUrl, $cookieStore, cloudUrl) {

    $scope.selectType = '';
    $scope.curPage = $location.search().currentPage ? $location.search().currentPage : 1;


    $scope.getAppUpdateLogs = function (type) {
      $http({
        method: 'GET',
        url: apiUrl + '/app/logs' + '?pageSize=20&page=' + $scope.curPage,
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
      })
        .then(function (res) {
          console.log(res.data);
          $scope.logs = res.data;
        }, function (res) {
          console.log("管理员数据获取失败");
        });
    };

    $scope.getAppUpdateLogs('');

    $scope.addAppUpdateLog = function () {
      $http({
        method: 'POST',
        url: apiUrl + '/app/logs',
        headers: {'X-AUTH-TOKEN': $cookieStore.get("authToken")},
        data: {
          desc: $scope.desc,
          remark: $scope.remark,
          apkVersionName: $scope.apkVersionName,
          apkVersion: $scope.apkVersion,
          isPublic: 1,
          isPush: 1,
          apkPath: $scope.apkPath
        }
      })
        .then(function (res) {
          alertService.add('success', res.data.success.message);
          $scope.getAppUpdateLogs('');
        }, function (res) {
          alertService.add('success', res.data.error.message);
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
          $scope.initUpload();
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
        max_file_size: '100mb',
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
            $scope.apkPath = res.key;
          },
          'Error': function (up, err, errTip) {
            console.log("上传出错");
            $scope.uploadStatus = '上传出错';
          }
        }
      });
    };
  });
