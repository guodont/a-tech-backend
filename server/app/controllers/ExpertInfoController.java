package controllers;

import controllers.secured.ExpertSecured;
import models.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * @author guodont
 *         <p>
 *         专家控制器
 */
public class ExpertInfoController extends BaseController {


    /**
     * 获取专家相册照片
     *
     * @return
     */
    public static Result getAlbumes(long id) {
        Expert expert = Expert.findExpertById(id);
        if (expert == null)
            return badRequest(new JsonResult("error", "没有此用户").toJsonResponse());
        initPageing();
        return ok(Json.toJson(Album.findAlbumsByUser(expert.user, page, pageSize)));
    }

    /**
     * 专家上传照片
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result uploadImage() {
        Form<AddImageForm> addImageFormForm = Form.form(AddImageForm.class).bindFromRequest();
        if (addImageFormForm.hasErrors()) {
            return badRequest(addImageFormForm.errorsAsJson());
        } else {
            Album album = new Album();
            album.user = getUser();
            album.name = addImageFormForm.get().name;
            album.path = addImageFormForm.get().path;
            album.description = addImageFormForm.get().description;
            album.save();
            return ok(new JsonResult("success", "Image added").toJsonResponse());
        }
    }

    /**
     * 获取专家动态
     *
     * @param id
     * @return
     */
    public static Result getTrends(long id) {
        Expert expert = Expert.findExpertById(id);
        if (expert == null)
            return badRequest(new JsonResult("error", "没有此用户").toJsonResponse());
        return ok(Json.toJson(Trend.findExpertsByUser(expert.user, page, pageSize)));
    }

    /**
     * 获取专家信息
     *
     * @param id
     * @return
     */
    public static Result getExpertInformation(long id) {
        return ok(Json.toJson(Expert.findExpertById(id)));
    }


    /**
     * 专家发布动态
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result addTrend() {

        Form<TrendForm> trendFormForm = Form.form(TrendForm.class).bindFromRequest();
        if (trendFormForm.hasErrors()) {
            return badRequest(trendFormForm.errorsAsJson());
        } else {
            Trend trend = new Trend();
            trend.user = getUser();
            trend.content = trendFormForm.get().content;
            trend.images = trendFormForm.get().images;
            trend.save();
            return ok(new JsonResult("success", "Trend added").toJsonResponse());
        }
    }

    /**
     * 发布动态表单数据
     */
    public static class TrendForm {

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String content;       //  内容

        @Constraints.MaxLength(255)
        public String images;        //  配图

    }

    /**
     * 添加照片数据
     */
    public static class AddImageForm {

        @Constraints.MaxLength(45)
        @Constraints.Required
        public String name;         //  照片名

        @Constraints.MaxLength(255)
        @Constraints.Required
        public String path;         //  存储路径

        @Constraints.MaxLength(255)
        public String description;  //  描述

    }
}
