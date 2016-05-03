package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import controllers.secured.AdminSecured;
import controllers.secured.Secured;

import models.Article;
import models.Category;
import models.Link;

import models.enums.ArticlePushState;
import models.enums.ArticleState;
import models.enums.ArticleType;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;
import utils.JsonResult;


import java.sql.Timestamp;

import java.util.List;

import static play.libs.Json.*;

/**
 * Created by j on 2016/4/22.
 */
public class LinkController extends BaseController{

    /**
     * 获取所有link
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getAllLink(){
        List<Link> link = new Link.Finder<Long , Link>(Long.class, Link.class).all();
        return Results.ok(toJson(link));
    }

    /**
     * 通过id获取link
     * @param id
     * @return
     */
    public static Result getVideoById(Long id) {
        Link link = Link.findById(id);
        return ok(Json.toJson(link));
    }
    /**
     * 管理员添加link
     * @param linkId
     * @return
     */

    @Security.Authenticated(AdminSecured.class)
    public static Result addLink(Long linkId) {
        Form<LinkForm> postForm = Form.form(LinkForm.class).bindFromRequest();
        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {
            //  保存超链接
            Link link = new Link();
            link.name = postForm.get().name;
            link.url = postForm.get().url;
            link.image = postForm.get().image;
            link.setVersion(postForm.get().version);
            link.save();
        }
        return ok(new JsonResult("success", "Link added successfully").toJsonResponse());

    }

    /**
     * 更新视频
     * @param linkId
     * @return Result
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result updateLink(long linkId) {
        Form<LinkController.LinkForm> postForm = Form.form(LinkController.LinkForm.class).bindFromRequest();

        if (postForm.hasErrors()) {

            return badRequest(postForm.errorsAsJson());

        } else {

            //  更新视频
            Link link = new Link();

            link.name = postForm.get().name;
            link.url = postForm.get().url;
            link.image = postForm.get().image;
            link.setVersion(postForm.get().version);
            link.update();
            return ok(new JsonResult("success", "Video updated successfully").toJsonResponse());
        }
    }



    /**
     * 删除link
     * @param linkId
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteLink(Long linkId)
    {
            Link link =Link.findById(linkId);
        if(link==null){
            return status(404, new JsonResult("error", "Link not found").toJsonResponse());
        }else{
            link.delete();
            return ok(new JsonResult("success", "Link deleted successfully").toJsonResponse());
        }
    }

    /**
     * 链接的表单数据
     */
    public class LinkForm {
        @Constraints.Required
        public Long id;//链接id

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String name;//链接名称

        @Constraints.Required
        public String url;//链接地址

        @Constraints.Required
        public String image;//链接图片

        public Long version;//版本

    }
}
