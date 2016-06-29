package controllers;

import controllers.secured.AdminSecured;
import models.AppUpdateLog;
import models.Category;
import models.Message;
import models.Video;
import models.enums.AppType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.util.List;

/**
 * app更新信息控制器
 */
public class AppUpdateController extends BaseController {

    /**
     * 发布新版app
     * @return Result
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result addNewVersionApp() {

        Form<AppUpdateForm> postForm = Form.form(AppUpdateForm.class).bindFromRequest();

        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {

            AppUpdateLog appUpdateLog = new AppUpdateLog();

            appUpdateLog.description = postForm.get().desc;
            appUpdateLog.apkPath = postForm.get().apkPath;
            appUpdateLog.appType = AppType.ANDROID;
            appUpdateLog.apkVersion = Integer.valueOf(postForm.get().apkVersion);
            appUpdateLog.apkVersionName = postForm.get().apkVersionName;
            appUpdateLog.remark = postForm.get().remark;

            if (postForm.get().isPublic == "1") {
                appUpdateLog.isPublic = true;
            } else {
                appUpdateLog.isPublic = false;
            }

            if (postForm.get().isPush == "1") {
                appUpdateLog.isPush = true;
                // TODO 推送
            } else {
                appUpdateLog.isPush = false;
            }

            appUpdateLog.admin = getAdmin();
            appUpdateLog.save();

            return ok(new JsonResult("success", "AppUpdateLog added successfully").toJsonResponse());

        }
    }

    /**
     * 获取更新日志
     *
     * @return
     */
    public static Result getAppUpdateLogs() {

        List<AppUpdateLog> appUpdateLogs = null;

        initPageing();

        appUpdateLogs = AppUpdateLog.getAppUpdateLogs(page, pageSize);

        return ok(Json.toJson(appUpdateLogs));
    }


    /**
     * 获取android最新版本信息
     * @return
     */
    public static Result getLatestVersionForAndroid() {
        return ok(Json.toJson(AppUpdateLog.findLatestLogForAndroid()));
    }

    /**
     * 获取android最新版本信息2
     * @return
     */
    public static Result getLatestVersionForAndroid2() {
        AppUpdateLog appUpdateLog = AppUpdateLog.findLatestLogForAndroid();
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.url = "http://storage.workerhub.cn/" + appUpdateLog.apkPath;
        updateInfo.updateMessage = appUpdateLog.description;
        updateInfo.versionCode = appUpdateLog.apkVersion;
        return ok(Json.toJson(updateInfo));
    }

    public static class UpdateInfo {
        public String url;
        public int versionCode;
        public String updateMessage;
    }

    /**
     * 发布新版app表单数据
     */
    public static class AppUpdateForm {

        @Constraints.Required
        public String desc;           //  更新内容

        @Constraints.MaxLength(255)
        public String remark;         // 备注

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String apkPath;        // 路径

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String apkVersionName; // 版本名称

        @Constraints.MaxLength(8)
        @Constraints.Required
        public String apkVersion;  // 版本号

        public String isPush;      // 推送标记

        public String isPublic;    // 发布标记


    }
}
