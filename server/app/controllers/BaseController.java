package controllers;

import models.Admin;
import models.User;
import play.mvc.Controller;
import play.mvc.Security;

/**
 * Created by guodont on 16/4/18.
 */
public class BaseController extends Controller{


    /**
     * 获取当前会话管理员
     * @return
     */
    public static Admin getAdmin() {
        return Admin.findByEmail(session().get("username"));
    }

    /**
     * 获取当前会话用户
     * @return
     */
    public static User getUser() {
        return User.findByEmail(session().get("username"));
    }

}
