////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import controllers.secured.ExpertSecured;
import models.*;
import models.enums.ArticlePushState;
import models.enums.ArticleState;
import models.enums.ArticleType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.io.File;
import java.util.UUID;

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
        User user = User.findById(id);
        initPageing();
        return ok(Json.toJson(Album.findAlbumsByUser(user, page, pageSize)));
    }

    /**
     * 专家上传照片
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result uploadImage() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart image = body.getFile("image");

        if (image != null) {

            String fileName = image.getFilename();
            String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            String accessName = UUID.randomUUID().toString() + ext;

            // 判断文件类型
            if (!ext.equals(".gif") && !ext.equals(".jpg") && !ext.equals(".jpeg") && !ext.equals(".bmp") && !ext.equals(".png")) {
                return badRequest(new JsonResult("error", "格式不支持").toJsonResponse());
            } else {
                File file = image.getFile();
                file.renameTo(new File("public/images", accessName));

                // 保存j图片信息到数据库
                Image imageData = new Image();
                imageData.name = accessName;
                imageData.oldName = fileName;
                imageData.src = accessName;
                imageData.save();

                return ok(new JsonResult("success", request().host() + "/assets/images/" + accessName).toJsonResponse());
            }

        } else {
            return badRequest(new JsonResult("error", "没有文件数据").toJsonResponse());
        }
    }

    /**
     * 获取专家动态
     *
     * @param id
     * @return
     */
    public static Result getTrends(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 获取专家信息
     *
     * @param id
     * @return
     */
    public static Result getExpertInformation(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 更新专家信息
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result updateExpertProfile() {
        return play.mvc.Results.TODO;
    }

    /**
     * 专家发布动态
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result addTrend() {
        return play.mvc.Results.TODO;
    }
}
