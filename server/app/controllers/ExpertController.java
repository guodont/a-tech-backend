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
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * @author guodont
 *         <p>
 *         专家控制器
 */
public class ExpertController extends BaseController {


    /**
     * 获取专家相册照片
     * @return
     */
    public static Result getAlbumes(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 专家上传照片
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result uploadImage() {
        return play.mvc.Results.TODO;
    }

    /**
     * 获取专家动态
     * @param id
     * @return
     */
    public static Result getTrends(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 获取专家信息
     * @param id
     * @return
     */
    public static Result getExpertInformation(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 更新专家信息
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result updateExpertProfile() {
        return play.mvc.Results.TODO;
    }

    /**
     * 专家发布动态
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result addTrend() {
        return play.mvc.Results.TODO;
    }
}
