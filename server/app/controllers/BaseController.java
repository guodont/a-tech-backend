package controllers;

import models.Admin;
import models.User;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Security;

/**
 * Created by guodont on 16/4/18.
 */
public class BaseController extends Controller {

    public static int page;
    public static int pageSize;

    public static void initPageing() {

        if (request().getQueryString("page") != null && request().getQueryString("pageSize") != null) {
            page = Integer.valueOf(request().getQueryString("page"));
            pageSize = Integer.valueOf(request().getQueryString("pageSize"));
        } else {
            page = 1;
            pageSize = 20;
        }
    }

    /**
     * 获取当前会话管理员
     *
     * @return
     */
    public static Admin getAdmin() {
        return (Admin) Http.Context.current().args.get("admin");
    }

    /**
     * 获取当前会话用户
     *
     * @return
     */
    public static User getUser() {
        return (User) Http.Context.current().args.get("user");
    }

}
