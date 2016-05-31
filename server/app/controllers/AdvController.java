package controllers;

import controllers.secured.AdminSecured;
import play.mvc.Result;
import play.mvc.Security;

/**
 * 广告控制器
 * Created by guodont on 16/5/31.
 */
public class AdvController {

    /**
     * 添加广告
     * @return
     */
    public static Result addAdv() {
        return play.mvc.Results.TODO;
    }

    /**
     * 根据id获取广告
     * @param id
     * @return
     */
    public static Result getAdvById(long id) {
        return play.mvc.Results.TODO;
    }

    /**
     * 获取广告
     * @return
     */
    public static Result getAdvs() {
        // TODO 根据位置获取广告
        // TODO 如果不传位置参数则默认显示所有广告
        return play.mvc.Results.TODO;
    }

    @Security.Authenticated(AdminSecured.class)
    public static Result updateAdv(long id) {
        return play.mvc.Results.TODO;
    }

    public static Result deleteAdv(long id) {
        return play.mvc.Results.TODO;
    }
}
