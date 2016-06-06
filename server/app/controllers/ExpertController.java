package controllers;

import controllers.secured.AdminSecured;
import controllers.secured.ExpertSecured;
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
public class ExpertController extends BaseController {

    /**
     * 设置普通用户为专家
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result addExpert(long id) {

        Form<ExpertForm> postForm = Form.form(ExpertForm.class).bindFromRequest();
        if (postForm.hasErrors()) {
            return badRequest(postForm.errorsAsJson());
        } else {
            //保存用户类型
            User user = User.findById(id);
            user.userType = UserType.EXPERT;
            user.update();

            Category category = Category.find.byId(postForm.get().categoryId);
            Expert expert = new Expert();
            expert.user = user;
            expert.category = category;
            expert.professional = postForm.get().professional;
            expert.duty = postForm.get().duty;
            expert.service = postForm.get().service;
            expert.introduction = postForm.get().introduction;
            expert.company = postForm.get().company;
            expert.save();

            return ok(new JsonResult("success", "指定专家成功").toJsonResponse());
        }
    }


    /**
     * 获取所有专家
     */
    public static Result getExperts() {
        initPageing();
        return ok(Json.toJson(Expert.findExperts(page, pageSize)));
    }

    /**
     * 通过专家id获取专家信息
     *
     * @return
     */
    public static Result getExpertById(Long id) {
        return ok(Json.toJson(Expert.findExpertById(id)));
    }

    /**
     * 通过专家id删除专家
     *
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

    /**
     * 更新专家信息 for user
     *
     * @return
     */
    @Security.Authenticated(ExpertSecured.class)
    public static Result updateExpertForExpert() {
        Form<ExpertForm> expertInfoFormForm = Form.form(ExpertForm.class).bindFromRequest();

        if (expertInfoFormForm.hasErrors()) {
            return badRequest(expertInfoFormForm.errorsAsJson());
        } else {
            //  更新用户信息
            Category category = Category.find.byId(expertInfoFormForm.get().categoryId);
            Expert expert = Expert.findExpertByUser(getUser());
            expert.category = category;
            expert.professional = expertInfoFormForm.get().professional;
            expert.duty = expertInfoFormForm.get().duty;
            expert.service = expertInfoFormForm.get().service;
            expert.introduction = expertInfoFormForm.get().introduction;
            expert.company = expertInfoFormForm.get().company;
            expert.update();
        }
        return ok(new JsonResult("success", "Article updated").toJsonResponse());
    }

    /**
     * 更新专家信息 for admin
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result updateExpertForAdmin(long id) {
        Form<ExpertForm> expertInfoFormForm = Form.form(ExpertForm.class).bindFromRequest();

        if (expertInfoFormForm.hasErrors()) {
            return badRequest(expertInfoFormForm.errorsAsJson());
        } else {
            //  更新用户信息
            Category category = Category.find.byId(expertInfoFormForm.get().categoryId);
            Expert expert = Expert.findExpertById(id);
            expert.category = category;
            expert.professional = expertInfoFormForm.get().professional;
            expert.duty = expertInfoFormForm.get().duty;
            expert.service = expertInfoFormForm.get().service;
            expert.introduction = expertInfoFormForm.get().introduction;
            expert.company = expertInfoFormForm.get().company;
            expert.update();
        }
        return ok(new JsonResult("success", "Article updated").toJsonResponse());
    }

    /**
     * 专家信息表单数据
     */
    public static class ExpertForm {
        public Long categoryId;     //  分类id

        @Constraints.MaxLength(45)
        public String professional;       //  职称

        @Constraints.MaxLength(45)
        public String duty;             //  职务

        @Constraints.MaxLength(255)
        public String introduction;        //  简介

        public String service;        //  服务项目

        @Constraints.MaxLength(255)
        public String company;        //  所在单位
    }
}

