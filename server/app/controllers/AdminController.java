package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.secured.AdminSecured;
import models.Admin;
import models.Admin;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;
import utils.JsonResult;

import static controllers.Application.AUTH_TOKEN;

/**
 * @author guodont
 *         <p>
 *         TODO 管理员控制器
 */
public class AdminController extends BaseController {


    /**
     * 获取所有管理员
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getAllAdmins() {
        
        initPageing();
        
        return ok(Json.toJson(Admin.findAdmins(page, pageSize)));
    }

    /**
     * 通过id查看管理员详细信息
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getAdmin(long id) {
        return ok(Json.toJson(Admin.findById(id)));
    }

    /**
     * 编辑管理员资料
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result updateAdmin(long id) {
        Form<AdminInfoForm> userInfoFormForm = Form.form(AdminInfoForm.class).bindFromRequest();

        if (userInfoFormForm.hasErrors()) {
            return badRequest(userInfoFormForm.errorsAsJson());
        } else {
            //  更新管理员信息
            Admin user = getAdmin();
            user.setEmail(userInfoFormForm.get().email);
            user.phone = userInfoFormForm.get().phone;
            user.save();
        }
        return ok(new JsonResult("success", "Article updated").toJsonResponse());
    }

    /**
     * 修改密码
     *
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result updateAdminPassword() {
        Form<AdminChangePasswordForm> userChangePasswordFormForm = Form.form(AdminChangePasswordForm.class).bindFromRequest();

        if (userChangePasswordFormForm.hasErrors()) {
            return badRequest(userChangePasswordFormForm.errorsAsJson());
        } else {

            Admin admin = Admin.findByEmailAndPassword(getAdmin().getEmail(), userChangePasswordFormForm.get().oldPassword);
            // 判断旧密码是否正确
            if (admin != null) {

                //  更新管理员密码信息
                admin.setPassword(userChangePasswordFormForm.get().newPassword);
                //  更新token
                String authToken = admin.createToken();
                admin.update();

                ObjectNode authTokenJson = Json.newObject();
                authTokenJson.put(AUTH_TOKEN, authToken);
                response().setCookie(AUTH_TOKEN, authToken);

                return ok(authTokenJson);

            } else {

                return badRequest(new JsonResult("error", "Admin not exist").toJsonResponse());
            }
        }
    }

    /**
     * 删除管理员
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteAdmin(long id) {
        Admin user = Admin.find.byId(id);
        if (user != null) {
            user.delete();
            return ok(new JsonResult("success", "Admin deleted successfully").toJsonResponse());
        } else {
            return ok(new JsonResult("error", "Admin not exist").toJsonResponse());
        }
    }

    /**
     * 管理员信息表单数据
     */
    public static class AdminInfoForm {

        @Constraints.MaxLength(255)
        public String email;        //  邮箱

        @Constraints.MaxLength(11)
        public String phone;      //  手机号

    }

    /**
     * 管理员修改密码表单数据
     */
    public static class AdminChangePasswordForm {

        @Constraints.MaxLength(255)
        public String oldPassword;      //  旧密码

        @Constraints.MaxLength(255)
        public String newPassword;      //  新密码

    }
}
