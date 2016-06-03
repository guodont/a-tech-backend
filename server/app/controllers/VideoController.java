package controllers;

import controllers.secured.AdminSecured;
import models.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Security;
import utils.JsonResult;
import play.mvc.Result;
import java.util.List;

/**
 * @author llz
 *         视频控制器
 */
public class VideoController extends BaseController {
    /**
     * 管理员添加视频
     * @return Result
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result addVideo() {

        Form<VideoForm> postForm = Form.form(VideoForm.class).bindFromRequest();

        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {
            Category category = Category.find.byId(postForm.get().categoryId);
            //  保存视频
            Video video = new Video();

            video.name = postForm.get().name;
            video.description = postForm.get().description;
            video.path = postForm.get().path;
            video.admin = getAdmin();
            video.category = category;
            video.clickCount = 0L;
            video.save();
            return ok(new JsonResult("success", "Video added successfully").toJsonResponse());

        }
    }

    /**
     * 删除视频
     * @param
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteVideo(long videoId) {
        Video video = Video.findVideoById(videoId);
        if(video==null){
           return status(404, new JsonResult("error", "Video not found").toJsonResponse());
        }else{
            video.delete();
            return ok(new JsonResult("success", "Video deleted successfully").toJsonResponse());
        }
    }

    /**
     * 更新视频
     * @param videoId
     * @return Result
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result updateVideo(long videoId) {
        Form<VideoForm> postForm = Form.form(VideoForm.class).bindFromRequest();

        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {
            Category category = Category.find.byId(postForm.get().categoryId);
            //  更新视频
            Video video =Video.findVideoById(videoId);

            video.name = postForm.get().name;
            video.description = postForm.get().description;
            video.path = postForm.get().path;
            video.admin = getAdmin();
            video.category = category;
            video.save();
            return ok(new JsonResult("success", "Video updated successfully").toJsonResponse());
        }
    }

    /**
     * 获取所有视频
     * @return Video
     */
    public static Result getVideos() {
        initPageing();
        List<Video> videos = Video.findVideos(page, pageSize);
        return ok(Json.toJson(videos));
    }

    /**
     * 根据ID获取视频
     * @param id
     * @return video
     */
    public static Result getVideoById(Long id) {
        Video video = Video.findVideoById(id);
        return ok(Json.toJson(video));
    }

    /**
     * 添加视频表单数据
     */
    public static class VideoForm {

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String name;           //  视频名称

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String description;      // 视频描述

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String path;          //  路径

        @Constraints.Required
        public Long categoryId;     //  分类id

    }
}

