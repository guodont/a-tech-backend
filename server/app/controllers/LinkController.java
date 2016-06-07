package controllers;


import controllers.secured.AdminSecured;
import models.Link;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.util.List;

/**
 * 超链接控制器
 * Created by dev-fzj on 16/6/1.
 */
public class LinkController extends BaseController{

    @Security.Authenticated(AdminSecured.class)
    public static Result addLink(){
        Form<LinkForm> postForm = Form.form(LinkForm.class).bindFromRequest();
        if(postForm.hasErrors()){
            return badRequest(postForm.errorsAsJson());
        }
        else{
//            添加超链接
            Link link =new Link();
            link.name =postForm.get().name;
            link.image =postForm.get().image;
            link.url =postForm.get().url;
            link.save();
            return ok(new JsonResult("success","Link added successfully").toJsonResponse());
        }
    }

    /**
     * 根据id获取link
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getLinkById(Long id){

        Link link =Link.findLinkById(id);
        return ok(Json.toJson(link));
    }

    /**
     * 查询所有link
     */
    public static  Result getAlllinks(){
        initPageing();
        List<Link> links =Link.findAlllinks(page,pageSize);
        return ok(Json.toJson(links));
    }

    /**
     * 更新link
     *
     */
    @Security.Authenticated(AdminSecured.class)
    public static  Result updateLinkById(Long id){
        Form<LinkForm> postForm =Form.form(LinkController.LinkForm.class).bindFromRequest();
        if(postForm.hasErrors()){
            return badRequest(postForm.errorsAsJson());
        }
        else {
            Link link =Link.findLinkById(id);
            link.url =postForm.get().url;
            link.image =postForm.get().image;
            link.name =postForm.get().name;
            link.update();
            return ok(new JsonResult("success","Link updated successfully").toJsonResponse());

        }
    }

    /**
     * 删除link
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteLink(Long id){
        Link link = Link.find.byId(id);
        if(link ==null){
            return status(404, new JsonResult("error", "Link not found").toJsonResponse());
        }
        else {
            link.delete();
            return status(200,new JsonResult("success", "Link deleted successfully").toJsonResponse());
        }
    }

    /**
     * link表单数据
     */
    public static class LinkForm{
        @Constraints.MaxLength(45)
        @Constraints.Required
        public String name;

        @Constraints.MaxLength(45)
        @Constraints.Required
        public String url;

        @Constraints.MaxLength(255)
        public String image;
    }
}
