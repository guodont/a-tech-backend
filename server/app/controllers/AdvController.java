package controllers;

import controllers.secured.AdminSecured;

import models.Advertisement;
import models.enums.Position;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import java.util.List;

/**
 * 广告控制器
 * Created by guodont on 16/5/31.
 */
public class AdvController extends BaseController{

    /**
     * 管理员添加广告
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result addAdv() {
        Form<AdvForm> postForm = Form.form(AdvForm.class).bindFromRequest();
        if(postForm.hasErrors()){
            return badRequest(postForm.errorsAsJson());
        }
        else {
//            添加广告
            Advertisement adv =new Advertisement();
            adv.name =postForm.get().name;
            adv.description =postForm.get().description;
            adv.url =postForm.get().url;
            adv.image =postForm.get().image;
            adv.position =postForm.get().position;
            adv.save();
            return ok(new JsonResult("success","Advertisement added successfully").toJsonResponse());
        }
    }

    /**
     * 根据id获取广告
     * @param id
     * @return
     */
    public static Result getAdvById(long id) {

        Advertisement adv =Advertisement.findAdvById(id);
        return ok(Json.toJson(adv));
    }

    /**
     * 获取广告
     * @return
     */
    public static Result getAdvs() {
        // TODO 根据位置获取广告

        String position =Position.AMONG.getName();
        Advertisement adv =new Advertisement();

        //判断广告位置

        if(position.equals(Position.AMONG.getName())){
            List<Advertisement> advertisementsByPos=Advertisement.findAdvertisementsByPos(Position.AMONG.getName());
            return(ok(Json.toJson(advertisementsByPos)));
        }
        else if(position.equals(Position.FLOAT.getName())){
            List<Advertisement> advertisementsByPos=Advertisement.findAdvertisementsByPos(Position.FLOAT.getName());
            return(ok(Json.toJson(advertisementsByPos)));
        }
        else if (position.equals(Position.TOP.getName())){
            List<Advertisement> advertisementsByPos=Advertisement.findAdvertisementsByPos(Position.TOP.getName());
            return(ok(Json.toJson(advertisementsByPos)));
        }



        //  如果不传位置参数则默认显示所有广告
        initPageing();
        List<Advertisement> advs =Advertisement.findAlladvs(page,pageSize);
        return ok(Json.toJson(advs));
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result updateAdv(long id) {

        Form<AdvForm> postForm =Form.form(AdvController.AdvForm.class).bindFromRequest();
        if(postForm.hasErrors()){
            return badRequest(postForm.errorsAsJson());
        }
        else
        {
            //更新广告
            Advertisement adv =Advertisement.findAdvById(id);
            adv.name =postForm.get().name;
            adv.description =postForm.get().description;
            adv.url =postForm.get().url;
            adv.image =postForm.get().image;
            adv.position =postForm.get().position;
            adv.update();

            return ok(new JsonResult("success", "Advertisement updated successfully").toJsonResponse());

        }

    }

    public static Result deleteAdv(long id) {

        Advertisement adv = Advertisement.find.byId(id);
        if(adv==null){
            return status(404, new JsonResult("error", "Advertisement not found").toJsonResponse());
        }
        else
        {
            adv.delete();
            return ok(new JsonResult("success","Advertisement deleted successfully").toJsonResponse());
        }

    }

    /**
     * 添加广告表单数据
     */
    public static class AdvForm {

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String name;   //  广告名称

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String description; //广告描述

        @Constraints.Required
        public String url; //广告链接

        @Constraints.Required
        public String image; //广告图片

        @Constraints.Required
        public Position position;//广告位置
    }
}
