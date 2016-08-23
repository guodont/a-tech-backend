/**
 * Created by fanqifeng on 2016/3/16.
 */

angular.module('clientApp')
  .service('deskNotice', ['$location', function ($location) {
    var self = this;

    self.showDeskNotice = function (title, content, tag) {//桌面提醒

      // iconUrl = '/assets/images/deskFace.png';


      // 老版本Chrome、Safari浏览器显示通知
      function showNotificationForOldChrome() {
        var notif = window.webkitNotifications.createNotification(title, content);
        notif.display = function () {
          setTimeout(function () {
            this.close();
          }, 3000)
        }
        notif.onerror = function () {

        }
        notif.onclose = function () {

        }
        notif.onclick = function () {
          window.focus();
          this.cancel();
        }
        notif.replaceId = 'Meteoric';
        notif.show();
      }

      if (window.webkitNotifications) {   // edited by Don 处理Safari浏览器通知问题
        //chrome老版本
        if (window.webkitNotifications.checkPermission() == 0) {
          showNotificationForOldChrome();
        } else {
          window.webkitNotifications.requestPermission(
            function (permission) {
              if (!('permission' in Notification)) {
                Notification.permission = permission;
              }
              //如果接受请求
              if (permission === "granted") {
                showNotificationForOldChrome();
              }
            }
          );
        }
      }
      else if ("Notification" in window) {
        var content = content.trim();
        var notification;
        // 判断是否有权限
        if (Notification.permission === "granted") {
          notification = new Notification(title, {
            // "icon": iconUrl,
            "body": content.trim(),
            "tag": tag
          });
          notification.onshow = function () {
            setTimeout(function () {
              notification.close();
            }, 3000);
          }
        }
        //如果没权限，则请求权限
        else if (Notification.permission !== 'denied') {
          Notification.requestPermission(function (permission) {
            // Whatever the user answers, we make sure we store the
            // information
            if (!('permission' in Notification)) {
              Notification.permission = permission;
            }
            //如果接受请求
            if (permission === "granted") {
              notification = new Notification(title, {
                // "icon": iconUrl,
                "body": content.trim(),
                "tag": tag
              });
              notification.onshow = function () {
                setTimeout(function () {
                  notification.close();
                }, 3000);
              }
            }
          });
        }

        notification.addEventListener('error', function (errorMsg) {
          console.log('something went wrong');
          console.log(errorMsg);
        });

        notification.addEventListener('click', notification_clicked);
      }
    };


    // 删除通知
    function delete_notification(evt) {
      var notifications_promise;
      if (evt.target) {
        // from click event
        notifications_promise = Promise.resolve([evt.target]);
      } else {
        // from system message
        notifications_promise = Notification.get({tag: evt.tag});
      }
      notifications_promise.then(function (notifications) {
        for (var i = 0; i < notifications.length; i++) {
          notifications[i].close();
        }
      });
    }

    // 点击通知
    function notification_clicked(evt) {
      window.focus();
      delete_notification(evt);
    }

    return self;
  }]);
