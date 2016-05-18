////////
// This sample is published as part of the blog article at www.toptal.com/blog
// Visit www.toptal.com/blog and subscribe to our newsletter to read great posts
////////

package controllers;

import controllers.secured.AdminSecured;
import models.User;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

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
        return play.mvc.Results.TODO;
    }

    /**
     * 根据用户类型获取用户列表
     *
     * @param userType
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result getUsersByType(String userType) {
        return play.mvc.Results.TODO;
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
        return play.mvc.Results.TODO;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Security.Authenticated(AdminSecured.class)
    public static Result deleteUser(long id) {
        return play.mvc.Results.TODO;
    }
}
