////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.secured.AdminSecured;
import models.Article;
import models.Category;
import models.User;
import models.enums.ArticleType;
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
 *         用户管理控制器
 */
public class UserController extends BaseController {


    /**
     * 获取所有用户
     *
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getAllUsers() {
        initPageing();
        User user = new User();
        response().setHeader("TOTAL_SIZE", String.valueOf(User.find.findRowCount()));
        response().setHeader("CUR_PAGE", String.valueOf(page));
        response().setHeader("PAGE_SIZE", String.valueOf(pageSize));
        return ok(Json.toJson(user.findUsers(page, pageSize)));
    }

    /**
     * 根据用户类型获取用户列表
     *
     * @param userType
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getUsersByType(String userType) {
        initPageing();
        User user = new User();
        response().setHeader("TOTAL_SIZE", String.valueOf(User.find.findRowCount()));
        response().setHeader("CUR_PAGE", String.valueOf(page));
        response().setHeader("PAGE_SIZE", String.valueOf(pageSize));
        return ok(Json.toJson(user.findUsersByType(userType, page, pageSize)));
    }

    /**
     * 通过id查看用户详细信息
     *
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result getUser(long id) {
        return ok(Json.toJson(getUser()));
    }

    /**
     * 编辑用户资料
     *
     * @param id
     * @return
     */
    @Security.Authenticated(Secured.class)
    public static Result updateUser(long id) {
        Form<UserInfoForm> userInfoFormForm = Form.form(UserInfoForm.class).bindFromRequest();

        if (userInfoFormForm.hasErrors()) {
            return badRequest(userInfoFormForm.errorsAsJson());
        } else {
            //  更新用户信息
            User user = getUser();
            user.setEmail(userInfoFormForm.get().email);
            user.setRealName(userInfoFormForm.get().realName);
            user.address = userInfoFormForm.get().address;
            user.scale = userInfoFormForm.get().scale;
            user.avatar = userInfoFormForm.get().avatar;
            user.industry = userInfoFormForm.get().industry;
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
    public static Result updateUserPassword() {
        Form<UserChangePasswordForm> userChangePasswordFormForm = Form.form(UserChangePasswordForm.class).bindFromRequest();

        if (userChangePasswordFormForm.hasErrors()) {
            return badRequest(userChangePasswordFormForm.errorsAsJson());
        } else {

            User user = User.findByNameAndPassword(getUser().name, userChangePasswordFormForm.get().oldPassword);
            // 判断旧密码是否正确
            if ( user != null) {

                //  更新用户密码信息
                user.setPassword(userChangePasswordFormForm.get().newPassword);
                //  更新token
                String authToken = user.createToken();
                user.update();

                ObjectNode authTokenJson = Json.newObject();
                authTokenJson.put(AUTH_TOKEN, authToken);
                response().setCookie(AUTH_TOKEN, authToken);

                return ok(authTokenJson);

            } else {

                return badRequest(new JsonResult("error", "User not exist").toJsonResponse());
            }
        }
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteUser(long id) {
        User user = User.find.byId(id);
        if (user != null) {
            user.delete();
            return ok(new JsonResult("success", "User deleted successfully").toJsonResponse());
        } else {
            return ok(new JsonResult("error", "User not exist").toJsonResponse());
        }
    }

    /**
     * 用户信息表单数据
     */
    public static class UserInfoForm {

        @Constraints.MaxLength(255)
        public String email;        //  邮箱

        @Constraints.MaxLength(255)
        public String address;      //  地址

        @Constraints.MaxLength(45)
        public String realName;     //  真实姓名

        @Constraints.MaxLength(45)
        public String industry;     //  用户经营的产业

        @Constraints.MaxLength(45)
        public String scale;        //  用户经营的产业规模

        @Constraints.MaxLength(255)
        public String avatar;       //  用户头像

    }

    /**
     * 用户修改密码表单数据
     */
    public static class UserChangePasswordForm {

        @Constraints.MaxLength(255)
        public String oldPassword;      //  旧密码

        @Constraints.MaxLength(255)
        public String newPassword;      //  新密码

    }
}
