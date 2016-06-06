package controllers;

import controllers.secured.AdminSecured;
import models.Admin;
import models.Category;
import models.Expert;
import models.User;
import models.enums.UserType;
import org.omg.CORBA.PUBLIC_MEMBER;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

/**
 * Created by j on 2016/6/4.
 */
public class ExpertController extends BaseController{

    /**
     * 设置普通用户为专家
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result changeType(long id) {

        Form<ExpertForm> postForm = Form.form(ExpertForm.class).bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(postForm.errorsAsJson());
        } else {
            //保存用户类型
            User user = new User();
            if (user.userType == UserType.PUBLIC) {
                user.userType = UserType.EXPERT;
            }
            user.save();

            Expert expert = Expert.findExpertById(id);
            Category category = Category.find.byId(postForm.get().categoryId);

            expert.user = user;
            expert.category =category;
            expert.professional=postForm.get().professional;
            expert.duty =postForm.get().duty;
            expert.service =postForm.get().service;
            expert.introduction =postForm.get().introduction;
            expert.company =postForm.get().company;

            expert.save();

            return ok(new JsonResult("success", "普通用户变成专家用户成功").toJsonResponse());
        }
    }


    /**
     * 获取所有专家
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getExperts(){
        initPageing();
        response().setHeader("TOTAL_SIZE", String.valueOf(Expert.find.findRowCount()));
        response().setHeader("CUR_PAGE",String.valueOf(page));
        response().setHeader("PAGE_SIZE",String.valueOf(pageSize));

        return ok(Json.toJson(Expert.findExpertByUser(getUser())));
    }

    /**
     * 通过专家id获取专家
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static  Result getExpertById(){
        return  ok(Json.toJson(getExpertById()));
    }


    /**
     * 通过专家id删除专家
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteExpert(long id) {
        Expert expert = Expert.find.byId(id);
        if (expert != null) {
            expert.delete();
            return ok(new JsonResult("success", "Expert deleted successfully").toJsonResponse());
        } else {
            return ok(new JsonResult("error", "Expert not exist").toJsonResponse());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result updateExpert(long id) {
//        Form<ExpertInfoForm> expertInfoFormForm = Form.form(ExpertInfoForm.class).bindFromRequest();
//
//        if (expertInfoFormForm.hasErrors()) {
//            return badRequest(expertInfoFormForm.errorsAsJson());
//        } else {
//            //  更新用户信息
//            Expert expert = getExpert();
//            expert.setEmail(userInfoFormForm.get().email);
//            expert.setRealName(userInfoFormForm.get().realName);
//            user.address = userInfoFormForm.get().address;
//            user.scale = userInfoFormForm.get().scale;
//            user.avatar = userInfoFormForm.get().avatar;
//            user.industry = userInfoFormForm.get().industry;
//            user.save();
//        }
        return ok(new JsonResult("success", "Article updated").toJsonResponse());
    }

    /**
     *  专家信息表单数据
     */
    public static class ExpertForm {
        @Constraints.Required
        public Long categoryId;     //  分类id

        @Constraints.Required
        @Constraints.MaxLength(45)
        public String professional;       //  职称


        @Constraints.Required
        @Constraints.MaxLength(45)
        public String duty;             //  职务

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String introduction;        //  简介

        @Constraints.Required
        public String service;        //  服务项目

        @Constraints.Required
        @Constraints.MaxLength(255)
        public String company;        //  所在单位
    }
}

